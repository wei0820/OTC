package com.tools.payhelper;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;

public class BitMapUtil {
    public static String getPicturePathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= 19) {
            return getPicturePathFromUriAboveApi19(context, uri);
        } else {
            return getPicturePathFromUriBelowAPI19(context, uri);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String getPicturePathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) { // MediaProvider
                // 使用':'分割
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};
                filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    private static String getPicturePathFromUriBelowAPI19(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    }


    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }


    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    /**
     * 对图片压缩
     */

    public static Bitmap compressPicture(String imgPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgPath, options);
        Log.e("log", "未压缩之前图片的宽：" + options.outWidth + "--未压缩之前图片的高："
                + options.outHeight + "--未压缩之前图片大小:" + options.outWidth * options.outHeight * 4 / 1024 / 1024 + "M");

        options.inSampleSize = calculateInSampleSize(options, 150, 150);
        Log.e("log", " inSampleSize:" + options.inSampleSize);
        options.inJustDecodeBounds = false;
        Bitmap afterCompressBm = BitmapFactory.decodeFile(imgPath, options);
//      //默认的图片格式是Bitmap.Config.ARGB_8888
        Log.e("log", " 图片的宽：" + afterCompressBm.getWidth() + "--图片的高："
                + afterCompressBm.getHeight() + "--图片大小:" + afterCompressBm.getWidth() * afterCompressBm.getHeight() * 4 / 1024 / 1024 + "M");
        return afterCompressBm;
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 4;
            final int halfWidth = width / 4;

            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    // 把filePath地址对应的图片转换成Bitmap，然后再将bitmap转换成Base64字符串String
    public static String bitmapToString(String filePath) {
        Bitmap bm = getSmallBitmap(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // 1.5M的压缩后在100Kb以内，测试得值,压缩后的大小=94486字节,压缩后的大小=74473字节
        // 这里的JPEG 如果换成PNG，那么压缩的就有600kB这样.
        // 实际项目中，可以根据需要考虑图片压缩以及压缩的质量。
        bm.compress(Bitmap.CompressFormat.PNG, 40, baos);
        byte[] b = baos.toByteArray();

        // 在这里获取到图片转换后的字符串，然后就可以将这个字符串当做普通的String字符串参数传给后台
        // 如果有很多张图片要上传，那么可以考虑将转换后的Base64字符串添加到一个List里面，一并传给后台。
        return Base64.encodeToString(b, Base64.NO_WRAP);
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = uploadcalculateInSampleSize(options, 140, 140);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }


    //计算图片的缩放质量
    public static int uploadcalculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
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
    //把bitmap转换成String
    public static String ImgageToString(String filePath) {
        Bitmap bm = getSmallBitmap(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        Log.d("jack_photo", "压缩后的大小=" + b.length);//1.5M的压缩后在100Kb以内，测试得值,压缩后的大小=94486,压缩后的大小=74473
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
    public static byte[] bitmap2Byte(Bitmap bitmap) {
        if (null == bitmap) throw new NullPointerException();
        // if (null == bitmap) return null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //把bitmap100%高质量压缩 到 output对象里
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return outputStream.toByteArray();
    }
    public static String byte2Base64(byte[] imageByte) {
        if(null == imageByte) return null;
        return Base64.encodeToString(imageByte, Base64.DEFAULT);
    }

}