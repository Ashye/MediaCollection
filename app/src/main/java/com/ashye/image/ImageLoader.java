package com.ashye.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.mediamemo.R;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * Created by Administrator on 2015/12/15.
 */
public class ImageLoader {

    private static com.nostra13.universalimageloader.core.ImageLoader imageLoader;
//    private Context
    private DisplayImageOptions defaultImageOptions;



    public ImageLoader(Context context) {
        init(context);
        initDisplayOptions();
    }

    public void init(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheFileCount(100)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(context, 5*1000, 30*1000))
                .build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
    }

    private void initDisplayOptions() {
          defaultImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
//                .decodingOptions(android.graphics.BitmapFactory.Options decodeingOptions)
                .resetViewBeforeLoading(true)
//                .displayer(new RoundedBitmapDisplayer(20))
                .displayer(new FadeInBitmapDisplayer(100))
                .build();
    }

    public void displayImageDefault(String uri, ImageView imageView) {
        displayImage(uri, imageView, defaultImageOptions);
    }

    public void displayImage(String uri, ImageView imageView, DisplayImageOptions options) {
        imageLoader.displayImage(uri, imageView, options);
    }
}
