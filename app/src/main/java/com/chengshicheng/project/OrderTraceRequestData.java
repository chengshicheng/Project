package com.chengshicheng.project;

/**
 * Created by chengshicheng on 2017/2/27.
 */
public class OrderTraceRequestData {

    /**
     * 订单编号，非必须字段
     */
    private String OrderCode;

    /**
     * 快递公司编码，必须
     */
    private String ShipperCode;

    /**
     * 物流单号，必须
     */
    private String LogisticCode;

    public OrderTraceRequestData getRequestData(String shipperCode, String logisticCode) {
        OrderTraceRequestData requestData = new OrderTraceRequestData();
        requestData.setOrderCode("");
        requestData.setShipperCode(shipperCode);
        requestData.setLogisticCode(logisticCode);
        return requestData;
    }

    public String getLogisticCode() {
        return LogisticCode;
    }

    public void setLogisticCode(String logisticCode) {
        LogisticCode = logisticCode;
    }

    public String getOrderCode() {
        return OrderCode;
    }

    public void setOrderCode(String orderCode) {
        OrderCode = orderCode;
    }

    public String getShipperCode() {
        return ShipperCode;
    }

    public void setShipperCode(String shipperCode) {
        ShipperCode = shipperCode;
    }
}
