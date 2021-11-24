package edu.nuist.ojs.message.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.LinkedList;

import javax.persistence.*;
import java.util.UUID;
import cn.hutool.crypto.SecureUtil;

@Entity
@Table(name = "message")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int type;   //1.PUBLIHSER, 2, journal, 3 user

    //如果是期刊的业务流，取的是期刊的ID和期刊配置的邮箱，如果没有配置，则使用出版社的邮箱账号
    //如果是出版社的业务流，取的是出版社的ID和出版社配置的邮箱
    //sender
    private String host;
    private int port;
    private String password;
    private long sendId;
    private String  senderName;
    private String senderAccount; 

    //receiver
    private long recvId;
    private String recvName;
    private String recvEmail;

    private String configPoint;
    private long timestamp;
    private boolean isEmail;
    private String emailId;
    private String title;
    private boolean isRead;
    @Lob
    @Column (columnDefinition="TEXT")
    private String content;

    //所有邮件附件都从云上走
    @Transient
    private List<EmailFile> appends;

    @Lob
    @Column (columnDefinition="TEXT")
    private String appendsJSONStr;

    public EmailServer getEmailServer(){
//        String emailKey = "<email."+ SecureUtil.md5( ""+ UUID.randomUUID())+"@ojs>";

        if( this.appendsJSONStr != null){
            this.appends = JSON.parseArray(this.appendsJSONStr, EmailFile.class);
        }

        EmailServer es = EmailServer.builder()
            .content(content)
            .emailFiles(appends)
//            .messageId(emailKey)
            .password(password)
            .host(host)
            .port(port)
            .revAddress(recvEmail)
            .revName(recvName)
            .sendAress(senderAccount)
            .subject(title)
            .build();
        return es;
    }
}
