package org.wahid.foody.utils;

import android.util.Log;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import org.wahid.foody.R;


public abstract class ImageLoader {
    private static final String TAG = "ImageLoader";
    public static void load(ImageView view,String url){
        Log.d(TAG, "load: "+ view + " :VIEW<----->URL: " + url);

        Glide.with(view.getContext())
                .load(url)
                .placeholder(R.drawable.image_placeholder)
                .into(view);
    }
}