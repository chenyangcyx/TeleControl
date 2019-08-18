package com.telecontrol.ActivityClass;

import com.telecontrol.R;
import com.telecontrol.App.OverAllData;
import com.telecontrol.SocketFunction.APIResultStruct;
import com.telecontrol.SocketFunction.UseWebAPI;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

public class Setting_UI extends AppCompatActivity {

    Button button_enter, button_reset;
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
        //从网页获取设置信息并设置
        InitSettingsFromWeb();
        //设置控件的值
        SetViewUnit();
        //刷新UI界面
        RefreshUIByMonitorSwitch();
    }

    //初始化控件
    void InitViewUnit()
    {
        button_reset = findViewById(R.id.setting_ui_button_reset);
        button_enter = findViewById(R.id.setting_ui_button_enter);
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
    }

    //设置控件的值
    @SuppressLint("SetTextI18n")
    void SetViewUnit()
    {
        text_ip.setText(all.setting_ip);                               //设置ip
        text_port.setText(String.valueOf(all.setting_port));           //设置port
        text_wendumin.setText(Integer.toString(all.wendu_min));        //温度最低值
        text_wendumax.setText(Integer.toString(all.wendu_max));        //温度最高值
        text_shidumin.setText(Integer.toString(all.shidu_min));        //湿度最低值
        text_shidumax.setText(Integer.toString(all.shidu_max));        //湿度最高值
        text_gzmin.setText(Integer.toString(all.guangzhao_min));       //光照最低值
        text_gzmax.setText(Integer.toString(all.guangzhao_max));       //光照最高值
        text_phone.setText(all.phone);                                 //号码
        switch_auto_monitor.setChecked(all.monitor_switch);            //自动监测按钮
        text_phone.requestFocus();                                     //设置默认焦点
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
                all.setting_ip=all.server_ip;                //设置ip
                all.setting_port=all.server_port;            //设置port
                all.wendu_min=all.wendu_min_init;            //温度监测最低值
                all.wendu_max=all.wendu_max_init;            //温度监测最高值
                all.shidu_min=all.shidu_min_init;            //湿度监测最低值
                all.shidu_max=all.shidu_max_init;            //湿度监测最高值
                all.guangzhao_min=all.guangzhao_min_init;    //光照控制最低值
                all.guangzhao_max=all.guangzhao_max_init;    //光照控制最高值
                all.phone=all.phone_init;                    //号码
                all.monitor_switch=all.monitor_switch_init;  //自动监测开关
                text_ip.setText(all.setting_ip);                               //设置ip
                text_port.setText(all.setting_port);                           //设置port
                text_wendumin.setText(Integer.toString(all.wendu_min));        //温度最低值
                text_wendumax.setText(Integer.toString(all.wendu_max));        //温度最高值
                text_shidumin.setText(Integer.toString(all.shidu_min));        //湿度最低值
                text_shidumax.setText(Integer.toString(all.shidu_max));        //湿度最高值
                text_gzmin.setText(Integer.toString(all.guangzhao_min));       //光照最低值
                text_gzmax.setText(Integer.toString(all.guangzhao_max));       //光照最高值
                text_phone.setText(all.phone);                                 //号码
                switch_auto_monitor.setChecked(all.monitor_switch);            //自动监测按钮
            }
        });

        //确认按钮
        button_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all.setting_ip=text_ip.getText().toString();
                all.setting_port=Integer.parseInt(text_port.getText().toString());
                all.wendu_min=text_wendumin.getText().toString().equals("")?0:Integer.parseInt(text_wendumin.getText().toString());
                all.wendu_max=text_wendumax.getText().toString().equals("")?0:Integer.parseInt(text_wendumax.getText().toString());
                all.shidu_min=text_shidumin.getText().toString().equals("")?0:Integer.parseInt(text_shidumin.getText().toString());
                all.shidu_max=text_shidumax.getText().toString().equals("")?0:Integer.parseInt(text_shidumax.getText().toString());
                all.guangzhao_min=text_gzmin.getText().toString().equals("")?0:Integer.parseInt(text_gzmin.getText().toString());
                all.guangzhao_max=text_gzmax.getText().toString().equals("")?0:Integer.parseInt(text_gzmax.getText().toString());
                all.phone=text_phone.getText().toString();
                all.monitor_switch=switch_auto_monitor.isChecked();
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

        //自动监测开关
        switch_auto_monitor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                all.monitor_switch=isChecked;
                RefreshUIByMonitorSwitch();
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

    //根据“自动监测”开关刷新页面控件
    void RefreshUIByMonitorSwitch()
    {
        if(all.monitor_switch)
        {
            text_ip.setEnabled(false);
            text_port.setEnabled(false);
            text_wendumin.setEnabled(false);
            text_wendumax.setEnabled(false);
            text_shidumin.setEnabled(false);
            text_shidumax.setEnabled(false);
            text_gzmin.setEnabled(false);
            text_gzmax.setEnabled(false);
            text_phone.setEnabled(false);
        }
        else
        {
            text_ip.setEnabled(true);
            text_port.setEnabled(true);
            text_wendumin.setEnabled(true);
            text_wendumax.setEnabled(true);
            text_shidumin.setEnabled(true);
            text_shidumax.setEnabled(true);
            text_gzmin.setEnabled(true);
            text_gzmax.setEnabled(true);
            text_phone.setEnabled(true);
        }
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
        str.append("&ip=").append(all.setting_ip)
                .append("&port=").append(all.setting_port)
                .append("&wendu_min=").append(all.wendu_min)
                .append("&wendu_max=").append(all.wendu_max)
                .append("&shidu_min=").append(all.shidu_min)
                .append("&shidu_max=").append(all.shidu_max)
                .append("&gz_min=").append(all.guangzhao_min)
                .append("&gz_max=").append(all.guangzhao_max)
                .append("&phone=").append(all.phone)
                .append("&monitor_switch=").append(all.monitor_switch ?"1":"0");
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
            new UseWebAPI(all.api_web_url+all.api_user_config+all.api_operation_getallsettings_string,struct).start();
        try {
            JSONObject json=new JSONObject(struct.getDetails());
            String ip=json.getString("ip");
            String port=json.getString("port");
            String wendu_min=json.getString("wendu_min");
            String wendu_max=json.getString("wendu_max");
            String shidu_min=json.getString("shidu_min");
            String shidu_max=json.getString("shidu_max");
            String gz_min=json.getString("gz_min");
            String gz_max=json.getString("gz_max");
            String phone=json.getString("phone");
            String monitor_switch=json.getString("monitor_switch");
            all.setting_ip=ip;
            all.setting_port=Integer.parseInt(port);
            all.wendu_min=Integer.parseInt(wendu_min);
            all.wendu_max=Integer.parseInt(wendu_max);
            all.shidu_min=Integer.parseInt(shidu_min);
            all.shidu_max=Integer.parseInt(shidu_max);
            all.guangzhao_min=Integer.parseInt(gz_min);
            all.guangzhao_max=Integer.parseInt(gz_max);
            all.phone=phone;
            all.monitor_switch= monitor_switch.equals("1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //发送Toast文本
    void ShowToastMessage(String str)
    {
        Toast.makeText(Setting_UI.this,str,Toast.LENGTH_SHORT).show();
    }
}
