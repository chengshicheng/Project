package com.chengshicheng.project;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chengshicheng.greendao.gen.OrderQueryDao;
import com.chengshicheng.project.greendao.GreenDaoHelper;
import com.chengshicheng.project.greendao.OrderQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * 快递鸟——未签收Fragment界面
 * Created by chengshicheng on 2017/2/26.
 */
public class KDUndoneFragment extends KDTabBaseFragment {


    private static KDRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private static ArrayList<OrderQuery> mDatas = new ArrayList<OrderQuery>();
    private View rootView;

    public static KDUndoneFragment newInstance(Context context, Bundle bundle) {
        mContext = context;
        KDUndoneFragment newFragment = new KDUndoneFragment();
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.kd_fragment, null);
            initView(rootView);// 控件初始化
        }
        return rootView;
    }

    protected void initView(View rootView) {
        mAdapter = new KDRecyclerAdapter(mContext, mDatas);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.lv_fragment1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ProjectApp.getContext());
        mRecyclerView.addItemDecoration(new RecycleViewDivider(
                ProjectApp.getContext(), LinearLayoutManager.HORIZONTAL, 3, getResources().getColor(R.color.listviewbg)));
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);

        GreenDaoHelper.initDatabase();
        mOrderDao = GreenDaoHelper.getDaoSession().getOrderQueryDao();
        initData();
    }

    public void initData() {
        mDatas.clear();
        List<OrderQuery> result = mOrderDao.queryBuilder().where(OrderQueryDao.Properties.State.notEq("3")).orderDesc(OrderQueryDao.Properties.ToTop).list();
        mDatas.addAll(result);
    }

    @Override
    protected OrderQuery getChosenItem(int position) {
        return mDatas.get(position);
    }


    public void refreshRecyclerView() {
        initData();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void update() {
        if (mAdapter != null) {
            refreshRecyclerView();
        }
    }
}
