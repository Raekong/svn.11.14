package edu.nuist.ojs.message.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

import com.sun.istack.ByteArrayDataSource;
import org.simplejavamail.api.email.AttachmentResource;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.email.EmailBuilder;

import javax.activation.FileDataSource;
import javax.persistence.Transient;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class EmailServer implements Serializable {

    private String host;
    private int port;
    private String revName;
    private String revAddress;
    
    private String sendAress;
    private String senderName;
    private String password;
    
    private String subject;
    private String content;
    private String messageId;
    
    @Transient
    private List<EmailFile> emailFiles;

    


    public Email loadEmail() throws MalformedURLException {
        if(emailFiles==null){
            Email email = EmailBuilder.startingBlank()
                    .from(senderName, sendAress)
                    .to(revName, revAddress)
                    .withSubject(subject)
                    .withPlainText(content)
                    .fixingMessageId(messageId)
                    .buildEmail();
          return  email;
        }
        else {
            List<AttachmentResource> attachmentResources=new ArrayList<>();
            for(int i=0;i<emailFiles.size();i++){

                if(emailFiles.get(i).isIslink()){
                    URL ur = new URL(emailFiles.get(i).getPath());
                    BufferedInputStream in = null;
                    ByteArrayOutputStream out = null;
                    try {
                        in = new BufferedInputStream(ur.openStream());
                        out = new ByteArrayOutputStream(1024);
                        byte[] temp = new byte[1024];
                        int size = 0;
                        while ((size = in.read(temp)) != -1) {
                            out.write(temp, 0, size);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    byte[] data = out.toByteArray();
                   AttachmentResource attachmentResource=new AttachmentResource(emailFiles.get(i).getFileName(), new ByteArrayDataSource(data,"text/plain") );
                   attachmentResources.add(attachmentResource);
                }
                else{
                    AttachmentResource attachmentResource=new AttachmentResource (emailFiles.get(i).getFileName(),new FileDataSource(emailFiles.get(i).getPath()+"/"+emailFiles.get(i).getFileName()+"."+emailFiles.get(i).getFileType()));
                    attachmentResources.add(attachmentResource);
                }
            }
            Email email = EmailBuilder.startingBlank()
                    .from(senderName, sendAress)
                    .to(revName, revAddress)
                    .withSubject(subject)
                    .withPlainText(content)
                    .fixingMessageId(messageId)
                    .withAttachments(attachmentResources)
                    .buildEmail();
            return  email;

        }
    }

}
