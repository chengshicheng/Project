package com.chengshicheng.project;

import java.util.ArrayList;

/**
 * Created by chengshicheng on 2017/2/27.
 */
public class CompanyUtils {

    private ArrayList<ShipperBean> commonList = new ArrayList<ShipperBean>();

    public CompanyUtils() {
        initList();
    }

    private void initList() {

        commonList.add(new ShipperBean("SF", "顺丰"));
        commonList.add(new ShipperBean("HTKY", "百世快递"));
        commonList.add(new ShipperBean("ZTO", "中通"));
        commonList.add(new ShipperBean("STO", "申通"));
        commonList.add(new ShipperBean("YTO", "圆通"));
        commonList.add(new ShipperBean("YD", "韵达"));
        commonList.add(new ShipperBean("EMS", "EMS"));
        commonList.add(new ShipperBean("YZPY", "邮政平邮"));
        commonList.add(new ShipperBean("HHTT", "天天"));
        commonList.add(new ShipperBean("QFKD", "全峰"));
        commonList.add(new ShipperBean("GTO", "国通"));
        commonList.add(new ShipperBean("JD", "京东快递"));
        commonList.add(new ShipperBean("UC", "优速物流"));
        commonList.add(new ShipperBean("DBL", "德邦物流"));
        commonList.add(new ShipperBean("FAST", "快捷"));
        commonList.add(new ShipperBean("AMAZON", "亚马逊"));
        commonList.add(new ShipperBean("ZJS", "宅急送"));
    }

    public ArrayList<ShipperBean> getCommonList() {
        return commonList;

    }
}
