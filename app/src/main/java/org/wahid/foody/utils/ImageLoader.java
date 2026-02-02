package org.wahid.foody.utils;

import android.widget.ImageView;
import com.bumptech.glide.Glide;
import org.wahid.foody.R;


public abstract class ImageLoader {

    public static void load(ImageView view,String url){
        Glide.with(view.getContext())
                .load(url)
                .placeholder(R.drawable.image_placeholder)
                .into(view);
    }
}