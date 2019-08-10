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
    OverAllData all=OverAllData.alldata;

    Handler main_handler,network_handler;

    public ReceiveMessageFromWeb(Handler main_handler,Handler network_handler)
    {
        this.main_handler=main_handler;
        this.network_handler=network_handler;
    }

    public String GetMessageFromWeb()
    {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try {
            byte[] data = new byte[1024];
            int len = 0;
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
            while (true)
            {
                sleep(all.ChartRefreshTime);
                Message message = new Message();
                message.what = all.MESSAGE_KIND_WEB;
                message.obj = GetMessageFromWeb();
                main_handler.sendMessage(message);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}