package com.chengshicheng.project;

import java.util.ArrayList;

/**
 * 服务器返回信息
 * 及时查询物流跟踪
 * Created by chengshicheng on 2017/2/27.
 * http://www.kdniao.com/api-track
 */

public class OrderTraceResponse {
    /**
     * 用户ID
     */
    private String EBusinessID;
    /**
     * 订单编号
     */
    private String OrderCode;
    /**
     * 快递公司编码
     */
    private String ShipperCode;
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
    private String Reason;
    /**
     * 物流状态：2-在途中,3-签收,4-问题件  //0?
     */
    private String State;
    /**
     * 物流详情
     */
    private ArrayList<OrderTrace> Traces;

    public String getEBusinessID() {
        return EBusinessID;
    }

    public String getLogisticCode() {
        return LogisticCode;
    }

    public String getOrderCode() {
        return OrderCode;
    }

    public String getReason() {
        return Reason;
    }

    public String getShipperCode() {
        return ShipperCode;
    }

    public String getState() {
        return State;
    }

    public boolean isSuccess() {
        return Success;
    }

    public ArrayList<OrderTrace> getTraces() {
        return Traces;
    }
}
