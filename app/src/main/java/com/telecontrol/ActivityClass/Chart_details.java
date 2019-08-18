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
import java.util.ArrayList;

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
                if(CURRENT_SHOWMODE!=SHOWMODE_CHART)
                {
                    CURRENT_SHOWMODE=SHOWMODE_CHART;
                    SetCurrentShowMode(CURRENT_SHOWMODE);
                }
                CURRENT_MODE=MODE_WENDU;
            }
        });

        //湿度按钮监听
        button_shidu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CURRENT_SHOWMODE!=SHOWMODE_CHART)
                {
                    CURRENT_SHOWMODE=SHOWMODE_CHART;
                    SetCurrentShowMode(CURRENT_SHOWMODE);
                }
                CURRENT_MODE=MODE_SHIDU;
            }
        });

        //光照按钮监听
        button_guangzhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CURRENT_SHOWMODE!=SHOWMODE_CHART)
                {
                    CURRENT_SHOWMODE=SHOWMODE_CHART;
                    SetCurrentShowMode(CURRENT_SHOWMODE);
                }
                CURRENT_MODE=MODE_GZ;
            }
        });

        //统计按钮监听
        button_tongji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CURRENT_SHOWMODE=SHOWMODE_TEXT;
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
    void RefreshTwoChart(String type_name)
    {
        StringBuilder data_name=new StringBuilder(type_name);
        String value_range_min="";
        String value_range_max="";
        ArrayList<Integer> data_info=null;       //使用到的数据指针
        switch (data_name.toString())
        {
            case "温度":
                value_range_min=all.CHART_MIN_WENDU;
                value_range_max=all.CHART_MAX_WENDU;
                data_info=all.data_wendu;
                break;
            case "湿度":
                value_range_min=all.CHART_MIN_SHIDU;
                value_range_max=all.CHART_MAX_SHIDU;
                data_info=all.data_shidu;
                break;
            case "光照":
                value_range_min=all.CHART_MIN_GZ;
                value_range_max=all.CHART_MAX_GZ;
                data_info=all.data_gz;
                break;
        }
        //web1的参数
        StringBuilder data_x_array=new StringBuilder("[]");
        StringBuilder data_value=new StringBuilder("[]");
        //web2的参数
        StringBuilder data_prex_array=new StringBuilder("[]");
        StringBuilder data_curx_array=new StringBuilder("[]");
        StringBuilder data_prevalue=new StringBuilder("[]");
        StringBuilder data_curvalue=new StringBuilder("[]");
        int length=all.data_time.size();
        assert data_info != null;
        //开始构造web1数据
        if(length==0)
            web_chart1.loadUrl("javascript:createChart('"+data_name+"',"+value_range_min+","+
                    value_range_max+","+data_x_array+","+data_value+")");
        if(length-all.CHART_DETAILS_WEB1_SIZE<0)
        {
            data_x_array.delete(0,data_x_array.length());
            data_x_array.append("['").append(all.data_time.get(0).substring(14)).append("'");
            data_value.delete(0,data_value.length());
            data_value.append("['").append(data_info.get(0)).append("'");
            for(int i=1;i<length;i++)
            {
                data_x_array.append(",'").append(all.data_time.get(i).substring(14)).append("'");
                data_value.append(",'").append(data_info.get(i)).append("'");
            }
            data_x_array.append("]");
            data_value.append("]");
        }
        else
        {
            data_x_array.delete(0,data_x_array.length());
            data_x_array.append("['").append(all.data_time.get(length-all.CHART_DETAILS_WEB1_SIZE).substring(14)).append("'");
            data_value.delete(0,data_value.length());
            data_value.append("['").append(data_info.get(length-all.CHART_DETAILS_WEB1_SIZE)).append("'");
            for(int i=length-all.CHART_DETAILS_WEB1_SIZE+1;i<length;i++)
            {
                data_x_array.append(",'").append(all.data_time.get(i).substring(14)).append("'");
                data_value.append(",'").append(data_info.get(i)).append("'");
            }
            data_x_array.append("]");
            data_value.append("]");
        }
        web_chart1.loadUrl("javascript:createChart('"+data_name+"',"+value_range_min+","+
                value_range_max+","+data_x_array+","+data_value+")");
        //开始构造web2数据
        if(length==0)
            web_chart2.loadUrl("javascript:createChart('"+data_name+"',"+value_range_min+","+
                    value_range_max+","+data_prex_array+","+data_curx_array+","+data_prevalue+","+
                    data_curvalue+")");
        else if(length-all.CHART_DETAILS_WEB1_SIZE<0)
        {
            data_curx_array.delete(0,data_curx_array.length());
            data_curx_array.append("['").append(all.data_time.get(0).substring(14)).append("'");
            data_curvalue.delete(0,data_curvalue.length());
            data_curvalue.append("['").append(data_info.get(0)).append("'");
            for(int i=1;i<length;i++)
            {
                data_curx_array.append(",'").append(all.data_time.get(i).substring(14)).append("'");
                data_curvalue.append(",'").append(data_info.get(i)).append("'");
            }
            data_curx_array.append("]");
            data_curvalue.append("]");
        }
        else if(length-all.CHART_DETAILS_WEB2_SIZE<0)
        {
            data_curx_array.delete(0,data_curx_array.length());
            data_curx_array.append("['").append(all.data_time.get(length-all.CHART_DETAILS_WEB1_SIZE).substring(14)).append("'");
            data_curvalue.delete(0,data_curvalue.length());
            data_curvalue.append("['").append(data_info.get(length-all.CHART_DETAILS_WEB1_SIZE)).append("'");
            for(int i=length-all.CHART_DETAILS_WEB1_SIZE+1;i<length;i++)
            {
                data_curx_array.append(",'").append(all.data_time.get(i).substring(14)).append("'");
                data_curvalue.append(",'").append(data_info.get(i)).append("'");
            }
            data_curx_array.append("]");
            data_curvalue.append("]");
        }
        else
        {
            data_prex_array.delete(0,data_prex_array.length());
            data_prex_array.append("['").append(all.data_time.get(length-all.CHART_DETAILS_WEB2_SIZE).substring(14)).append("'");
            data_curx_array.delete(0,data_curx_array.length());
            data_curx_array.append("['").append(all.data_time.get(length-all.CHART_DETAILS_WEB1_SIZE).substring(14)).append("'");
            data_prevalue.delete(0,data_prevalue.length());
            data_prevalue.append("['").append(data_info.get(length-all.CHART_DETAILS_WEB2_SIZE)).append("'");
            data_curvalue.delete(0,data_curvalue.length());
            data_curvalue.append("['").append(data_info.get(length-all.CHART_DETAILS_WEB1_SIZE)).append("'");
            for(int i=length-all.CHART_DETAILS_WEB2_SIZE+1;i<length-all.CHART_DETAILS_WEB1_SIZE;i++)
            {
                data_prex_array.append(",'").append(all.data_time.get(i).substring(14)).append("'");
                data_prevalue.append(",'").append(data_info.get(i)).append("'");
            }
            for(int i=length-all.CHART_DETAILS_WEB1_SIZE+1;i<length;i++)
            {
                data_curx_array.append(",'").append(all.data_time.get(i).substring(14)).append("'");
                data_curvalue.append(",'").append(data_info.get(i)).append("'");
            }
            data_prex_array.append("]");
            data_prevalue.append("]");
            data_curx_array.append("]");
            data_curvalue.append("]");
        }
        web_chart2.loadUrl("javascript:createChart('"+data_name+"',"+value_range_min+","+
                value_range_max+","+data_prex_array+","+data_curx_array+","+data_prevalue+","+
                data_curvalue+")");
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
                            RefreshTwoChart("温度");
                            break;
                        case MODE_SHIDU:
                            RefreshTwoChart("湿度");
                            break;
                        case MODE_GZ:
                            RefreshTwoChart("光照");
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
            //默认刷新一次数据区域
            text.setText(GetTongjiInfo());
        }
    }

    //获取统计信息
    String GetTongjiInfo()
    {
        StringBuilder info_wendu=new StringBuilder();       //温度数据
        StringBuilder info_shidu=new StringBuilder();       //湿度数据
        StringBuilder info_gz=new StringBuilder();          //光照数据
        StringBuilder str=new StringBuilder();              //最后合成的数据
        String separation="\t\t\t\t\t\t";               //分隔符
        if(all.data_time.size()>0)
        {
            //构造温度、湿度、光照数据
            for(int i=0;i<all.data_time.size();i++)
            {
                info_wendu.append(all.data_time.get(i).substring(5)).append(separation).append(separation).append(all.data_wendu.get(i)).append(all.sep);
                info_shidu.append(all.data_time.get(i).substring(5)).append(separation).append(separation).append(all.data_shidu.get(i)).append(all.sep);
                info_gz.append(all.data_time.get(i).substring(5)).append(separation).append(separation).append(all.data_gz.get(i)).append(all.sep);
            }
            //数据合并
            str.append("------------温度数据------------").append(all.sep);
            str.append(separation).append("时间").append(separation).append(separation).append("\t\t\t\t").append("温度值").append(all.sep);
            str.append(info_wendu).append(all.sep).append(all.sep);
            str.append("------------湿度数据------------").append(all.sep);
            str.append(separation).append("时间").append(separation).append(separation).append("\t\t\t\t").append("湿度值").append(all.sep);
            str.append(info_shidu).append(all.sep).append(all.sep);
            str.append("------------光照数据------------").append(all.sep);
            str.append(separation).append("时间").append(separation).append(separation).append("\t\t\t\t").append("光照值").append(all.sep);
            str.append(info_gz).append(all.sep).append(all.sep);
            //添加注释
            str.append("注：默认显示自APP启动以来所接收到的数据，全部数据请使用API查询！");
        }
        else
            str.append("暂无数据").append(all.sep);
        return str.toString();
    }

    //发送Toast文本
    public void ShowToastMessage(String str)
    {
        Toast.makeText(Chart_details.this,str,Toast.LENGTH_SHORT).show();
    }
}
