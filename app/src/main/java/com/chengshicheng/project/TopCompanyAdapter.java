package com.chengshicheng.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chengshicheng.project.ChooseCompanyActivity;
import com.chengshicheng.project.ProjectApp;
import com.chengshicheng.project.R;
import com.chengshicheng.project.ShipperBean;

import java.util.ArrayList;

/**
 * Created by chengshicheng on 2017/2/27.
 */
public class TopCompanyAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<ShipperBean> shipperBean = new ArrayList<ShipperBean>();

    public TopCompanyAdapter(Context context, ArrayList<ShipperBean> shippers) {
        this.mInflater = LayoutInflater.from(context);
        this.shipperBean = shippers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.choose_company_item,
                    null);
            holder = new ViewHolder();
            holder.comanyImage = (ImageView) view.findViewById(R.id.comanyImage);
            holder.comanyText = (TextView) view.findViewById(R.id.comanyText);
            holder.expNumberText = (TextView) view.findViewById(R.id.expNO);
            holder.rightImage = (ImageView) view.findViewById(R.id.rightImage);

            holder.comanyImage.setImageResource(R.drawable.aap);
            holder.rightImage.setImageResource(R.drawable.a7o);
            holder.comanyText.setTextColor(ProjectApp.getContext().getResources().getColor(R.color.colorAccent));
            holder.expNumberText.setTextColor(ProjectApp.getContext().getResources().getColor(R.color.colorAccent));

            view.setTag(holder);//绑定ViewHolder对象
        } else {
            view = convertView;
            holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
        }

        holder.comanyText.setText(shipperBean.get(position).getShipperName());
        holder.expNumberText.setText(ChooseCompanyActivity.expNO);

        return view;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        if (shipperBean == null) {
            return 0;
        } else {
            return shipperBean.size();
        }
    }

    class ViewHolder {
        ImageView comanyImage;
        TextView comanyText;
        TextView expNumberText;
        ImageView rightImage;
    }
}