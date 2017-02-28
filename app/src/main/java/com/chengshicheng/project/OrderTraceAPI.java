package com.chengshicheng.project;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import static com.chengshicheng.project.StringUtils.AppKey;
import static com.chengshicheng.project.StringUtils.EBusinessID;
import static com.chengshicheng.project.StringUtils.encrypt;
import static com.chengshicheng.project.StringUtils.urlEncoder;

/**
 * 及时查询订单轨迹
 *
 * Created by chengshicheng on 2017/2/27.
 */
public class OrderTraceAPI {

    //请求url
    private static final String ReqURL = "http://api.kdniao.cc/Ebusiness/EbusinessOrderHandle.aspx";
//    private String ReqURL = "http://api.kdniao.cc/api/dist";

    /**
     * Json方式 查询订单物流轨迹
     *
     * @throws Exception
     */
    public static void getOrderTracesByJson(String expCode, String expNo, WebCallBackListener listener) throws Exception {
//        String requestData = "{'OrderCode':'','ShipperCode':'" + expCode + "','LogisticCode':'" + expNo + "'}";
        Gson gson = new Gson();
        String requestData = gson.toJson(new OrderTraceRequestData().getRequestData(expCode, expNo));
        LogUtils.PrintDebug("即时查询RequestData:" + requestData);

        Map<String, String> params = new HashMap<String, String>();
        params.put("RequestData", urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", EBusinessID);
        params.put("RequestType", "1002");
        String dataSign = encrypt(requestData, AppKey, "UTF-8");
        params.put("DataSign", urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");

//        String result = sendPost(ReqURL, params);
        new WebService(ReqURL, params, listener).start();
        //根据公司业务处理返回的信息......

    }
}
