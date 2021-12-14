package edu.nuist.ojs.message.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "log")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String messageId;
    private String startDate;
    private String endDate;
    private String sender;
    private String messageSize;
    private String targetIPAddress;
    private String smtpReplyCode;
    private String subject;
    private String target;
    private String status;
    private String recipientAddress;
    
    @Transient
    private List<Event> events;


}
