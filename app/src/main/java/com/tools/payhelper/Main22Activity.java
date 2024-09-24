package com.tools.payhelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.jingyu.pay.ui.bankcard.BankCardDateModel;
import com.jingyu.pay.ui.bankcard.BankCardListActivity;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;
import com.tools.payhelper.pay.ToastManager;
import com.tools.payhelper.pay.ui.bankcard.AddBankCardData;
import com.tools.payhelper.pay.ui.bankcard.AddPayCardDialog;

import java.io.ByteArrayOutputStream;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Main22Activity extends AppCompatActivity implements View.OnClickListener {
    private EditText  name,tel,googleedt,usernaem,eusername,payedt,pd;
    private AddPayCardDialog.OnAddCallback onAddCallback;
    private AddPayCardDialog.OnAddBanKListCallback onAddBanKListCallback;
    private Handler handlerLoading = new Handler();
    private Button btn_scan, btn_album;
    private EditText padd;
    public static final String CAMERA = Manifest.permission.CAMERA;
    public static final String WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final int SCAN_RESULT = 1120;
    public final static int DEVICE_PHOTO_REQUEST = 1234;
    BankCardDateModel bankCardDateModel = new BankCardDateModel();
    private Button messageButton;
    public void setOnAddCallback(AddPayCardDialog.OnAddCallback onAddCallback) {
        this.onAddCallback = onAddCallback;
    }

    public  void  setAddBankCallback(AddPayCardDialog.OnAddBanKListCallback onAddBanKListCallback){
        this.onAddBanKListCallback = onAddBanKListCallback;

    }

    public interface OnAddCallback {
        void onAdd(String cardNo, String bankName, String bankCode);
    }
    public  interface  OnAddBanKListCallback{
        void onResponse(AddBankCardData addBankCardData);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        setContentView(R.layout.activity_main22);
        initView();
    }

    private void initView() {
        btn_scan = findViewById(R.id.scanbtn);
//        btn_album = findViewById(R.id.btn_album);
//        tv_content = findViewById(R.id.tv_content);
        btn_scan.setOnClickListener(this);
//        btn_album.setOnClickListener(this);
        messageButton = findViewById(R.id.message);
        messageButton.setOnClickListener(this);

        name = findViewById(R.id.bank_card);
        pd = findViewById(R.id.bank_card_no);
        tel = findViewById(R.id.teledt);
        googleedt = findViewById(R.id.googleedt);
        usernaem = findViewById(R.id.nameedt);
        eusername = findViewById(R.id.enameedt);
        payedt = findViewById(R.id.payedt);
        padd = findViewById(R.id.addurl);


        findViewById(R.id.closeBtn).setOnClickListener(v -> {
            Main22Activity.this.finish();

        });




        findViewById(R.id.okBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n = "支付宝扫码";
                String p = pd.getText().toString();
                String t = padd.getText().toString();
                String google = googleedt.getText().toString();
                String username = usernaem.getText().toString();
                String euserName = eusername.getText().toString();
                String pay = payedt.getText().toString().isEmpty() ?"50000" : payedt.getText().toString();
                Float payF = Float.parseFloat(pay);
                bankCardDateModel.setBankCard(Main22Activity.this, n, p, t, payF, google, username, euserName, new BankCardDateModel.BankCardResponse() {
                    @Override
                    public void getResponse(@NonNull String s) {
                        if (!s.isEmpty()){
                            AddBankCardData addBankCardData = new Gson().fromJson(s,AddBankCardData.class);
                            if (addBankCardData !=null){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (addBankCardData.code==0){
                                            ToastManager.showToastCenter(Main22Activity.this,addBankCardData.msg);
                                            Main22Activity.this.finish();
//                                            Intent intent = new Intent();
//                                            intent.setClass(Main22Activity.this, BankCardListActivity.class);
//                                            startActivity(intent);

                                        }else {
                                            ToastManager.showToastCenter(Main22Activity.this,addBankCardData.msg);
                                        }
                                    }
                                });

                            }
                        }
                    }
                });


            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.scanbtn://二维码扫描
