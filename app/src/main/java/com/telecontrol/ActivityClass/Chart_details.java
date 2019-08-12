package com.telecontrol.ActivityClass;

import com.telecontrol.R;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class Chart_details extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts_details);
        //设置setting界面的状态栏
        Toolbar toolbar = findViewById(R.id.charts_toolbar);
        setSupportActionBar(toolbar);
        //初始化控件
        InitViewUnit();
        //添加控件监听器
        AddUnitActionListener();
    }

    //初始化控件
    private void InitViewUnit()
    {
    }

    //添加控件监听器
    private void AddUnitActionListener()
    {
    }
}
