# SlideBanner

### Usage
---
##### Add the dependencies to your gradle file:
    dependencies {
        compile 'com.hiyangyue.lib:slidebanner:1.0.0'
    }
        
##### XML

    <com.hiyueyang.lib.SlideBanner
      android:layout_width="match_parent"
      android:layout_height="300dp"/>

##### setResource(Glide or Local)
    slideBanner.setData(String[] imgUrls,int dotMargin,int dotSize);
    slideBanner.setData(int[] resourceId,int dotMargin,int dotSize)

##### ItemListener
    slideBanner.setViewListener(position,listener);

