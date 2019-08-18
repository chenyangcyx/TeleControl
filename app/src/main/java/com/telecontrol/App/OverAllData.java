package com.telecontrol.App;

import com.telecontrol.SocketFunction.ReceiveMessageFromWeb;

import android.annotation.SuppressLint;
import android.os.Handler;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class OverAllData
{
    public static OverAllData alldata=new OverAllData();

    /*连接相关*/
    public final String server_ip="47.100.206.8";  //服务器的IP地址
    public final int server_port=6000;             //服务器的端口
    /*连接相关*/

    /*控制命令相关*/
    final String PQS_OFF_to_ON_WEB="control:A0";        //排气扇OFF到ON
    final String PQS_ON_to_OFF_WEB="control:A1";        //排气扇ON到OFF
    final String ZGL_OFF_to_ON_WEB="control:B0";        //遮光帘OFF到ON
    final String ZGL_ON_to_OFF_WEB="control:B1";        //遮光帘ON到OFF
    final String GG_OFF_to_ON_WEB="control:C0";         //灌溉开关OFF到ON
    final String GG_ON_to_OFF_WEB="control:C1";         //灌溉开关ON到OFF
    final int GG_TIME=10*1000;                          //灌溉时间
    final String BGD_OFF_to_ON_WEB="control:D0";        //补光灯从OFF到ON
    final String BGD_ON_to_OFF_WEB="control:D1";        //补光灯从ON到OFF
    /*控制命令相关*/

    /*程序配置*/
    public String setting_ip=server_ip;        //当前设置的ip地址
    public int setting_port=server_port;       //当前设置的端口

    public final int wendu_min_init=0;          //温度监测的初始化最低值
    public final int wendu_max_init=99;        //温度监测的初始化最高值
    public final int shidu_min_init=0;          //湿度监测的初始化最低值
    public final int shidu_max_init=99;        //湿度监测的初始化最高值
    public final int guangzhao_min_init=0;      //光照监测的初始化最低值
    public final int guangzhao_max_init=99999;  //光照监测的初始化最高值
    public final String phone_init="00000000000";          //号码的初始化值
    public final boolean monitor_switch_init=false;        //是否自动监测的初始化值
    public int wendu_min=wendu_min_init;            //温度监测最低值
    public int wendu_max=wendu_max_init;            //温度监测最高值
    public int shidu_min=shidu_min_init;            //湿度监测最低值
    public int shidu_max=shidu_max_init;            //湿度监测最高值
    public int guangzhao_min=guangzhao_min_init;    //光照控制最低值
    public int guangzhao_max=guangzhao_max_init;    //光照控制最高值
    public String phone;             //号码
    public boolean monitor_switch;   //是否自动监测

    private long last_open_gg_time=0;                 //上次开启灌溉按钮的时间
    void RefreshOpenGGTime(){this.last_open_gg_time=System.currentTimeMillis();}     //更新上次开启灌溉按钮的时间
    boolean IfCloudOpenGG(){return System.currentTimeMillis()-this.last_open_gg_time> 60 * 1000;}       //判断是否能够开启灌溉按钮
    int SurplusCloudGGTime(){return (60 * 1000 -(int)(System.currentTimeMillis()-this.last_open_gg_time))/1000;}      //剩余能够开启灌溉所需要的时间
    /*程序配置*/

    /*存储所有数据的结构*/
    public ArrayList<Integer> data_wendu=new ArrayList<>();
    public ArrayList<Integer> data_shidu=new ArrayList<>();
    public ArrayList<Integer> data_gz=new ArrayList<>();
    public ArrayList<String> data_time=new ArrayList<>();
    int current_wendu=0;
    int current_shidu=0;
    int current_gz=0;
    int MAIN_UI_CHART_MAX_SIZE=8;      //主页图表最长长度
    public int CHART_DETAILS_WEB1_SIZE=8;       //详细信息页，web1的x轴长度
    public int CHART_DETAILS_WEB2_SIZE=16;      //详细信息页，web2的x轴长度
    public int API_MESSGAE_SIZE=16;            //调用网页API所获取的数值长度
    public String CHART_MIN_WENDU="10";          //图表中温度最小值
    public String CHART_MAX_WENDU="60";         //图表中温度最大值
    public String CHART_MIN_SHIDU="20";         //图表中湿度最小值
    public String CHART_MAX_SHIDU="100";        //图表中湿度最大值
    public String CHART_MIN_GZ="0";             //图表中光照最小值
    public String CHART_MAX_GZ="150";           //图表中光照最大值
    /*存储所有数据的结构*/

    //获取数据的网址
    public final String info_web_url="http://47.100.206.8:6500/getinfo.php";
    //api的网址
    public final String api_web_url="http://47.100.206.8:6500/api.php";
    /*API的参数*/
    public final String api_user_config="?user=admin&userkey=4a87545601d00981a52b762076f9380d6b48ae13";
    public final String api_operation_getallsettings_string="&operation=getallsettings";
    public final String api_operation_setallsettings_string="&operation=setallsettings";
    /*API的参数*/
    //图表的刷新时间
    public final int ChartRefreshTime=500;
    //从网页接收信息的间隔时间
    public final int WebRefreshTime=5000;
    //接收消息线程的暂停时间
    public final int ReceiveThreadPauseTime=50;
    //主页面的Handler
    public Handler handler;

    //服务器模式，接收线程
    ReceiveMessageFromWeb thread_web=null;

    //获取时间
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat df=new SimpleDateFormat("mm:ss");
    private String GetCurrentTime()
    {
        return df.format(System.currentTimeMillis());
    }
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat df2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String GetFullTime(){return df2.format(System.currentTimeMillis());}

    //解析收到的信息，服务器模式
    void EncodeReceiveMessageFromWeb(String str)
    {
        String te;
        try {
            JSONObject jsonObject = new JSONObject(str);
            this.current_wendu=Integer.parseInt(jsonObject.getString(API_MESSGAE_SIZE+"wendu"));
            this.current_shidu=Integer.parseInt(jsonObject.getString(API_MESSGAE_SIZE+"shidu"));
            this.current_gz=Integer.parseInt(jsonObject.getString(API_MESSGAE_SIZE+"guangzhao"));
            for(int i = 1; i<=this.API_MESSGAE_SIZE; i++)
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
    void EncodeReceiveMessageFromLAN(String str)
    {
        if(!str.contains("temperature:"))
            return;
        int wendu,shidu,guangzhao;
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
    public StringBuilder network_message=new StringBuilder();          //用来存储所有信息的变量
    public final int NETWORK_MESSAGE_REFRESH_INTERVAL=500;             //网络信息页面的刷新时间间隔
    //系统换行符号
    public final String sep=System.getProperty("line.separator");
    //记录网络信息
    public void RecordNetworkMessage(String str){ network_message.append(GetFullTime()).append(sep).append(str).append(sep).append(sep); }
    //自动翻页按钮状态
    public boolean auto_down_switch_state=true;
    /*网络信息*/
}
