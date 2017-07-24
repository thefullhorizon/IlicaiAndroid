package com.ailicai.app.ui.account;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ailicai.app.R;
import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.widget.DialogBuilder;
import com.intsig.idcardscan.sdk.IDCardScanSDK;
import com.intsig.idcardscan.sdk.ResultData;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class IDCardScanActivity extends Activity implements Camera.PreviewCallback {
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    private static final String APP_KEY ="WTP295AW1163Hg5K8eMy70Pb";

    private DetectThread mDetectThread = null;
    private Preview mPreview = null;
    private Camera mCamera = null;
    private int numberOfCameras;
    private int cameraCurrentlyLocked;

    // The first rear facing camera
    private int defaultCameraId;

    private float mDensity = 2.0f;

    private IDCardScanSDK mIDCardScanSDK = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventLog.upEventLog("1","photograph");

        mDensity = getResources().getDisplayMetrics().density;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Create a RelativeLayout container that will hold a SurfaceView,
        // and set it as the content of our activity.
        mPreview = new Preview(this);
        setContentView(mPreview);

        // Find the total number of cameras available
        numberOfCameras = Camera.getNumberOfCameras();

        // Find the ID of the default camera
        CameraInfo cameraInfo = new CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                defaultCameraId = i;
            }
        }
        mIDCardScanSDK = new IDCardScanSDK();
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                int ret = mIDCardScanSDK.initIDCardScan(IDCardScanActivity.this, APP_KEY);
                return ret;
            }

            @Override
            protected void onPostExecute(Integer result) {
                if (isFinishing())  return;

                if (result != 0) {
                    DialogBuilder.showSimpleDialog(IDCardScanActivity.this, null, "Error " + result, null, null, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                }
            }
        }.execute();
        mPreview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mCamera != null) {
                    mCamera.autoFocus(null);
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                // 执行获取权限后的操作
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            } else {
                openCamera();
            }
        }
    }

    private void openCamera() {
        try {
            mCamera = Camera.open(defaultCameraId);
        } catch (Exception e) {
            Toast.makeText(IDCardScanActivity.this, "无法打开摄像头，请稍候重试", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        cameraCurrentlyLocked = defaultCameraId;
        mPreview.setCamera(mCamera);
        setDisplayOrientation();
        mCamera.setOneShotPreviewCallback(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    finish();
                }
                return;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Because the Camera object is a shared resource, it's very
        // important to release it when the activity is paused.
        if (mCamera != null) {
            mCamera.setOneShotPreviewCallback(null);
            mPreview.setCamera(null);
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIDCardScanSDK.release();
        if (mDetectThread != null)  mDetectThread.stopRun();
        mHandler.removeMessages(MSG_AUTO_FOCUS);
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Size size = camera.getParameters().getPreviewSize();
        if (mDetectThread == null) {
            mDetectThread = new DetectThread();
            mDetectThread.start();
        }
        mDetectThread.addDetect(data, size.width, size.height);
    }

    // thread to detect and recognize.
    private class DetectThread extends Thread {
        private boolean mode_vertical = false;
        private ArrayBlockingQueue<byte[]> mPreviewQueue = new ArrayBlockingQueue<byte[]>(1);
        private int width;
        private int height;

        public void stopRun() {
            addDetect(new byte[] {0}, -1, -1);
        }

        boolean switchMode() {
            mode_vertical = !mode_vertical;
            return mode_vertical;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    byte[] data = mPreviewQueue.take();// block here, if no data in the queue.
                    if (data.length == 1) {// quit the thread, if we got special byte array put by stopRun().
                        return;
                    }
                    float left, top, right, bottom;
                    int newWidth = height;
                    int newHeight = width;

                    if (mode_vertical) {// vertical
                        float dis = 1 / 16f;
                        left = newWidth * dis;
                        right = newWidth - left;
                        top = (newHeight - (newWidth - left - left) * 0.618f) / 2;
                        bottom = newHeight - top;
                    } else {// horizental
                        float dis = 1 / 8f;// 10
                        left = newWidth * dis;
                        right = newWidth - left;
                        top = (newHeight - (newWidth - left - left) / 0.618f) / 2;
                        bottom = newHeight - top;
                    }
//                    System.out.println("left > " + left + ", top > " + top + ", right > " + right + ", bottom > " + bottom);

                    // the (left, top, right, bottom) is base on preview image's
                    // coordinate. that's different with ui coordinate.
                    int[] out = mIDCardScanSDK.detectBorder(data, width, height, (int) top, (int) (height - right), (int) bottom, (int) (height - left));
                    // if activity is port mode then (x,y)->(preview.height-y,x)
                    if (out != null) {// find border
//                        System.out.println("DetectCard >>>>>>>>>>>>> " + Arrays.toString(out));
                        for (int i = 0; i < 4; i++) {
                            int tmp = out[0 + i * 2]; 
                            out[0 + i * 2] = height - out[1 + i * 2];
                            out[1 + i * 2] = tmp;
                        }
                        boolean match = isMatch((int) left, (int) top, (int) right, (int) bottom, out);
                        mPreview.showBorder(out, match);
                        if (match) { // get matched border
                            ResultData result = mIDCardScanSDK.recognize(data, width, height, true);
                            if (result != null) {
                                idCardScanOver(result);
                                // showDialog(result);
                                continue;
                            }
                        }
                    } else {// no find border, continue to preview;
                        mPreview.showBorder(null, false);
                    }
                    // continue to preview;
                    resumePreviewCallback();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int continue_match_time = 0;

        public boolean isMatch(int left, int top, int right, int bottom, int[] qua) {
            int dif = 120;
            int num = 0;

            if (Math.abs(left - qua[6]) < dif && Math.abs(top - qua[7]) < dif) {
                num++;
            }
            if (Math.abs(right - qua[0]) < dif && Math.abs(top - qua[1]) < dif) {
                num++;
            }
            if (Math.abs(right - qua[2]) < dif && Math.abs(bottom - qua[3]) < dif) {
                num++;
            }
            if (Math.abs(left - qua[4]) < dif && Math.abs(bottom - qua[5]) < dif) {
                num++;
            }
//            System.out.println("inside " + Arrays.toString(qua) + " <>" + left + ", " + top + ", "
//                            + right + ", " + bottom + "           " + num);
            if (num > 2) {
                continue_match_time++;
                if (continue_match_time >= 1)
                    return true;
            } else {
                continue_match_time = 0;
            }
            return false;
        }

        public void addDetect(byte[] data, int width, int height) {
            if (mPreviewQueue.size() == 1) {
                mPreviewQueue.clear();
            }
            mPreviewQueue.add(data);
            this.width = width;
            this.height = height;
        }
    }

    private void idCardScanOver(ResultData result) {
        Intent intent = new Intent();
        intent.putExtra("cardName", result.getName());
        intent.putExtra("idCardNumber", result.getId());
        intent.putExtra("cardIdshots", result.getIdshots());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showDialog(final ResultData result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String data = null;
                LinearLayout view = new LinearLayout(IDCardScanActivity.this);
                view.setOrientation(LinearLayout.VERTICAL);
                TextView textview = new TextView(IDCardScanActivity.this);
                view.addView(textview);
                if (result.isFront()) {//front
                    StringBuffer sb = new StringBuffer("身份证正面:\n");
                    sb.append("姓名:").append(result.getName()).append("\n")
                    .append("性别:").append(result.getSex()).append("\n")
                    .append("民族:").append(result.getNational()).append("\n")
                    .append("出生:").append(result.getBirthday()).append("\n")
                    .append("住址:").append(result.getAddress()).append("\n")
                    .append("公民身份证号码:").append(result.getId());
                    data = sb.toString();
                    ImageView img = new ImageView(IDCardScanActivity.this);
                    img.setImageBitmap(result.getIdshots());
                    view.addView(img);
                } else {
                    StringBuffer sb = new StringBuffer("身份证反面:\n");
                    sb.append("签发机关:").append(result.getIssueauthority()).append("\n")
                    .append("有效期限:").append(result.getValidity());
                    data = sb.toString();
                }
                textview.setText(data);
                new AlertDialog.Builder(IDCardScanActivity.this, R.style.AppAppCompatTheme)
                .setTitle("Recognize Result")
                .setView(view)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resumePreviewCallback();
                    }
                }).create().show();
            }
        });
    }

    private void resumePreviewCallback() {
        if (mCamera != null) {
            mCamera.setOneShotPreviewCallback(this);
        }
    }

    private void setDisplayOrientation() {
        CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(defaultCameraId, info);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result = (info.orientation - degrees + 360) % 360;
        mCamera.setDisplayOrientation(result);

        Parameters params = mCamera.getParameters();
        String focusMode = Parameters.FOCUS_MODE_AUTO;
        if (!TextUtils.equals("samsung", android.os.Build.MANUFACTURER)) {
            focusMode = Parameters.FOCUS_MODE_CONTINUOUS_PICTURE;
        }
        if (!isSupported(focusMode, params.getSupportedFocusModes())) {
            // For some reasons, the driver does not support the current
            // focus mode. Fall back to auto.
            if (isSupported(Parameters.FOCUS_MODE_AUTO, params.getSupportedFocusModes())) {
                focusMode = Parameters.FOCUS_MODE_AUTO;
            } else {
                focusMode = params.getFocusMode();
            }
        }
        params.setFocusMode(focusMode);
        mCamera.setParameters(params);
        if (!TextUtils.equals(focusMode, Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            mHandler.sendEmptyMessageDelayed(MSG_AUTO_FOCUS, 2000);
        }
    }

    public boolean isSupported(String value, List<String> supported) {
        return supported != null && supported.indexOf(value) >= 0;
    }

    private static final int MSG_AUTO_FOCUS = 100;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == MSG_AUTO_FOCUS) {
                autoFocus();
            }
        }
    };

    private void autoFocus() {
        if (mCamera != null) {
            try {
                mCamera.autoFocus(null);
            } catch (Exception e) {
                e.printStackTrace();
                mCamera.cancelAutoFocus();
            }
            mHandler.sendEmptyMessageDelayed(MSG_AUTO_FOCUS, 2000);
        }
    }

    /**
     * A simple wrapper around a Camera and a SurfaceView that renders a centered preview of the
     * Camera to the surface. We need to center the SurfaceView because not all devices have cameras
     * that support preview sizes at the same aspect ratio as the device's display.
     */
    class Preview extends ViewGroup implements SurfaceHolder.Callback, View.OnClickListener,
            OnCheckedChangeListener {
        private final String TAG = "Preview";
        private SurfaceView mSurfaceView = null;
        private SurfaceHolder mHolder = null;
        private Size mPreviewSize = null;
        private List<Size> mSupportedPreviewSizes = null;
        private Camera mCamera = null;
        private DetectView mDetectView = null;
//        private TextView mInfoView = null;
//        private Button mSwitchButton = null;
        private CheckBox mCkFlashLight = null;
        private CheckBox mCkBack = null;
//        private TextView mCopyRight = null;;

        public Preview(Context context) {
            super(context);
            mSurfaceView = new SurfaceView(context);
            addView(mSurfaceView);

//            mInfoView = new TextView(context);
//            addView(mInfoView);

            mDetectView = new DetectView(context);
            addView(mDetectView);

//            mSwitchButton = new Button(context);
//            mSwitchButton.setText("Switch");
//            addView(mSwitchButton);
//            mSwitchButton.setOnClickListener(this);

            mCkFlashLight = new CheckBox(context);
            mCkFlashLight.setChecked(false);
            mCkFlashLight.setButtonDrawable(R.drawable.scan_flashlight_cb);
            addView(mCkFlashLight);
            mCkFlashLight.setOnCheckedChangeListener(this);

            mCkBack = new CheckBox(context);
            mCkBack.setChecked(false);
            mCkBack.setButtonDrawable(R.drawable.scan_back_cb);
            addView(mCkBack);
            mCkBack.setOnCheckedChangeListener(this);

//            mCopyRight = new TextView(IDCardScanActivity.this);
//            mCopyRight.setGravity(Gravity.CENTER);
//            mCopyRight.setText("IntSig Information Co.,Ltd.\\n© 2009-2015 Intsig. All Rights Reserved.");
//            addView(mCopyRight);

            mHolder = mSurfaceView.getHolder();
            mHolder.addCallback(this);
        }

        public void onClick(View view) {
            if (view instanceof Button) {
                boolean mode = mDetectThread.switchMode();
                mDetectView.switchMode(mode);
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView == mCkBack) {
	            if (isChecked) {
	                finish();
	                return;
				}
            } else {
	            if (isChecked) {
	                openFlashLight();
	            } else {
	                closeFlashLight();
	            }
			}
        }

        private void closeFlashLight() {
            controlFlashLight(false);
        }

        private void openFlashLight() {
            controlFlashLight(true);
        }

        private void controlFlashLight(boolean turnOn) {
            if (mCamera == null) {
                return;
            }
            Parameters parameters = mCamera.getParameters();
            if (parameters == null) {
                return;
            }
            List<String> flashModes = parameters.getSupportedFlashModes();
            if (flashModes == null) {
                return;
            }
            String currentMode = parameters.getFlashMode();
            String flashMode = Parameters.FLASH_MODE_OFF;
            if (turnOn) {
                flashMode = Parameters.FLASH_MODE_TORCH;
            }
            if (!flashMode.equals(currentMode)) {
                if (flashModes.contains(flashMode)) {
                    parameters.setFlashMode(flashMode);
                    mCamera.setParameters(parameters);
                }
            }
        }

        public void setCamera(Camera camera) {
            mCamera = camera;
            if (mCamera != null) {
                mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
                requestLayout();
            }
        }

        public void switchCamera(Camera camera) {
            setCamera(camera);
            try {
                camera.setPreviewDisplay(mHolder);
            } catch (IOException exception) {
                Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
            }
            Parameters parameters = camera.getParameters();
            parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
            requestLayout();

            camera.setParameters(parameters);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            // We purposely disregard child measurements because act as a
            // wrapper to a SurfaceView that centers the camera preview instead
            // of stretching it.
            final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
            final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
            setMeasuredDimension(width, height);

            if (mSupportedPreviewSizes != null) {
                mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, height, width);// 竖屏模式，寬高颠倒
            }
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            if (changed && getChildCount() > 0) {
                final View child = getChildAt(0);

                final int width = r - l;
                final int height = b - t;

                int previewWidth = width;
                int previewHeight = height;
                if (mPreviewSize != null) {
                    previewWidth = mPreviewSize.height;
                    previewHeight = mPreviewSize.width;
                }

                // Center the child SurfaceView within the parent.
                if (width * previewHeight > height * previewWidth) {
                    final int scaledChildWidth = previewWidth * height / previewHeight;
                    child.layout((width - scaledChildWidth) / 2, 0, (width + scaledChildWidth) / 2,
                                    height);
                    mDetectView.layout((width - scaledChildWidth) / 2, 0,
                                    (width + scaledChildWidth) / 2, height);
                } else {
                    final int scaledChildHeight = previewHeight * width / previewWidth;
                    child.layout(0, (height - scaledChildHeight) / 2, width,
                                    (height + scaledChildHeight) / 2);
                    mDetectView.layout(0, (height - scaledChildHeight) / 2, width,
                                    (height + scaledChildHeight) / 2);
                }
                getChildAt(1).layout(l, t, r, b);

//                mSwitchButton.layout((int) (r - 96 * mDensity), (int) (b - 86 * mDensity), r,
//                                (int) (b - 48 * mDensity));
                mCkFlashLight.layout((int) (r - 48 * mDensity), (int) (t + 18 * mDensity), (int) (r - 8 * mDensity), (int) (t + 58 * mDensity));
                mCkBack.layout((int) (l + 8 * mDensity), (int) (t + 18 * mDensity), (int) (l + 48 * mDensity), (int) (t + 58 * mDensity));
//                mCopyRight.layout(l, (int) (b - 48 * mDensity), (int) (r - 8 * mDensity), b);
            }
        }

        public void surfaceCreated(SurfaceHolder holder) {
            // The Surface has been created, acquire the camera and tell it where to draw.
            try {
                if (mCamera != null) {
                    mCamera.setPreviewDisplay(holder);
                }
            } catch (IOException exception) {
                Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // Surface will be destroyed when we return, so stop the preview.
            if (mCamera != null) {
                mCamera.stopPreview();
            }
        }

        private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
            final double ASPECT_TOLERANCE = 0.1;
            double targetRatio = (double) w / h;
            if (sizes == null)
                return null;
            Size optimalSize = null;
            double minDiff = Double.MAX_VALUE;

            int targetHeight = h;

            // Try to find an size match aspect ratio and size
            for (Size size : sizes) {
                double ratio = (double) size.width / size.height;
                if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                    continue;
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }

            // Cannot find the one match the aspect ratio, ignore the
            // requirement
            if (optimalSize == null) {
                minDiff = Double.MAX_VALUE;
                for (Size size : sizes) {
                    if (Math.abs(size.height - targetHeight) < minDiff) {
                        optimalSize = size;
                        minDiff = Math.abs(size.height - targetHeight);
                    }
                }
            }
            return optimalSize;
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            if (mCamera != null) {
                // Now that the size is known, set up the camera parameters and begin the preview.
                Parameters parameters = mCamera.getParameters();
                parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
                parameters.setPreviewFormat(ImageFormat.NV21);
                requestLayout();
                mDetectView.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
//                mInfoView.setText("preview：" + mPreviewSize.width + "," + mPreviewSize.height);
                mCamera.setParameters(parameters);
                mCamera.startPreview();
            }
        }

        public void showBorder(int[] border, boolean match) {
            mDetectView.showBorder(border, match);
        }
    }

    /**
     * the view show bank card border.
     */
    private class DetectView extends View {
        private boolean mode_vertical = false;

        private Paint paint = null;
        private int[] border = null;
        private String result = null;
        private boolean match = false;
        private int previewWidth;
        private int previewHeight;

        public void showBorder(int[] border, boolean match) {
            this.border = border;
            this.match = match;
            postInvalidate();
        }

        public DetectView(Context context) {
            super(context);
            paint = new Paint();
            paint.setColor(0xffff0000);
        }

        public void switchMode(boolean mode) {
            mode_vertical = mode;
            invalidate();
        }

        public void setPreviewSize(int width, int height) {
            this.previewWidth = width;
            this.previewHeight = height;
        }

        @Override
        public void onDraw(Canvas c) {
//            if (border != null) {
//                paint.setStrokeWidth(3);
//                int height = getWidth();
//                float scale = getWidth() / (float) previewHeight;
//                c.drawLine(border[0] * scale, border[1] * scale, border[2] * scale, border[3]
//                                * scale, paint);
//                c.drawLine(border[2] * scale, border[3] * scale, border[4] * scale, border[5]
//                                * scale, paint);
//                c.drawLine(border[4] * scale, border[5] * scale, border[6] * scale, border[7]
//                                * scale, paint);
//                c.drawLine(border[6] * scale, border[7] * scale, border[0] * scale, border[1]
//                                * scale, paint);
//
//            }
//            if (match) {
//                paint.setColor(0xff00ff00);
//                paint.setStrokeWidth(20);
//            } else {
//                paint.setColor(0xffff0000);
//                paint.setStrokeWidth(3);
//            }

            float left, top, right, bottom;
            if (mode_vertical) {// vertical
                float dis = 1 / 16f;
                left = getWidth() * dis;
                right = getWidth() - left;

                top = (getHeight() - (getWidth() - left - left) * 0.618f) / 2;
                bottom = getHeight() - top;
            } else {
                float dis = 1 / 8f;
                left = getWidth() * dis;
                right = getWidth() - left;

                top = (getHeight() - (getWidth() - left - left) / 0.618f) / 2;
                bottom = getHeight() - top;
            }

            c.save();
            c.restore();
//            paint.setColor(0xff000fff);
//            paint.setStyle(Paint.Style.STROKE);
//            c.drawRect(left, top, right, bottom, paint);

            paint.setColor(Color.parseColor("#e84a01"));
            paint.setStrokeWidth(0);
            paint.setStyle(Paint.Style.FILL);
            c.drawRect(new RectF(left, top, left + 200, top + 20), paint);
            c.drawRect(new RectF(right - 200, top, right, top + 20), paint);

            c.drawRect(new RectF(right - 20, top, right, top + 200), paint);
            c.drawRect(new RectF(right - 20, bottom - 200, right, bottom), paint);

            c.drawRect(new RectF(right - 200, bottom - 20, right, bottom), paint);
            c.drawRect(new RectF(left, bottom - 20, left + 200, bottom), paint);

            c.drawRect(new RectF(left, bottom - 200, left + 20, bottom), paint);
            c.drawRect(new RectF(left, top, left + 20, top + 200), paint);

            paint.setColor(Color.parseColor("#54000000"));
            c.drawRect(new RectF(0, 0, left + right, top), paint);
            c.drawRect(new RectF(0, top, left, bottom), paint);
            c.drawRect(new RectF(right, top, left + right, bottom), paint);
            c.drawRect(new RectF(0, bottom, left + right, bottom + top), paint);

            c.rotate(90, (left + right) / 2, (top + bottom) / 2);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setColor(Color.parseColor("#FFD0CFD1"));
            paint.setTextSize(50);
            c.drawText("请将身份证正面置于此区域", (left + right) / 2, (top + bottom) / 2 - 25, paint);
            c.drawText("尝试对齐边缘", (left + right) / 2, (top + bottom) / 2 + 25, paint);
        }
    }

}
