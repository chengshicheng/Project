package com.chengshicheng.project;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by chengshicheng on 2017/2/27.
 */
public class TraceResultAdapter extends RecyclerView.Adapter<TraceResultAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<OrderTrace> traceList;

    public TraceResultAdapter(Context context, ArrayList<OrderTrace> traces) {
        this.context = context;
        this.traceList = traces;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_trace, parent, false);
        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final OrderTrace trace = traceList.get(position);
        if (position == 0) {
            // 第一行头的竖线不显示
            holder.tvTopLine.setVisibility(View.INVISIBLE);
            // 字体颜色蓝色
            holder.tvAcceptTime.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.tvAcceptStation.setTextColor(context.getResources().getColor(R.color.colorAccent));
            //第一行圆点设置大一点
            holder.tvDot.getLayoutParams().width = DensityUtils.dp2px(context, 10);
            holder.tvDot.getLayoutParams().height = DensityUtils.dp2px(context, 10);
            holder.tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
        } else {
            holder.tvTopLine.setVisibility(View.VISIBLE);
            holder.tvAcceptTime.setTextColor(context.getResources().getColor(R.color.greyText));
            holder.tvAcceptStation.setTextColor(context.getResources().getColor(R.color.greyText));
            holder.tvDot.getLayoutParams().width = DensityUtils.dp2px(context, 7);
            holder.tvDot.getLayoutParams().height = DensityUtils.dp2px(context, 7);
            holder.tvDot.setBackgroundResource(R.drawable.timelline_dot_normal);
        }
        holder.tvAcceptTime.setText(trace.getAcceptTime());
        holder.tvAcceptStation.setText(trace.getAcceptStation().replace("到达：", ""));

    }

    @Override
    public int getItemCount() {
        return traceList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAcceptTime, tvAcceptStation, tvTopLine, tvDot;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvAcceptTime = (TextView) itemView.findViewById(R.id.tvAcceptTime);
            tvAcceptStation = (TextView) itemView.findViewById(R.id.tvAcceptStation);
            tvTopLine = (TextView) itemView.findViewById(R.id.tvTopLine);
            tvDot = (TextView) itemView.findViewById(R.id.tvDot);
        }
    }
}
