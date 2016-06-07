package com.renjk.gank.request;


import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by admin on 2016/6/7.
 */
public class OkHttpManager {

    private static OkHttpManager mInstance;
    private OkHttpClient mOkHttpClient;
    private static final String TAG = "OkHttpManager";

    private OkHttpManager()
    {
        mOkHttpClient = new OkHttpClient();
    }

    public static OkHttpManager getInstance()
    {
        if (mInstance == null)
        {
            synchronized (OkHttpManager.class)
            {
                if (mInstance == null)
                {
                    mInstance = new OkHttpManager();
                }
            }
        }
        return mInstance;
    }


    public  void downLoad(final String url, final String destFileDir,final String fileName, final GankCallBack gankCallBack)
    {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = mOkHttpClient.newCall(request);


        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                gankCallBack.onFailure(call);
            }

            @Override
            public void onResponse(Call call, Response response){
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                is = response.body().byteStream();


                File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), destFileDir);
                if (!dir.exists()) {
                    dir.mkdir();
                }

                File file = new File(dir,fileName);
                try {
                    if(!file.exists()){
                        file.createNewFile();
                        fos = new FileOutputStream(file);
                        while ((len = is.read(buf)) != -1)
                        {
                            fos.write(buf, 0, len);
                        }
                        fos.flush();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                gankCallBack.onRespose(call,response);

            }
        });


    }


    public interface  GankCallBack{

        void onRespose(Call call,Response response);

        void onFailure(Call call);
    }

}
