package com.ailicai.app.common.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Base64;
import android.util.SparseArray;
import android.view.View;

import com.ailicai.app.MyApplication;
import com.huoqiu.framework.util.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class BitmapUtil {
    private static BitmapUtil sInstance;

    private SparseArray<Bitmap> mBitmapCachedMap;

    private BitmapUtil() {
        if (mBitmapCachedMap == null) {
            mBitmapCachedMap = new SparseArray<Bitmap>();
        }
    }

    public static BitmapUtil getInstance() {
        if (sInstance == null) {
            sInstance = new BitmapUtil();
        }
        return sInstance;
    }

    public Bitmap getBitmap(int id) {
        if (mBitmapCachedMap.get(id) != null && !mBitmapCachedMap.get(id).isRecycled()) {
            return mBitmapCachedMap.get(id);
        } else {
            mBitmapCachedMap.remove(id);
            Bitmap bitmap = BitmapFactory.decodeResource(MyApplication.getInstance().getResources(), id);
            mBitmapCachedMap.put(id, bitmap);
            return bitmap;
        }
    }

    /**
     * 判断Bitmap对象是否有效
     *
     * @param bmp Bitmap对象
     * @return true if bitmap is not null and not be recycled
     */
    public static boolean isBitmapAvailable(Bitmap bmp) {
        return bmp != null && !bmp.isRecycled();
    }

    /**
     * Bitmap对象转换为二进制数据
     *
     * @param srcBmp Bitmap对象
     * @return 二进制数据
     */
    public static byte[] convertBitmapToBytes(Bitmap srcBmp) {
        if (isBitmapAvailable(srcBmp)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            srcBmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        }
        return null;
    }

    /**
     * 二进制数值转换为Bitmap对象
     *
     * @param srcBytes 二进制数据
     * @return Bitmap对象, 使用完后注意回收
     */
    public static Bitmap convertBytesToBitmap(byte[] srcBytes) {
        if (srcBytes != null && srcBytes.length > 0) {
            return BitmapFactory.decodeByteArray(srcBytes, 0, srcBytes.length);
        }
        return null;
    }

    /**
     * 缩放Bitmap对象
     *
     * @param srcBmp Bitmap对象
     * @param width  缩放后的宽度
     * @param height 缩放后的高度
     * @return 缩放后的Bitmap对象
     */
    public static Bitmap resizeBitmap(Bitmap srcBmp, int width, int height) {
        Bitmap dstBmp = null;
        if (isBitmapAvailable(srcBmp)) {
            int w = srcBmp.getWidth();
            int h = srcBmp.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(((float) width / w), ((float) height / h));
            dstBmp = Bitmap.createBitmap(srcBmp, 0, 0, w, h, matrix, true);
        }
        return dstBmp;
    }

    /**
     * 读取Bitmap文件，并作缩放处理
     *
     * @param filePath Bitmap对象
     * @param width    缩放后的宽度
     * @param height   缩放后的高度
     * @return 缩放后的Bitmap对象, 使用完后注意回收
     */
    public static Bitmap getScaledBitmapFromFile(String filePath, int width, int height) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }

        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inPreferredConfig = Bitmap.Config.RGB_565;
        op.inPurgeable = true;
        op.inInputShareable = true;
        op.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, op);

        op.inSampleSize = (int) Math.max(((float) op.outWidth / width), ((float) op.outHeight / height));
        op.inJustDecodeBounds = false;
        op.outWidth = width;
        op.outHeight = height;

        return BitmapFactory.decodeFile(filePath, op);
    }

    public static Bitmap getScaledBitmapFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        return BitmapFactory.decodeFile(filePath);
    }

    /**
     * Bitmap对象转换为Drawable对象
     *
     * @param bmp Bitmap对象
     * @return Drawable对象
     */
    @SuppressWarnings("deprecation")
    public static Drawable convertBitmapToDrawable(Bitmap bmp) {
        return new BitmapDrawable(bmp);
    }

    /**
     * 无拉伸图压缩，并截取中间部分
     *
     * @param source    原图
     * @param newHeight 缩略图高度
     * @param newWidth  缩略图宽度
     * @return 缩略图
     */
    public static Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
        if (source == null || source.isRecycled()) {
            throw new IllegalArgumentException("source bitmap for scale is not available");
        }

        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        // The target rectangle for the new, scaled version of the source bitmap will now
        // be
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
    }

    /**
     * Drawable对象转换为Bitmap对象
     *
     * @param drawable Drawable对象
     * @return Bitmap对象, 使用完后注意回收
     */
    public static Bitmap convertDrawbaleToBitmap(Drawable drawable) {
        Bitmap dstBmp = null;
        if (drawable != null) {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();

            Bitmap.Config config =
                    (drawable.getOpacity() != PixelFormat.OPAQUE) ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
            dstBmp = Bitmap.createBitmap(width, height, config);
            Canvas canvas = new Canvas(dstBmp);
            drawable.setBounds(0, 0, width, height);
            drawable.draw(canvas);
        }
        return dstBmp;
    }

    /**
     * 获取带圆角的图片
     *
     * @param srcBmp     原图
     * @param round      圆角半径
     * @param roundColor 圆角填充颜色
     * @return 带圆角的Bitmap图片
     */
    public static Bitmap createRoundCornerBitmap(Bitmap srcBmp, float round, int roundColor) {
        Bitmap dstBmp = null;
        if (isBitmapAvailable(srcBmp)) {
            int width = srcBmp.getWidth();
            int height = srcBmp.getHeight();

            dstBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            Rect rect = new Rect(0, 0, width, height);
            Canvas canvas = new Canvas(dstBmp);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(roundColor);

            canvas.drawARGB(0, 0, 0, 0);
            canvas.drawRoundRect(new RectF(rect), round, round, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(srcBmp, rect, rect, paint);
        }
        return dstBmp;
    }

    /**
     * 获取带倒影的图片
     *
     * @param srcBmp 原图
     * @param gap    原图与倒影间的空隙
     * @return 带倒影效果的Bitmap图片
     */
    public static Bitmap createReflectionBitmap(Bitmap srcBmp, int gap) {
        Bitmap dstBmp = null;
        if (isBitmapAvailable(srcBmp)) {
            int width = srcBmp.getWidth();
            int height = srcBmp.getHeight();
            int tmpGap = Math.min(gap, height / 2);

            Matrix matrix = new Matrix();
            matrix.preScale(1, -1);

            Bitmap tmpBmp = Bitmap.createBitmap(srcBmp, 0, height / 2, width, height / 2, matrix, false);
            dstBmp = Bitmap.createBitmap(width, height + height / 2, Config.RGB_565);

            Canvas canvas = new Canvas(dstBmp);
            Paint paint = new Paint();
            canvas.drawBitmap(srcBmp, 0, 0, null);
            canvas.drawRect(0, height, width, height + tmpGap, paint);
            canvas.drawBitmap(tmpBmp, 0, height + tmpGap, null);

            LinearGradient shader = new LinearGradient(0, srcBmp.getHeight(),
                    0, dstBmp.getHeight() + tmpGap, 0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
            canvas.drawRect(0, height, width, dstBmp.getHeight() + tmpGap, paint);

            if (tmpBmp != null) {
                tmpBmp.recycle();
                tmpBmp = null;
            }
        }
        return dstBmp;
    }

    public void recycleBitmap(int iconEyeVisible) {
        if (mBitmapCachedMap.get(iconEyeVisible) != null) {
            Bitmap bitmap = mBitmapCachedMap.get(iconEyeVisible);
            mBitmapCachedMap.remove(iconEyeVisible);
            try {
                bitmap.recycle();
                bitmap = null;
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    /**
     * 将图片剪切为圆
     *
     * @param bitmap
     * @return
     */
    public static Bitmap clipImgToCircle(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getWidth() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 从view 得到图片
     *
     * @param view
     * @return
     */
    public static Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
    }


    /**
     * 获取合适的inSampleSize值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 资源文件生成合适的Bitmap.
     *
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    //把bitmap转换成String
    public static String bitmapToBase64String(String filePath) {

        if (!FileUtils.fileExist(filePath)) return "";

        Bitmap bm = getSmallBitmap(filePath);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    //获取缩放后的图片
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//不把图片读到内存中也可以获取图片宽高
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = getImageZoomValue(options, 1080, 1920);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    //计算图片的缩放值
    public static int getImageZoomValue(BitmapFactory.Options options,
                                        int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static File compressImage(String filePath) {
        Bitmap bitmap = getScaledBitmapFromFile(filePath);


        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int options = 50;
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, out);
        while (options > 40 && out.toByteArray().length / 1024 > 100) {
            out.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, out);
            options -= 10;
        }


        String path = Environment.getExternalStorageDirectory()+"/iwjw/photo/crop";
        File cropDirectory = new File(path);
        if (!cropDirectory.exists()) {
            cropDirectory.mkdirs();
        }
        File file = new File(path+"/"+ System.currentTimeMillis()+""+ UUID.randomUUID());
        FileOutputStream outputStream = null;
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file);
            byte[] contentInBytes = out.toByteArray();
            outputStream.write(contentInBytes);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
