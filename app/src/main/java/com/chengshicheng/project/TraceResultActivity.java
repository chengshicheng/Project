package com.chengshicheng.project;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.chengshicheng.greendao.gen.OrderQueryDao;
import com.chengshicheng.project.greendao.GreenDaoHelper;
import com.chengshicheng.project.greendao.OrderQuery;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by chengshicheng on 2017/2/27.
 */

public class TraceResultActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private TextView tvCompany, tvRemark, tvOrderState;
    private String expCode, expName, expNO;
    private static String state = "-1";
    private RecyclerView recyclerView;
    private TraceResultAdapter resultAdapter;
    private ArrayList<OrderTrace> tracesList = new ArrayList<OrderTrace>();
    private OrderQueryDao mOrderDao;
    private SwipeRefreshLayout refreshLayout;


    private static final int QUERY_FAILED = 0;

    /**
     * 系统错误、服务器接口异常等
     */
    private static final int API_EXCEPTION = 1;

    private static final int QUERY_SUCCESS = 2;

    private static final int NET_ERROR = 3;
    private CardView headerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace_result);
        expCode = getIntent().getStringExtra("expCode");
        expName = getIntent().getStringExtra("expName");
        expNO = getIntent().getStringExtra("expNO");
        mOrderDao = GreenDaoHelper.getDaoSession().getOrderQueryDao();
        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        });
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("物流详情");
//        collapsingToolbar.setCollapsedTitleGravity(Gravity.LEFT);
        View floatBtn = findViewById(R.id.floatingButton);
        floatBtn.setOnClickListener(this);

        headerView = (CardView) findViewById(R.id.headerLayout);
        headerView.setVisibility(View.GONE);
        tvCompany = (TextView) headerView.findViewById(R.id.tvOrderCompany);
        tvRemark = (TextView) headerView.findViewById(R.id.tvOrderRemark);
        tvOrderState = (TextView) headerView.findViewById(R.id.tvOrderState);
        tvCompany.setText(expName + "  " + expNO);

        recyclerView = (RecyclerView) findViewById(R.id.lvTrace);
        resultAdapter = new TraceResultAdapter(TraceResultActivity.this, tracesList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(resultAdapter);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);


        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);//底部状态栏透明
            getWindow().setStatusBarColor(Color.TRANSPARENT);//顶部状态栏透明
        }


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            headerView.setVisibility(View.VISIBLE);
            switch (msg.what) {
                case QUERY_SUCCESS:
                    OrderTraceResponse response = (OrderTraceResponse) msg.obj;
                    showTraces(response);
                    refreshLayout.setRefreshing(false);
                    break;
                case QUERY_FAILED:
                    OrderTraceResponse response_error = (OrderTraceResponse) msg.obj;
                    tvOrderState.setText("查询失败：" + response_error.getReason());
                    refreshLayout.setRefreshing(false);
                    break;
                case API_EXCEPTION:
                    tvOrderState.setText("查询失败,请稍后查询");
                    refreshLayout.setRefreshing(false);
                    break;
                case NET_ERROR:
                    tvOrderState.setText("查询失败,请检查网络");
                    refreshLayout.setRefreshing(false);
                    DialogUtils.ShowToast("您的设备没有连接网络");
                    break;
            }
        }
    };

    /**
     * 保存到数据库
     *
     * @param response
     */
    private void insertToDataBase(OrderTraceResponse response) {
        //老数据
        OrderQuery oldOrder = mOrderDao.queryBuilder().where(OrderQueryDao.Properties.OrderNum.eq(expNO)).unique();

        OrderQuery save = new OrderQuery();
        save.setOrderNum(Long.valueOf(response.getLogisticCode()));
        save.setOrderCode(response.getShipperCode());
        save.setOrderName(expName);
        save.setLastQueryTime(System.currentTimeMillis());
        save.setIsSuccess(response.isSuccess());
        save.setState(response.getState());
        if (null != oldOrder) {
            save.setRemark(oldOrder.getRemark());
            if (!TextUtils.isEmpty(save.getRemark())) {
                tvRemark.setText("备注：" + save.getRemark());
                tvRemark.setVisibility(View.VISIBLE);
            } else {
                tvRemark.setVisibility(View.GONE);
            }
        }

        Gson gson = new Gson();
        String traces = gson.toJson(response.getTraces());
        LogUtils.PrintDebug("-----------------" + traces);
        save.setTraces2Json(traces);
        //更新数据
        mOrderDao.insertOrReplace(save);
        sendBroadCastToRefresh();
        //
        //        OrderQuery query = mOrderDao.queryBuilder().where(OrderQueryDao.Properties.OrderCode.eq("YD")).unique();
        //        if (query != null) {
        //            LogUtil.PrintDebug(query.getOrderNum() + "");
        //        }
    }


    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        doQueryTraceAPI(expCode, expNO);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.floatingButton) {
            final OrderQuery oldOrder = mOrderDao.queryBuilder().where(OrderQueryDao.Properties.OrderNum.eq(expNO)).unique();
            if (oldOrder != null) {
                Dialog dialog = DialogUtils.createInputDialog(this, "输入备注", R.layout.dialog_input_remark, oldOrder.getRemark(), new AlertDialogListener() {
                    @Override
                    public void OnPositive(String text) {
                        oldOrder.setRemark(text);
                        mOrderDao.insertOrReplace(oldOrder);
                        sendBroadCastToRefresh();
                        if (!TextUtils.isEmpty(oldOrder.getRemark())) {
                            tvRemark.setText("备注：" + oldOrder.getRemark());
                            tvRemark.setVisibility(View.VISIBLE);
                        } else {
                            tvRemark.setVisibility(View.GONE);
                        }
                    }
                });
                dialog.show();
            }
        }
    }

    /****
     * 通知主界面刷新列表
     */
    private void sendBroadCastToRefresh() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        Intent intent = new Intent(StringUtils.refreshAction);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void showTraces(OrderTraceResponse response) {
        //物流状态：2-在途中,3-签收,4-问题件,0 暂无物流轨迹
        state = (null == response.getState()) ? "" : response.getState();
        ArrayList<OrderTrace> traces = response.getTraces();
        switch (state) {
            case "0":
                tvOrderState.setText("状态：暂无物流信息");
                insertToDataBase(response);
                break;
            case "2":
                tvOrderState.setText("状态：在途中");
                insertToDataBase(response);
                break;
            case "3":
                tvOrderState.setText("状态：已签收");
                insertToDataBase(response);
                break;
            case "4":
                tvOrderState.setText("状态：问题件");
                insertToDataBase(response);
                break;
            default:
                tvOrderState.setText("状态：没有物流信息，请检查单号");
                break;
        }
        tracesList.clear();
        Collections.reverse(traces);
        tracesList.addAll(traces);
        resultAdapter.notifyDataSetChanged();
    }

    /**
     * doQueryAPI("YD", "1000797534377");
     *
     * @param code
     * @param num
     */
    private void doQueryTraceAPI(final String code, final String num) {
        if (!ProjectApp.isNetworkConnected(this)) {
            handler.sendEmptyMessage(NET_ERROR);
            return;
        }

        try {
            OrderTraceAPI.getOrderTracesByJson(code, num, new WebCallBackListener() {
                Message message = Message.obtain();

                @Override
                public void onSuccess(String result) {
                    LogUtils.PrintDebug(result);
                    Gson gson = new Gson();
                    OrderTraceResponse response = gson.fromJson(result, OrderTraceResponse.class);
                    message.obj = response;
                    if (response.isSuccess()) {
                        //查询成功
                        message.what = QUERY_SUCCESS;
                    } else {
                        //查询物流信息失败
                        message.what = QUERY_FAILED;

                    }
                    handler.sendMessage(message);
                }

                @Override
                public void onFailed() {
                    handler.sendEmptyMessage(API_EXCEPTION);
                }
            });
        } catch (Exception e) {
            LogUtils.PrintError("OrderTraceAPI Error", e);
            handler.sendEmptyMessage(API_EXCEPTION);
        }
    }

    @Override
    public void onBackPressed() {
        if (TextUtils.isEmpty(state))
            setResult(RESULT_CANCELED);
        else {
            setResult(RESULT_OK);
        }
        super.onBackPressed();
    }
}
