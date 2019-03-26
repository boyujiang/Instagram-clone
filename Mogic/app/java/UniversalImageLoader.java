package com.cas.jiamin.mogic.Utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.cas.jiamin.mogic.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 *
 */

/**
 * UniversalImageLoader
 * the helper class to load the image
 */
public class UniversalImageLoader {

    private static final int defaultImage = R.drawable.ic_home;
    private Context mContext;

    /**
     * UniversalImageLoader
     * the constructor of the class that take the using field
     * @param context the field of the class
     */
    public UniversalImageLoader(Context context) {
        mContext = context;
    }

    /**
     * getConfig
     * set the configuration of the image
     * @return the configuration type of the image
     */
    public ImageLoaderConfiguration getConfig(){
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultImage)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .cacheOnDisk(true).cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(mContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();

        return configuration;
    }

    /**
     * this method can be sued to set images that are static. It can't be used if the images
     * are being changed in the Fragment/Activity - OR if they are being set in a list or
     * a grid
     * @param imgURL the address of the source
     * @param image the image view
     * @param mProgressBar the progress bar to hold the state when loading the picture
     * @param append the append before the URL to fix the address
     */
    public static void setImage(String imgURL, ImageView image, final ProgressBar mProgressBar, String append){

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(append + imgURL, image, new ImageLoadingListener() {

            /**
             * onLoadingStarted
             * set the visibility of the progress bar
             * @param imageUri the address of the image
             * @param view the image view
             */
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if(mProgressBar != null){
                    mProgressBar.setVisibility(View.VISIBLE);
                }
            }

            /**
             * onLoadingFailed
             * the state if loading failed
             * @param imageUri the address of the image
             * @param view the image view
             * @param failReason the failed reason
             */
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if(mProgressBar != null){
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            /**
             * onLoadingComplete
             * the state when finished loading
             * @param imageUri the address of the image
             * @param view the image view
             * @param loadedImage load image
             */
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if(mProgressBar != null){
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            /**
             * onLoadingCancelled
             * the state when the loading cancelled
             * @param imageUri the address of the picture
             * @param view the image view
             */
            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if(mProgressBar != null){
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}