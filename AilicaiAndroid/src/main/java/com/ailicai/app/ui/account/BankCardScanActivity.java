package com.ailicai.app.ui.account;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import com.ailicai.app.R;
import com.ailicai.app.common.logCollect.EventLog;
import com.ailicai.app.common.utils.GestureLockTools;
import com.intsig.ccrengine.CCREngine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class BankCardScanActivity extends Activity implements Camera.PreviewCallback {
	public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;

	private Preview mPreview;
	Camera mCamera;
	int numberOfCameras;
	int cameraCurrentlyLocked;

	// The first rear facing camera
	int defaultCameraId;
    CCREngine mCCREngine;
	private float mDensity = 2.0f;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventLog.upEventLog("1","photograph");

		mDensity = getResources().getDisplayMetrics().density;

//		System.out.println("xxxxxx onCreate  ");
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
		String sdcard = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		new File(sdcard + "/bcs").mkdir();
        mCCREngine = new CCREngine();
        new AsyncTask<Void,Void,Integer>(){



            @Override
            protected Integer doInBackground(Void... params) {
                int ret = mCCREngine.init(BankCardScanActivity.this, "WTP295AW1163Hg5K8eMy70Pb");
                return ret;
            }
            @Override
            protected void onPostExecute(Integer result) {
				if (isFinishing())  return;

                if(result!=0){
                    new AlertDialog.Builder(BankCardScanActivity.this, R.style.AppCompatDialog).setMessage("Error " + result).setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setCancelable(false).create().show();
                }
            }

        }.execute(   );
        mPreview.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(mCamera!=null)
				mCamera.autoFocus(null);
				return false;
			}
		});
	}

	@Override
	protected void onDestroy() {
//		System.out.println("xxxxxx onDestroy  ");
		super.onDestroy();
        mCCREngine.release();
		if (mDetectThread != null) mDetectThread.stopRun();
		mDetectThread = null;
	}



	byte[] mCallbackBuffer;

	@Override
	protected void onResume() {
		super.onResume();
//		System.out.println("xxxxxx onResume  ");
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

	@Override
	protected void onPostResume() {
		super.onPostResume();
		GestureLockTools.checkGesture(this);
	}

	private void openCamera() {
		// Open the default i.e. the first rear facing camera.
		try {
			mCamera = Camera.open(defaultCameraId);
		} catch (Exception e) {
			Toast.makeText(BankCardScanActivity.this, "无法打开摄像头，请稍候重试", Toast.LENGTH_SHORT).show();
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

	void setDisplayOrientation() {
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
	/*	Camera.Parameters params = mCamera.getParameters();
		if (isSupported(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE,
				params.getSupportedFocusModes())) {
			params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
			mCamera.setParameters(params);
		}else*/{
			mHandler.sendEmptyMessageDelayed(100, 1000);
		}
	}
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what==100){
				try {
					if(mCamera!=null)
					mCamera.autoFocus(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				mHandler.sendEmptyMessageDelayed(100, 2000);
			}
			super.handleMessage(msg);
		}
		
	};
	
	public boolean isSupported(String value, List<String> supported) {
		return supported != null && supported.indexOf(value) >= 0;
	}

	@Override
	protected void onPause() {

//		System.out.println("xxxxxx onPause  ");
		super.onPause();
		// Because the Camera object is a shared resource, it's very
		// important to release it when the activity is paused.
		if (mCamera != null) {
			mCamera.setOneShotPreviewCallback(null);
			mPreview.setCamera(null);
			mCamera.release();
			mCamera = null;
		}
		mHandler.removeMessages(100);
	}

	// ----------------------------------------------------------------------
	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
//		System.out.println("xxxxxx onPreviewFrame");
		Size size = camera.getParameters().getPreviewSize();
		if (mDetectThread == null) {
			mDetectThread = new DetectThread();
			mDetectThread.start();
		}
		mDetectThread.addDetect(data, size.width, size.height);
	}

	private void resumePreviewCallback() {
		if (!isFinishing()) {
			try {
				if(mCamera!=null)
					mCamera.setOneShotPreviewCallback(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
String getCardType(int type){
	switch(type){
	case CCREngine.CCR_CARD_TYPE_VISA :// 4; // Visa
		return "Visa";
	case CCREngine.CCR_CARD_TYPE_MASTER :// 5; // MasterCard
		return "MasterCard";
	case CCREngine.CCR_CARD_TYPE_MAESTRO :// 6; // Maestro
		return "Maestro";
	case CCREngine.CCR_CARD_TYPE_AMEX :// 7; // American Express
		return "American Express";
	case CCREngine.CCR_CARD_TYPE_DINERS :// 8; // Diners Club
		return "Diners Club";
	case CCREngine.CCR_CARD_TYPE_DISCOVER :// 9; // Discover
		return "Discover";
	case CCREngine.CCR_CARD_TYPE_JCB :// 10; // JCB
		return "JCB";
	case CCREngine.CCR_CARD_TYPE_CHINA_UNIONPAY:// 11; // 银联
		return "银联";
		default:
			return " ";
	}
}
public static Bitmap sBitmap;
public static CCREngine.ResultData sResult;
public static long sRecognizedTime;
	void showResultDialog(final CCREngine.ResultData ret, final byte[] preview,
                          final int w, final int h, final long time, Bitmap bmp) {
		sBitmap = bmp;
		sResult = ret;
		sRecognizedTime = time;
//		Intent intent =new Intent(this, ResultShowActivity.class);
//		startActivity(intent);
		
	/*	runOnUiThread(new Runnable() {
			@Override
			public void run() {
				String msg = ret.getCardInsName()+" "+getCardType(ret.getCardType())+"\n"+
						ret.getCardNumber() 
						+ "\n"+getString(R.string.label_valid_thru)
						+ ret.getCardValidThru()
						+ "\n"+getString(R.string.label_holder_name)
						+ ret.getCardHolderName()
						+"\n"+getString(R.string.label_num_pos)+Arrays.toString(ret.getCardNumPos())
						+"\n "+getString(R.string.label_recog_time)+time+"ms";
				 AlertDialog ad = new AlertDialog.Builder(MainActivity.this)
						.setTitle(getString(R.string.title_recg_result))
						.setMessage(msg)
						.setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                resumePreviewCallback();
                            }
                            })
						.create();
				ad.show();

			}
		});*/
	}

	private void bankCardScanOver(CCREngine.ResultData result, Bitmap bmp) {
		Intent intent = new Intent();
		intent.putExtra("bankCardNumber", result.getCardNumber());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
		intent.putExtra("cardIdshots", baos.toByteArray());
		// intent.putExtra("cardValidThru", result.getCardValidThru());
		// intent.putExtra("cardType", result.getCardType());
		// intent.putExtra("cardInsName", result.getCardInsName());
		setResult(RESULT_OK, intent);
		finish();
		bmp.recycle();
		bmp = null;
	}

	int max_time = 0;
	int num = 0;
	DetectThread mDetectThread;

	// thread to detect and recognize.
	class DetectThread extends Thread {
		boolean mode_vertical = false;
		ArrayBlockingQueue<byte[]> mPreviewQueue = new ArrayBlockingQueue<byte[]>(
				1);
		int width;
		int height;

		public void stopRun() {
			// put a special on elements byte array to tell the thread stop.
			addDetect(new byte[] { 0 }, -1, -1);
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
						float dis = 1 / 10f;
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

//					long time2 = System.currentTimeMillis();
					// the (left, top, right, bottom) is base on preview image's
					// coordinate. that's different with ui coordinate.
                    int[] out= mCCREngine.detectBorder(data, width, height,
							(int) top, (int) (height - right), (int) bottom,
							(int) (height - left));
//					long time = System.currentTimeMillis();
//					System.out.println("DetectCard retXX " + out + "  "
//							+ (time - time2) + "           "
//							+ Arrays.toString(out));

					// if activity is port mode then (x,y)->(preview.height-y,x)


					if (out!=null) {// find border
                        for (int i = 0; i < 4; i++) {
                            int tmp = out[0 + i * 2];
                            out[0 + i * 2] = height - out[1 + i * 2];
                            out[1 + i * 2] = tmp;
                        }
						boolean match = isMatch((int) left, (int) top,
								(int) right, (int) bottom, out);
						mPreview.showBorder(out, match);
						if (match) { // get matched border

//							time = System.currentTimeMillis();
                            CCREngine.ResultData result =  mCCREngine.recognize(data,
                                    width, height);
							
//							time2 = System.currentTimeMillis();
//							System.out.println("RecognizeCard retXXX " + result
//									+ " " + (time2 - time));

							if (result.getCode() > 0) {// &&!TextUtils.isEmpty(result.getCardNumber())){
//								System.out.println("RecognizeCard retXX "
//										+ result);
								// show Result
//								max_time += (time2 - time);
								num++;
//								System.out.println("RecognizeCard retXXX "
//										+ (max_time / num) + " \t" + result);
								Bitmap bmp  = yuv2rgba(data, width, height, result.getCardNumPos(), result.getRotateAngle());
//								showResultDialog(result, data, width, height, 2*(time2 - time), bmp);
//								continue;

								bankCardScanOver(result, bmp);
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

		Bitmap yuv2rgba(byte[] preview, int width, int height, int[] pos, int rotate) {
			Bitmap out;
			try {
				YuvImage img = new YuvImage(preview, ImageFormat.NV21, width,
						height, null);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				img.compressToJpeg(new Rect(0, 0, width, height), 80, bos);
				Bitmap bmp = BitmapFactory.decodeByteArray(bos.toByteArray(), 0,
						bos.size());
				Matrix m = new Matrix();
				m.postRotate(rotate);
//				int left =pos[0];
//				int top = pos[1];
//				int w = pos[4]-pos[0];
//				int h = pos[5]-pos[1];
//				int tmp = h;
//				if(h<(w/10)){
//					tmp = (w/10);
//					top -=(tmp-h)/2;
//					h = tmp;
//				}
				// 定义要显示的切图的切割属性
				int w;
				int h;
				int left;
				int top;
				// 扫描核心区域：CoreAres = ca
				int ca_w = pos[4] - pos[0];
				int ca_h = pos[5] - pos[1];
				int ca_left = pos[0];
				int ca_top = pos[1];

				w = ca_w;
				h = w / 2;
				left = ca_left;
				// 核心区域竖直方向的正中心位置 - h / 2
				top = (pos[5] + pos[1]) / 2 - h / 2;
				out = Bitmap.createBitmap(bmp, left, top, w, h,  m, false);
						bmp.recycle();
						return out;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		int continue_match_time = 0;

		public boolean isMatch(int left, int top, int right, int bottom,
				int[] qua) {
			int dif = 200;
			int num = 0;

			if (Math.abs(left - qua[6]) < dif && Math.abs(top - qua[7]) < dif) {
				num++;
			}
			if (Math.abs(right - qua[0]) < dif && Math.abs(top - qua[1]) < dif) {
				num++;
			}
			if (Math.abs(right - qua[2]) < dif
					&& Math.abs(bottom - qua[3]) < dif) {
				num++;
			}
			if (Math.abs(left - qua[4]) < dif
					&& Math.abs(bottom - qua[5]) < dif) {
				num++;
			}
//			System.out.println("inside " + Arrays.toString(qua) + " <>" + left
//					+ ", " + top + ", " + right + ", " + bottom + "           "
//					+ num);
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
			try {
				mPreviewQueue.add(data);
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.width = width;
			this.height = height;
		}
	}

	/**
	 * A simple wrapper around a Camera and a SurfaceView that renders a
	 * centered preview of the Camera to the surface. We need to center the
	 * SurfaceView because not all devices have cameras that support preview
	 * sizes at the same aspect ratio as the device's display.
	 */
	class Preview extends ViewGroup implements SurfaceHolder.Callback,
			View.OnClickListener, CompoundButton.OnCheckedChangeListener {
		private final String TAG = "Preview";

		SurfaceView mSurfaceView;
		SurfaceHolder mHolder;
		Size mPreviewSize;
		List<Size> mSupportedPreviewSizes;
		Camera mCamera;
		DetectView mDetectView;
//		TextView mInfoView;
		private CheckBox mCkFlashLight = null;
		private CheckBox mCkBack = null;

		Preview(Context context) {
			super(context);

			mSurfaceView = new SurfaceView(context);
			addView(mSurfaceView);

//			mInfoView = new TextView(context);
//			addView(mInfoView);

			mDetectView = new DetectView(context);
			addView(mDetectView);

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

			// Install a SurfaceHolder.Callback so we get notified when the
			// underlying surface is created and destroyed.
			mHolder = mSurfaceView.getHolder();
			mHolder.addCallback(this);
			// mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		public void onClick(View view) {
			//
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
			Camera.Parameters parameters = mCamera.getParameters();
			if (parameters == null) {
				return;
			}
			List<String> flashModes = parameters.getSupportedFlashModes();
			if (flashModes == null) {
				return;
			}
			String currentMode = parameters.getFlashMode();
			String flashMode = Camera.Parameters.FLASH_MODE_OFF;
			if (turnOn) {
				flashMode = Camera.Parameters.FLASH_MODE_TORCH;
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
				mSupportedPreviewSizes = mCamera.getParameters()
						.getSupportedPreviewSizes();
				requestLayout();
			}
		}

		public void switchCamera(Camera camera) {
			setCamera(camera);
			try {
				camera.setPreviewDisplay(mHolder);
			} catch (IOException exception) {
				Log.e(TAG, "IOException caused by setPreviewDisplay()",
						exception);
			}
			Camera.Parameters parameters = camera.getParameters();
			parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
			requestLayout();

			camera.setParameters(parameters);
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			// We purposely disregard child measurements because act as a
			// wrapper to a SurfaceView that centers the camera preview instead
			// of stretching it.
			final int width = resolveSize(getSuggestedMinimumWidth(),
					widthMeasureSpec);
			final int height = resolveSize(getSuggestedMinimumHeight(),
					heightMeasureSpec);
			setMeasuredDimension(width, height);

			if (mSupportedPreviewSizes != null) {
				mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes,
						height, width);// 竖屏模式，寬高颠倒
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
					final int scaledChildWidth = previewWidth * height
							/ previewHeight;
					child.layout((width - scaledChildWidth) / 2, 0,
							(width + scaledChildWidth) / 2, height);
					mDetectView.layout((width - scaledChildWidth) / 2, 0,
							(width + scaledChildWidth) / 2, height);
				} else {
					final int scaledChildHeight = previewHeight * width
							/ previewWidth;
					child.layout(0, (height - scaledChildHeight) / 2, width,
							(height + scaledChildHeight) / 2);
					mDetectView.layout(0, (height - scaledChildHeight) / 2,
							width, (height + scaledChildHeight) / 2);
				}
				getChildAt(1).layout(l, t, r, b);

				mCkFlashLight.layout((int) (r - 48 * mDensity), (int) (t + 18 * mDensity), (int) (r - 8 * mDensity), (int) (t + 58 * mDensity));
				mCkBack.layout((int) (l + 8 * mDensity), (int) (t + 18 * mDensity), (int) (l + 48 * mDensity), (int) (t + 58 * mDensity));
			}
		}

		public void surfaceCreated(SurfaceHolder holder) {
			// The Surface has been created, acquire the camera and tell it
			// where
			// to draw.
			try {
				if (mCamera != null) {
					mCamera.setPreviewDisplay(holder);
				}
			} catch (IOException exception) {
				Log.e(TAG, "IOException caused by setPreviewDisplay()",
						exception);
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

		public void surfaceChanged(SurfaceHolder holder, int format, int w,
                                   int h) {
			// Now that the size is known, set up the camera parameters and
			// begin
			// the preview.
			Camera.Parameters parameters = mCamera.getParameters();
			parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
			parameters.setPreviewFormat(ImageFormat.NV21);
			requestLayout();
			// int iff = ImageFormat.NV21;
			// int f = parameters.getPreviewFormat();
			// List<Integer> lf = parameters.getSupportedPreviewFormats();
			// System.out.println("prefiew format"+f+" <"+
			// Arrays.toString(lf.toArray()));
			mDetectView.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
//			mInfoView.setText("preview：" + mPreviewSize.width + ","
//					+ mPreviewSize.height);
			mCamera.setParameters(parameters);
			mCamera.startPreview();
		}

		public void showBorder(int[] border, boolean match) {
			mDetectView.showBorder(border, match);
		}

	}

	/**
	 * the view show bank card border.
	 */
	class DetectView extends View {
		Paint paint;
		int[] border;
		String result;
		boolean match;
		int previewWidth;
		int previewHeight;

		public void showBorder(int[] border, boolean match) {
			this.border = border;
			this.match = match;
			postInvalidate();
		}

		public DetectView(Context context) {
			super(context);
			paint = new Paint();
			paint.setColor(0xffff0000);
			// paint.setStrokeWidth(3);

		}

		boolean mode_vertical = false;

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

//			if (border != null) {
//				paint.setStrokeWidth(3);
//				int height = getWidth();
//				float scale = getWidth() / (float) previewHeight;
//				c.drawLine(border[0] * scale, border[1] * scale, border[2]
//						* scale, border[3] * scale, paint);
//				c.drawLine(border[2] * scale, border[3] * scale, border[4]
//						* scale, border[5] * scale, paint);
//				c.drawLine(border[4] * scale, border[5] * scale, border[6]
//						* scale, border[7] * scale, paint);
//				c.drawLine(border[6] * scale, border[7] * scale, border[0]
//						* scale, border[1] * scale, paint);
//
//			}
//            if (match) {
//                paint.setColor(0xff00ff00);
//                paint.setStrokeWidth(20);
//            } else {
//                paint.setColor(0xffff0000);
//                paint.setStrokeWidth(3);
//            }

			float left, top, right, bottom;
			if (mode_vertical) {// vertical
				float dis = 1 / 10f;
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
			c.clipRect(new RectF(left, top, right, bottom),
					Region.Op.DIFFERENCE);
//			c.drawColor(0xAA666666);
			c.restore();
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

			paint.setColor(Color.parseColor("#8A000000"));
			c.drawRect(new RectF(0, 0, left + right, top), paint);
			c.drawRect(new RectF(0, top, left, bottom), paint);
			c.drawRect(new RectF(right, top, left + right, bottom), paint);
			c.drawRect(new RectF(0, bottom, left + right, bottom + top), paint);

			c.rotate(90, (left + right) / 2, (top + bottom) / 2);
			paint.setTextAlign(Paint.Align.CENTER);
			paint.setColor(Color.parseColor("#FFD0CFD1"));
			paint.setTextSize(50);
			c.drawText("请将银行卡正面置于此区域", (left + right) / 2, (top + bottom) / 2 - 25, paint);
			c.drawText("尝试对齐边缘", (left + right) / 2, (top + bottom) / 2 + 25, paint);
		}

	}

}