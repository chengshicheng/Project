package com.chengshicheng.project;

import java.util.ArrayList;

/**
 * 服务器返回信息
 * 单号识别http://www.kdniao.com/api-recognise
 * Created by chengshicheng on 2017/2/27.
 */
public class OrderDistinguishResponse {
    /**
     * 用户ID
     */
    private String EBusinessID;

    /**
     * 物流运单号
     */
    private String LogisticCode;
    /**
     * 成功与否
     */
    private boolean Success;
    /**
     * 失败原因
     */
    private int Code;

    /**
     * 单号对应的物流公司信息
     */
    private ArrayList<ShipperBean> Shippers;

    public int getCode() {
        return Code;
    }

    public String getEBusinessID() {
        return EBusinessID;
    }

    public String getLogisticCode() {
        return LogisticCode;
    }

    public ArrayList<ShipperBean> getShippers() {
        return Shippers;
    }

    public boolean isSuccess() {
        return Success;
    }
}
