package com.vchatcloud.androidsample;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * 메시지 객체
 */
public class Message {
    private String nickName = "";
    private String clientKey = "";
    private String message = "";
    private String mimeType = "";
    private String messageDt = "";
    private String date = "";
    private String roomId = "";
    private String grade = "";
    private String type = "msg";
    private JSONObject messageType = null;
    private JSONObject userInfo = null;

    public JSONObject getMessageType() {
        return messageType;
    }

    public void setMessageType(JSONObject messageType) {
        this.messageType = messageType;
    }

    public JSONObject getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(JSONObject userInfo) {
        this.userInfo = userInfo;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getClientKey() {
        return clientKey;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMessageDt() {
        return messageDt;
    }

    public void setMessageDt(String messageDt) {
        this.messageDt = messageDt;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Message(JSONObject jsonObject) {
        if (jsonObject.containsKey("nickName")) { this.nickName = jsonObject.get("nickName").toString(); }
        if (jsonObject.containsKey("clientKey")) { this.clientKey = jsonObject.get("clientKey").toString(); }
        if (jsonObject.containsKey("message")) { this.message = jsonObject.get("message").toString(); }
        if (jsonObject.containsKey("mimeType")) { this.mimeType = jsonObject.get("mimeType").toString(); }
        if (jsonObject.containsKey("messageDt")) { this.messageDt = jsonObject.get("messageDt").toString(); }
        if (jsonObject.containsKey("date")) {this.date = jsonObject.get("date").toString();}
        if (jsonObject.containsKey("roomId")) { this.roomId = jsonObject.get("roomId").toString(); }
        if (jsonObject.containsKey("grade")) { this.grade = jsonObject.get("grade").toString(); }
        if (jsonObject.containsKey("type")) { this.type = jsonObject.get("type").toString(); }
        try {
            if (jsonObject.containsKey("messageType")) { this.messageType = (jsonObject.get("messageType") == null) ? null : (JSONObject) new JSONParser().parse(jsonObject.get("messageType").toString()); };
            if (jsonObject.containsKey("userInfo")) { this.userInfo = (jsonObject.get("userInfo") == null) ? null : (JSONObject) new JSONParser().parse(jsonObject.get("userInfo").toString()); };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Message{" +
                "nickName='" + nickName + '\'' +
                ", clientKey='" + clientKey + '\'' +
                ", message='" + message + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", messageDt='" + messageDt + '\'' +
                ", date='" + date + '\'' +
                ", roomId='" + roomId + '\'' +
                ", grade='" + grade + '\'' +
                ", type='" + type + '\'' +
                ", messageType=" + messageType +
                ", userInfo=" + userInfo +
                '}';
    }
}
