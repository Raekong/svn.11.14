package edu.nuist.ojs.message.controller;

import com.alibaba.fastjson.JSON;
import edu.nuist.ojs.message.entity.EmailServer;
import edu.nuist.ojs.message.entity.Message;
import edu.nuist.ojs.message.entity.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import edu.nuist.ojs.message.redis.*;

@RestController
public class MessageController {
    @Autowired
    MessageService messageService;

    @Autowired
    RedisRouter redisRouter;

    
    @RequestMapping("/message/recv")
    public List<Message> getRecvMessage(@RequestAttribute long recvId){
        List<Message> messages = messageService.findByRecvId(recvId);
        if (messages.size()>0){
            return messages;
        }
        return null;
    }

    @RequestMapping("/message/getMessage")
    public Message getMessage(@RequestAttribute long id){
        return messageService.findById(id);
    }

    @RequestMapping("/message/getMessageByRevIdAndConfigPoint")
    public Message getMessageByRevIdAndConfigPoint(@RequestAttribute long id, @RequestAttribute String configPoint){
        return messageService.findByRecvIdAndConfigPoint(id, configPoint);
    }


//    @RequestMapping("/message/save")
//    public Message saveMessage(@RequestAttribute Message message){
//        return messageService.save(message);
//    }

    @RequestMapping("/message/send")
    public Message sendMessage(@RequestAttribute Message message){
        Message msg = messageService.save(message);
        if( msg.isEmail() ){
            EmailServer es = msg.getEmailServer();
            redisRouter.saveEmail(es);
            msg.setEmailId(es.getMessageId());
        }
        
        return msg;
    }
    @RequestMapping("/message/search")
    public String searchMessage(@RequestAttribute int  page,
                                       @RequestAttribute int size,
                                       @RequestAttribute long id,
                                       @RequestAttribute(required = false) String sender,
                                       @RequestAttribute(required = false)String title,
                                       @RequestAttribute(required = false)String content){
     Message message=new Message();
     message.setSenderAccount(sender);
     message.setTitle(title);
     message.setContent(content);
     message.setRecvId(id);
        ExampleMatcher matcher=ExampleMatcher.matching()
                .withMatcher("senderAccount", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("title",ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("content",ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("recvId",ExampleMatcher.GenericPropertyMatchers.exact())
                .withIgnorePaths("id")
                .withIgnorePaths("type")
                .withIgnorePaths("port")
                .withIgnorePaths("sendId")
                .withIgnorePaths("timestamp")
                .withIgnorePaths("isEmail");
        Example<Message> example=Example.of(message, matcher);
        PageRequest pageRequest=PageRequest.of(page-1,size);
        Page<Message> messages=messageService.findAll(example,pageRequest);
        return JSON.toJSONString(messages);
    }

    @RequestMapping("/message/read")
    public void read(@RequestAttribute long  id){
        messageService.read(id);
    }

}
