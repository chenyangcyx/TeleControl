package com.example.telecontrol;

import android.os.Message;

public class ControlMethod
{
    OverAllData all=OverAllData.alldata;

    //排气扇控制
    public void PQS_Control(String mess)
    {
        //连接模式为联网模式
        if(all.LINK_MODE==all.LINK_MODE_WEB)
        {
            if(mess.equals("ON")==true)     //开指令
            {
                new SendMessage(all.PQS_OFF_to_ON_WEB).start();
            }
            else if (mess.equals("OFF")==true)
            {
                new SendMessage(all.PQS_ON_to_OFF_WEB).start();
            }
            else
            {
                return;
            }
        }
        //连接模式为局域网模式
        else
        {
            if(mess.equals("ON")==true)     //开指令
            {
                new SendMessage(all.PQS_OFF_to_ON_LAN).start();
            }
            else if (mess.equals("OFF")==true)
            {
                new SendMessage(all.PQS_ON_to_OFF_LAN).start();
            }
            else
            {
                return;
            }
        }
    }

    //遮光帘控制
    public void ZGL_Control(String mess)
    {
        //连接模式为联网模式
        if(all.LINK_MODE==all.LINK_MODE_WEB)
        {
            if(mess.equals("ON")==true)     //开指令
            {
                new SendMessage(all.ZGL_OFF_to_ON_WEB).start();
            }
            else if (mess.equals("OFF")==true)
            {
                new SendMessage(all.ZGL_ON_to_OFF_WEB).start();
            }
            else
            {
                return;
            }
        }
        //连接模式为局域网模式
        else
        {
            if(mess.equals("ON")==true)     //开指令
            {
                new SendMessage(all.ZGL_OFF_to_ON_LAN).start();
            }
            else if (mess.equals("OFF")==true)
            {
                new SendMessage(all.ZGL_ON_to_OFF_LAN).start();
            }
            else
            {
                return;
            }
        }
    }

    //灌溉控制
    public void GG_Control(String mess)
    {
        //连接模式为联网模式
        if(all.LINK_MODE==all.LINK_MODE_WEB)
        {
            if(mess.equals("ON")==true)     //开指令
            {
                new SendMessage(all.GG_OFF_to_ON_WEB).start();
            }
            else if (mess.equals("OFF")==true)
            {
                new SendMessage(all.GG_ON_to_OFF_WEB).start();
            }
            else
            {
                return;
            }
        }
        //连接模式为局域网模式
        else
        {
            if(mess.equals("ON")==true)     //开指令
            {
                new SendMessage(all.GG_OFF_to_ON_LAN).start();
            }
            else if (mess.equals("OFF")==true)
            {
                new SendMessage(all.GG_ON_to_OFF_LAN).start();
            }
            else
            {
                return;
            }
        }
    }

    //补光灯控制
    public void BGD_Control(String mess)
    {
        //连接模式为联网模式
        if(all.LINK_MODE==all.LINK_MODE_WEB)
        {
            if(mess.equals("ON")==true)     //开指令
            {
                new SendMessage(all.BGD_OFF_to_ON_WEB).start();
            }
            else if (mess.equals("OFF")==true)
            {
                new SendMessage(all.BGD_ON_to_OFF_WEB).start();
            }
            else
            {
                return;
            }
        }
        //连接模式为局域网模式
        else
        {
            if(mess.equals("ON")==true)     //开指令
            {
                new SendMessage(all.BGD_OFF_to_ON_LAN).start();
            }
            else if (mess.equals("OFF")==true)
            {
                new SendMessage(all.BGD_ON_to_OFF_LAN).start();
            }
            else
            {
                return;
            }
        }
    }
}
