package com.android.praktikum.inputoutputstream2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static Button downloadPdf, downloadDoc, downloadZip, downloadVideo, downloadMp3, openDownloadedFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setListener();

    }
    private void initViews() {
        downloadPdf = findViewById(R.id.downloadPdf);
        downloadDoc = findViewById(R.id.downloadDoc);
        downloadZip = findViewById(R.id.downloadZip);
        downloadVideo = findViewById(R.id.downloadVideo);
        downloadMp3 = findViewById(R.id.downloadMp3);
        openDownloadedFolder = findViewById(R.id.openDownloadedFolder);
    }

    private void setListener() {
        downloadPdf.setOnClickListener(this);
        downloadDoc.setOnClickListener(this);
        downloadZip.setOnClickListener(this);
        downloadVideo.setOnClickListener(this);
        downloadMp3.setOnClickListener(this);
        openDownloadedFolder.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.downloadPdf:
                if (isConnectingToInternet())
                    new DownloadTask(MainActivity.this, downloadPdf, Utils.downloadPdfUrl);
                else
                    Toast.makeText(MainActivity.this, "Oops!! There is no internet connection. Please enable internet connection and try again.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.downloadDoc:
                if (isConnectingToInternet())
                    new DownloadTask(MainActivity.this, downloadDoc, Utils.downloadDocUrl);
                else
                    Toast.makeText(MainActivity.this, "Oops!! There is no internet connection. Please enable internet connection and try again.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.downloadZip:
                if (isConnectingToInternet())
                    new DownloadTask(MainActivity.this, downloadZip, Utils.downloadZipUrl);
                else
                    Toast.makeText(MainActivity.this, "Oops!! There is no internet connection. Please enable internet connection and try again.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.downloadVideo:
                if (isConnectingToInternet())
                    new DownloadTask(MainActivity.this, downloadVideo, Utils.downloadVideoUrl);
                else
                    Toast.makeText(MainActivity.this, "Oops!! There is no internet connection. Please enable internet connection and try again.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.downloadMp3:
                if (isConnectingToInternet())
                    new DownloadTask(MainActivity.this, downloadMp3, Utils.downloadMp3Url);
                else
                    Toast.makeText(MainActivity.this, "Oops!! There is no internet connection. Please enable internet connection and try again.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.openDownloadedFolder:
                openDownloadedFolder();
                break;

        }
    }

    private void openDownloadedFolder() {

        if (new CheckForSDCard().isSDCardPresent()){
            File apkStorage = new File(
                    Environment.getExternalStorageDirectory() + "/" + Utils.downloadDirectory);

            if(!apkStorage.exists())
                Toast.makeText(MainActivity.this, "Right now there is no directory. Please download some file first.",Toast.LENGTH_SHORT).show();
            else {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                    + "/" + Utils.downloadDirectory);
                intent.setDataAndType(uri, "file/*");
                startActivity(Intent.createChooser(intent, "Open Download Folder"));
            }
        }else {
            Toast.makeText(MainActivity.this, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

}
