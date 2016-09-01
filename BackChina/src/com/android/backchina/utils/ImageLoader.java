package com.android.backchina.utils;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Glide 图片加载辅助类
 * 适配圆形图片加载情况
 */

public class ImageLoader {
    private ImageLoader() {
    }

    public static void loadImage(RequestManager loader, ImageView view, String url) {
        loadImage(loader, view, url, 0);
    }

    public static void loadImage(RequestManager loader, ImageView view, String url, int placeholder) {
        loadImage(loader, view, url, placeholder, placeholder);
    }

    public static void loadImage(RequestManager loader, ImageView view, String url, int placeholder, int error) {
        boolean isCenterCrop = false;
//        if (view instanceof CircleImageView){
//            isCenterCrop = true;
//        }
        loadImage(loader, view, url, placeholder, error, isCenterCrop);
    }

    public static void loadImage(RequestManager loader, ImageView view, String url, int placeholder, int error, boolean isCenterCrop) {
    	TLog.d("url = "+url);
        if (TextUtils.isEmpty(url)) {
            view.setImageResource(placeholder);
        } else {
//            if (view instanceof CircleImageView) {
//                BitmapRequestBuilder builder = loader.load(url).asBitmap()
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .placeholder(placeholder)
//                        .error(error);
//                if (isCenterCrop)
//                    builder.centerCrop();
//
//                builder.into(
//                        new BitmapImageViewTarget(view) {
//                            @Override
//                            protected void setResource(Bitmap resource) {
//                                RoundedBitmapDrawable circularBitmapDrawable =
//                                        RoundedBitmapDrawableFactory.create(view.getResources(), resource);
//                                circularBitmapDrawable.setCircular(true);
//                                view.setImageDrawable(circularBitmapDrawable);
//                            }
//                        });
//            } else {
                DrawableRequestBuilder builder = loader.load(url).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(placeholder).error(error);
                if (isCenterCrop)
                    builder.centerCrop();
                builder.into(view);
//            }
        }
    }
}