//                checkCamera();
//                break;

            case R.id.scanbtn://打开相册
                checkStorage();
                break;
            case R.id.message:
                Uri uri = Uri.parse("https://www.online-qr-scanner.com/zh-tw/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;

        }
    }

    private void checkStorage() {
        PermissionX.init(this).permissions(WRITE_STORAGE).onExplainRequestReason(new ExplainReasonCallback() {
            @Override
            public void onExplainReason(ExplainScope scope, List<String> deniedList) {
                scope.showRequestReasonDialog(deniedList, "读取相册需要该权限", "允许");
            }
        })
                .onForwardToSettings(new ForwardToSettingsCallback() {
                    @Override
                    public void onForwardToSettings(ForwardScope scope, List<String> deniedList) {
                        scope.showForwardToSettingsDialog(deniedList, "需要在应用程序设置中手动开启", "OK");
                    }
                })
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
                            Intent intent = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, DEVICE_PHOTO_REQUEST);
                        } else {
                            Toast.makeText(getApplicationContext(), "权限已拒绝", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkCamera() {
        PermissionX.init(this).permissions(CAMERA).onExplainRequestReason(new ExplainReasonCallback() {
            @Override
            public void onExplainReason(ExplainScope scope, List<String> deniedList) {
                scope.showRequestReasonDialog(deniedList, "扫描二维码需要开启摄像头", "允许");
            }
        }).onForwardToSettings(new ForwardToSettingsCallback() {
            @Override
            public void onForwardToSettings(ForwardScope scope, List<String> deniedList) {
                scope.showForwardToSettingsDialog(deniedList, "需要在应用程序设置中手动开启", "OK");
            }
        }).request(new RequestCallback() {
            @Override
            public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                if (allGranted) {
//                    startActivityForResult(
////                            new Intent(MainActivity.this, ScanQRCodeActivity.class)
//                            , SCAN_RESULT);
                } else {
                    Toast.makeText(getApplicationContext(), "权限已拒绝", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    Bitmap generatedQRCode;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //系统相册返回请求


        switch (requestCode) {
            case DEVICE_PHOTO_REQUEST:
                if (data != null) {
                    try {
                        Uri uri = data.getData();
                        bitmaptoBase(uri);

                        String imagePath = BitMapUtil.getPicturePathFromUri(this, uri);

//                        Log.d("jack_photo",BitMapUtil.bitmapToString(imagePath));

                        //对获取到的二维码照片进行压缩
                        Bitmap generatedQRCode = BitMapUtil.compressPicture(imagePath);
                        Result result = setZxingResult(generatedQRCode);
                        if (result == null) {
                            ToastManager.showToastCenter(this,"解析失败,请确认档案为正确的二维码图档");
                        } else {
                            padd.setText(result.getText());
                        }
                    }catch (Exception e){
                        ToastManager.showToastCenter(this,"解析失败,请确认档案为正确的二维码图档"+e.getLocalizedMessage());
                    }

                }
                break;
        }


        //扫描二维码返回结果码
//        switch (resultCode) {
//            case ScanQRCodeActivity.SCAN_SUCCESS:
//                tv_content.setText(data.getStringExtra("success_result"));
//                break;
//
//            case ScanQRCodeActivity.SCAN_FAIL:
//                tv_content.setText(data.getStringExtra("fail_result"));
//                break;
//        }
    }


    private static Result setZxingResult(Bitmap bitmap) {
        if (bitmap == null) return null;
        int picWidth = bitmap.getWidth();
        int picHeight = bitmap.getHeight();
        int[] pix = new int[picWidth * picHeight];
        //Log.e(TAG, "decodeFromPicture:图片大小： " + bitmap.getByteCount() / 1024 / 1024 + "M");
        bitmap.getPixels(pix, 0, picWidth, 0, 0, picWidth, picHeight);
        //构造LuminanceSource对象
        RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(picWidth
                , picHeight, pix);
        BinaryBitmap bb = new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));
        //因为解析的条码类型是二维码，所以这边用QRCodeReader最合适。
        QRCodeReader qrCodeReader = new QRCodeReader();
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        hints.put(DecodeHintType.TRY_HARDER, true);

        int width = bitmap.getWidth(), height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        bitmap.recycle();
        bitmap = null;
        RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
        BinaryBitmap bBitmap = new BinaryBitmap(new HybridBinarizer(source));
        MultiFormatReader reader = new MultiFormatReader();
        Result result;
        try {
            result = reader.decode(bb);
            return result;
        } catch (NotFoundException  e) {
            e.printStackTrace();
            return null;
        }


    }

    private void bitmaptoBase(Uri uri){
        Log.d("encode","in");


        try {
            //get the image path
            String[] projection = {MediaStore.Images.Media.DATA};
            CursorLoader cursorLoader = new CursorLoader(this,uri,projection,null,null,null);
            Cursor cursor = cursorLoader.loadInBackground();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();

            String path = cursor.getString(column_index);
            Log.d("encode","real path: "+path);
            encode(path);
        } catch (Exception ex) {
            Log.e("encode", "failed." + ex.getMessage());
        }
    }



    private void encode(String path) {


        Bitmap bm = getSmallBitmap(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        Log.d("encode", "压缩后的大小=" + b.length);//1.5M的压缩后在100Kb以内，测试得值,压缩后的大小=94486,压缩后的大小=74473
        //convert to byte array
        Log("encode","encodeString: "+"data:image/png;base64,"+Base64.encodeToString(b,Base64.NO_WRAP));
//        padd.setText(Base64.encodeToString(b,Base64.NO_WRAP));
        copy(Base64.encodeToString(b,Base64.NO_WRAP),this);
    }

    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 240, 400);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }


    public static void Log(String tag, String msg) {  //信息太长,分段打印
        //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
        //  把4*1024的MAX字节打印长度改为2001字符数
        int max_str_length = 2001 - tag.length();
        //大于4000时
        while (msg.length() > max_str_length) {
            Log.i(tag, msg.substring(0, max_str_length));
            msg = msg.substring(max_str_length);

        }
        //剩余部分
        Log.i(tag, msg);
    }
    public static void copy(String content, Context context)
    {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }
    /**
     * 实现粘贴功能
     * add by wangqianzhou
     * @param context
     * @return
     */
    public static String paste(Context context)
    {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }

}

