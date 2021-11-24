package edu.nuist.ojs.message.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class ServerRouter {
    @RequestMapping("/messageServerRouter")
    public void serverRouter(@RequestBody String data, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject jsonObject = JSONObject.parseObject(data);
        String api = jsonObject.get("api").toString();

        System.out.println( api + "----------------===========" );
        JSONObject parameter = jsonObject.getJSONObject("data");
        ServerParameterHelper.exec(parameter, api, request);
        request.getRequestDispatcher(api).forward(request, response);
    }

}
