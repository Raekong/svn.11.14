package edu.nuist.ojs.message.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String logId;
    private String eventTarget;
    private String eventDate;
    private String eventStatus;
    private String eventTargetIPAddress;
    private String eventSMTPReplyCode;
    @Column(columnDefinition="TEXT")
    private String eventDescription;

}
