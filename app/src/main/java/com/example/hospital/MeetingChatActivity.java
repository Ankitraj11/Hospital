package com.example.hospital;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MeetingChatActivity extends AppCompatActivity {
    private List<MeetingMsgModel> meetingMsgModelList;
    private MeetingMsgAdapter meetingMsgAdapter;
    private RecyclerView recyclerView;
    private FirebaseUser user;
    String currentUserProfession;
    private EditText writeMeetingMessage;
    private Button sendMeetingMessageBtn;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_chat);
        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();




        LinearLayoutManager layoutManager = new LinearLayoutManager(MeetingChatActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = findViewById(R.id.meeting_message_recyclerview);
        recyclerView.setLayoutManager(layoutManager);



        writeMeetingMessage = findViewById(R.id.write_meeting_message);
        sendMeetingMessageBtn = findViewById(R.id.send_meeting_message_btn);


        ref.child("Users").child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        currentUserProfession=snapshot.child("profession").getValue().toString();
                        if(currentUserProfession.equals("Doctor"))
                        {

                            meetingMsgModelList=new ArrayList<>();
                            meetingMsgAdapter=new MeetingMsgAdapter(meetingMsgModelList);
                            recyclerView.setAdapter(meetingMsgAdapter);
                            final String patientid=getIntent().getStringExtra("patientid");

                            ref.child("meeting_messages").child(user.getUid()).child(patientid)
                                    .addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                            if(snapshot.exists())
                                            {
                                                MeetingMsgModel meetingMsgModel=snapshot.getValue(MeetingMsgModel.class);
                                                meetingMsgModelList.add(meetingMsgModel);

                                                meetingMsgAdapter.notifyDataSetChanged();


                                            }
                                        }

                                        @Override
                                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                        }

                                        @Override
                                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                            sendMeetingMessageBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String meetingmessage=writeMeetingMessage.getText().toString();
                                    writeMeetingMessage.setText("");
                                    if(!TextUtils.isEmpty(meetingmessage))
                                    {
                                        final String meetingmessageid=ref.child("meeting_messages").child(user.getUid())
                                                .child(patientid).push().getKey();
                                        final MeetingMsgModel meetingMsgModel=new MeetingMsgModel(user.getUid(),patientid,meetingmessage,meetingmessageid);
                                        ref.child("meeting_messages").child(user.getUid()).child(patientid)
                                                .child(meetingmessageid).setValue(meetingMsgModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                ref.child("meeting_messages").child(patientid).child(user.getUid())
                                                        .child(meetingmessageid).setValue(meetingMsgModel)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                              //  Toast.makeText(MeetingChatActivity.this,"Message sned",Toast.LENGTH_SHORT).show();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                      //  Toast.makeText(MeetingChatActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }
                            });


                        }
                        if(currentUserProfession.equals("Patient"))
                        {

                            meetingMsgModelList=new ArrayList<>();
                            meetingMsgAdapter=new MeetingMsgAdapter(meetingMsgModelList);
                            recyclerView.setAdapter(meetingMsgAdapter);
                            final String docotrid=getIntent().getStringExtra("doctorid");
                            ref.child("meeting_messages").child(user.getUid()).child(docotrid)
                                    .addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                            if(snapshot.exists())
                                            {

                                                MeetingMsgModel meetingMsgModel=snapshot.getValue(MeetingMsgModel.class);
                                                meetingMsgModelList.add(meetingMsgModel);
                                                meetingMsgAdapter.notifyDataSetChanged();

                                            }

                                        }

                                        @Override
                                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                        }

                                        @Override
                                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                            sendMeetingMessageBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String meetingmessage=writeMeetingMessage.getText().toString();
                                    writeMeetingMessage.setText("");
                                    if(!TextUtils.isEmpty(meetingmessage))
                                    {
                                        final String meetingmessageid=ref.child("meeting_messages").child(user.getUid())
                                                .child(docotrid).push().getKey();
                                        final MeetingMsgModel meetingMsgModel=new MeetingMsgModel(user.getUid(),docotrid,meetingmessage,meetingmessageid);
                                        ref.child("meeting_messages").child(user.getUid()).child(docotrid)
                                                .child(meetingmessageid).setValue(meetingMsgModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                ref.child("meeting_messages").child(docotrid).child(user.getUid())
                                                        .child(meetingmessageid).setValue(meetingMsgModel)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                Toast.makeText(MeetingChatActivity.this,"Message sned",Toast.LENGTH_SHORT).show();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                        Toast.makeText(MeetingChatActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }
                            });



                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }
}