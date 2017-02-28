package com.chengshicheng.project;

/**
 * 服务器回调接口
 * <p>
 * Created by chengshicheng on 2017/2/27.
 */
public interface WebCallBackListener {
    void onSuccess(String result);

    void onFailed();
}
