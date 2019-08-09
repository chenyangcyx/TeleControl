package com.example.telecontrol;

import android.os.AsyncTask;
import java.io.*;
import java.net.Socket;

public class SendMessage extends Thread
{
    OverAllData all=OverAllData.alldata;

    Socket so;
    PrintStream wri;
    String send_message=null;

    SendMessage(String str)
    {
        this.send_message=str;
    }

    public void run()
    {
        try
        {
            this.so=new Socket(all.setting_ip,all.setting_port);
            wri = new PrintStream(new PrintStream(this.so.getOutputStream()));
            wri.println(send_message);
            wri.flush();
            wri.close();
            this.so.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
