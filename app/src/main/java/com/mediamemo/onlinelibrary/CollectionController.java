package com.mediamemo.onlinelibrary;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.ashye.storage.SharePrefStorage;
import com.mediamemo.localcollection.CollectionBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/10.
 */
public class CollectionController extends SharePrefStorage {

    private static final String COLLECTIONS = "beans";
    private List<CollectionBean> collectionBeans;

    private Context context;
    private OnCollectionDataChangedListener dataChangedListener;



    public CollectionController() {
        collectionBeans = new ArrayList<CollectionBean>();
    }

    public CollectionController(Context context) {
        this();
        this.context = context;
        load();
    }

    public boolean queryItem(CollectionBean bean) {
        for (CollectionBean target: collectionBeans) {
            if (bean.getUrl().equals(target.getUrl())) {
                return true;
            }
        }
        return false;
    }

    public boolean addItem(CollectionBean bean) {
        collectionBeans.add(0, bean);
        flush();
        return true;
    }

    public boolean deleteItem(CollectionBean bean) {
        boolean ret = collectionBeans.remove(bean);
        flush();
        return ret;
    }

    public boolean deleteItemAt(int id) {
        CollectionBean ret = collectionBeans.remove(id);
        if (ret != null) {
            flush(); 
        }
        return true;
    }

    private boolean flush() {
        dataChangeNotify();
        return this.save(context, COLLECTIONS, JSON.toJSONString(collectionBeans));
    }

    private void load() {
        List<CollectionBean> list = JSON.parseArray(this.loadString(context, COLLECTIONS), CollectionBean.class);
        collectionBeans.clear();
        if (list != null) {
            collectionBeans.addAll(list);
        }
        dataChangeNotify();
    }

    public void dataChangeNotify() {
        if (dataChangedListener != null) {
            dataChangedListener.onCollectionChanged(collectionBeans);
        }
    }


    public void setDataChangedListener(OnCollectionDataChangedListener dataChangedListener) {
        this.dataChangedListener = dataChangedListener;
    }

    /**
     * Collection data changed listener
     */
    public interface OnCollectionDataChangedListener {
        void onCollectionChanged(List<CollectionBean> after);
    }
}
