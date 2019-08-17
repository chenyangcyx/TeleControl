package com.telecontrol.ActivityClass;

import com.telecontrol.R;
import com.telecontrol.App.OverAllData;
import com.telecontrol.SocketFunction.APIResultStruct;
import com.telecontrol.SocketFunction.ReceiveMessageFromLAN;
import com.telecontrol.SocketFunction.UseWebAPI;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class Setting_UI extends AppCompatActivity {

    Button button_enter, button_reset, button_changemode;
    EditText text_ip, text_port, text_wendumin, text_wendumax, text_shidumin, text_shidumax, text_gzmin, text_gzmax,text_phone;
    Switch switch_auto_monitor;
    OverAllData all=OverAllData.alldata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_ui);
        //设置setting界面的状态栏
        Toolbar toolbar = findViewById(R.id.setting_ui_toolbar);
        setSupportActionBar(toolbar);
        //初始化控件
        InitViewUnit();
        //添加控件监听器
        AddUnitActionListener();
        //从网页获取设置信息并初始化
        InitSettingsFromWeb();
    }

    //初始化控件
    void InitViewUnit()
    {
        button_reset = findViewById(R.id.setting_ui_button_reset);
        button_enter = findViewById(R.id.setting_ui_button_enter);
        button_changemode = findViewById(R.id.setting_ui_button_changemode);
        text_ip = findViewById(R.id.setting_ui_text_ip);
        text_port = findViewById(R.id.setting_ui_text_port);
        text_wendumin = findViewById(R.id.setting_ui_text_wendumin);
        text_wendumax = findViewById(R.id.setting_ui_text_wendumax);
        text_shidumin = findViewById(R.id.setting_ui_text_shidumin);
        text_shidumax = findViewById(R.id.setting_ui_text_shidumax);
        text_gzmin = findViewById(R.id.setting_ui_text_guangzhaomin);
        text_gzmax = findViewById(R.id.setting_ui_text_guangzhaomax);
        text_phone = findViewById(R.id.setting_ui_phone);
        switch_auto_monitor = findViewById(R.id.setting_ui_automonitor);
        text_wendumin.setText(String.valueOf(all.wendu_min));
        text_wendumax.setText(String.valueOf(all.wendu_max));
        text_shidumin.setText(String.valueOf(all.shidu_min));
        text_shidumax.setText(String.valueOf(all.shidu_max));
        text_gzmin.setText(String.valueOf(all.guangzhao_min));
        text_gzmax.setText(String.valueOf(all.guangzhao_max));
        //初始化服务器IP和端口的输入框
        text_ip.setText(String.valueOf(all.setting_ip));
        text_port.setText(String.valueOf(all.setting_port));
    }

    //添加控件监听器
    void AddUnitActionListener()
    {
        //重置按钮
        button_reset.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v)
            {
                all.wendu_min=all.wendu_min_init;            //温度监测最低值
                all.wendu_max=all.wendu_max_init;            //温度监测最高值
                all.shidu_min=all.shidu_min_init;            //湿度监测最低值
                all.shidu_max=all.shidu_max_init;            //湿度监测最高值
                all.guangzhao_min=all.guangzhao_min_init;    //光照控制最低值
                all.guangzhao_max=all.guangzhao_max_inti;    //光照控制最高值
                text_wendumin.setText(Integer.toString(all.wendu_min));        //温度低
                text_wendumax.setText(Integer.toString(all.wendu_max));        //温度高
                text_shidumin.setText(Integer.toString(all.shidu_min));        //湿度低
                text_shidumax.setText(Integer.toString(all.shidu_max));        //湿度高
                text_gzmin.setText(Integer.toString(all.guangzhao_min));       //光照低
                text_gzmax.setText(Integer.toString(all.guangzhao_max));       //光照高
            }
        });

        //确认按钮
        button_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all.wendu_min= text_wendumin.getText().toString().equals("")?0:Integer.parseInt(text_wendumin.getText().toString());
                all.wendu_max= text_wendumax.getText().toString().equals("")?0:Integer.parseInt(text_wendumax.getText().toString());
                all.shidu_min= text_shidumin.getText().toString().equals("")?0:Integer.parseInt(text_shidumin.getText().toString());
                all.shidu_max= text_shidumax.getText().toString().equals("")?0:Integer.parseInt(text_shidumax.getText().toString());
                all.guangzhao_min= text_gzmin.getText().toString().equals("")?0:Integer.parseInt(text_gzmin.getText().toString());
                all.guangzhao_max= text_gzmax.getText().toString().equals("")?0:Integer.parseInt(text_gzmax.getText().toString());
                String result;
                if(!(result=CheckScopen()).equals("OK"))
                {
                    ShowToastMessage(result);
                    return;
                }
                //调用网页API发送设置信息
                UseAPISendSetting();
                //发送信息
                ShowToastMessage("设置成功！");
                Setting_UI.this.finish();
            }
        });

        //切换模式按钮
        button_changemode.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                all.LINK_MODE=1-all.LINK_MODE;
                if(all.LINK_MODE==0)        //联网模式
                {
                    ShowToastMessage("切换到服务器模式");       //显示提示消息
                    all.setting_ip=all.server_ip;                 //更换连接的ip
                    all.setting_port=all.server_port;             //更换连接的端口
                    text_ip.setText("    "+all.setting_ip);                  //更换显示的文本框内容，ip
                    text_port.setText("    "+ all.setting_port +"    (服务器模式)");  //更换显示的文本框内容，port
                }
                else                        //局域网模式
                {
                    ShowToastMessage("切换到局域网模式");       //显示提示消息
                    all.setting_ip=all.lan_chip_ip;                 //更换连接的ip
                    all.setting_port=all.lan_chip_port;             //更换连接的端口
                    text_ip.setText("    "+all.setting_ip);                  //更换显示的文本框内容，ip
                    text_port.setText("    "+ all.setting_port +"      (局域网模式)");  //更换显示的文本框内容，port
                    //开启局域网模式消息接收线程
                    all.thread_lan=new ReceiveMessageFromLAN(all.handler);
                    all.thread_lan.start();
                }
            }
        });
    }

    //检查三个范围值是否有误
    String CheckScopen()
    {
        if(all.wendu_min>all.wendu_max)
            return "温度范围输入有误！";
        if(all.shidu_min>all.shidu_max)
            return "湿度范围输入有误！";
        if(all.guangzhao_min>all.guangzhao_max)
            return "光照范围输入有误！";
        return "OK";
    }

    //调用网页API发送所有设置信息
    void UseAPISendSetting()
    {
        StringBuilder str=new StringBuilder(all.api_web_url);
        //添加用户认证信息
        str.append(all.api_user_config);
        //添加功能调用
        str.append(all.api_operation_setallsettings_string);
        //添加信息
        str.append("&ip=").append(text_ip.getText())
                .append("&port=").append(text_port.getText())
                .append("&wendu_min=").append(text_wendumin.getText())
                .append("&wendu_max=").append(text_wendumax.getText())
                .append("&shidu_min=").append(text_shidumin.getText())
                .append("&shidu_max=").append(text_shidumax.getText())
                .append("&gz_min=").append(text_gzmin.getText())
                .append("&gz_max=").append(text_gzmax.getText())
                .append("&phone=").append(text_phone.getText())
                .append("&monitor_switch=").append(switch_auto_monitor.isChecked() ?"1":"0");
        //调用网页API
        APIResultStruct struct=new APIResultStruct();
        while(struct.getCode()!=1)
            new UseWebAPI(str.toString(),struct).start();
    }

    //从网页获取设置信息并初始化
    void InitSettingsFromWeb()
    {
        APIResultStruct struct=new APIResultStruct();
        while(struct.getCode()!=1)
            new UseWebAPI(all.api_web_url+all.api_user_config+all.api_operation_getallsettings_string,struct);

    }

    //发送Toast文本
    void ShowToastMessage(String str)
    {
        Toast.makeText(Setting_UI.this,str,Toast.LENGTH_SHORT).show();
    }
}
