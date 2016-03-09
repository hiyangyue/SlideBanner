package com.hiyueyang.lib;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SlideBanner extends FrameLayout {

    private final static boolean isAutoPlay = true;

    private String[] imageUrls;
    private int[] imageResIds;
    private ArrayList<ImageView> imageViewsList = new ArrayList<>();
    private ArrayList<ImageView> dotViewsList = new ArrayList<>();
    private BannerViewPager viewPager;
    private int currentItem = 0;
    private Timer timer;
    private int dotMargin = 0;
    private int dotSize = 0;

    LinearLayout dotParent;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);
        }

    };

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            synchronized (viewPager) {
                currentItem = (currentItem + 1) % imageViewsList.size();
                handler.obtainMessage().sendToTarget();
            }
        }
    };

    public SlideBanner(Context context) {
        super(context);
    }

    public SlideBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initUI(context);

    }

    private void startPlay() {
        timer.schedule(timerTask, 2000, 2000);
    }

    private void stopPlay() {
        timer.cancel();
    }

    public void setData(String[] imageUrls,int dotMargin,int dotSize) {
        this.imageUrls = imageUrls;

        Context context = getContext();

        for (int i = 0; i < imageUrls.length; i++) {

            ImageView view = new ImageView(context);
            view.setId(i);
            Glide.with(context)
                    .load(imageUrls[i])
                    .centerCrop()
                    .into(view);
            imageViewsList.add(view);

            addDot(dotMargin,dotSize);
        }

        setAdapter();
    }

    public void setData(int[] imageResIds,int dotMargin,int dotSize) {
        this.imageResIds = imageResIds;
        imageViewsList = new ArrayList<>();
        dotViewsList = new ArrayList<>();

        Context context = getContext();

        for (int i = 0; i < imageResIds.length; i++) {

            ImageView view = new ImageView(context);
            view.setId(i);
            view.setBackgroundResource(imageResIds[i]);
            imageViewsList.add(view);

            addDot(dotMargin,dotSize);
        }

        setAdapter();
    }

    void setAdapter() {
        viewPager.setAdapter(new BannerPagerAdapter(imageViewsList));
        viewPager.addOnPageChangeListener(new BannerPagerListener(this));

        if (isAutoPlay) {
            startPlay();
        }
    }

    void addDot(int dotMargin,int dotSize) {

        ImageView dot = (ImageView) LayoutInflater.from(getContext()).inflate(R.layout.item_dot_view, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dotSize, dotSize);
        layoutParams.setMargins(dotMargin, 0, dotMargin, 0);
        dot.setLayoutParams(layoutParams);
        dotParent.addView(dot);
        dotViewsList.add(dot);
    }


    public void setViewListener(int position, final OnClickListener listener) {
        imageViewsList.get(position).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
    }

    public int getCount() {

        if (imageViewsList == null && imageResIds == null) {
            return 0;
        }

        if (imageViewsList != null) {
            return imageUrls.length;
        }

        return imageResIds.length;
    }


    private void initUI(Context context) {
        LayoutInflater.from(context).inflate(R.layout.slide_banner, this, true);
        dotParent = (LinearLayout) findViewById(R.id.dot_parent);
        timer = new Timer();
        viewPager = (BannerViewPager) findViewById(R.id.pager);
        viewPager.setFocusable(true);
    }

    private static class BannerPagerAdapter extends PagerAdapter {

        ArrayList<ImageView> data;

        public BannerPagerAdapter(ArrayList<ImageView> data) {
            this.data = data;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(data.get(position));
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(data.get(position));
            return data.get(position);
        }

        @Override
        public int getCount() {
            if (data == null) {
                return 0;
            }
            return data.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

    private static class BannerPagerListener implements ViewPager.OnPageChangeListener {

        private boolean isAutoPlay = false;

        private WeakReference<SlideBanner> swf;

        public BannerPagerListener(SlideBanner slideBanner) {
            this.swf = new WeakReference<>(slideBanner);
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            switch (arg0) {
                case 1:
                    isAutoPlay = false;
                    break;
                case 2:
                    isAutoPlay = true;
                    break;
                case 0:
                    ViewPager viewPager = swf.get().viewPager;

                    if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        viewPager.setCurrentItem(0);
                    } else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
                    }
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int pos) {
            swf.get().currentItem = pos;
            ArrayList<ImageView> dotViewsList = swf.get().dotViewsList;
            for (int i = 0; i < dotViewsList.size(); i++) {
                if (i == pos) {
                    dotViewsList.get(pos).setEnabled(true);
                } else {
                    dotViewsList.get(i).setEnabled(false);
                }
            }
        }

    }

    private void destroyBitmaps() {

        for (int i = 0; i < getCount(); i++) {
            ImageView imageView = imageViewsList.get(i);
            Drawable drawable = imageView.getDrawable();

            if (drawable != null) {
                drawable.setCallback(null);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        destroyBitmaps();
        stopPlay();
    }
}
