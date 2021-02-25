package com.example.hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientDescriptionActivity extends AppCompatActivity {
    private CircleImageView circleImageView;
    private TextView patientname;
    private DatabaseReference ref;
    private FirebaseDatabase db;
    private String currentState="new";
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Button patientButton1;
    private Button patientButton2;
    private ProgressBar progressBar;
    private String patientid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_description);
        patientButton1 = findViewById(R.id.patient_button1);
        patientButton2 = findViewById(R.id.patient_button2);
        progressBar = findViewById(R.id.progressBar);
        patientButton1.setEnabled(false);
        patientButton1.setVisibility(View.INVISIBLE);
        patientButton2.setVisibility(View.INVISIBLE);
        patientButton2.setEnabled(false);
        circleImageView = findViewById(R.id.patient_desc_image);
        patientname = findViewById(R.id.patient_desc_name);
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        ref = db.getReference();
        Intent intent = getIntent();
        patientid = intent.getExtras().get("patientid").toString();

            ref.child("Users").child(patientid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    PatientUserModel patientUserModel = snapshot.getValue(PatientUserModel.class);
                    String patientName = patientUserModel.getName();
                    String patientProfession = patientUserModel.getProfession();
                    String patientImage = patientUserModel.getImage();
                    Picasso.get().load(patientImage).into(circleImageView);
                    patientname.setText(patientName);

                    managedoctorRequest();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            patientButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressBar.setVisibility(View.VISIBLE);


                    if (currentState.equals("new")) {
                        //  placeAppointmentfordoctor();
                    }
                    if (currentState.equals("appointment_placed")) {
                        //   cancelAppointmentfordoctor();

                    }
                    if (currentState.equals("appointment_received")) {

                        acceptAppointmentfordoctor();
                    }
                    if (currentState.equals("appointment_accepted")) {
                        removeAppointmentfordoctor();
                    }


                }
            });



    }
    private void removeAppointmentfordoctor() {
        patientButton1.setEnabled(false);


        ref.child("my_appointment").child(user.getUid())
                .child(patientid)
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {

                    ref.child("my_appointment").child(patientid)
                            .child(user.getUid())
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                currentState="new";
                                patientButton1.setVisibility(View.INVISIBLE);
                                patientButton1.setEnabled(false);
                                patientButton2.setVisibility(View.INVISIBLE);
                                patientButton2.setEnabled(false);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });


    }

    private void acceptAppointmentfordoctor() {

        patientButton1.setEnabled(false);
        ref.child("my_appointment").child(user.getUid())
                .child(patientid).child("my_appointment")
                .setValue("saved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            ref.child("my_appointment").child(patientid)
                                    .child(user.getUid()).child("my_appointment")
                                    .setValue("saved")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                ref.child("appointment_request").child(user.getUid())
                                                        .child(patientid).removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful())
                                                                {
                                                                    ref.child("appointment_request").child(patientid)
                                                                            .child(user.getUid()).removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                                    patientButton2.setEnabled(false);
                                                                                    patientButton1.setVisibility(View.VISIBLE);
                                                                                    patientButton1.setText("Remove Appointement");
                                                                                    patientButton2.setVisibility(View.INVISIBLE);
                                                                                    patientButton1.setEnabled(true);
                                                                                    currentState="appointment_accepted";
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });


                        }
                    }
                });

    }

    private void rejectAppointmentfordoctor(final String patientid) {

        patientButton2.setEnabled(false);
        ref.child("appointment_request").child(user.getUid())
                .child(patientid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                ref.child("appointment_request").child(patientid)
                        .child(user.getUid()).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                currentState="new";
                                patientButton1.setEnabled(false);
                                patientButton1.setVisibility(View.INVISIBLE);
                                patientButton2.setVisibility(View.INVISIBLE);
                              //  patientButton1.setText("Place Appointment");
                              progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
            }
        });
    }

    private void managedoctorRequest() {

        ref.child("appointment_request").child(user.getUid()).child(patientid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists())
                        {
                            String requestType=snapshot.child("request_type").getValue().toString();
                       //     if(requestType.equals("send"))
                         //   {
                           //     currentState="appointment_placed";
                             //   patientButton1.setText("Cancel Appointment");
                            //    patientButton2.setVisibility(View.INVISIBLE);
                            //    patientButton2.setEnabled(false);


                           // }
                            if(requestType.equals("received"))
                            {
                                currentState="appointment_received";
                                patientButton1.setVisibility(View.VISIBLE);
                                patientButton2.setText("Reject Appointment");
                                patientButton1.setText("Accept Appointment");
                                patientButton2.setVisibility(View.VISIBLE);
                                patientButton2.setEnabled(true);
                                patientButton1.setEnabled(true);
                                patientButton2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        progressBar.setVisibility(View.VISIBLE);
                                        rejectAppointmentfordoctor(patientid);
                                    }
                                });
                            }
                        }
                        else{

                            ref.child("my_appointment").child(user.getUid())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            if(snapshot.exists() && snapshot.hasChild(patientid))
                                            {
                                                patientButton1.setVisibility(View.VISIBLE);
                                                currentState="appointment_accepted";
                                                patientButton1.setText("Remove Apointment");
                                                patientButton2.setVisibility(View.INVISIBLE);
                                                patientButton2.setEnabled(false);
                                                patientButton1.setEnabled(true);

                                            }
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