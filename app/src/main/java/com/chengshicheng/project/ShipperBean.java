package com.chengshicheng.project;

/**
 * 单号识别Shipper
 * http://www.kdniao.com/api-recognise
 * Created by chengshicheng on 2017/2/27.
 */

public class ShipperBean {
    /**
     * 快递公司编码
     */
    private String ShipperCode;
    /**
     * 快递公司名称
     */
    private String ShipperName;

    public ShipperBean() {

    }

    public ShipperBean(String code, String name) {
        this.ShipperCode = code;
        this.ShipperName = name;
    }

    public String getShipperCode() {
        return ShipperCode;
    }

    public String getShipperName() {
        return ShipperName;
    }

    public void setShipperCode(String shipperCode) {
        ShipperCode = shipperCode;
    }

    public void setShipperName(String shipperName) {
        ShipperName = shipperName;
    }
}
