package com.mediamemo.localcollection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;
import com.mediamemo.R;
import com.mediamemo.datacontroller.CollectionController;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocalCollectionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocalCollectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocalCollectionFragment extends Fragment implements AdapterView.OnItemClickListener,
        CollectionController.OnCollectionDataChangedListener,
        AdapterView.OnItemLongClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocalCollectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocalCollectionFragment newInstance(String param1, String param2) {
        LocalCollectionFragment fragment = new LocalCollectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public LocalCollectionFragment() {
        // Required empty public constructor
    }


    private GridView gridView;
    private SwipeRefreshLayout refreshLayout;
    private CollectionGVAdapter gvAdapter;
    private List<CollectionBean> collectionDatas;
    private CollectionController dataController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_local_collection, container, false);
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.collection_refresh);
        refreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataController.reload();
            }
        });
        gridView = (GridView) v.findViewById(R.id.collection_grid_view);

        collectionDatas = new ArrayList<CollectionBean>();
        gvAdapter = new CollectionGVAdapter(getActivity(), collectionDatas);
        gridView.setAdapter(gvAdapter);
        gvAdapter.notifyDataSetChanged();
        gridView.setOnItemClickListener(this);
        gridView.setOnItemLongClickListener(this);
        dataController.setDataChangedListener(this);
        dataController.dataChangeNotify();
        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CollectionBean bean = (CollectionBean) gvAdapter.getItem(i);
//        SnackBarMessage("打开详情页面");
        Intent intent = new Intent(getActivity(), CollectionBeanDetailActivity.class);
        intent.putExtra("bean", JSON.toJSONString(bean));
        intent.putExtra("idx", i);
        startActivityForResult(intent, 0);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
        final CollectionBean bean = (CollectionBean) gvAdapter.getItem(i);

        Snackbar.make(gridView, "删除 "+bean.getTitle(), Snackbar.LENGTH_LONG)
        .setAction("确定", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataController.deleteItemAt(i);
                Snackbar.make(gridView, "删除成功", Snackbar.LENGTH_SHORT).show();
            }
        }).show();

        return true;
    }

    private final Handler handler = new Handler(Looper.getMainLooper());
    @Override
    public void onCollectionChanged(List<CollectionBean> after) {
        if (after != null && collectionDatas != null) {
            collectionDatas.clear();
            collectionDatas.addAll(after);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setRefreshing(false);
                    gvAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    public void setDataController(CollectionController dataController) {
        this.dataController = dataController;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (CollectionBeanDetailActivity.DetailBack.DELETE.ordinal() == resultCode) {
                int idx = data.getIntExtra("idx", -1);
                if (idx >=0) {
                    dataController.deleteItemAt(idx);
                }
            }else if (CollectionBeanDetailActivity.DetailBack.UPDATED.ordinal() == resultCode) {
                int idx = data.getIntExtra("idx", -1);
                if (idx >=0) {
                    CollectionBean bean = JSON.parseObject(data.getStringExtra("bean"), CollectionBean.class);
                    if (bean != null) {
                        dataController.replaceItem(idx, bean);
                    }
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
