package com.example.gregsimoncustomersupport;

import java.util.HashMap;
import java.util.Set;

public class Ticket {
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

    public HashMap<String, String> getAttachments() {
        return attachments;
    }

    public void setAttachments(HashMap<String, String> attachments) {
        this.attachments = attachments;
    }

    String customerName;
    String Subject;
    String bodyOfTicket;
    HashMap<String,String> attachments = new HashMap<>();

    public Ticket(String customerName, String Subject, String bodyOfTicket, HashMap<String,String> attachments){
        this.customerName = customerName;
        this.Subject = Subject;
        this.bodyOfTicket = bodyOfTicket;
        this.attachments = attachments;
    }
    public void Tickets(){

    }
    public void addAttactment(String attachmentTitle, String attachmentBody){
        //adds attachment
        this.attachments.put(attachmentTitle, attachmentBody);
    }
    public int getNumberOfAttachments(){
        //Get the Number of attachments
        return this.attachments.size();//get Individual attachment
    }
    public String getIndividualAttachment(int indexValue){
        //get Individual attachment
        // get the key set
        Set<String> keySet = this.attachments.keySet();
        Integer[] keyArray = ((Set<?>) keySet).toArray(new Integer[keySet.size()]);
        // taking input of index
        Integer index = indexValue;
        Integer key = keyArray[index - 1];
        return this.attachments.get(indexValue);
    }
    public HashMap<String, String> getAllAttachments(){
        //get All attachment
       return this.attachments;
    }



}
