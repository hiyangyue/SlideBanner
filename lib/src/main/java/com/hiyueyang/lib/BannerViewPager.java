package com.hiyueyang.lib;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by YueYang on 2016/3/9.
 */
public class BannerViewPager extends ViewPager{

    public BannerViewPager(Context context) {
        super(context);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if(v != this && v instanceof ViewPager) {
            return true;
        }
        return super.canScroll(v, checkV, dx, x, y);
    }

}
