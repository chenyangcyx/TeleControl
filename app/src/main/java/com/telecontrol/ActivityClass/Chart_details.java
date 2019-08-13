package com.telecontrol.ActivityClass;

import com.telecontrol.App.OverAllData;
import com.telecontrol.R;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Chart_details extends AppCompatActivity
{
    Button button_back,button_wendu,button_shidu,button_guangzhao,button_tongji;
    Button button_refresh,button_copy;
    ScrollView scroll;
    TextView text;
    WebView web_chart1, web_chart2;
    View line;
    OverAllData all=OverAllData.alldata;

    final int MODE_WENDU=1;         //温度
    final int MODE_SHIDU=2;         //湿度
    final int MODE_GZ=3;            //光照
    final int MODE_XX=4;            //详细
    int CURRENT_MODE=MODE_WENDU;    //当前模式

    final int SHOWMODE_CHART=0;     //图表显示模式
    final int SHOWMODE_TEXT=1;      //文本显示模式
    int CURRENT_SHOWMODE=SHOWMODE_CHART;    //当前显示模式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts_details);
        //设置setting界面的状态栏
        Toolbar toolbar = findViewById(R.id.chart_details_ui_toolbar);
        setSupportActionBar(toolbar);
        //初始化控件
        InitViewUnit();
        //添加控件监听器
        AddUnitActionListener();
        //设置显示模式为图表模式
        SetCurrentShowMode(SHOWMODE_CHART);
        //开启图表的自动刷新
        AutoRefreshChart();
    }

    //初始化控件
    @SuppressLint("SetJavaScriptEnabled")
    void InitViewUnit()
    {
        button_back=findViewById(R.id.chart_details_ui_back);
        button_wendu=findViewById(R.id.chart_details_ui_button_wendu);
        button_shidu=findViewById(R.id.chart_details_ui_button_shidu);
        button_guangzhao=findViewById(R.id.chart_details_ui_button_gz);
        button_tongji=findViewById(R.id.chart_details_ui_button_tongji);
        web_chart1=findViewById(R.id.chart_details_ui_web1);
        web_chart2=findViewById(R.id.chart_details_ui_web2);
        button_refresh=findViewById(R.id.chart_details_ui_button_refresh);
        button_copy=findViewById(R.id.chart_details_ui__button_copy);
        scroll=findViewById(R.id.chart_details_ui_scroll);
        text=findViewById(R.id.chart_details_ui_text);
        line=findViewById(R.id.chart_details_ui_line);
        //设置WebView1
        web_chart1.getSettings().setJavaScriptEnabled(true);
        web_chart1.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        web_chart1.getSettings().setAppCacheEnabled(false);
        web_chart1.getSettings().setDomStorageEnabled(true);
        web_chart1.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    view.loadUrl(url);
                    web_chart1.stopLoading();
                    return true;
                }
                return false;
            }
        });
        web_chart1.loadUrl("file:///android_asset/chart_detail1.html");
        //设置WebView2
        web_chart2.getSettings().setJavaScriptEnabled(true);
        web_chart2.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        web_chart2.getSettings().setAppCacheEnabled(false);
        web_chart2.getSettings().setDomStorageEnabled(true);
        web_chart2.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    view.loadUrl(url);
                    web_chart2.stopLoading();
                    return true;
                }
                return false;
            }
        });
        web_chart2.loadUrl("file:///android_asset/chart_detail2.html");
    }

    //添加控件监听器
    void AddUnitActionListener()
    {
        //返回按钮监听
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chart_details.this.finish();
            }
        });

        //温度按钮监听
        button_wendu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CURRENT_MODE=MODE_WENDU;
            }
        });

        //湿度按钮监听
        button_shidu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CURRENT_MODE=MODE_SHIDU;
            }
        });

        //光照按钮监听
        button_guangzhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CURRENT_MODE=MODE_GZ;
            }
        });

        //统计按钮监听
        button_tongji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CURRENT_SHOWMODE=1-CURRENT_SHOWMODE;
                SetCurrentShowMode(CURRENT_SHOWMODE);
            }
        });

        //复制按钮监听
        button_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    //获取剪切板管理器
                    ClipboardManager cm=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                    //创建普通字符型ClipData
                    ClipData mClipData=ClipData.newPlainText("Label",text.getText());
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

        //刷新按钮监听
        button_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text.setText(GetTongjiInfo());
            }
        });
    }

    //刷新温度图表
    void RefreshWenduChart()
    {
        web_chart1.loadUrl("javascript:createChart('湿度',0,100,['10','22'],['44','77'])");
        web_chart2.loadUrl("javascript:createChart('温度',0,100,['1','2'],['3','4'],['10','20'],['50','20'])");
    }

    //刷新湿度图表
    void RefreshShiduChart()
    {
        web_chart1.loadUrl("javascript:createChart('湿度',0,50,['10','22'],['44','77'])");
        web_chart2.loadUrl("javascript:createChart('光照',50,100,['1','2'],['3','4'],['60','80'],['77','20'])");
    }

    //刷新光照图表
    void RefreshGuangzhaoChart()
    {
    }

    //图表的自动刷新
    void AutoRefreshChart()
    {
        final Handler han=new Handler();
        han.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(CURRENT_SHOWMODE==SHOWMODE_CHART)
                    switch (CURRENT_MODE)
                    {
                        case MODE_WENDU:
                            RefreshWenduChart();
                            break;
                        case MODE_SHIDU:
                            RefreshShiduChart();
                            break;
                        case MODE_GZ:
                            RefreshGuangzhaoChart();
                            break;
                    }
                han.postDelayed(this,all.ChartRefreshTime);
            }
        }, 0);
    }

    //设置模式
    void SetCurrentShowMode(int mode)
    {
        if(mode==SHOWMODE_TEXT)            //图表显示模式
        {
            web_chart1.setVisibility(View.INVISIBLE);       //图表1不可见
            web_chart2.setVisibility(View.INVISIBLE);       //图表2不可见
            line.setVisibility(View.INVISIBLE);             //分隔线不可见
            scroll.setVisibility(View.VISIBLE);             //文本框滚动可见
            text.setVisibility(View.VISIBLE);               //文本框可见
            button_copy.setVisibility(View.VISIBLE);        //复制按钮可见
            button_refresh.setVisibility(View.VISIBLE);     //刷新按钮可见
        }
        else                                //文本显示模式
        {
            web_chart1.setVisibility(View.VISIBLE);         //图表1可见
            web_chart2.setVisibility(View.VISIBLE);         //图表2可见
            line.setVisibility(View.VISIBLE);               //分隔线可见
            scroll.setVisibility(View.INVISIBLE);           //文本框滚动不可见
            text.setVisibility(View.INVISIBLE);             //文本框不可见
            button_copy.setVisibility(View.INVISIBLE);      //复制按钮不可见
            button_refresh.setVisibility(View.INVISIBLE);   //刷新按钮不可见
        }
    }

    //获取统计信息
    String GetTongjiInfo()
    {
        return null;
    }

    //发送Toast文本
    public void ShowToastMessage(String str)
    {
        Toast.makeText(Chart_details.this,str,Toast.LENGTH_SHORT).show();
    }
}
