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
    private ImageLoader imageLoader;


    public CollectionGVAdapter(Context context, final List<CollectionBean> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
        imageLoader = new ImageLoader(context);
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

        return view;
    }

    private static class ViewHolder {
        public ImageView icon;
        public TextView title;
        public TextView latest;
    }
}
