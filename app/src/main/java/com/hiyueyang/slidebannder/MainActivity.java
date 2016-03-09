package com.hiyueyang.slidebannder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hiyueyang.lib.SlideBanner;

public class MainActivity extends AppCompatActivity {

    private SlideBanner slideBanner;
    private String[] imgUrls = {
            "http://imgsrc.baidu.com/forum/pic/item/ebc3b57eca8065386315ecef97dda144af34829b.jpg",
            "http://imgsrc.baidu.com/forum/pic/item/ebc3b57eca8065386315ecef97dda144af34829b.jpg",
            "http://imgsrc.baidu.com/forum/pic/item/ebc3b57eca8065386315ecef97dda144af34829b.jpg"
    };

    private int[] imgIds = {
            R.drawable.test,
            R.drawable.test,
            R.drawable.test
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        slideBanner = (SlideBanner) findViewById(R.id.slide_banner);

        //load urls
        slideBanner.setData(imgUrls, 20, 20);
        //load img from local
//        slideBanner.setData(imgIds,20,20);
        slideBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
