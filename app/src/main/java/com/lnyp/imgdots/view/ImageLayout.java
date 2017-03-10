package com.lnyp.imgdots.view;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lnyp.imgdots.R;
import com.lnyp.imgdots.bean.PointSimple;

import java.io.IOException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ImageLayout extends FrameLayout implements View.OnClickListener {

    ArrayList<PointSimple> points;

    FrameLayout layouPoints;

    ImageView imgBg;

    Context mContext;
    private MediaPlayer mediaPlayer;

    public ImageLayout(Context context) {
        this(context, null);
    }

    public ImageLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mediaPlayer = new MediaPlayer();
        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = context.getAssets().openFd("aa.mp3");
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),fileDescriptor.getStartOffset(), fileDescriptor.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }

        initView(context, attrs);
    }


    private void initView(Context context, AttributeSet attrs) {

        mContext = context;

        View imgPointLayout = inflate(context, R.layout.layout_imgview_point, this);

        imgBg = (ImageView) imgPointLayout.findViewById(R.id.imgBg);
        layouPoints = (FrameLayout) imgPointLayout.findViewById(R.id.layouPoints);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setImgBg(int width, int height, String imgUrl) {

        ViewGroup.LayoutParams lp = imgBg.getLayoutParams();
        lp.width = width;
        lp.height = height;

        imgBg.setLayoutParams(lp);

        ViewGroup.LayoutParams lp1 = layouPoints.getLayoutParams();
        lp1.width = width;
        lp1.height = height;

        layouPoints.setLayoutParams(lp1);

        Glide.with(mContext).load(imgUrl).asBitmap().into(imgBg);

        addPoints(width, height);
        addAnnotations(width, height);
        addError(width, height);
    }

    public void setPoints(ArrayList<PointSimple> points) {

        this.points = points;
    }

    private void addAnnotations(int width, int height){
        LinearLayout view = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.lin_checked, this, false);
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        layoutParams.leftMargin = (int) (width * 0.186f);
        layoutParams.topMargin = (int) (height * 0.164f);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "addAnnotations " , Toast.LENGTH_SHORT).show();
            }
        });
        layouPoints.addView(view, layoutParams);
    }
    private void addError(int width, int height){
        LinearLayout view = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.lin_error, this, false);
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        layoutParams.leftMargin = (int) (width * 0.386f);
        layoutParams.topMargin = (int) (height * 0.364f);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "addError " , Toast.LENGTH_SHORT).show();
            }
        });
        layouPoints.addView(view, layoutParams);
    }
    private void addPoints(int width, int height) {

        layouPoints.removeAllViews();

        for (int i = 0; i < points.size(); i++) {

            double width_scale = points.get(i).width_scale;
            double height_scale = points.get(i).height_scale;


            LinearLayout view = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_img_point, this, false);
            CheckBox checkbox = (CheckBox) view.findViewById(R.id.imgPoint);
            checkbox.setTag(i);
            TextView tv = (TextView) view.findViewById(R.id.txtPoint);
            tv.setText("批注"+i);
//            AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
//            animationDrawable.start();

            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();

            layoutParams.leftMargin = (int) (width * width_scale);
            layoutParams.topMargin = (int) (height * height_scale);

            checkbox.setOnClickListener(this);

            layouPoints.addView(view, layoutParams);
        }
    }


    @Override
    public void onClick(View view) {
        int pos = (int) view.getTag();

        CheckBox checkBox = (CheckBox) view;
        Log.d(TAG, "---->>onClick: "+checkBox.isChecked());
        if (checkBox.isChecked()){
            try {
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mediaPlayer.stop();
        }
        Toast.makeText(getContext(), "pos : " + pos, Toast.LENGTH_SHORT).show();
    }


}
