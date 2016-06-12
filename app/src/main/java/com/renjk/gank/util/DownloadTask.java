package com.renjk.gank.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by admin on 2016/6/7.
 */
public class DownloadTask extends AsyncTask<String, Integer, String> {

    private String urlStr = "";
    private Context context;

    public DownloadTask(Context context,String url) {
        this.urlStr = url;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {

        OutputStream output = null;
        try {
            URL url = null;
            url = new URL(urlStr);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            InputStream input = urlConn.getInputStream();
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "gank");
            if (!dir.exists()) {
                dir.mkdir();
            }

            File file = new File(dir, System.currentTimeMillis() + ".jpg");
            if (!file.exists()) {
                file.createNewFile();
                output = new FileOutputStream(file);
                byte buffer[] = new byte[1024];
                int inputSize = -1;
                while ((inputSize = input.read(buffer)) != -1) {
                    output.write(buffer, 0, inputSize);
                }
                output.flush();
            }
            return "success";

        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }


    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Toast.makeText(context,s,Toast.LENGTH_LONG).show();
    }
}




