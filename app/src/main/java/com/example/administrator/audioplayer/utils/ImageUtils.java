package com.example.administrator.audioplayer.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by on 2017/2/21 0021.
 */

public class ImageUtils {


    /**
     * 根据一个bitmap创建一个模糊的drawable，利用RenderScript,默认压缩倍数20倍
     * @param bitmap
     * @param context
     * @return
     */
    public static Drawable createBlurredImageFromBitmap(Bitmap bitmap, Context context) {
        //设置压缩倍数，让其更加模糊
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 20;
        return createBlurredImageFromBitmap(bitmap, context, options);
    }


    /**
     * 根据一个bitmap创建一个模糊的drawable，利用RenderScript
     * @param bitmap
     * @param context
     * @Param options 图片的参数
     * @return
     */
    public static Drawable createBlurredImageFromBitmap(Bitmap bitmap, Context context, BitmapFactory.Options options) {

        //把传进来的bitmap进行内存压缩
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(imageInByte);
        //获取压缩后的bitmap，该bitmap用来作为渲染的输入和输出
        Bitmap blurTemplate = BitmapFactory.decodeStream(bis, null, options);


        // 创建RenderScript内核对象
        RenderScript rs = RenderScript.create(context);

        // 创建一个模糊效果的RenderScript的工具对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
        Allocation input = Allocation.createFromBitmap(rs, blurTemplate);
        Allocation output = Allocation.createTyped(rs, input.getType());

        // 设置渲染的模糊程度, 25f是最大模糊度
        blurScript.setRadius(8f);
        // 设置blurScript对象的输入内存
        blurScript.setInput(input);
        // 将输出数据保存到输出内存中
        blurScript.forEach(output);

        // 将output Allocation数据填充到输出图片中
        output.copyTo(blurTemplate);

        return new BitmapDrawable(context.getResources(), blurTemplate);

    }





    /**
     * 从文件中获取像素压缩过的图片
     * @param file
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getCompassImage(File file, int width, int height) {
        int sampleSize = 1;

        //设置inJustDecodeBounds为true，先获取图片的宽和高
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        int outwidth = options.outWidth;
        int outheight = options.outHeight;
        //计算出sampleSize（压缩倍数）;
        while (outwidth > width && outheight > height) {
            sampleSize <<= 1;
            outwidth >>= 1;
            outheight >>= 1;
        }

        //设置inJustDecodeBounds为false，获取整个图片，设置压缩倍数
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        if(bitmap != null) {
            //若宽高不同设置的值，则对图片进行缩放
            if(outwidth != width || outheight != height) {
                Bitmap resultBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

                //回收bitmap
                if(resultBitmap != bitmap) {
                    bitmap.recycle();
                }
                bitmap = resultBitmap;
            }
        }

        return bitmap;
    }



}
