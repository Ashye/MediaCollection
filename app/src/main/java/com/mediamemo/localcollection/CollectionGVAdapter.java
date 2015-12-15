package com.mediamemo.localcollection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashye.image.ImageLoader;
import com.mediamemo.R;

import java.util.List;

/**
 * Created by Administrator on 2015/12/10.
 */
public class CollectionGVAdapter extends BaseAdapter {

    private List<CollectionBean> list;
    private LayoutInflater inflater;
//    private ImageLoader imageLoader;
//    private DisplayImageOptions imageOptions;
    private ImageLoader imageLoader;


    public CollectionGVAdapter(Context context, final List<CollectionBean> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
        imageLoader = new ImageLoader(context);
//        imageLoader = ImageLoader.getInstance();
//        imageOptions = initImageLoaderDisplay();
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.collection_grid_view_item, null);
            holder = new ViewHolder();
            holder.icon = (ImageView) view.findViewById(R.id.item_icon);
            holder.title = (TextView) view.findViewById(R.id.item_title);
            holder.latest = (TextView) view.findViewById(R.id.item_latest);

            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        CollectionBean bean = list.get(i);
        holder.title.setText(bean.getTitle());
        holder.latest.setText(bean.getLatest());
        imageLoader.displayImageDefault(bean.getIconUrl(), holder.icon);
//        imageLoader.displayImage(bean.getIconUrl(), holder.icon, imageOptions);

        return view;
    }

    private static class ViewHolder {
        public ImageView icon;
        public TextView title;
        public TextView latest;
    }

//    private DisplayImageOptions initImageLoaderDisplay() {
//        return new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.ic_launcher)
//                .showImageForEmptyUri(R.mipmap.ic_launcher)
//                .showImageOnFail(R.mipmap.ic_launcher)
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .considerExifParams(true)
//                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
//                .bitmapConfig(Bitmap.Config.RGB_565)
////                .decodingOptions(android.graphics.BitmapFactory.Options decodeingOptions)
//                .resetViewBeforeLoading(true)
//                .displayer(new RoundedBitmapDisplayer(20))
//                .displayer(new FadeInBitmapDisplayer(100))
//                .build();
//    }
}
