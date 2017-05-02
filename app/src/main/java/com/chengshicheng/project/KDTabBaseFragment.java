package com.chengshicheng.project;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.chengshicheng.greendao.gen.OrderQueryDao;
import com.chengshicheng.project.greendao.OrderQuery;

import static android.app.Activity.RESULT_OK;

/**
 * 快递鸟——Fragment 基类，主要实现对点击事件的统一处理
 * Created by chengshicheng on 2017/2/25.
 */

public abstract class KDTabBaseFragment extends Fragment implements OnRecyclerViewItemClickListener, OnRecyclerViewItemLongClickListener, Observer {
    public static OrderQueryDao mOrderDao;
    private LocalBroadcastManager broadcastManager;
    private static OrderQuery chosenOrder;
    public static Context mContext;
    private MenuItem searchItem;


    private int requestCode = 100;
    private MainActivity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity mContext = getActivity();
        mContext.setTitle("快递鸟");
        setHasOptionsMenu(true);//onCreateOptionsMenu生效
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.kd_fragment, menu);
        searchItem = menu.findItem(R.id.menu_search);//在菜单中找到对应控件的item

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(mContext, ChooseCompanyActivity.class);
                if (query.length() < 6 || query.length() > 50) {
                    DialogUtils.ShowToast("单号格式错误");
                } else {
                    intent.putExtra("requestType", 1);
                    intent.putExtra("requestNumber", query);
                    startActivityForResult(intent, requestCode);
                }
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_scan:
                Intent intent = new Intent();
                intent.setClass(mContext, ChooseCompanyActivity.class);
                intent.putExtra("requestType", 2);
                startActivityForResult(intent, requestCode);
                break;
            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(View view, int position) {
        chosenOrder = getChosenItem(position);
        Intent intent = new Intent();
        intent.setClass(mContext, TraceResultActivity.class);
        intent.putExtra("expCode", chosenOrder.getOrderCode());
        intent.putExtra("expName", chosenOrder.getOrderName());
        intent.putExtra("expNO", chosenOrder.getOrderNum().toString());
        //正常查询快递requestCode为100.从主界面直接点进去，为101
        startActivityForResult(intent, 101);
    }

    protected abstract OrderQuery getChosenItem(int position);


    @Override
    public void onItemLongClick(View view, int position) {
        chosenOrder = getChosenItem(position);
        String title = chosenOrder.getOrderName() + "  " + chosenOrder.getOrderNum();
        final String[] items = {"置顶", "删除", "修改备注", "复制单号"};

        if (chosenOrder.getToTop()) {
            items[0] = "取消置顶";
        }

        Dialog dialog = DialogUtils.createListDialog(mContext, title, items,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogUtils.PrintDebug("handleLongClick:" + which);
                        handleLongClick(which);
                    }
                });
        dialog.show();
    }

    /***
     * 长按弹出窗口的点击事件
     *
     * @param which
     */
    public void handleLongClick(final int which) {

        final long orderNum = chosenOrder.getOrderNum();
        final OrderQuery query = mOrderDao.queryBuilder().where(OrderQueryDao.Properties.OrderNum.eq(orderNum)).unique();
        switch (which) {
            case 0:
                query.setToTop(!query.getToTop());
                mOrderDao.insertOrReplace(query);
                mActivity = (MainActivity) mContext;
                mActivity.notifyObservers();
                break;
            case 1:
                mOrderDao.delete(query);
                mActivity = (MainActivity) mContext;
                mActivity.notifyObservers();
                break;
            case 2:
                Dialog dialog = DialogUtils.createInputDialog(mContext, "输入备注", R.layout.dialog_input_remark, query.getRemark(), new AlertDialogListener() {
                    @Override
                    public void OnPositive(String text) {
                        chosenOrder.setRemark(text);
                        mOrderDao.insertOrReplace(query);
                        mActivity = (MainActivity) mContext;
                        mActivity.notifyObservers();
                    }
                });
                dialog.show();
                break;
            case 3:
                StringUtils.toCopy(String.valueOf(orderNum), mContext);
                DialogUtils.ShowToast("复制成功");
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            MenuItemCompat.collapseActionView(searchItem);//收起搜索框
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
