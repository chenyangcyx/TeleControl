package com.example.telecontrol;

import android.os.Handler;
import android.os.Message;
import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ReceiveMessageFromLAN extends Thread
{
    OverAllData all=OverAllData.alldata;

    private ServerSocket server;
    private DataInputStream in;
    Handler handler;

    public ReceiveMessageFromLAN(Handler han)
    {
        this.handler=han;
    }

    public void run() {
        try {
            server = new ServerSocket(all.lan_app_port);
            while (true)
            {
                Socket so=server.accept();
                in=new DataInputStream(so.getInputStream());
                byte[] receice=new byte[128];
                in.read(receice);
                in.close();

                Message message=new Message();
                message.what=all.MESSAGE_KIND_LAN;
                message.obj=new String(receice);
                handler.sendMessage(message);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}