package com.telecontrol.App;

import com.telecontrol.SocketFunction.SendMessage;

class ControlMethod
{
    private OverAllData all=OverAllData.alldata;

    //排气扇控制
    void PQS_Control(String mess)
    {
        if(mess.equals("ON"))     //开指令
            new SendMessage(all.PQS_OFF_to_ON_WEB).start();
        else if (mess.equals("OFF"))
            new SendMessage(all.PQS_ON_to_OFF_WEB).start();
    }

    //遮光帘控制
    void ZGL_Control(String mess)
    {
        if(mess.equals("ON"))     //开指令
            new SendMessage(all.ZGL_OFF_to_ON_WEB).start();
        else if (mess.equals("OFF"))
            new SendMessage(all.ZGL_ON_to_OFF_WEB).start();
    }

    //灌溉控制
    void GG_Control(String mess)
    {
        if(mess.equals("ON"))     //开指令
            new SendMessage(all.GG_OFF_to_ON_WEB).start();
        else if (mess.equals("OFF"))
            new SendMessage(all.GG_ON_to_OFF_WEB).start();
    }

    //补光灯控制
    void BGD_Control(String mess)
    {
        if(mess.equals("ON"))     //开指令
            new SendMessage(all.BGD_OFF_to_ON_WEB).start();
        else if (mess.equals("OFF"))
            new SendMessage(all.BGD_ON_to_OFF_WEB).start();
    }
}
