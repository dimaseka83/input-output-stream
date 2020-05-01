package com.android.praktikum.inputoutputstream2;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask {
    private static final String TAG = "Download Task";
    private Context context;
    private Button buttonText;
    private String downloadUrl = "", downloadFileName ="";

    public DownloadTask(Context context, Button buttonText, String downloadUrl) {
        this.context = context;
        this.buttonText = buttonText;
        this.downloadUrl = downloadUrl;

        downloadFileName = downloadUrl.replace(Utils.mainUrl, "");
        Log.e(TAG, downloadFileName);

        new DownloadingTask().execute();
    }

    private class DownloadingTask extends AsyncTask<Void, Void, Void> {
        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            buttonText.setEnabled(false);
            buttonText.setText(R.string.downloadStarted);
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
                    buttonText.setEnabled(true);
                    buttonText.setText(R.string.downloadCompleted);
                } else {
                    buttonText.setText(R.string.downloadFailed);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            buttonText.setEnabled(true);
                            buttonText.setText(R.string.downloadAgain);
                        }
                    }, 3000);
                    Log.e(TAG, "Download Failed");
                }
            } catch (Exception e) {
                e.printStackTrace();

                buttonText.setText(R.string.downloadFailed);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        buttonText.setEnabled(true);
                        buttonText.setText(R.string.downloadAgain);
                    }
                }, 3000);
                Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());
            }
            super.onPostExecute(result);
        }


        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(downloadUrl);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.connect();

                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                            + " " + c.getResponseMessage());
                }

                if (new CheckForSDCard().isSDCardPresent()) {
                    apkStorage = new File(
                            Environment.getExternalStorageDirectory() + "/"
                                    + Utils.downloadDirectory);
                } else
                    Toast.makeText(context, "Ooops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                    Log.e(TAG, "Directory Created");
                }
                outputFile = new File(apkStorage, downloadFileName);
                if(!outputFile.exists()){
                    outputFile.createNewFile();
                    Log.e(TAG,"File Created");
                }

                FileOutputStream fos = new FileOutputStream(outputFile);

                InputStream is = c.getInputStream();

                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }

                fos.close();
                is.close();

            }catch(Exception e){
                    e.printStackTrace();
                    outputFile = null;
                    Log.e(TAG, "Download Error Exception " + e.getMessage());
                }
                return null;
        }
    }
}
