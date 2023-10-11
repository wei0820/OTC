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

import com.tools.payhelper.BuildConfig;
import com.tools.payhelper.R;

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
    private boolean isForcedUpdate = false;
    private String fileName = "jingyupay.apk";
    private String _url;


    public UpdateAlertDialog(Context context,String url ) {
        super(context);
        this._url = url;
    }

    protected UpdateAlertDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected UpdateAlertDialog(Context context, int themeResId) {
        super(context, themeResId);
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

        findViewById(R.id.updateBtn).setOnClickListener(v -> {

            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("正在下载...");
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgressDrawable(getContext().getResources().getDrawable(R.drawable.load_msg_progress));

            final DownloadTask downloadTask = new DownloadTask(getContext());
            downloadTask.execute(String.format("%s?t=%d", _url, System.currentTimeMillis()));
            progressDialog.setOnCancelListener(dialog -> downloadTask.cancel(true));
        });

        View cancelBtn = findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(v -> {

            dismiss();
        });

        if (isForcedUpdate) {
            cancelBtn.setVisibility(View.GONE);
        } else {
            cancelBtn.setVisibility(View.VISIBLE);
        }
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public void setIsForcedUpdate(boolean isForcedUpdate) {
        this.isForcedUpdate = isForcedUpdate;
    }

    @SuppressLint("StaticFieldLeak")
    private  class DownloadTask extends AsyncTask<String,Integer,String> {
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
        protected String doInBackground(String... url) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            Log.d("UpdateAlertDialog",url.toString());
            try {
                URL urll=new URL(url[0]);
                connection = (HttpURLConnection) urll.openConnection();
                connection.connect();
                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();
                }
                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();
                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName));
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        //在调用这个方法后，执行onProgressUpdate(Progress... values)，
                        //运行在主线程，用来更新pregressbar
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }
                if (connection != null)
                    connection.disconnect();
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
                Toast.makeText(context, "监控下载失败，请重新下载" + result, Toast.LENGTH_LONG).show();
                Log.d("UpdateAlertDialog",result);

            }
            else {
                Toast.makeText(context,"监控下载完成", Toast.LENGTH_SHORT).show();
            }


            Intent installIntent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Toast.makeText(context,"1", Toast.LENGTH_SHORT).show();

                installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                installIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName));
                installIntent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                Toast.makeText(context,"2", Toast.LENGTH_SHORT).show();

                installIntent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName)), "application/vnd.android.package-archive");
                installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(installIntent);
        }

    }
}
