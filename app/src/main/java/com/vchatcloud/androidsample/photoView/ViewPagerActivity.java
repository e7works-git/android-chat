package com.vchatcloud.androidsample.photoView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.vchatcloud.androidsample.R;

import uk.co.senab.photoview.PhotoView;

public class ViewPagerActivity extends Activity {
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        Intent intent = getIntent();
        String inUrl = intent.getExtras().get("url") != null ? intent.getExtras().get("url").toString() : null;
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new SamplePagerAdapter(inUrl));
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    static class SamplePagerAdapter extends PagerAdapter {
        private String url;
        SamplePagerAdapter(String _url) {
            this.url = _url;
        }
        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            Glide.with(photoView)
                    .load(url)
                    .thumbnail(Glide.with(photoView).load(R.drawable.loading_spinner))
                    .error(R.drawable.ic_baseline_image_24)
                    .override(Target.SIZE_ORIGINAL)
                    .fallback(R.drawable.ic_baseline_image_24)
                    .into(photoView);
            photoView.setClipToOutline(true);
            photoView.setMaximumScale(50);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}