package com.telecontrol.App;

import com.telecontrol.SocketFunction.SendMessage;

class ControlMethod
{
    private OverAllData all=OverAllData.alldata;

    //排气扇控制
    void PQS_Control(String mess)
    {
        //连接模式为服务器模式
        if(all.LINK_MODE_WEB == all.LINK_MODE)
        {
            if(mess.equals("ON"))     //开指令
            {
                new SendMessage(all.PQS_OFF_to_ON_WEB).start();
            }
            else if (mess.equals("OFF"))
            {
                new SendMessage(all.PQS_ON_to_OFF_WEB).start();
            }
        }
        //连接模式为局域网模式
        else
        {
            if(mess.equals("ON"))     //开指令
            {
                new SendMessage(all.PQS_OFF_to_ON_LAN).start();
            }
            else if (mess.equals("OFF"))
            {
                new SendMessage(all.PQS_ON_to_OFF_LAN).start();
            }
        }
    }

    //遮光帘控制
    void ZGL_Control(String mess)
    {
        //连接模式为服务器模式
        if(all.LINK_MODE == all.LINK_MODE_WEB)
        {
            if(mess.equals("ON"))     //开指令
            {
                new SendMessage(all.ZGL_OFF_to_ON_WEB).start();
            }
            else if (mess.equals("OFF"))
            {
                new SendMessage(all.ZGL_ON_to_OFF_WEB).start();
            }
        }
        //连接模式为局域网模式
        else
        {
            if(mess.equals("ON"))     //开指令
            {
                new SendMessage(all.ZGL_OFF_to_ON_LAN).start();
            }
            else if (mess.equals("OFF"))
            {
                new SendMessage(all.ZGL_ON_to_OFF_LAN).start();
            }
        }
    }

    //灌溉控制
    void GG_Control(String mess)
    {
        //连接模式为服务器模式
        if(all.LINK_MODE == all.LINK_MODE_WEB)
        {
            if(mess.equals("ON"))     //开指令
            {
                new SendMessage(all.GG_OFF_to_ON_WEB).start();
            }
            else if (mess.equals("OFF"))
            {
                new SendMessage(all.GG_ON_to_OFF_WEB).start();
            }
        }
        //连接模式为局域网模式
        else
        {
            if(mess.equals("ON"))     //开指令
            {
                new SendMessage(all.GG_OFF_to_ON_LAN).start();
            }
            else if (mess.equals("OFF"))
            {
                new SendMessage(all.GG_ON_to_OFF_LAN).start();
            }
        }
    }

    //补光灯控制
    void BGD_Control(String mess)
    {
        //连接模式为服务器模式
        if(all.LINK_MODE == all.LINK_MODE_WEB)
        {
            if(mess.equals("ON"))     //开指令
            {
                new SendMessage(all.BGD_OFF_to_ON_WEB).start();
            }
            else if (mess.equals("OFF"))
            {
                new SendMessage(all.BGD_ON_to_OFF_WEB).start();
            }
        }
        //连接模式为局域网模式
        else
        {
            if(mess.equals("ON"))     //开指令
            {
                new SendMessage(all.BGD_OFF_to_ON_LAN).start();
            }
            else if (mess.equals("OFF"))
            {
                new SendMessage(all.BGD_ON_to_OFF_LAN).start();
            }
        }
    }
}
