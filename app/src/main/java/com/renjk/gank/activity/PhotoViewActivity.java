package com.renjk.gank.activity;

import android.os.Bundle;
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

import okhttp3.Call;
import okhttp3.Response;
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

    private Handler mHanler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
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

                return true;
            }
        });
    }

    private void dowloadImage() {

//      new DownloadTask(imageUrl).execute();
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



}
