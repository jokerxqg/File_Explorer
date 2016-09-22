package com.joker.explorer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.joker.explorer.R;


import fixed.OperateCode;

public class OperateDialogActivity extends Activity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate_dialog);
        findButtonView();
    }


    private void findButtonView() {
        Integer[] buttonIds = new Integer[]{
                R.id.btn_done_all, R.id.btn_cancel_all, R.id.btn_delete, R.id.btn_order_az, R.id.btn_old_new};
        for (int i = 0; i < buttonIds.length; i++) {
            Button button = (Button) findViewById(buttonIds[i]);
            button.setOnClickListener(this);
        }
    }

    //    操作button的点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_done_all:
                setResult(OperateCode.DONE_ALL);
                finish();
                break;
            case R.id.btn_cancel_all:
                setResult(OperateCode.CANCEL_DONE_ALL);
                finish();
                break;
            case R.id.btn_delete:
                setResult(OperateCode.DELETE);
                finish();
                break;
            case R.id.btn_order_az:
                setResult(OperateCode.ORDER_AZ);
                finish();
                break;
            case R.id.btn_old_new:
                setResult(OperateCode.ORDER_OLD);
                finish();
                break;
        }
    }
}
