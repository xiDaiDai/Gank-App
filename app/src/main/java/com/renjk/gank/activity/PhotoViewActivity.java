package com.renjk.gank.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.renjk.gank.R;
import com.renjk.gank.request.OkHttpManager;
import com.renjk.gank.util.DownloadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by admin on 2016/6/7.
 */
public class PhotoViewActivity extends SwipeBackActivity {
    private Toolbar toolbar;
    private PhotoView photoView;
    private String imageUrl;

    private String TAG = "PhotoViewActivity";
    private static final int SUCCESS = 0;
    private static final int FAIL = 1;

    private Handler mHanler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    Toast.makeText(PhotoViewActivity.this, "下载成功", Toast.LENGTH_LONG).show();
                    break;
                case FAIL:
                    Toast.makeText(PhotoViewActivity.this, "下载失败", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoview);
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);
        initView();

    }

    private void initView() {
        imageUrl = getIntent().getStringExtra("url");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        toolbar.setTitle(R.string.app_name);
        photoView = (PhotoView) findViewById(R.id.photoView);
        Glide.with(this).load(imageUrl).into(photoView);

        photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(PhotoViewActivity.this, "downloading", Toast.LENGTH_LONG).show();
                 dowloadImage();
//                 RxJavaDownLoad();
//                new DownloadTask(PhotoViewActivity.this, imageUrl).execute();
                return true;
            }
        });
    }

    //OkHttpDownLoad
    private void dowloadImage() {


        OkHttpManager.getInstance().downLoad(imageUrl, "gank", System.currentTimeMillis() + ".jpg", new OkHttpManager.GankCallBack() {
            @Override
            public void onRespose(Call call, Response response) {
                Message msg = new Message();
                msg.what = SUCCESS;
                mHanler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call call) {
                Message msg = new Message();
                msg.what = FAIL;
                mHanler.sendMessage(msg);
            }
        });

    }

    //RxJavaDownload
    private void RxJavaDownLoad() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                DownLoadPic(imageUrl, subscriber);

            }
        })
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onNext(String result) {
                        Toast.makeText(PhotoViewActivity.this, result, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(PhotoViewActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void DownLoadPic(String urlStr, Subscriber subscriber) {
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
            subscriber.onNext("Success!");

        } catch (Exception e) {
            e.printStackTrace();
            subscriber.onNext("Failure!");
        }

    }


}
