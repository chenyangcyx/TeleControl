package com.example.telecontrol;

import android.os.Handler;
import android.os.Message;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReceiveMessageFromWeb extends Thread
{
    private OverAllData all=OverAllData.alldata;

    private Handler handler;

    ReceiveMessageFromWeb(Handler handler)
    {
        this.handler=handler;
    }

    private String GetMessageFromWeb()
    {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try {
            byte[] data = new byte[1024];
            int len;
            URL url = new URL(all.info_web_url);
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            InputStream inStream = conn.getInputStream();
            while ((len = inStream.read(data)) != -1) {
                outStream.write(data, 0, len);
            }
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(outStream.toByteArray());
    }

    public void run() {
        try {
            //noinspection InfiniteLoopStatement
            while (true)
            {
                sleep(all.ChartRefreshTime);
                Message message = new Message();
                message.what = all.MESSAGE_KIND_WEB;
                message.obj = GetMessageFromWeb();
                handler.sendMessage(message);
                if(all.LINK_MODE==all.LINK_MODE_WEB)
                    all.RecordNetworkMessage("服务器模式，URL："+all.info_web_url+"，收到信息："+message.obj);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}