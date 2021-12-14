package edu.nuist.ojs.message.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Component
@PropertySource(value = {"classpath:/application-logstatus.properties"} )
@ConfigurationProperties("email.status")
@Data
public class LogStatusMapper {
//    private HashMap<String,Map<String,String>> status=new HashMap<>();
    private Map<String,String > local;
    private Map<String,String > google;
//    public String getLocal(String error){
//        return status.get("local").get(error);
//    }
}
