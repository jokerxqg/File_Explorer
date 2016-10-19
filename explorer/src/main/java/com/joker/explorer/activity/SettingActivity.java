package com.joker.explorer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.joker.explorer.R;

import utils.FileUtils;

public class SettingActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    //    初始化控件以及绑定监听
    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        final Switch hind = (Switch) findViewById(R.id.hindSystemFile);
        hind.setChecked(FileUtils.hindSystemFile);

        hind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hind.isChecked()) {
                    FileUtils.hindSystemFile = true;
                } else {
                    FileUtils.hindSystemFile = false;
                }
            }
        });
    }

    //    按返回键的监听
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
