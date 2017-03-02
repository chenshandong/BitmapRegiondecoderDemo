package com.chensd.bitmapregiondecoderdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class BigImageView extends ImageView {
    private int windowWidth, windowHeight;
    private BitmapRegionDecoder mDecoder;
    private Rect bsrc;
    private Rect src;
    private Rect dst;
    private Paint paint;
    private ArrayList<Bitmap> bmps;
    private ProgressDialog show;

    public BigImageView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }

    public BigImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    private void init() {
        // TODO Auto-generated method stub
        windowWidth = getResources().getDisplayMetrics().widthPixels;
        bmps = new ArrayList<Bitmap>();
        src = new Rect();
        dst = new Rect();
        bsrc = new Rect();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        int count = bmps.size();
        for(int i = 0;i<count;i++){
            Bitmap bmp = bmps.get(i);
            src.left = 0;
            src.top = 0;
            src.right = bmp.getWidth();
            src.bottom = bmp.getHeight();

            dst.left = 0;
            dst.top = (i*getHeight()/count);
            dst.right = getWidth();
            dst.bottom = dst.top+getHeight()/count;

            canvas.drawBitmap(bmp, src, dst, paint);

        }
    }


    @Override
    public void setImageBitmap(final Bitmap bm) {
        super.setImageBitmap(bm);
        if(bm!=null){
            float imgmul = (float)bm.getHeight()/(float)bm.getWidth();
            ViewGroup.LayoutParams lp = this.getLayoutParams();
            lp.width = windowWidth;
            lp.height = (int) (imgmul*windowWidth);
            this.setLayoutParams(lp);
        }
        show = ProgressDialog.show(getContext(), "", "图片加载中...");
        new Thread() {
            public void run() {
                try{
                    int maxHeight = ImageUtils.getMaxHeight();
                    int bmHeight = bm.getHeight();
                    int imageCount = bmHeight / maxHeight;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
                    mDecoder = BitmapRegionDecoder.newInstance(isBm, true);
                    for (int i = 0; i < imageCount; i++) {
                        bsrc.left = 0;
                        bsrc.top = i*bmHeight/imageCount;
                        bsrc.right = bm.getWidth();
                        bsrc.bottom = bsrc.top+bmHeight/imageCount;
                        Bitmap bmp = mDecoder.decodeRegion(bsrc,null);
                        bmps.add(bmp);
                    }
                    handler.sendEmptyMessage(0);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }.start();

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            show.cancel();
            postInvalidate();
        }
    };

}