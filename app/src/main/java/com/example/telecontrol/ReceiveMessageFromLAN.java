package com.example.telecontrol;

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

    ReceiveMessageFromLAN(Handler handler)
    {
        this.handler=handler;
    }

    public void run() {
        try {
            ServerSocket server = new ServerSocket(all.lan_app_port);
            //noinspection InfiniteLoopStatement
            while (true)
            {
                Socket so= server.accept();
                DataInputStream in = new DataInputStream(so.getInputStream());
                byte[] receice=new byte[128];
                //noinspection ResultOfMethodCallIgnored
                in.read(receice);
                in.close();

                Message message=new Message();
                message.what=all.MESSAGE_KIND_LAN;
                message.obj=new String(receice);
                handler.sendMessage(message);

                if(all.LINK_MODE==all.LINK_MODE_LAN)
                    all.RecordNetworkMessage("局域网模式，IP："+all.lan_chip_ip+"，Port："+all.lan_chip_port+"，收到信息："+ Arrays.toString(receice));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}