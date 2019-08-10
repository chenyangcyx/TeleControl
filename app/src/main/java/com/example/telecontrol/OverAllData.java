package com.example.telecontrol;

import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class OverAllData
{
    public static OverAllData alldata=new OverAllData();

    /*连接相关*/
    public final String server_ip="47.100.206.8";    //服务器的IP地址
    public final int server_port=6000;              //服务器的端口
    public final String lan_chip_ip="192.168.4.1";  //单片机的IP地址
    public final int lan_chip_port=333;             //单片机的端口
    public final int lan_app_port=5000;             //局域网下APP要监听的端口
    /*连接相关*/

    /*控制命令相关*/
    public final String PQS_OFF_to_ON_WEB="control:A0";         //排气扇OFF到ON，联网模式
    public final String PQS_OFF_to_ON_LAN="A0";         //排气扇OFF到ON，局域网模式
    public final String PQS_ON_to_OFF_WEB="control:A1";         //排气扇ON到OFF，联网模式
    public final String PQS_ON_to_OFF_LAN="A1";         //排气扇ON到OFF，局域网模式
    public final String ZGL_OFF_to_ON_WEB="control:B0";         //遮光帘OFF到ON，联网模式
    public final String ZGL_OFF_to_ON_LAN="B0";         //遮光帘OFF到ON，局域网模式
    public final int ZGL_DELAY_TIME=5*1000;             //遮光帘等待关闭时间
    public final String ZGL_ON_to_OFF_WEB="control:B1";         //遮光帘ON到OFF，联网模式
    public final String ZGL_ON_to_OFF_LAN="B1";         //遮光帘ON到OFF，局域网模式
    public final String GG_OFF_to_ON_WEB="control:C0";          //灌溉开关OFF到ON，联网模式
    public final String GG_OFF_to_ON_LAN="C0";          //灌溉开关OFF到ON，局域网模式
    public final String GG_ON_to_OFF_WEB="control:C1";          //灌溉开关ON到OFF，联网模式
    public final String GG_ON_to_OFF_LAN="C1";          //灌溉开关ON到OFF，局域网模式
    public final int GG_TIME=10*1000;                   //灌溉时间
    public final String BGD_OFF_to_ON_WEB="control:D0";         //补光灯从OFF到ON，联网模式
    public final String BGD_OFF_to_ON_LAN="D0";         //补光灯从OFF到ON，局域网模式
    public final String BGD_ON_to_OFF_WEB="control:D1";         //补光灯从ON到OFF，联网模式
    public final String BGD_ON_to_OFF_LAN="D1";         //补光灯从ON到OFF，局域网模式
    /*控制命令相关*/

    /*程序配置*/
    public String setting_ip=server_ip;        //当前设置的ip地址
    public int setting_port=server_port;       //当前设置的端口
    public final int LINK_MODE_WEB=0;     //连接模式：联网模式
    public final int LINK_MODE_LAN=1;     //连接模式：局域网模式
    public int LINK_MODE=LINK_MODE_WEB;     //程序当前的连接模式

    public final int wendu_min_init=0;          //温度监测的初始化最低值
    public final int wendu_max_init=99;        //温度监测的初始化最高值
    public final int shidu_min_init=0;          //湿度监测的初始化最低值
    public final int shidu_max_init=99;        //湿度监测的初始化最高值
    public final int guangzhao_min_init=0;      //光照监测的初始化最低值
    public final int guangzhao_max_inti=99999;  //光照监测的初始化最高值
    public int wendu_min=wendu_min_init;            //温度监测最低值
    public int wendu_max=wendu_max_init;            //温度监测最高值
    public int shidu_min=shidu_min_init;            //湿度监测最低值
    public int shidu_max=shidu_max_init;            //湿度监测最高值
    public int guangzhao_min=guangzhao_min_init;    //光照控制最低值
    public int guangzhao_max=guangzhao_max_inti;    //光照控制最高值

    public long last_open_gg_time=0;                 //上次开启灌溉按钮的时间
    public void RefreshOpenGGTime(){this.last_open_gg_time=System.currentTimeMillis();}     //更新上次开启灌溉按钮的时间
    public boolean IfCloudOpenGG(){return System.currentTimeMillis()-this.last_open_gg_time>1*60*1000;}       //判断是否能够开启灌溉按钮
    public int SurplusCloudGGTime(){return (1*60*1000-(int)(System.currentTimeMillis()-this.last_open_gg_time))/1000;}      //剩余能够开启灌溉所需要的时间
    /*程序配置*/

    /*存储所有数据的结构*/
    ArrayList<Integer> data_wendu=new ArrayList<>();
    ArrayList<Integer> data_shidu=new ArrayList<>();
    ArrayList<Integer> data_gz=new ArrayList<>();
    ArrayList<String> data_time=new ArrayList<>();
    public int current_wendu=0;
    public int current_shidu=0;
    public int current_gz=0;
    public int CHART_MAX_SIZE=8;        //图表最长长度
    public String CHART_MIN_WENDU="0";       //图表中温度最小值
    public String CHART_MAX_WENDU="50";      //图表中温度最大值
    public String CHART_MIN_SHIDU="0";       //图表中湿度最小值
    public String CHART_MAX_SHIDU="100";     //图表中湿度最大值
    /*存储所有数据的结构*/

    //获取数据的网址
    public final String info_web_url="http://47.100.206.8:6500/getinfo.php";
    //图表的刷新时间
    public final int ChartRefreshTime=1000;

    //消息类型
    public final int MESSAGE_KIND_WEB=1;        //由服务器模式接收到的message类型
    public final int MESSAGE_KIND_LAN=2;        //由局域网模式接收到的message类型

    //服务器模式，接收线程
    ReceiveMessageFromWeb thread_web=null;
    //局域网模式，接收线程
    ReceiveMessageFromLAN thread_lan=null;

    //获取时间
    SimpleDateFormat df=new SimpleDateFormat("mm:ss");
    public String GetCurrentTime()
    {
        return df.format(System.currentTimeMillis());
    }
    SimpleDateFormat df2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public String GetFullTime(){return df2.format(System.currentTimeMillis());}

    //解析收到的信息，服务器模式
    public void EncodeReceiveMessageFromWeb(String str)
    {
        String te;
        try {
            JSONObject jsonObject = new JSONObject(str);
            this.current_wendu=Integer.parseInt(jsonObject.getString("8wendu"));
            this.current_shidu=Integer.parseInt(jsonObject.getString("8shidu"));
            this.current_gz=Integer.parseInt(jsonObject.getString("8guangzhao"));
            for(int i=1;i<=this.CHART_MAX_SIZE;i++)
            {
                te=jsonObject.getString("" + i +"time");
                if(!this.data_time.contains(te))
                {
                    this.data_time.add(te);
                    this.data_wendu.add(Integer.parseInt(jsonObject.getString("" + i +"wendu")));
                    this.data_shidu.add(Integer.parseInt(jsonObject.getString("" + i +"shidu")));
                    this.data_gz.add(Integer.parseInt(jsonObject.getString("" + i +"guangzhao")));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //解析收到的信息，局域网模式
    public void EncodeReceiveMessageFromLAN(String str)
    {
        if(!str.contains("temperature:"))
            return;
        int wendu=0,shidu=0,guangzhao=0;
        wendu=Integer.parseInt(str.substring(str.indexOf("temperature:")+"temperature:".length(), str.indexOf(",humidity:")));
        shidu=Integer.parseInt(str.substring(str.indexOf("humidity:")+"humidity:".length(), str.indexOf(",illumination:")));
        guangzhao=Integer.parseInt(str.substring(str.indexOf("illumination:")+"illumination:".length(), str.indexOf("}")));
        this.current_wendu=wendu;
        this.current_shidu=shidu;
        this.current_gz=guangzhao;
        this.data_time.add(GetCurrentTime());
        this.data_wendu.add(wendu);
        this.data_shidu.add(shidu);
        this.data_gz.add(guangzhao);
    }

    /*网络信息*/
    StringBuilder network_message=new StringBuilder();      //用来存储所有信息的变量
    /*网络信息*/
}
