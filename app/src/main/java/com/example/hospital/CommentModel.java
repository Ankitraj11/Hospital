package com.example.hospital;

public class CommentModel {

    String comments;
    String senderid;
    String receiverid;
    String commentid;
    String rating;

    public CommentModel() {
    }


    public CommentModel(String rating,String comments, String senderid, String receiverid, String commentid) {
        this.comments = comments;
        this.rating=rating;
        this.senderid = senderid;
        this.receiverid = receiverid;
        this.commentid = commentid;
    }

    public String getComments() {
        return comments;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }
}
