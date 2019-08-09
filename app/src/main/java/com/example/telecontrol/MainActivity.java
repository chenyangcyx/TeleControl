package com.example.telecontrol;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    Button main_setting;
    EditText ui_wendu,ui_shidu,ui_guangzhaoqiangdu;
    Switch sw_pqs,sw_zgl,sw_gg,sw_bgd;
    WebView ui_chart;
    ControlMethod con=new ControlMethod();
    OverAllData all=OverAllData.alldata;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置main界面的状态栏
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        //初始化控件
        InitViewUnit();
        //添加控件监听器
        AddUnitActionListener();
        //开启服务器模式消息接收线程
        all.thread_web=new ReceiveMessageFromWeb(handler);
        all.thread_web.start();
        //开启局域网模式消息接收线程
        all.thread_lan=new ReceiveMessageFromLAN(handler);
        all.thread_lan.start();
    }

    //初始化控件
    private void InitViewUnit()
    {
        //获取button控件
        main_setting=(Button)findViewById(R.id.main_setting);
        //获取三个文本框控件
        ui_wendu=(EditText) findViewById(R.id.text_wendu);
        ui_shidu=(EditText)findViewById(R.id.text_shidu);
        ui_guangzhaoqiangdu=(EditText)findViewById(R.id.text_gzqd);
        //获取四个开关控件
        sw_pqs =(Switch)findViewById(R.id.switch_pqs);
        sw_zgl=(Switch)findViewById(R.id.switch_zgl);
        sw_gg=(Switch)findViewById(R.id.switch_gg);
        sw_bgd=(Switch)findViewById(R.id.switch_bgd);
        //初始化开关控件
        sw_pqs.setChecked(all.switch_pqs_state);
        sw_zgl.setChecked(all.switch_zgl_state);
        sw_gg.setChecked(all.switch_gg_state);
        sw_bgd.setChecked(all.switch_bgd_state);
        //初始化WebView
        ui_chart=(WebView)findViewById(R.id.main_chart);
        ui_chart.clearCache(true);
        ui_chart.clearHistory();
        ui_chart.requestFocus();
        ui_chart.getSettings().setJavaScriptEnabled(true);
        ui_chart.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        ui_chart.getSettings().setAppCacheEnabled(false);
        ui_chart.getSettings().setDomStorageEnabled(true);
        ui_chart.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不另跳浏览器
                // 在2.3上面不加这句话，可以加载出页面，在4.0上面必须要加入，不然出现白屏
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    view.loadUrl(url);
                    ui_chart.stopLoading();
                    return true;
                }
                return false;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        ui_chart.loadUrl("file:///android_asset/charts.html");
    }

    //添加控件监听器
    private void AddUnitActionListener()
    {
        //主界面setting按钮绑定事件
        main_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Setting_UI.class);
                startActivity(intent);
            }
        });
        //排气扇开关监听
        sw_pqs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!buttonView.isPressed())
                    return;
                if(isChecked)
                {
                    con.PQS_Control("ON");
                    ShowToastMessage("打开排气扇");
                    all.switch_pqs_state=true;
                }
                else
                {
                    con.PQS_Control("OFF");
                    ShowToastMessage("关闭排气扇");
                    all.switch_pqs_state=false;
                }
            }
        });
        //遮光帘开关监听
        sw_zgl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!buttonView.isPressed())
                    return;
                if(isChecked)
                {
                    con.ZGL_Control("ON");
                    ShowToastMessage("打开遮光帘");
                    all.switch_zgl_state=true;
                }
                else
                {
                    con.ZGL_Control("OFF");
                    ShowToastMessage("关闭遮光帘");
                    all.switch_zgl_state=false;
                }
            }
        });
        //灌溉开关监听
        sw_gg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!buttonView.isPressed())
                    return;
                if(isChecked)
                {
                    if(all.IfCloudOpenGG())
                    {
                        con.GG_Control("ON");
                        ShowToastMessage("打开灌溉");
                        all.switch_gg_state=true;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sw_gg.setChecked(false);
                                all.switch_gg_state=false;
                                ShowToastMessage("灌溉完毕，关闭");
                            }
                        }, all.GG_TIME);
                        all.RefreshOpenGGTime();
                    }
                    else
                    {
                        ShowToastMessage(""+all.SurplusCloudGGTime()+"秒后才可重新灌溉！");
                        sw_gg.setChecked(false);
                    }
                }
                else
                {
                    con.GG_Control("OFF");
                    ShowToastMessage("关闭灌溉");
                    all.switch_gg_state=false;
                }
            }
        });
        //补光灯开关监听
        sw_bgd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!buttonView.isPressed())
                    return;
                if(isChecked)
                {
                    con.BGD_Control("ON");
                    ShowToastMessage("打开补光灯");
                    all.switch_bgd_state=true;
                }
                else
                {
                    con.BGD_Control("OFF");
                    ShowToastMessage("关闭补光灯");
                    all.switch_bgd_state=false;
                }
            }
        });
    }

    //更新Web图表
    public void RefreshChart()
    {
        String wendu_array="[]";
        String shidu_array="[]";
        String time_array="[]";
        int length=all.data_time.size();
        if(length==0)
        {
            ui_chart.loadUrl("javascript:createChart("+time_array+","+wendu_array+","+shidu_array+
                    ","+all.CHART_MIN_WENDU+","+all.CHART_MAX_WENDU+","+all.CHART_MIN_SHIDU+","+all.CHART_MAX_SHIDU+")");
            return;
        }
        if(length-all.CHART_MAX_SIZE<0)
        {
            wendu_array="['"+all.data_wendu.get(0).toString()+"'";
            shidu_array="['"+all.data_shidu.get(0).toString()+"'";
            time_array="['"+all.data_time.get(0)+"'";
            for(int i=1;i<length;i++)
            {
                wendu_array+=",'"+all.data_wendu.get(i).toString()+"'";
                shidu_array+=",'"+all.data_shidu.get(i).toString()+"'";
                time_array+=",'"+all.data_time.get(i)+"'";
            }
            wendu_array+="]";
            shidu_array+="]";
            time_array+="]";
        }
        else
        {
            wendu_array="['"+all.data_wendu.get(length-all.CHART_MAX_SIZE).toString()+"'";
            shidu_array="['"+all.data_shidu.get(length-all.CHART_MAX_SIZE).toString()+"'";
            time_array="['"+all.data_time.get(length-all.CHART_MAX_SIZE)+"'";
            for(int i=length-all.CHART_MAX_SIZE+1;i<length;i++)
            {
                wendu_array+=",'"+all.data_wendu.get(i).toString()+"'";
                shidu_array+=",'"+all.data_shidu.get(i).toString()+"'";
                time_array+=",'"+all.data_time.get(i)+"'";
            }
            wendu_array+="]";
            shidu_array+="]";
            time_array+="]";
        }
        ui_chart.loadUrl("javascript:createChart("+time_array+","+wendu_array+","+shidu_array+
                ","+all.CHART_MIN_WENDU+","+all.CHART_MAX_WENDU+","+all.CHART_MIN_SHIDU+","+all.CHART_MAX_SHIDU+")");
    }

    //数值监控
    public void MonitorValue()
    {
        if (all.current_wendu > all.wendu_max || all.current_gz > all.guangzhao_max) {
            if (!sw_zgl.isChecked()) {
                con.ZGL_Control("ON");
                sw_zgl.setChecked(true);
                all.switch_zgl_state=true;
                Toast.makeText(MainActivity.this, "温度过高，打开遮光帘", Toast.LENGTH_LONG).show();
            }
        }
        if (all.current_wendu < all.wendu_min || all.current_gz < all.guangzhao_min) {
            if (sw_zgl.isChecked()) {
                con.ZGL_Control("OFF");
                sw_zgl.setChecked(false);
                all.switch_zgl_state=false;
                Toast.makeText(MainActivity.this, "温度过低，关闭遮光帘", Toast.LENGTH_LONG).show();
            }
        }
        if (all.current_shidu > all.shidu_max && !sw_pqs.isChecked()) {
            con.PQS_Control("ON");
            sw_pqs.setChecked(true);
            all.switch_pqs_state=true;
            Toast.makeText(MainActivity.this, "湿度过高，开启排气扇", Toast.LENGTH_LONG).show();
        }
        if (all.current_shidu < all.shidu_min && !sw_gg.isChecked()) {
            if(all.IfCloudOpenGG())         //可以开启灌溉
            {
                con.GG_Control("ON");
                sw_gg.setChecked(true);
                all.switch_gg_state=true;
                Toast.makeText(MainActivity.this, "湿度过低，开启灌溉", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sw_gg.setChecked(false);
                        all.switch_gg_state=false;
                        ShowToastMessage("灌溉完毕，关闭灌溉");
                    }
                }, all.GG_TIME);
                all.RefreshOpenGGTime();
            }
        }
    }

    //创建Handler事件
    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:             //服务器模式
                    //检查模式
                    if(all.LINK_MODE!=all.LINK_MODE_WEB)
                        break;
                    //消息解析
                    all.EncodeReceiveMessageFromWeb(msg.obj.toString());
                    //设置当前数值
                    ui_wendu.setText("" + all.current_wendu + " ℃");
                    ui_shidu.setText("" + all.current_shidu + " %");
                    ui_guangzhaoqiangdu.setText("" + all.current_gz);
                    //更新图表
                    RefreshChart();
                    //监控
                        MonitorValue();
                    break;
                case 2:             //局域网模式
                    //检查模式
                    if(all.LINK_MODE!=all.LINK_MODE_LAN)
                        break;
                    //消息解析
                    all.EncodeReceiveMessageFromLAN(msg.obj.toString());
                    //设置当前数值
                    ui_wendu.setText("" + all.current_wendu + " ℃");
                    ui_shidu.setText("" + all.current_shidu + " %");
                    ui_guangzhaoqiangdu.setText("" + all.current_gz);
                    //更新图表
                    RefreshChart();
                    //监控
                    MonitorValue();
                    break;
            }
        }
    };

    //发送Toast文本
    public void ShowToastMessage(String str)
    {
        Toast.makeText(MainActivity.this,str,Toast.LENGTH_SHORT).show();
    }
}
