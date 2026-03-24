package com.tools.payhelper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateAlertDialog extends AlertDialog {
    private String message;
    private ProgressDialog progressDialog;
    private String fileName = "jingyuotcpay.apk";
//    private String fileName = "duobao.apk";

    private String _url;

    public UpdateAlertDialog(Context context, String url) {
        super(context);
        this._url = url;
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_update);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        TextView messageText = findViewById(R.id.message);
        messageText.setText(message);

        // 本地下載升級 按鈕
        findViewById(R.id.updateBtn).setOnClickListener(v -> {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("正在下載...");
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);

            // 下載任務
            final DownloadTask downloadTask = new DownloadTask(getContext());
            downloadTask.execute(String.format("%s?t=%d", _url, System.currentTimeMillis()));
            progressDialog.setOnCancelListener(dialog -> downloadTask.cancel(true));
        });

        // 網頁下載升級 按鈕
        View cancelBtn = findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(_url));
            getContext().startActivity(intent);
        });
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    @SuppressLint("StaticFieldLeak")
    private class DownloadTask extends AsyncTask<String, Integer, String> {
        private Context context;
        private PowerManager.WakeLock mWakeLock;

        DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
            mWakeLock.acquire();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                // 【核心修正 1】：準備檔案路徑並檢查是否存在
                File downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File apkFile = new File(downloadFolder, fileName);

                // 如果檔案已存在，先刪除它，避免 EEXIST 錯誤
                if (apkFile.exists()) {
                    boolean deleted = apkFile.delete();
                    Log.d("UpdateAlertDialog", "舊檔案存在，刪除結果: " + deleted);
                }

                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();

                // 【核心修正 2】：設定超時，避免後端沒反應讓 App 乾等
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "伺服器錯誤: " + connection.getResponseCode() + " " + connection.getResponseMessage();
                }

                int fileLength = connection.getContentLength();
                input = connection.getInputStream();

                // 建立新的檔案輸出流
                output = new FileOutputStream(apkFile);

                byte[] data = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    if (isCancelled()) {
                        return null;
                    }
                    total += count;
                    if (fileLength > 0)
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null) output.close();
                    if (input != null) input.close();
                } catch (IOException ignored) {}
                if (connection != null) connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            progressDialog.dismiss();

            if (result != null) {
                // 這裡會顯示你影片中看到的那個灰底黑字警告
                Toast.makeText(context, "下載失敗: " + result, Toast.LENGTH_LONG).show();
                return; // 下載失敗就不執行安裝
            }

            // 下載成功，執行安裝
            Toast.makeText(context, "下載完成，準備安裝", Toast.LENGTH_SHORT).show();
            File apkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);

            Intent installIntent = new Intent(Intent.ACTION_VIEW);
            installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", apkFile);
                installIntent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                installIntent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            }
            context.startActivity(installIntent);
        }
    }
}