package com.telecontrol.SocketFunction;

import com.telecontrol.App.OverAllData;

import java.io.PrintStream;
import java.net.Socket;

public class SendMessage extends Thread
{
    private OverAllData all=OverAllData.alldata;

    private String send_message;

    public SendMessage(String str)
    {
        send_message = null;
        this.send_message=str;
    }

    public void run()
    {
        try
        {
            Socket so = new Socket(all.setting_ip, all.setting_port);
            PrintStream wri = new PrintStream(new PrintStream(so.getOutputStream()));
            wri.println(send_message);
            wri.flush();
            wri.close();
            so.close();
            all.RecordNetworkMessage("服务器模式，IP："+all.server_ip+"，Port："+all.server_port+"，发送信息："+send_message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
