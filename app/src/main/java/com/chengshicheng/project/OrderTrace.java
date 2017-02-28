package com.chengshicheng.project;

/**
 * 及时查询物流跟踪
 * Traces详情
 * Created by chengshicheng on 2017/2/26.
 */

public class OrderTrace {
    /**
     * 时间
     */
    private String AcceptTime;
    /**
     * 描述
     */
    private String AcceptStation;
    /**
     * 备注
     */
    private String Remark;

    public OrderTrace(String time, String Station) {
        this.AcceptTime = time;
        this.AcceptStation = Station;
    }

    public String getAcceptStation() {
        return AcceptStation;
    }

    public String getAcceptTime() {
        return AcceptTime;
    }

    public String getRemark() {
        return Remark;
    }
}
