package com.example.gregsimoncustomersupport;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class Ticket {
    public Ticket() {

    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getBodyOfTicket() {
        return bodyOfTicket;
    }

    public void setBodyOfTicket(String bodyOfTicket) {
        this.bodyOfTicket = bodyOfTicket;
    }

    public HashMap<String, Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(int ticket_ID, HashMap<String, Attachment> attachments) {
        this.attachments = attachments;
    }

    String customerName;
    String Subject;
    String bodyOfTicket;
    HashMap<String, Attachment> attachments = new HashMap<String, Attachment>();

    public Ticket(String customerName, String Subject, String bodyOfTicket, HashMap<String, Attachment> attachments){
        this.customerName = customerName;
        this.Subject = Subject;
        this.bodyOfTicket = bodyOfTicket;
        this.attachments = attachments;
    }
    public void addAttachment(Attachment attachment)
    {
        this.attachments.put(attachment.getName(), attachment);
    }
    public int getNumberOfAttachments(){
        //Get the Number of attachments
        return this.attachments.size();//get Individual attachment
    }
    public Attachment getIndividualAttachment(String indexValue){
        return this.attachments.get(indexValue);
    }
    public Collection<Attachment> getAllAttachments(){
        //get All attachment
        return this.attachments.values();
    }
    public boolean hasAttachment(){
        return attachments != null;
    }



}
