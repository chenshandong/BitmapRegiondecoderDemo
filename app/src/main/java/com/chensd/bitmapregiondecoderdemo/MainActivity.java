package com.chensd.bitmapregiondecoderdemo;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

public class MainActivity extends AppCompatActivity {

    private static final String URL = "http://7q5aqg.com1.z0.glb.clouddn.com/5000x%E9%BB%91%E7%99%BD%E7%89%88.jpg";

    private BigImageView mImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImg = (BigImageView) findViewById(R.id.img);
    }

    public void load(View v){
        Glide.with(this).load(URL).placeholder(R.mipmap.ic_launcher).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                Bitmap bitmap = ((GlideBitmapDrawable)resource).getBitmap();
                mImg.setImageBitmap(bitmap);
            }
        });
    }
}
