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
    Button main_setting,main_network,main_details;
    EditText ui_wendu,ui_shidu,ui_guangzhaoqiangdu;
    Switch sw_pqs,sw_zgl,sw_gg,sw_bgd;
    WebView ui_chart;
    ControlMethod con=new ControlMethod();
    Handler han=null;
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
        //初始化该页面的Handler控件
        InitMainHandler();
        //开启服务器模式消息接收线程
        all.thread_web=new ReceiveMessageFromWeb(han);
        all.thread_web.start();
        //开启局域网模式消息接收线程
        all.thread_lan=new ReceiveMessageFromLAN(han);
        all.thread_lan.start();
    }

    //初始化控件
    @SuppressLint("SetJavaScriptEnabled")
    private void InitViewUnit()
    {
        //获取button控件
        main_setting= findViewById(R.id.main_setting);
        main_network= findViewById(R.id.main_network);
        main_details= findViewById(R.id.main_details);
        //获取三个文本框控件
        ui_wendu= findViewById(R.id.text_wendu);
        ui_shidu= findViewById(R.id.text_shidu);
        ui_guangzhaoqiangdu= findViewById(R.id.text_gzqd);
        //获取四个开关控件
        sw_pqs = findViewById(R.id.switch_pqs);
        sw_zgl= findViewById(R.id.switch_zgl);
        sw_gg= findViewById(R.id.switch_gg);
        sw_bgd= findViewById(R.id.switch_bgd);
        //初始化WebView
        ui_chart= findViewById(R.id.main_chart);
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
        //网络信息按钮
        main_network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Network_details.class);
                startActivity(intent);
            }
        });
        //详细信息按钮
        main_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                }
                else
                {
                    con.PQS_Control("OFF");
                    ShowToastMessage("关闭排气扇");
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
                }
                else
                {
                    con.ZGL_Control("OFF");
                    ShowToastMessage("关闭遮光帘");
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
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sw_gg.setChecked(false);
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
                }
                else
                {
                    con.BGD_Control("OFF");
                    ShowToastMessage("关闭补光灯");
                }
            }
        });
    }

    //初始化该页面的Handler控件
    @SuppressLint("HandlerLeak")
    public void InitMainHandler()
    {
        //main页面的Handler事件初始化
        this.han=new Handler(){
            @SuppressLint("SetTextI18n")
            @SuppressWarnings("NullableProblems")
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
                        ui_guangzhaoqiangdu.setText("" + all.current_gz+" lx");
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
                        ui_guangzhaoqiangdu.setText("" + all.current_gz+" lx");
                        //更新图表
                        RefreshChart();
                        //监控
                        MonitorValue();
                        break;
                }
            }
        };
    }

    //更新Web图表
    public void RefreshChart()
    {
        StringBuilder wendu_array=new StringBuilder("[]");
        StringBuilder shidu_array=new StringBuilder("[]");
        StringBuilder time_array=new StringBuilder("[]");
        int length=all.data_time.size();
        if(length==0)
        {
            ui_chart.loadUrl("javascript:createChart("+time_array+","+wendu_array+","+shidu_array+
                    ","+all.CHART_MIN_WENDU+","+all.CHART_MAX_WENDU+","+all.CHART_MIN_SHIDU+","+all.CHART_MAX_SHIDU+")");
            return;
        }
        if(length-all.CHART_MAX_SIZE<0)
        {
            wendu_array.delete(0,wendu_array.length());
            wendu_array.append("['").append(all.data_wendu.get(0).toString()).append("'");
            shidu_array.delete(0,shidu_array.length());
            shidu_array.append("['").append(all.data_shidu.get(0).toString()).append("'");
            time_array.delete(0,time_array.length());
            time_array.append("['").append(all.data_time.get(0)).append("'");
            for(int i=1;i<length;i++)
            {
                wendu_array.append(",'").append(all.data_wendu.get(i).toString()).append("'");
                shidu_array.append(",'").append(all.data_shidu.get(i).toString()).append("'");
                time_array.append(",'").append(all.data_time.get(i)).append("'");
            }
            wendu_array.append("]");
            shidu_array.append("]");
            time_array.append("]");
        }
        else
        {
            wendu_array.delete(0,wendu_array.length());
            wendu_array.append("['").append(all.data_wendu.get(length - all.CHART_MAX_SIZE).toString()).append("'");
            shidu_array.delete(0,shidu_array.length());
            shidu_array.append("['").append(all.data_shidu.get(length - all.CHART_MAX_SIZE).toString()).append("'");
            time_array.delete(0,time_array.length());
            time_array.append("['").append(all.data_time.get(length - all.CHART_MAX_SIZE)).append("'");
            for(int i=length-all.CHART_MAX_SIZE+1;i<length;i++)
            {
                wendu_array.append(",'").append(all.data_wendu.get(i).toString()).append("'");
                shidu_array.append(",'").append(all.data_shidu.get(i).toString()).append("'");
                time_array.append(",'").append(all.data_time.get(i)).append("'");
            }
            wendu_array.append("]");
            shidu_array.append("]");
            time_array.append("]");
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
                Toast.makeText(MainActivity.this, "温度过高，打开遮光帘", Toast.LENGTH_LONG).show();
            }
        }
        if (all.current_wendu < all.wendu_min || all.current_gz < all.guangzhao_min) {
            if (sw_zgl.isChecked()) {
                con.ZGL_Control("OFF");
                sw_zgl.setChecked(false);
                Toast.makeText(MainActivity.this, "温度过低，关闭遮光帘", Toast.LENGTH_LONG).show();
            }
        }
        if (all.current_shidu > all.shidu_max && !sw_pqs.isChecked()) {
            con.PQS_Control("ON");
            sw_pqs.setChecked(true);
            Toast.makeText(MainActivity.this, "湿度过高，开启排气扇", Toast.LENGTH_LONG).show();
        }
        if (all.current_shidu < all.shidu_min && !sw_gg.isChecked()) {
            if(all.IfCloudOpenGG())         //可以开启灌溉
            {
                con.GG_Control("ON");
                sw_gg.setChecked(true);
                Toast.makeText(MainActivity.this, "湿度过低，开启灌溉", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sw_gg.setChecked(false);
                        ShowToastMessage("灌溉完毕，关闭灌溉");
                    }
                }, all.GG_TIME);
                all.RefreshOpenGGTime();
            }
        }
    }

    //发送Toast文本
    public void ShowToastMessage(String str)
    {
        Toast.makeText(MainActivity.this,str,Toast.LENGTH_SHORT).show();
    }
}
