package com.telecontrol.SocketFunction;

import com.telecontrol.App.OverAllData;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReceiveMessageFromWeb extends Thread
{
    private OverAllData all=OverAllData.alldata;

    private Handler handler;

    public ReceiveMessageFromWeb(Handler handler)
    {
        this.handler=handler;
    }

    public String GetInfoFromWeb()
    {
        StringBuilder str=new StringBuilder();
        try {
            URL url = new URL(all.info_web_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line;
            while((line=reader.readLine())!=null)
            {
                str.append(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    public void run() {
        try {
            //noinspection InfiniteLoopStatement
            while (true)
            {
                sleep(all.ReceiveThreadPauseTime);
                Message message = new Message();
                message.what = 1;
                message.obj = GetInfoFromWeb();
                handler.sendMessage(message);
                all.RecordNetworkMessage("服务器模式，URL："+all.info_web_url+"，收到信息："+message.obj);
                sleep(all.WebRefreshTime);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}