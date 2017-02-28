package com.chengshicheng.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.chengshicheng.greendao.gen.OrderQueryDao;
import com.chengshicheng.project.greendao.OrderQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengshicheng on 2017/2/26.
 */
public class KDFragment1 extends KDTabBaseFragment {


    private KDRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private static ArrayList<OrderQuery> mDatas = new ArrayList<OrderQuery>();

    public static KDFragment1 newInstance(Context context, Bundle bundle) {
        KDFragment1 newFragment = new KDFragment1();
        newFragment.setArguments(bundle);
        return newFragment;
    }


    @Override
    protected View initView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.kd_fragment, null);
        mAdapter = new KDRecyclerAdapter(mActivity, mDatas);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.lv_fragment1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ProjectApp.getContext());
        mRecyclerView.addItemDecoration(new RecycleViewDivider(
                ProjectApp.getContext(), LinearLayoutManager.HORIZONTAL, 3, getResources().getColor(R.color.listviewbg)));
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
        return view;
    }

    public void initData() {
        mDatas.clear();
        List<OrderQuery> result = mOrderDao.queryBuilder().orderDesc(OrderQueryDao.Properties.ToTop).list();
        mDatas.addAll(result);
    }

    @Override
    protected OrderQuery getChosenItem(int position) {
        return mDatas.get(position);
    }


    @Override
    public void refreshRecyclerView() {
        initData();
        mAdapter.notifyDataSetChanged();
    }

}
