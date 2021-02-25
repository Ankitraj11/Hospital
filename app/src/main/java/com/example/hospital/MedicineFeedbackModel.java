package com.example.hospital;

public class MedicineFeedbackModel {

    String feedback;
    String feedbacksenderid;
    String feedbackreceiverid;
    String feedbacksign;
    String feedbackid;

    public MedicineFeedbackModel() {
    }

    public MedicineFeedbackModel(String feedback,String feedbacksign,String feedbacksenderid, String feedbackreceiverid, String feedbackid) {
        this.feedback = feedback;
        this.feedbacksenderid = feedbacksenderid;
        this.feedbackreceiverid = feedbackreceiverid;
        this.feedbacksign = feedbacksign;
        this.feedbackid = feedbackid;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getFeedbacksenderid() {
        return feedbacksenderid;
    }

    public void setFeedbacksenderid(String feedbacksenderid) {
        this.feedbacksenderid = feedbacksenderid;
    }

    public String getFeedbackreceiverid() {
        return feedbackreceiverid;
    }

    public void setFeedbackreceiverid(String feedbackreceiverid) {
        this.feedbackreceiverid = feedbackreceiverid;
    }

    public String getFeedbacksign() {
        return feedbacksign;
    }

    public void setFeedbacksign(String feedbacksign) {
        this.feedbacksign = feedbacksign;
    }

    public String getFeedbackid() {
        return feedbackid;
    }

    public void setFeedbackid(String feedbackid) {
        this.feedbackid = feedbackid;
    }
}
