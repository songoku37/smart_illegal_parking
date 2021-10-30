package com.example.carepree;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private List<ChatData> mDataset;
    private String myNickName;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView TextView_nickname;
        public TextView TextView_msg;
        public View rootView;

        public MyViewHolder(View v) {
            super(v);
            TextView_nickname = v.findViewById(R.id.TextView_nickname);
            TextView_msg = v.findViewById(R.id.TextView_msg);
            rootView = v;

            v.setClickable(true);
            v.setEnabled(true);
        }
    }


    public ChatAdapter (List<ChatData> myDataset, Context context, String myNickName) {
        mDataset = myDataset;
        this.myNickName = myNickName;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ChatData chat = mDataset.get(position);

        holder.TextView_nickname.setText(chat.getNickname());
        holder.TextView_msg.setText(chat.getMsg());

        if(chat.getNickname().equals(this.myNickName)){
            holder.TextView_msg.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            holder.TextView_nickname.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        }
        else {
            holder.TextView_msg.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            holder.TextView_nickname.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);

        }

    }

    @Override
    public int getItemCount() {
            return mDataset==null ? 0 : mDataset.size();
        }

    public ChatData getChat(int position){
        return mDataset != null ? mDataset.get(position) : null;
    }

    public void addChat(ChatData chat) {
        mDataset.add(chat);
        notifyItemInserted(mDataset.size()-1);
    }

}
