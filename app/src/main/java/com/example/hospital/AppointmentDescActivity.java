package com.example.hospital;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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

public class AppointmentDescActivity extends AppCompatActivity {
    private String  doctorid;
    private String  patientid;

    private List<AppointmentDescModel> appointmentDescModelList;
    private AppointMentDescAdapter appointMentDescAdapter;
    private RecyclerView recyclerView;
    private DatabaseReference ref;
    private FirebaseUser user;
    String appointmentreceiverid;
    String currentUserProfession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_appointment_desc);
        ref= FirebaseDatabase.getInstance().getReference();
        user= FirebaseAuth.getInstance().getCurrentUser();
        recyclerView=findViewById(R.id.appontment_desc_recyclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(AppointmentDescActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        ref.child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUserProfession=snapshot.child("profession").getValue().toString();
                if(currentUserProfession.equals("Patient"))
                {

                    appointmentDescModelList=new ArrayList<>();
                    appointMentDescAdapter=new AppointMentDescAdapter(appointmentDescModelList);
                    recyclerView.setAdapter(appointMentDescAdapter);

                    doctorid=getIntent().getStringExtra("doctorid");
                    ref.child("appointment_description").child(user.getUid())
                            .child(doctorid).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            if(snapshot.exists())
                            {
                                AppointmentDescModel appointmentDescModel=snapshot.getValue(AppointmentDescModel.class);
                                appointmentreceiverid=appointmentDescModel.getReceiverid();
                                if(appointmentDescModel.getReceiverid().equals(user.getUid()) && appointmentDescModel.getSenderid().equals(doctorid))
                                {

                                    appointmentDescModelList.add(appointmentDescModel);
                                    appointMentDescAdapter.notifyDataSetChanged();

                                }
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


                }
                if(currentUserProfession.equals("Doctor"))
                {

                    appointmentDescModelList=new ArrayList<>();
                    appointMentDescAdapter=new AppointMentDescAdapter(appointmentDescModelList);
                    recyclerView.setAdapter(appointMentDescAdapter);

                    patientid=getIntent().getStringExtra("patientid");
                    ref.child("appointment_description").child(user.getUid())
                            .child(patientid).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            if(snapshot.exists())
                            {
                                AppointmentDescModel appointmentDescModel=snapshot.getValue(AppointmentDescModel.class);
                                String receiverid=appointmentDescModel.getReceiverid();
                                String senderid=appointmentDescModel.getSenderid();
                                if(senderid.equals(user.getUid()) && receiverid.equals(patientid))
                                {
                                    appointmentDescModelList.add(appointmentDescModel);
                                    appointMentDescAdapter.notifyDataSetChanged();

                                }
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



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}