package com.example.hospital;

public class MeetingMsgModel  {

    String senderid;
    String receiverid;
    String messages;
    String messageid;

    public MeetingMsgModel() {
    }

    public MeetingMsgModel(String senderid, String receiverid, String messages, String messageid) {
        this.senderid = senderid;
        this.receiverid = receiverid;
        this.messages = messages;
        this.messageid = messageid;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public String getReceiverid() {
        return receiverid;
    }

    public void setReceiverid(String receiverid) {
        this.receiverid = receiverid;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }
}
