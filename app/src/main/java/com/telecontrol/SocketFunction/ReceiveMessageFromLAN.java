package com.telecontrol.SocketFunction;

import com.telecontrol.App.OverAllData;

import android.os.Handler;
import android.os.Message;
import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class ReceiveMessageFromLAN extends Thread
{
    private OverAllData all=OverAllData.alldata;

    private Handler handler;

    public ReceiveMessageFromLAN(Handler handler)
    {
        this.handler=handler;
    }

    public void run() {
        try {
            ServerSocket ser=new ServerSocket(all.lan_app_port);
            //noinspection InfiniteLoopStatement
            while (true)
            {
                if(all.LINK_MODE!=all.LINK_MODE_LAN)
                    continue;
                Socket so=ser.accept();
                DataInputStream in = new DataInputStream(so.getInputStream());
                byte[] receive=new byte[128];
                //noinspection ResultOfMethodCallIgnored
                in.read(receive);
                in.close();
                so.close();
                Message message=new Message();
                message.what=all.MESSAGE_KIND_LAN;
                message.obj=new String(receive);
                handler.sendMessage(message);
                all.RecordNetworkMessage("局域网模式，IP："+all.lan_chip_ip+"，Port："+all.lan_chip_port+"，收到信息："+ Arrays.toString(receive));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}