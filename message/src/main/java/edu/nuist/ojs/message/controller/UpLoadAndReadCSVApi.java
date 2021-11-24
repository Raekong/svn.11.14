package edu.nuist.ojs.message.controller;

import com.opencsv.CSVReader;
import edu.nuist.ojs.message.entity.Log;
import edu.nuist.ojs.message.entity.LogService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@RestController
public class UpLoadAndReadCSVApi {
    
    @Autowired
    LogService logService;
    private HashMap<String ,  Log> logs = new  HashMap<>();

    @RequestMapping("/upload")
    public void uploadExcel(@RequestAttribute String  url ){
        try {
            URL newUrl =new URL(url);
            HttpURLConnection conn = (HttpURLConnection) newUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(20 *1000);
            CSVReader reader = new CSVReader( new InputStreamReader(conn.getInputStream(), "UTF-8") );
            String[] strs;
            while ((strs = reader.readNext()) != null) {
                Log log=Log.builder()
                        .messageId(strs[0])
                        .startDate(strs[1])
                        .endDate(strs[2])
                        .sender(strs[3])
                        .messageSize(strs[4])
                        .subject(strs[5])
                        .target(strs[9])
                        .targetIPAddress(strs[12])
                        .smtpReplyCode(strs[14])
                        .recipientAddress(strs[8])
                        .status(strs[11])
                        .build();
                if(logs.get(log.getMessageId())!=null){
                    if(!logs.get(log.getMessageId()).getStatus().equals("DELIVERED")){
                        logs.put(log.getMessageId(), log);
                    }
                }
                else logs.put(log.getMessageId(),log);

            }
            reader.close();
        }catch(Exception e){

        }
         for (String key:logs.keySet()){
            logService.save(logs.get(key));
         }

    }
}


