package com.example.hospital;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MeetingMsgAdapter extends RecyclerView.Adapter<MeetingMsgAdapter.ViewHolder> {
    List<MeetingMsgModel> meetingMsgModelList;
    private DatabaseReference ref;
    private FirebaseUser user;
    public MeetingMsgAdapter(List<MeetingMsgModel> meetingMsgModelList) {
        this.meetingMsgModelList = meetingMsgModelList;
    }

    @NonNull
    @Override
    public MeetingMsgAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_meeting_message,null);
        ref= FirebaseDatabase.getInstance().getReference();
        user= FirebaseAuth.getInstance().getCurrentUser();
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MeetingMsgAdapter.ViewHolder holder, int position) {
        MeetingMsgModel meetingMsgModel=meetingMsgModelList.get(position);
        final String senderid=meetingMsgModel.getSenderid();
        String receiverid=meetingMsgModel.getReceiverid();
        final String messages=meetingMsgModel.getMessages();
        ref.child("Users").child(senderid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String name=snapshot.child("name").getValue().toString();
                if(senderid.equals(user.getUid()))
                {
                    holder.senderName.setText(name);
                    holder.receiverMsgs.setVisibility(View.INVISIBLE);
                    holder.receiverName.setVisibility(View.INVISIBLE);
                    holder.senderMsgs.setText(messages);
                }
                else{
                    holder.receiverName.setText(name);
                    holder.receiverMsgs.setText(messages);
                    holder.senderMsgs.setVisibility(View.INVISIBLE);
                    holder.senderName.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return meetingMsgModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView senderName;
        private TextView senderMsgs;
        private TextView receiverName;
        private TextView receiverMsgs;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            senderName=itemView.findViewById(R.id.single_sender_name);
            senderMsgs=itemView.findViewById(R.id.single_sender_msg);
            receiverName=itemView.findViewById(R.id.single_receiver_name);
            receiverMsgs=itemView.findViewById(R.id.single_receiver_msg);

        }



    }
}

