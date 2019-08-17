package com.telecontrol.SocketFunction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UseWebAPI extends Thread
{
    private String url_address;
    private APIResultStruct result;

    public UseWebAPI(String url_address, APIResultStruct result)
    {
        this.url_address=url_address;
        this.result=result;
    }

    public void run()
    {
        StringBuilder str=new StringBuilder();
        try {
            URL url = new URL(url_address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf8"));
            String line;
            while((line=reader.readLine())!=null)
            {
                str.append(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonobj=new JSONObject(str.toString());
            result.setCode(Integer.parseInt(jsonobj.getString("code")));
            result.setDetails(jsonobj.getString("details"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
