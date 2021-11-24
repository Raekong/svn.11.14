package edu.nuist.ojs.message.util;

import com.alibaba.fastjson.JSONObject;
import edu.nuist.ojs.message.entity.Message;


import javax.servlet.http.HttpServletRequest;

public class ServerParameterHelper {
    public static HttpServletRequest exec(JSONObject parameter, String api, HttpServletRequest request){
        switch (api){
            case "/message/send":{
                Message message=parameter.getObject("message", Message.class);
                request.setAttribute("message",message);
                break;
            }
            default:{
                for (String key : parameter.keySet()) {
                    request.setAttribute(key, parameter.get(key));
                }
            }
        }
        
        return request;
    }
}