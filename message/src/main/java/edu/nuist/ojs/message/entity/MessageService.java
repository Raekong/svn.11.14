package edu.nuist.ojs.message.entity;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageReps messageReps;

    public Message save(Message message){
        return messageReps.save(message);
    }

    public List<Message> findByRecvId(long recvId){
        return messageReps.findByRecvId(recvId);
    }

    public Message findById(long id){
        return messageReps.findById(id);
    }

    public Message findByRecvIdAndConfigPoint(long id, String configPoint){
        return messageReps.findByRecvIdAndConfigPoint(id, configPoint);
    }
    public Page<Message> findAll(Example<Message> example, PageRequest pageRequest){
      return messageReps.findAll(example,pageRequest);
    }
    public void read(long id){
       messageReps.read(id);
    }

    public void deleteTop(long recvId,int length){
        messageReps.deleteTop(recvId,length);
    }
    public int countByRecvId(long recvId){
        return messageReps.countByRecvId(recvId);
    }
}
