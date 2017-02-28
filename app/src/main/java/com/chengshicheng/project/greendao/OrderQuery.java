package com.chengshicheng.project.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chengshicheng on 2017/2/26.
 */
@Entity
public class OrderQuery {
    @Id
    private Long OrderNum;

    private String OrderCode;
    private String OrderName;
    private long LastQueryTime;
    private boolean isSuccess;
    private String State;
    private String Traces2Json;
    //用户备注
    private String remark;

    //用户备注
    private boolean toTop;

    @Generated(hash = 1133180536)
    public OrderQuery(Long OrderNum, String OrderCode, String OrderName,
            long LastQueryTime, boolean isSuccess, String State, String Traces2Json,
            String remark, boolean toTop) {
        this.OrderNum = OrderNum;
        this.OrderCode = OrderCode;
        this.OrderName = OrderName;
        this.LastQueryTime = LastQueryTime;
        this.isSuccess = isSuccess;
        this.State = State;
        this.Traces2Json = Traces2Json;
        this.remark = remark;
        this.toTop = toTop;
    }

    @Generated(hash = 678038440)
    public OrderQuery() {
    }

    public Long getOrderNum() {
        return this.OrderNum;
    }

    public void setOrderNum(Long OrderNum) {
        this.OrderNum = OrderNum;
    }

    public String getOrderCode() {
        return this.OrderCode;
    }

    public void setOrderCode(String OrderCode) {
        this.OrderCode = OrderCode;
    }

    public String getOrderName() {
        return this.OrderName;
    }

    public void setOrderName(String OrderName) {
        this.OrderName = OrderName;
    }

    public long getLastQueryTime() {
        return this.LastQueryTime;
    }

    public void setLastQueryTime(long LastQueryTime) {
        this.LastQueryTime = LastQueryTime;
    }

    public boolean getIsSuccess() {
        return this.isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getState() {
        return this.State;
    }

    public void setState(String State) {
        this.State = State;
    }

    public String getTraces2Json() {
        return this.Traces2Json;
    }

    public void setTraces2Json(String Traces2Json) {
        this.Traces2Json = Traces2Json;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean getToTop() {
        return this.toTop;
    }

    public void setToTop(boolean toTop) {
        this.toTop = toTop;
    }
}
