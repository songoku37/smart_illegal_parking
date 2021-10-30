package com.example.carepree;

import java.util.HashMap;
import java.util.Map;

public class User
{
    public String name;
    public String profileImgUrl;
    public String uid;
    public String pushToken;
}
class ChatModel
{
    public Map<String,Boolean> users = new HashMap<>(); //채팅방 유저
    public Map<String,Comment> comments = new HashMap<>(); //채팅 메시지

    public static class Comment
    {
        public String uid;
        public String message;
        public Object timestamp;
    }
}