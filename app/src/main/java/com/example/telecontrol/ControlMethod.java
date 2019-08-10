package com.example.telecontrol;

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
                all.RecordNetworkMessage("服务器模式，发送指令："+all.PQS_OFF_to_ON_WEB);
            }
            else if (mess.equals("OFF"))
            {
                new SendMessage(all.PQS_ON_to_OFF_WEB).start();
                all.RecordNetworkMessage("服务器模式，发送指令："+all.PQS_ON_to_OFF_WEB);
            }
        }
        //连接模式为局域网模式
        else
        {
            if(mess.equals("ON"))     //开指令
            {
                new SendMessage(all.PQS_OFF_to_ON_LAN).start();
                all.RecordNetworkMessage("局域网模式，发送指令："+all.PQS_OFF_to_ON_LAN);
            }
            else if (mess.equals("OFF"))
            {
                new SendMessage(all.PQS_ON_to_OFF_LAN).start();
                all.RecordNetworkMessage("局域网模式，发送指令："+all.PQS_ON_to_OFF_LAN);
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
                all.RecordNetworkMessage("服务器模式，发送指令："+all.ZGL_OFF_to_ON_WEB);
            }
            else if (mess.equals("OFF"))
            {
                new SendMessage(all.ZGL_ON_to_OFF_WEB).start();
                all.RecordNetworkMessage("服务器模式，发送指令："+all.ZGL_ON_to_OFF_WEB);
            }
        }
        //连接模式为局域网模式
        else
        {
            if(mess.equals("ON"))     //开指令
            {
                new SendMessage(all.ZGL_OFF_to_ON_LAN).start();
                all.RecordNetworkMessage("局域网模式，发送指令："+all.ZGL_OFF_to_ON_LAN);
            }
            else if (mess.equals("OFF"))
            {
                new SendMessage(all.ZGL_ON_to_OFF_LAN).start();
                all.RecordNetworkMessage("局域网模式，发送指令："+all.ZGL_ON_to_OFF_LAN);
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
                all.RecordNetworkMessage("服务器模式，发送指令："+all.GG_OFF_to_ON_WEB);
            }
            else if (mess.equals("OFF"))
            {
                new SendMessage(all.GG_ON_to_OFF_WEB).start();
                all.RecordNetworkMessage("服务器模式，发送指令："+all.GG_ON_to_OFF_WEB);
            }
        }
        //连接模式为局域网模式
        else
        {
            if(mess.equals("ON"))     //开指令
            {
                new SendMessage(all.GG_OFF_to_ON_LAN).start();
                all.RecordNetworkMessage("局域网模式，发送指令："+all.GG_OFF_to_ON_LAN);
            }
            else if (mess.equals("OFF"))
            {
                new SendMessage(all.GG_ON_to_OFF_LAN).start();
                all.RecordNetworkMessage("局域网模式，发送指令："+all.GG_ON_to_OFF_LAN);
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
                all.RecordNetworkMessage("服务器模式，发送指令："+all.BGD_OFF_to_ON_WEB);
            }
            else if (mess.equals("OFF"))
            {
                new SendMessage(all.BGD_ON_to_OFF_WEB).start();
                all.RecordNetworkMessage("服务器模式，发送指令："+all.BGD_ON_to_OFF_WEB);
            }
        }
        //连接模式为局域网模式
        else
        {
            if(mess.equals("ON"))     //开指令
            {
                new SendMessage(all.BGD_OFF_to_ON_LAN).start();
                all.RecordNetworkMessage("局域网模式，发送指令："+all.BGD_OFF_to_ON_LAN);
            }
            else if (mess.equals("OFF"))
            {
                new SendMessage(all.BGD_ON_to_OFF_LAN).start();
                all.RecordNetworkMessage("局域网模式，发送指令："+all.BGD_ON_to_OFF_LAN);
            }
        }
    }
}
