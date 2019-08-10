package com.example.telecontrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Setting_UI extends AppCompatActivity {

    Button setting_enter,setting_reset,setting_changemode;
    EditText setting_ip,setting_port,setting_wendumin,setting_wendumax,setting_shidumin,setting_shidumax,setting_gzmin,setting_gzmax;
    OverAllData all=OverAllData.alldata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_ui);
        //设置setting界面的状态栏
        Toolbar toolbar = findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);
        //初始化控件
        InitViewUnit();
        //添加控件监听器
        AddUnitActionListener();
    }

    //初始化控件
    private void InitViewUnit()
    {
        setting_reset=(Button)findViewById(R.id.setting_reset);
        setting_enter=(Button)findViewById(R.id.setting_enter);
        setting_changemode=(Button)findViewById(R.id.setting_changemode);
        setting_ip=(EditText)findViewById(R.id.setting_ip);
        setting_port=(EditText)findViewById(R.id.setting_port);
        setting_wendumin=(EditText)findViewById(R.id.setting_wendumin);
        setting_wendumax=(EditText)findViewById(R.id.setting_wendumax);
        setting_shidumin=(EditText)findViewById(R.id.setting_shidumin);
        setting_shidumax=(EditText)findViewById(R.id.setting_shidumax);
        setting_gzmin=(EditText)findViewById(R.id.setting_guangzhaomin);
        setting_gzmax=(EditText)findViewById(R.id.setting_guangzhaomax);
        setting_wendumin.setText(Integer.toString(all.wendu_min));
        setting_wendumax.setText(Integer.toString(all.wendu_max));
        setting_shidumin.setText(Integer.toString(all.shidu_min));
        setting_shidumax.setText(Integer.toString(all.shidu_max));
        setting_gzmin.setText(Integer.toString(all.guangzhao_min));
        setting_gzmax.setText(Integer.toString(all.guangzhao_max));
        //初始化服务器IP和端口的输入框
        setting_ip.setText("    "+all.setting_ip);
        if(all.LINK_MODE==all.LINK_MODE_WEB)
            setting_port.setText("    "+Integer.toString(all.setting_port)+"    (服务器模式)");
        else
            setting_port.setText("    "+Integer.toString(all.setting_port)+"      (局域网模式)");
        setting_ip.setFocusable(false);
        setting_port.setFocusable(false);
    }

    //添加控件监听器
    private void AddUnitActionListener()
    {
        //重置按钮
        setting_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                all.wendu_min=all.wendu_min_init;            //温度监测最低值
                all.wendu_max=all.wendu_max_init;            //温度监测最高值
                all.shidu_min=all.shidu_min_init;            //湿度监测最低值
                all.shidu_max=all.shidu_max_init;            //湿度监测最高值
                all.guangzhao_min=all.guangzhao_min_init;    //光照控制最低值
                all.guangzhao_max=all.guangzhao_max_inti;    //光照控制最高值
                setting_wendumin.setText(Integer.toString(all.wendu_min));        //温度低
                setting_wendumax.setText(Integer.toString(all.wendu_max));        //温度高
                setting_shidumin.setText(Integer.toString(all.shidu_min));        //湿度低
                setting_shidumax.setText(Integer.toString(all.shidu_max));        //湿度高
                setting_gzmin.setText(Integer.toString(all.guangzhao_min));       //光照低
                setting_gzmax.setText(Integer.toString(all.guangzhao_max));       //光照高
            }
        });

        //确认按钮
        setting_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all.wendu_min=setting_wendumin.getText().toString().equals("")?0:Integer.parseInt(setting_wendumin.getText().toString());
                all.wendu_max=setting_wendumax.getText().toString().equals("")?0:Integer.parseInt(setting_wendumax.getText().toString());
                all.shidu_min=setting_shidumin.getText().toString().equals("")?0:Integer.parseInt(setting_shidumin.getText().toString());
                all.shidu_max=setting_shidumax.getText().toString().equals("")?0:Integer.parseInt(setting_shidumax.getText().toString());
                all.guangzhao_min=setting_gzmin.getText().toString().equals("")?0:Integer.parseInt(setting_gzmin.getText().toString());
                all.guangzhao_max=setting_gzmax.getText().toString().equals("")?0:Integer.parseInt(setting_gzmax.getText().toString());
                if(!CheckScopen().equals("OK"))
                {
                    ShowToastMessage(CheckScopen());
                    return;
                }
                Intent intent=new Intent(Setting_UI.this,MainActivity.class);
                startActivity(intent);
            }
        });

        //切换模式按钮
        setting_changemode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all.LINK_MODE=1-all.LINK_MODE;
                if(all.LINK_MODE==0)        //联网模式
                {
                    ShowToastMessage("切换到服务器模式");       //显示提示消息
                    all.setting_ip=all.server_ip;                 //更换连接的ip
                    all.setting_port=all.server_port;             //更换连接的端口
                    setting_ip.setText("    "+all.setting_ip);                  //更换显示的文本框内容，ip
                    setting_port.setText("    "+Integer.toString(all.setting_port)+"    (服务器模式)");  //更换显示的文本框内容，port
                }
                else                        //局域网模式
                {
                    ShowToastMessage("切换到局域网模式");       //显示提示消息
                    all.setting_ip=all.lan_chip_ip;                 //更换连接的ip
                    all.setting_port=all.lan_chip_port;             //更换连接的端口
                    setting_ip.setText("    "+all.setting_ip);                  //更换显示的文本框内容，ip
                    setting_port.setText("    "+Integer.toString(all.setting_port)+"      (局域网模式)");  //更换显示的文本框内容，port
                }
            }
        });
    }

    //检查三个范围值是否有误
    private String CheckScopen()
    {
        if(all.wendu_min>all.wendu_max)
            return "温度范围输入有误！";
        if(all.shidu_min>all.shidu_max)
            return "湿度范围输入有误！";
        if(all.guangzhao_min>all.guangzhao_max)
            return "光照范围输入有误！";
        return "OK";
    }

    //发送Toast文本
    public void ShowToastMessage(String str)
    {
        Toast.makeText(Setting_UI.this,str,Toast.LENGTH_SHORT).show();
    }
}
