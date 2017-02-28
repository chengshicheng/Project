package com.chengshicheng.project;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

/**
 * Created by chengshicheng on 2017/2/27.
 */
public class ChooseCompanyActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private static int requestCode;

    /**
     * 快递单号
     */
    public static String expNO = "";

    private static ArrayList<ShipperBean> scanShippers;//单号识别返回的的结果
    private ListView listView, commonListView;
    private TopCompanyAdapter topadapter;//顶部推荐快递
    private CommonCompanyAdapter commonadapter;//常用快递
    private static ArrayList<ShipperBean> topList = new ArrayList<ShipperBean>();//top适配器数据源
    private static ArrayList<ShipperBean> commList = new ArrayList<ShipperBean>();//常用快递适配器数据源
    /**
     * 单号查询失败
     */
    private static final int QUERY_FAILED = 0;
    /**
     * 单号查询成功
     */
    private static final int QUERY_SUCCESS = 2;
    /**
     * 系统错误、服务器接口异常等
     */
    private static final int API_EXCEPTION = 3;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case QUERY_FAILED:
                    int errCode = (int) (msg.obj);
                    listView.setVisibility(View.GONE);
                    LogUtils.PrintDebug("单号识别结果失败，错误码:" + errCode);
                    //查询失败,显示所有列表
                    break;
                case QUERY_SUCCESS:
                    scanShippers = (ArrayList<ShipperBean>) msg.obj;
                    LogUtils.PrintDebug("单号识别成功，结果size =" + scanShippers.size());
                    refreshListView();
                    break;
                default:
                    listView.setVisibility(View.GONE);
                    break;
            }
        }
    };

    private void refreshListView() {
        topList.clear();
        topList.addAll(scanShippers);
        filterList(topList, commList);
        topadapter.notifyDataSetChanged();
        commonadapter.notifyDataSetChanged();
    }

    /**
     * 从commList移除topList数据，避免界面出现重复数据
     * 由于ShipperName可能不一致，以ShipperCode为标准
     *
     * @param topList  要移除的数据
     * @param commList
     */
    private void filterList(ArrayList<ShipperBean> topList, ArrayList<ShipperBean> commList) {
        ArrayList<ShipperBean> tempList = new ArrayList<ShipperBean>();
        for (ShipperBean commonBean : commList) {
            for (ShipperBean topBean : topList) {
                if (commonBean.getShipperCode().equals(topBean.getShipperCode())) {
                    tempList.add(commonBean);
                }
            }
        }
        commList.removeAll(tempList);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_company);
        commList = new CompanyUtils().getCommonList();
        initViews();
        Intent intent = getIntent();
        doRequest(intent);
    }


    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//显示title
        toolbar.setTitle("选择快递");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.too);
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        listView = (ListView) findViewById(R.id.listView);
        listView.setDivider(new ColorDrawable(Color.GRAY));
        listView.setDividerHeight(1);
        topadapter = new TopCompanyAdapter(ChooseCompanyActivity.this, topList);
        listView.setAdapter(topadapter);
        listView.setOnItemClickListener(this);

        commonListView = (ListView) findViewById(R.id.allListView);
        commonListView.setDivider(new ColorDrawable(Color.GRAY));
        commonListView.setDividerHeight(1);
        commonadapter = new CommonCompanyAdapter(ChooseCompanyActivity.this, commList);
        commonListView.setAdapter(commonadapter);
        commonListView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(this, TraceResultActivity.class);
        if (parent == listView) {
            intent.putExtra("expCode", topList.get(position).getShipperCode());
            intent.putExtra("expName", topList.get(position).getShipperName());
        } else if (parent == commonListView) {
            intent.putExtra("expCode", commList.get(position).getShipperCode());
            intent.putExtra("expName", commList.get(position).getShipperName());
        }
        intent.putExtra("expNO", expNO);
        //正常查询快递requestCode为100.从主界面直接点进去，为101
        startActivityForResult(intent, 100);
    }


    private void doRequest(Intent intent) {
        requestCode = intent.getIntExtra("requestType", 0);
        LogUtils.PrintDebug("requestType = " + requestCode);
        if (1 == requestCode) {
            expNO = intent.getStringExtra("requestNumber");
            LogUtils.PrintDebug("requestNumber = " + expNO);
            //查询快递公司
            doQueryCompany(expNO);
        } else if (2 == requestCode) {
            toCustomScanActivity();
        }
    }


    /**
     * 跳转到扫描二维码界面
     * https://github.com/journeyapps/zxing-android-embedded
     */
    private void toCustomScanActivity() {
//         zxinging自带的扫描界面
//        IntentIntegrator.forSupportFragment(Fragment1.this).initiateScan();

        //重定义zxing的扫描界面 CustomScanActivity
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.setPrompt("");
        integrator.setCaptureActivity(CustomScanActivity.class);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }


    /**
     * 获取扫描结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        } else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    LogUtils.PrintDebug("Scan Cancelled");
                    finish();

                } else {
                    expNO = result.getContents();
                    LogUtils.PrintDebug("Scan Succeed : " + expNO);
                    doQueryCompany(expNO);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 单号识别，根据单号查询物流公司
     *
     * @param num
     */
    private void doQueryCompany(final String num) {

        try {
            OrderDistinguishAPI.getCompanyByJson(num, new WebCallBackListener() {
                Message message = Message.obtain();

                @Override
                public void onSuccess(String result) {
                    LogUtils.PrintDebug(result);
                    Gson gson = new Gson();
                    OrderDistinguishResponse response = gson.fromJson(result, OrderDistinguishResponse.class);

                    if (response.isSuccess()) {
                        //查询成功
                        ArrayList<ShipperBean> shippers = response.getShippers();
                        message.what = QUERY_SUCCESS;
                        message.obj = shippers;
                    } else {
                        message.what = QUERY_FAILED;
                        message.obj = response.getCode();//失败错误码
                    }
                    handler.sendMessage(message);
                }

                @Override
                public void onFailed() {
                    handler.sendEmptyMessage(API_EXCEPTION);
                }
            });

        } catch (Exception e) {
            LogUtils.PrintError("OrderDistinguishAPI Error", e);
            handler.sendEmptyMessage(API_EXCEPTION);
        }
    }

}
