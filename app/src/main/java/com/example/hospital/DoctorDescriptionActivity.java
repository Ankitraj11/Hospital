package com.example.hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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

public class DoctorDescriptionActivity extends AppCompatActivity {
    private DatabaseReference ref;
    private FirebaseUser user;
    private RatingBar ratingBar;
    private Button giveRating;
    private TextView name;
    private Button viewomments;
    private TextView profession;
    private TextView speciality;
    private CircleImageView circleImageView;
    private String patientid;
    private String doctorid;
    private TextView comment;
    private Button button1;
    private Button button2;
    String currentUserProfession;
    private EditText writeComments;
    private Button postCommentsBtn;
    private ProgressBar doctorProgressbar;
    private String currentState = "new";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_description);
        ref = FirebaseDatabase.getInstance().getReference();
        circleImageView = findViewById(R.id.user_desc_image);
        viewomments=findViewById(R.id.view_comments);
        ratingBar=findViewById(R.id.rating_bar);
        comment=findViewById(R.id.user_comment);
        name = findViewById(R.id.user_desc_name);
        button1 = findViewById(R.id.button1);
        doctorProgressbar=findViewById(R.id.doctor_progressbar);
        button1.setText("Place Appointment");

        writeComments=findViewById(R.id.write_comments);
        postCommentsBtn=findViewById(R.id.save_comments_btn);

        profession = findViewById(R.id.user_desc_profession);
        speciality = findViewById(R.id.user_desc_speciality);
      //  patientid = getIntent().ggetStringExtra("patientid");
        Intent intent=getIntent();
        doctorid=intent.getStringExtra("doctorid");
        user = FirebaseAuth.getInstance().getCurrentUser();




         ref.child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {

                 currentUserProfession = snapshot.child("profession").getValue().toString();

                 if (currentUserProfession.equals("Patient")) {


                     ftechNOofappointmentandMedicine();


                     viewomments.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View view) {

                             Intent intent = new Intent(DoctorDescriptionActivity.this, CommentActivity.class);
                             intent.putExtra("doctorid", doctorid);
                             startActivity(intent);
                         }

                     });
                     postCommentsBtn.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View view) {
                             String comments = writeComments.getText().toString();
                             writeComments.setText("");
                             if (TextUtils.isEmpty(comments)) {
                                 Toast.makeText(DoctorDescriptionActivity.this, "please write some comments", Toast.LENGTH_SHORT).show();

                             } else {


                                 postComment(comments);


                             }
                         }
                     });


                     ref.child("Users").child(doctorid)
                             .addValueEventListener(new ValueEventListener() {
                                 @Override
                                 public void onDataChange(@NonNull DataSnapshot snapshot) {

                                     DoctorUserModel doctorUserModel = snapshot.getValue(DoctorUserModel.class);
                                     String doctorname = doctorUserModel.getName();
                                     String doctorimage = doctorUserModel.getImage();
                                     String doctorprofession = doctorUserModel.getProfession();
                                     String doctorspeciality = doctorUserModel.getSpeciality();
                                     Picasso.get().load(doctorimage).into(circleImageView);
                                     name.setText(doctorname);
                                     profession.setText(doctorprofession);
                                     speciality.setText(doctorspeciality);
                                     managepatientRequest();
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


         button1.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 doctorProgressbar.setVisibility(View.VISIBLE);
                 if (currentUserProfession.equals("Patient")) {
                     if (currentState.equals("new")) {
                         placeAppointmentforpatient();
                     }
                     if (currentState.equals("appointment_placed")) {
                         cancelAppointmentforpatient();

                     }
                     if (currentState.equals("appointment_received")) {

                         acceptAppointmentforpatient();
                     }
                     if (currentState.equals("appointment_accepted")) {


                         removeAppointmentforpatient();
                     }
                 }

             }
         });


    }

    private void ftechNOofappointmentandMedicine() {

        ref.child("no_of_appointment").child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists() && snapshot.hasChild(doctorid))
                        {
                            ref.child("no_of_medicine").child(user.getUid())
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            if(snapshot.hasChild(doctorid) && snapshot.exists())
                                            {
                                                writeComments.setVisibility(View.VISIBLE);
                                                postCommentsBtn.setVisibility(View.VISIBLE);
                                                ratingBar.setVisibility(View.VISIBLE);
                                                comment.setVisibility(View.VISIBLE);


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

    private void postComment(String comments) {
        String commentid=ref.child("comments").push().getKey();
        String rating=String.valueOf(ratingBar.getRating());
        CommentModel commentModel=new CommentModel(rating,comments,user.getUid(),doctorid,commentid);

        ref.child("comments").child(commentid).setValue(commentModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(DoctorDescriptionActivity.this,"Comment published",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DoctorDescriptionActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });



    }










    private void removeAppointmentforpatient() {
        button1.setEnabled(false);

      try{
          ref.child("my_appointment").child(doctorid)
                  .child(user.getUid()).removeValue()
                  .addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                          if(task.isSuccessful())
                          {
                              ref.child("my_appointment").child(user.getUid())
                                      .child(doctorid).removeValue()
                                      .addOnCompleteListener(new OnCompleteListener<Void>() {
                                          @Override
                                          public void onComplete(@NonNull Task<Void> task) {
                                              if(task.isSuccessful())
                                              {
                                                  currentState="new";
                                                  button1.setEnabled(true);
                                                  button1.setText("Place Appointment");
                                                  button1.setVisibility(View.VISIBLE);
                                                  doctorProgressbar.setVisibility(View.INVISIBLE);
                                              }
                                          }
                                      });


                          }
                      }
                  });
      }
      catch (Exception e)
      {
          Toast.makeText(DoctorDescriptionActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
      }




    }

    private void rejectAppointmentforpatient() {

        ref.child("appointment_request").child(user.getUid())
                .child(doctorid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                ref.child("appointment_request").child(doctorid)
                        .child(user.getUid()).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                currentState="new";
                                button1.setEnabled(true);
                                button1.setVisibility(View.VISIBLE);

                                button1.setText("Place Appointment");

                            }
                        });


            }
        });
    }
    private void acceptAppointmentforpatient() {

        button1.setEnabled(false);
        ref.child("my_appointment").child(user.getUid())
                .child(doctorid).child("status")
                .setValue("saved").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ref.child("my_appointment").child(doctorid)
                        .child(user.getUid()).child("status")
                        .setValue("saved").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        ref.child("appointment_request").child(user.getUid())
                                .child(doctorid).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        ref.child("appointment_request").child(doctorid)
                                                .child(user.getUid()).removeValue()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        button1.setVisibility(View.VISIBLE);
                                                        button1.setText("Remove Appointement");

                                                        button1.setEnabled(true);
                                                        currentState="appointment_accepted";
                                                    }
                                                });
                                    }
                                });
                    }
                });

            }
        });

    }

    private void cancelAppointmentforpatient() {
        button1.setEnabled(false);
        ref.child("appointment_request").child(user.getUid())
                .child(doctorid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                ref.child("appointment_request").child(doctorid)
                        .child(user.getUid()).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                currentState="new";
                                button1.setEnabled(true);
                                button1.setText("Place Appointment");
                                doctorProgressbar.setVisibility(View.INVISIBLE);



                            }
                        });
            }
        });


    }

    private void managepatientRequest() {


        ref.child("appointment_request").child(user.getUid()).child(doctorid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            String requestType = snapshot.child("request_type").getValue().toString();
                            if (requestType.equals("send")) {
                                currentState = "appointment_placed";
                                button1.setText("Cancel Appointment");




                            }
                            if (requestType.equals("received")) {
                                currentState = "appointment_received";
                                button1.setVisibility(View.VISIBLE);

                                button1.setText("Accept Appointment");


                            }
                        }

                        else{

                            ref.child("my_appointment").child(user.getUid())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            if(snapshot.exists() &&snapshot.hasChild(doctorid))
                                            {
                                                currentState="appointment_accepted";
                                                button1.setText("Remove Apointment");


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

    private void managedoctorRequest() {

        ref.child("appointment_request").child(user.getUid()).child(patientid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists())
                        {
                            String requestType=snapshot.child("request_type").getValue().toString();
                            if(requestType.equals("send"))
                            {
                                currentState="appointment_placed";
                                button1.setText("Cancel Appointment");

                            }
                            if(requestType.equals("received"))
                            {
                                currentState="appointment_received";
                                button1.setVisibility(View.VISIBLE);

                                button1.setText("Accept Appointment");

                            }
                        }
                        else{

                            ref.child("my_appointment").child(user.getUid())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            if(snapshot.exists() && snapshot.hasChild(patientid))
                                            {

                                                currentState="appointment_accepted";
                                                button1.setText("Remove Apointment");


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



    private void placeAppointmentforpatient() {

        button1.setEnabled(false);
        ref.child("appointment_request").child(user.getUid())
                .child(doctorid).child("request_type").setValue("send")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ref.child("appointment_request").child(doctorid)
                                .child(user.getUid()).child("request_type").setValue("received")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        button1.setEnabled(true);
                                        currentState="appointment_placed";
                                        button1.setVisibility(View.VISIBLE);

                                        button1.setText("Cancel Appointment");
                                        doctorProgressbar.setVisibility(View.INVISIBLE);
                                    }
                                });

                    }
                });



    }
}