package edu.nuist.ojs.message.controller;

import com.alibaba.fastjson.JSON;
import edu.nuist.ojs.message.util.LogStatusMapper;
import lombok.Synchronized;
import org.hazlewood.connor.bottema.emailaddress.EmailAddressCriteria;
import org.hazlewood.connor.bottema.emailaddress.EmailAddressValidator;
import org.simplejavamail.MailException;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.AsyncResponse;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.mailer.MailerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import edu.nuist.ojs.message.entity.EmailServer;
import edu.nuist.ojs.message.entity.LocalLog;
import edu.nuist.ojs.message.entity.LocalLogService;

import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.*;

@RestController
@EnableScheduling
public class SendEmailApi {
    final Logger logger = LoggerFactory.getLogger(getClass());
    List<EmailServer> emailServers = new ArrayList<>();
    @Autowired
    LogStatusMapper logStatusMapper;

    @Autowired
     LocalLogService lService;

    @Autowired
    RedisTemplate redisTemplate;

    @Scheduled(fixedRate = 5000)
    @Synchronized
    public void send() {

        synchronized(emailServers){
            if( emailServers.size()>0 ) return; //上次定时任务未完成直接返回
        }

        Set<String> keys= redisTemplate.keys("<email*");    //读所有未发的信息
        logger.info("Now send email num:............" + keys.size());
        if( keys.size() == 0 ) return;//本次没有要发送的邮件
        for(String key:keys){   //取信待发
            EmailServer emailServer= JSON.parseObject(redisTemplate.opsForValue().get(key).toString(), EmailServer.class);
            redisTemplate.delete(key);
            emailServers.add(emailServer);
        }

        for (EmailServer emailServer:emailServers){
            logger.info("Now send email:............" + emailServer.getMessageId() + "===" + emailServer.getRevAddress());
            //校验信
            if (EmailAddressValidator.isValid(emailServer.getRevAddress(),
                    EmailAddressCriteria.RFC_COMPLIANT)
                    &&EmailAddressValidator.isValid(emailServer.getRevAddress(),
                    EnumSet.of(EmailAddressCriteria.ALLOW_QUOTED_IDENTIFIERS, EmailAddressCriteria.ALLOW_PARENS_IN_LOCALPART))) {
                try {
                    Mailer inhouseMailer = MailerBuilder
                            .withSMTPServer(emailServer.getHost(), emailServer.getPort(), emailServer.getSendAress(), emailServer.getPassword())
                            .async()
                            .withConnectionPoolCoreSize(5) // keep 10 connections up at all times, automatically refreshed after expiry policy closes it (default 0)
                            .withConnectionPoolMaxSize(15) // scale up to max 500 connections until expiry policy kicks in and cleans up (default 4)
                            .withConnectionPoolClaimTimeoutMillis((int) TimeUnit.MINUTES.toMillis(1)) // wait max 1 minute for available connection (default forever)
                            .withConnectionPoolExpireAfterMillis((int)TimeUnit.MINUTES.toMillis(30)) // keep connections spinning for half an hour (default 5 seconds)
                            .withEmailAddressCriteria(EmailAddressCriteria.RFC_COMPLIANT)
                            .withTransportStrategy(TransportStrategy.SMTPS)
                            .withDebugLogging(true)
                            .buildMailer();
                    Email email=emailServer.loadEmail();
                    AsyncResponse asyncResponse = inhouseMailer.sendMail(email, true);

                    asyncResponse.onSuccess(() -> logger.warn("to"+ emailServer.getRevAddress() +"sending"));
                    asyncResponse.onException((e) ->logger.error("to"+ emailServer.getRevAddress()+"send failed"));

                    Future f = asyncResponse.getFuture(); 
                    while(!f.isDone()) Thread.sleep(100);
                    f.get();
                    lService.save(
                        LocalLog.builder()
                            .messageId(emailServer.getMessageId())
                            .state(logStatusMapper.getLocal().get("success"))
                            .build()
                        );

                } catch( MailException m) { //与邮件服务交互错
                    lService.save(
                        LocalLog.builder()
                            .messageId(emailServer.getMessageId())
                            .state(logStatusMapper.getLocal().get("commerror"))
                            .build()
                        );

                } catch( MalformedURLException e) { //邮件协议与格式错
                    lService.save(
                        LocalLog.builder()
                            .messageId(emailServer.getMessageId())
                            .state(logStatusMapper.getLocal().get("urlerror"))
                            .build()
                        );
                } catch( ExecutionException  | InterruptedException i) { //发送线程错
                    lService.save(
                        LocalLog.builder()
                            .messageId(emailServer.getMessageId())
                            .state(logStatusMapper.getLocal().get("threaderror"))
                            .build()
                        );
                }
                catch(Exception e){    //未知错
                    lService.save(
                        LocalLog.builder()
                            .messageId(emailServer.getMessageId())
                            .state(logStatusMapper.getLocal().get("unknown"))
                            .build()
                        );
                }
            }else{//邮件地址校验不通过
                lService.save(
                    LocalLog.builder()
                        .messageId(emailServer.getMessageId())
                        .state(logStatusMapper.getLocal().get("addresserror"))
                        .build()
                    );
            }
        }
        emailServers.clear();

    }
}
