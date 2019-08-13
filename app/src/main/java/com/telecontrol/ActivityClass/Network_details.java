package com.telecontrol.ActivityClass;

import com.telecontrol.R;
import com.telecontrol.App.OverAllData;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Network_details extends AppCompatActivity
{
    TextView text_details;      //显示状态信息的框
    ScrollView scroll;     //下拉框
    Button button_back,button_clear,button_copy;        //底下的三个按钮
    Switch switch_autodown;     //是否自动翻页

    OverAllData all=OverAllData.alldata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_details);
        //设置setting界面的状态栏
        Toolbar toolbar = findViewById(R.id.network_details_ui_toolbar);
        setSupportActionBar(toolbar);
        //初始化控件
        InitViewUnit();
        //添加控件监听器
        AddUnitActionListener();
        //拉到最下
        if(all.auto_down_switch_state)
            scroll.fullScroll(ScrollView.FOCUS_DOWN);
        //页面刷新
        RefreshUI();
    }

    //初始化控件
    public void InitViewUnit()
    {
        text_details= findViewById(R.id.network_details_ui_text);
        button_back= findViewById(R.id.network_details_ui_button_back);
        button_clear= findViewById(R.id.network_details_ui_button_clear);
        button_copy= findViewById(R.id.network_details_ui_button_copy);
        scroll = findViewById(R.id.network_details_ui_scroll);
        switch_autodown=findViewById(R.id.network_details_ui_switch_autodown);
        //设置“自动翻页”按钮的状态
        switch_autodown.setChecked(all.auto_down_switch_state);
    }

    //添加控件监听器
    public void AddUnitActionListener()
    {
        //返回按钮
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Network_details.this.finish();
            }
        });

        //清空按钮
        button_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                all.network_message.delete(0,all.network_message.length());
                text_details.setText("");
            }
        });

        //复制按钮
        button_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    //获取剪切板管理器
                    ClipboardManager cm=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                    //创建普通字符型ClipData
                    ClipData mClipData=ClipData.newPlainText("Label",all.network_message);
                    //将ClipData内容放到系统剪切板里
                    if (cm != null) {
                        cm.setPrimaryClip(mClipData);
                        //显示消息
                        ShowToastMessage("已复制到剪切板");
                    }
                    else
                    {
                        //显示消息
                        ShowToastMessage("复制到剪切板失败");
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        //自动翻页按钮
        switch_autodown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!buttonView.isPressed())
                    return;
                all.auto_down_switch_state=isChecked;
            }
        });
    }

    //页面刷新
    private void RefreshUI()
    {
        final Handler han=new Handler();
        han.postDelayed(new Runnable() {
            @Override
            public void run() {
                RefreshTextViewer();
                if(all.auto_down_switch_state)
                    scroll.fullScroll(ScrollView.FOCUS_DOWN);
                han.postDelayed(this,all.NETWORK_MESSAGE_REFRESH_INTERVAL);
            }
        }, 0);
    }

    //刷新TextView
    public void RefreshTextViewer()
    {
        text_details.setText(all.network_message);
    }

    //发送Toast文本
    public void ShowToastMessage(String str)
    {
        Toast.makeText(Network_details.this,str,Toast.LENGTH_SHORT).show();
    }
}
