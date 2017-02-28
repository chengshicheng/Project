package com.chengshicheng.project;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by chengshicheng on 2017/2/26.
 */

public class DialogUtils {
    private static AlertDialogListener mAlertDialogListener;

    public static void ShowToast(String string) {
        Toast.makeText(ProjectApp.getContext(), string, Toast.LENGTH_SHORT).show();
    }

    /**
     * 创建列表对话框
     *
     * @param context     上下文 必填
     * @param title       标题 必填
     * @param itemsString 列表项 必填
     * @return
     */
    public static Dialog createListDialog(Context context, String title, String[] itemsString,
                                          DialogInterface.OnClickListener itemClickListener) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(itemsString, itemClickListener);
        dialog = builder.create();
        return dialog;
    }


    public static Dialog createInputDialog(Context context, String title, int viewID, String remark,
                                           final AlertDialogListener alertDialogListener) {
        Dialog dialog = null;
        final View view = LayoutInflater.from(context).inflate(viewID, null);//这里必须是final的
        final EditText intput = (EditText) view.findViewById(R.id.editText);
        if (null != remark) {
            intput.setText(remark);
            intput.setSelection(remark.length());//将光标移至文字末尾
        }
        dialog = new AlertDialog.Builder(context)
                .setTitle(title)//提示框标题
                .setView(view)
                .setPositiveButton("确定",//提示框的两个按钮
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialogListener.OnPositive(intput.getText().toString());
                            }
                        }).setNegativeButton("取消", null).create();

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        return dialog;
    }

}