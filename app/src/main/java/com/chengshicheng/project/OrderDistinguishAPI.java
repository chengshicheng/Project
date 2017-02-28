package com.chengshicheng.project;

import java.util.HashMap;
import java.util.Map;

import static com.chengshicheng.project.StringUtils.AppKey;
import static com.chengshicheng.project.StringUtils.EBusinessID;
import static com.chengshicheng.project.StringUtils.encrypt;
import static com.chengshicheng.project.StringUtils.urlEncoder;

/**
 * Created by chengshicheng on 2017/2/27.
 */
public class OrderDistinguishAPI {    //    API测试地址：http://testapi.kdniao.cc:8081/Ebusiness/EbusinessOrderHandle.aspx
    //    API正式地址：http://api.kdniao.cc/Ebusiness/EbusinessOrderHandle.aspx
    //测试地址返回shipper为空  2017-01-16，采用正式地址
    //请求url
    private static final String ReqURL = "http://api.kdniao.cc/Ebusiness/EbusinessOrderHandle.aspx";

    /**
     * 单号识别
     *
     * @throws Exception
     */
    public static void getCompanyByJson(String expNo, WebCallBackListener listener) throws Exception {
        String requestData = "{'LogisticCode':'" + expNo + "'}";
        LogUtils.PrintDebug("单号识别RequestData:" + requestData);

        Map<String, String> params = new HashMap<String, String>();
        params.put("RequestData", urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", EBusinessID);
        params.put("RequestType", "2002");
        String dataSign = encrypt(requestData, AppKey, "UTF-8");
        params.put("DataSign", urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");

//        String result = sendPost(ReqURL, params);

        //根据公司业务处理返回的信息......
        new WebService(ReqURL, params, listener).start();
    }

}
