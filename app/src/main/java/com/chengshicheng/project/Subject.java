package com.chengshicheng.project;


/**
 * 主题（发布者、被观察者）
 * Created by chengshicheng on 2017/5/2.
 */

public interface Subject {
    /**
     * 注册观察者
     */
    void registerObserver(Observer observer);

    /**
     * 移除观察者
     */
    void removeObserver(Observer observer);

    /**
     * 通知观察者
     */
    void notifyObservers();
}
