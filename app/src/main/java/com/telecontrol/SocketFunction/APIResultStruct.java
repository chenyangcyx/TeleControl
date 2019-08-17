package com.telecontrol.SocketFunction;

public class APIResultStruct
{
    private int code;
    private String details;

    public APIResultStruct()
    {
        this.code=-1;
        this.details=null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
