package com.example.hospital;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAppointmentActivity extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference ref;
    private RecyclerView recyclerView;
    private String RequestMessage="I want your next Appointment";
    FirebaseRecyclerAdapter<PatientUserModel, AcceptedbyDoctorViewHolder> accceptedbydocadapter;
    FirebaseRecyclerAdapter<DoctorUserModel, AcceptedbyPatientViewHolder> acceptedbypatientadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointment);

        ref= FirebaseDatabase.getInstance().getReference();
        user= FirebaseAuth.getInstance().getCurrentUser();
        recyclerView=findViewById(R.id.accepted_appointment_recyclerview);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(MyAppointmentActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        ref.child("Users").child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String currentUserProfession=snapshot.child("profession").getValue().toString();
                        if(currentUserProfession.equals("Doctor"))
                        {

                            FirebaseRecyclerOptions<PatientUserModel> options=new FirebaseRecyclerOptions.Builder<PatientUserModel>()
                                    .setQuery(ref.child("my_appointment").child(user.getUid()),PatientUserModel.class).build();

                            accceptedbydocadapter=new FirebaseRecyclerAdapter<PatientUserModel, AcceptedbyDoctorViewHolder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull final AcceptedbyDoctorViewHolder holder, final int position, @NonNull PatientUserModel model) {

                                    final String list_user_id = getRef(position).getKey();
                                    DatabaseReference getref = getRef(position).child("my_appointment").getRef();
                                    getref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            try {
                                                String status = snapshot.getValue().toString();
                                                if (status.equals("saved")) {
                                                    ref.child("Users").child(list_user_id)
                                                            .addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                    PatientUserModel patientUserModel = snapshot.getValue(PatientUserModel.class);
                                                                    String name = patientUserModel.getName();
                                                                    String image = patientUserModel.getImage();
                                                                    String profession = patientUserModel.getProfession();
                                                                    Picasso.get().load(image).into(holder.circleImageView);
                                                                    holder.name.setText(name);
                                                                    holder.profession.setText(profession);
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                }
                                            }
                                            catch(NullPointerException e)
                                            {
                                                
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                                    //     ref.child("appointment_description").child(user.getUid())
                                    //         .addValueEventListener(new ValueEventListener() {
                                    //              @Override
                                    //            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    //              if(snapshot.exists() && snapshot.hasChild(list_user_id))
                                    //            {
                                    //              holder.timeDateappointMsg.setText("Appointment description is send to your patient ");
                                    //            holder.timeDateGiveBtn.setEnabled(false);
                                    //          holder.timeDateGiveBtn.setVisibility(View.INVISIBLE);
                                    //        holder.viewAppointmentBtn.setVisibility(View.VISIBLE);
                                    //      holder.viewAppointmentBtn.setEnabled(true);

                                    //                        }
                                    //                             /                      else {
///
                                    //                                                     holder.timeDateappointMsg.setText("Appointment  description is not send by you");
                                    //                                                holder.viewAppointmentBtn.setVisibility(View.INVISIBLE);
                                    //                                                 holder.viewAppointmentBtn.setEnabled(false);
                                    //                                        }
                                    //                                  }
///
                                    //                                             @Override
                                    //                                           public void onCancelled(@NonNull DatabaseError error) {
//
                                    //   });
                                    //                });



                                    ref.child("medicine_description").child(user.getUid())
                                            .child(list_user_id).addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                            if(snapshot.exists())
                                            {
                                                MedicineModel medicineModel=snapshot.getValue(MedicineModel.class);
                                                String medicineStatus=medicineModel.getMedicineStatus();
                                                if(medicineStatus.equals("not_expired"))
                                                {
                                                    holder.medicinestatusMsg.setText("Medicine for current appointment is given");
                                                    holder.medicinestatusMsg.setVisibility(View.VISIBLE);
                                                    holder.showMedicineBtn.setVisibility(View.VISIBLE);
                                                    holder.showMedicineBtn.setEnabled(true);
                                                    holder.giveMedicineBtn.setVisibility(View.INVISIBLE);
                                                    holder.giveMedicineBtn.setEnabled(false);
                                                }
                                                else {


                                                    holder.giveMedicineBtn.setEnabled(true);
                                                    holder.giveMedicineBtn.setVisibility(View.VISIBLE);
                                                    holder.medicinestatusMsg.setText("Medicines are expired.....Send new Medicines");
                                                    holder.medicinestatusMsg.setVisibility(View.VISIBLE);
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

                                    ref.child("appointment_description").child(user.getUid())
                                            .child(list_user_id).addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                            if(snapshot.exists())
                                            {

                                                AppointmentDescModel appointmentDescModel=snapshot.getValue(AppointmentDescModel.class);
                                                if(appointmentDescModel.getSenderid().equals(user.getUid()) && appointmentDescModel.getReceiverid().equals(list_user_id)) {
                                                    String status = appointmentDescModel.getAppointmentStatus();
                                                    if (status.equals("pending")) {

                                                        holder.timeDateappointMsg.setText("Appointment description is send to your patient ");
                                                        holder.timeDateGiveBtn.setEnabled(false);
                                                        holder.timeDateGiveBtn.setVisibility(View.INVISIBLE);
                                                        holder.viewAppointmentBtn.setVisibility(View.VISIBLE);
                                                        holder.viewAppointmentBtn.setEnabled(true);
                                                        holder.timeDateappointMsg.setVisibility(View.VISIBLE);
                                                    }
                                                    else {

                                                        holder.timeDateappointMsg.setText("Previous Appointments are completed");
                                                        holder.timeDateGiveBtn.setVisibility(View.VISIBLE);
                                                        holder.timeDateappointMsg.setVisibility(View.INVISIBLE);
                                                        holder.timeDateGiveBtn.setEnabled(true);
                                                        holder.timeDateappointMsg.setVisibility(View.VISIBLE);
                                                    }
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

                                    holder.timeDateGiveBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            Bundle bundle = new Bundle();
                                            AppointTimeDateByDocDialog appointTimeDateByDocDialog = new AppointTimeDateByDocDialog();
                                            bundle.putString("patientid", list_user_id);
                                            appointTimeDateByDocDialog.setArguments(bundle);
                                            appointTimeDateByDocDialog.show(getSupportFragmentManager(), "open dialog");
                                        }
                                    });


                                    holder.viewAppointmentBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(MyAppointmentActivity.this, AppointmentDescActivity.class);
                                            intent.putExtra("patientid", list_user_id);
                                            startActivity(intent);

                                        }
                                    });
                                    holder.giveMedicineBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            MedicineDescDialog medicineDescDialog = new MedicineDescDialog();

                                            Bundle bundle = new Bundle();
                                            bundle.putString("patientid", list_user_id);
                                            medicineDescDialog.setArguments(bundle);

                                            medicineDescDialog.show(getSupportFragmentManager(), "open dialog");

                                        }
                                    });

                                    //    ref.child("medicine_description").child(user.getUid())
                                    //             .addValueEventListener(new ValueEventListener() {
                                    //             @Override
                                    //           public void onDataChange(@NonNull DataSnapshot snapshot) {
//
                                    //                                              if (snapshot.exists() && snapshot.hasChild(list_user_id)) {
                                    //                                                holder.medicinestatusMsg.setText("Medicines are given ");
                                    //                                              holder.giveMedicineBtn.setVisibility(View.INVISIBLE);
                                    //                                            holder.giveMedicineBtn.setEnabled(false);
                                    //                                          holder.medicinestatusMsg.setVisibility(View.VISIBLE);
//
                                    //                                              } else {
                                    //                                              holder.medicinestatusMsg.setText("Medicines are not given");
                                    //                                               holder.showMedicineBtn.setVisibility(View.INVISIBLE);
                                    //                                           holder.showMedicineBtn.setEnabled(false);
                                    //                                         holder.giveMedicineBtn.setVisibility(View.VISIBLE);
                                    //                                       holder.giveMedicineBtn.setEnabled(true);
                                    //                                 }
                                    //                           }

                                    //                         @Override
                                    //                       public void onCancelled(@NonNull DatabaseError error) {


                                    //                     }
                                    //               });




                                    holder.showMedicineBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent=new Intent(MyAppointmentActivity.this,MedicineDescActivity.class);
                                            intent.putExtra("patientid",list_user_id);
                                            startActivity(intent);
                                        }
                                    });

                                    holder.startMeetingBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent=new Intent(MyAppointmentActivity.this,MeetingChatActivity.class);
                                            intent.putExtra("patientid",list_user_id);
                                            startActivity(intent);


                                        }
                                    });

                                    holder.shhowFeedbackDoctorBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            FeedbackDialogForDoctor feedbackDialogForDoctor=new FeedbackDialogForDoctor();
                                            Bundle bundle=new Bundle();
                                            bundle.putString("patientid",list_user_id);
                                            feedbackDialogForDoctor.setArguments(bundle);
                                            feedbackDialogForDoctor.show(getSupportFragmentManager(),"open dialog");
                                        }
                                    });

                                    holder.newAppointmentRquestAcceptBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            acceptRequestedAppointentForDoctor(list_user_id,holder);
                                        }
                                    });


                                    fetchAndDisplayNewRequestedApppointmentCustomMsgStatusForDoctor(list_user_id,holder);



                                }


                                @NonNull
                                @Override
                                public AcceptedbyDoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_accepted_appointment_doctor, null);
                                    return new AcceptedbyDoctorViewHolder(view);
                                }
                            };
                            recyclerView.setAdapter(accceptedbydocadapter);
                            accceptedbydocadapter.notifyDataSetChanged();
                            accceptedbydocadapter.startListening();


                        }
                        if(currentUserProfession.equals("Patient"))
                        {

                            FirebaseRecyclerOptions<DoctorUserModel> options=new FirebaseRecyclerOptions.Builder<DoctorUserModel>()
                                    .setQuery(ref.child("my_appointment").child(user.getUid()),DoctorUserModel.class).build();

                            acceptedbypatientadapter=new FirebaseRecyclerAdapter<DoctorUserModel, AcceptedbyPatientViewHolder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull final AcceptedbyPatientViewHolder holder, int position, @NonNull DoctorUserModel model) {

                                    final String list_user_id = getRef(position).getKey();
                                    DatabaseReference getref = getRef(position).child("my_appointment").getRef();
                                    getref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            try {
                                                String status = snapshot.getValue().toString();
                                                if (status.equals("saved")) {
                                                    ref.child("Users").child(list_user_id)
                                                            .addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                    DoctorUserModel doctorUserModel = snapshot.getValue(DoctorUserModel.class);
                                                                    String name = doctorUserModel.getName();
                                                                    String image = doctorUserModel.getImage();
                                                                    String profession = doctorUserModel.getProfession();
                                                                    Picasso.get().load(image).into(holder.circleImageView);
                                                                    holder.name.setText(name);
                                                                    holder.profession.setText(profession);
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                }
                                            }
                                            catch (NullPointerException e)
                                            {
                                                Toast.makeText(MyAppointmentActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    holder.viewappointmentdescBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(MyAppointmentActivity.this, AppointmentDescActivity.class);
                                            intent.putExtra("doctorid", list_user_id);
                                            startActivity(intent);
                                        }
                                    });
                                    holder.viewMedicineDescBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            Intent intent=new Intent(MyAppointmentActivity.this,MedicineDescActivity.class);
                                            intent.putExtra("doctorid",list_user_id);
                                            startActivity(intent);
                                        }
                                    });
                                    holder.startmeetingBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent=new Intent(MyAppointmentActivity.this,MeetingChatActivity.class);
                                            intent.putExtra("doctorid",list_user_id);
                                            startActivity(intent);
                                        }
                                    });

                                    ref.child("appointment_description").child(user.getUid())
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists() && snapshot.hasChild(list_user_id)) {



                                                        holder.appointmentStatusMsg.setText("Time and date is delivered by your doctor");
                                                        holder.viewappointmentdescBtn.setVisibility(View.VISIBLE);


                                                    } else {

                                                        holder.appointmentStatusMsg.setText("Time and date is not delivered by your doctor");
                                                        holder.viewappointmentdescBtn.setEnabled(false);
                                                        holder.viewappointmentdescBtn.setVisibility(View.INVISIBLE);
                                                    }

                                                }




                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                    ref.child("medicine_description").child(user.getUid())
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                    if(snapshot.exists() && snapshot.hasChild(list_user_id))
                                                    {

                                                        holder.medicineStatusMsg.setText("Medicine is given by your doctor");
                                                        holder.viewMedicineDescBtn.setVisibility(View.VISIBLE);
                                                        holder.viewMedicineDescBtn.setEnabled(true);
                                                    }
                                                    else{
                                                        holder.medicineStatusMsg.setText("Medicine is  not given by your doctor");
                                                        holder.viewMedicineDescBtn.setVisibility(View.INVISIBLE);
                                                        holder.viewMedicineDescBtn.setEnabled(false);

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });


                                    holder.lastmedicineFeedbackBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            LastMedicineFeedbackDialogForPatient feedbackDialog=new LastMedicineFeedbackDialogForPatient();
                                            Bundle bundle=new Bundle();
                                            bundle.putString("doctorid",list_user_id);
                                            feedbackDialog.setArguments(bundle);
                                            feedbackDialog.show(getSupportFragmentManager(),"open dialog");


                                        }
                                    });



                                    holder.viewFeedbackBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            ViewFeedbackPatientDialog viewFeedbackPatientDialog=new ViewFeedbackPatientDialog();
                                            Bundle bundle=new Bundle();
                                            bundle.putString("doctorid",list_user_id);
                                            viewFeedbackPatientDialog.setArguments(bundle);
                                            viewFeedbackPatientDialog.show(getSupportFragmentManager(),"open dialog");
                                        }
                                    });

                                    //  holder.patientRequestNewAppointmentBtn.setOnClickListener(new View.OnClickListener() {
                                    //     @Override
                                    //   public void onClick(View view) {
                                    //        requestNewAppointmentbypatientwithoutMsg(list_user_id);
                                    //       }
                                    // });

                                    holder.patientRequestNewAppointmentCustomMsgBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            RequestedAppointmentMessageDialog requestedAppointmentMessageDialog=new RequestedAppointmentMessageDialog();
                                            Bundle bundle=new Bundle();
                                            bundle.putString("doctorid",list_user_id);
                                            requestedAppointmentMessageDialog.setArguments(bundle);
                                            requestedAppointmentMessageDialog.show(getSupportFragmentManager(),"open dialog");
                                        }
                                    });


                                    fetchAndDisplayNewRequestedApppointmentCustomMsgStatusForPatient(list_user_id,holder);

                                }

                                @NonNull
                                @Override
                                public AcceptedbyPatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                    View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.single_accepted_appointment_patient,null);
                                    return new AcceptedbyPatientViewHolder(view);
                                }
                            };
                            recyclerView.setAdapter(acceptedbypatientadapter);
                            acceptedbypatientadapter.notifyDataSetChanged();
                            acceptedbypatientadapter.startListening();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void fetchAndDisplayNewRequestedApppointmentCustomMsgStatusForDoctor(String list_user_id, final AcceptedbyDoctorViewHolder holder) {
        ref.child("request_new_appointment_with_custom_message").child(user.getUid())
                .child(list_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.hasChild("request_type") && snapshot.hasChild("message"))
                {
                    String requestType=snapshot.child("request_type").getValue().toString();
                    if(requestType.equals("received"))
                    {

                        String message=snapshot.child("message").getValue().toString();
                        holder.newAppointMentRequestStatusMsgForDoctor.setText(message);
                        holder.newAppointmentRquestAcceptBtn.setVisibility(View.VISIBLE);
                        holder.newAppointmentRquestAcceptBtn.setEnabled(true);


                    }


                }
                else {
                    holder.newAppointmentRquestAcceptBtn.setVisibility(View.INVISIBLE);
                    holder.newAppointmentRquestAcceptBtn.setEnabled(false);
                    holder.newAppointMentRequestStatusMsgForDoctor.setText("NO new Appointment Request");
                    holder.newAppointMentRequestStatusMsgForDoctor.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void fetchAndDisplayNewRequestedApppointmentCustomMsgStatusForPatient(String list_user_id, final AcceptedbyPatientViewHolder holder) {

        ref.child("request_new_appointment_with_custom_message").child(user.getUid())
                .child(list_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.hasChild("request_type") && snapshot.hasChild("message"))
                {

                    String requestType=snapshot.child("request_type").getValue().toString();
                    String message=snapshot.child("message").getValue().toString();
                    if(requestType.equals("send"))
                    {
                        holder.patientRequestNewAppointmentCustomMsgBtn.setVisibility(View.INVISIBLE);
                        holder.patientRequestNewAppointmentCustomMsgBtn.setEnabled(false);
                        holder.requestNewAppointmentStatusMsg.setVisibility(View.VISIBLE);
                        holder.requestNewAppointmentStatusMsg.setText("Request send to your doctor");
                    }


                }
                else {
                    holder.requestNewAppointmentStatusMsg.setText("");
                    holder.requestNewAppointmentStatusMsg.setVisibility(View.INVISIBLE);
                    holder.patientRequestNewAppointmentCustomMsgBtn.setEnabled(true);
                    holder.patientRequestNewAppointmentCustomMsgBtn.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    private void acceptRequestedAppointentForDoctor(final String list_user_id, AcceptedbyDoctorViewHolder holder) {



        ref.child("request_new_appointment_with_custom_message").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists() && snapshot.hasChild(list_user_id))
                {
                    ref.child("request_new_appointment_with_custom_message").child(user.getUid()).child(list_user_id).removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    ref.child("request_new_appointment_with_custom_message").child(list_user_id).child(user.getUid()).removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    Toast.makeText(MyAppointmentActivity.this,"Request Accpepted",Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MyAppointmentActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }



    public class AcceptedbyPatientViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView circleImageView;
        private TextView name;
        private  TextView profession;
        private TextView speciality;
        private TextView appointmentStatusMsg;
        private TextView medicineStatusMsg;
        private  Button startmeetingBtn;
        private  Button viewMedicineDescBtn;
        private  Button viewappointmentdescBtn;
        private Button lastmedicineFeedbackBtn;
        private  Button viewFeedbackBtn;

        private TextView requestNewAppointmentStatusMsg;
        private  Button patientRequestNewAppointmentBtn;
        private  Button patientRequestNewAppointmentCustomMsgBtn;



        public AcceptedbyPatientViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView=itemView.findViewById(R.id.single_accepted_doctor_image);
            name=itemView.findViewById(R.id.single_accepted_doctor_name);
            profession=itemView.findViewById(R.id.single_accepted_doctor_profession);
            speciality=itemView.findViewById(R.id.single_accepted_doctor_speciality);
            startmeetingBtn=itemView.findViewById(R.id.start_meeting_btn_for_patient);
            viewMedicineDescBtn=itemView.findViewById(R.id.view_medicine_desc_for_patient);
            viewappointmentdescBtn=itemView.findViewById(R.id.view_appointment_desc_for_patient);
            appointmentStatusMsg=itemView.findViewById(R.id.time_date_status_msg_for_patient);
            medicineStatusMsg=itemView.findViewById(R.id.medicine_status_msg_for_patient);
            lastmedicineFeedbackBtn=itemView.findViewById(R.id.give_feedback_patient_btn);
            viewFeedbackBtn=itemView.findViewById(R.id.view_feedback_patient_btn);

            patientRequestNewAppointmentCustomMsgBtn=itemView.findViewById(R.id.patient_request_new_appointment_custom_msg_btn);
            requestNewAppointmentStatusMsg=itemView.findViewById(R.id.patient_request_new_appointment_status_msg);
        }
    }

    public class AcceptedbyDoctorViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView circleImageView;
        private TextView name;
        private  TextView profession;
        private TextView speciality;
        private TextView timeDateappointMsg;
        private TextView medicinestatusMsg;
        private Button timeDateGiveBtn;
        private Button giveMedicineBtn;
        private Button startMeetingBtn;
        private Button viewAppointmentBtn;
        private Button showMedicineBtn;
        private Button shhowFeedbackDoctorBtn;
        private TextView feedbackmsgview;
        private TextView newAppointMentRequestStatusMsgForDoctor;
        private Button newAppointmentRquestAcceptBtn;
        public AcceptedbyDoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.single_accepted_patient_image);
            name=itemView.findViewById(R.id.single_accepted_patient_name);
            profession=itemView.findViewById(R.id.single_accepted_patient_profession);
            timeDateappointMsg=itemView.findViewById(R.id.time_date_status_msg);
            timeDateGiveBtn=itemView.findViewById(R.id.appoint_time_date_btn);
            medicinestatusMsg=itemView.findViewById(R.id.medicine_status_msg);
            giveMedicineBtn=itemView.findViewById(R.id.single_accepted_doctor_give_medicine_btn);
            startMeetingBtn=itemView.findViewById(R.id.start_meeting_btn);
            showMedicineBtn=itemView.findViewById(R.id.show_medicine_btn_doctor);
            shhowFeedbackDoctorBtn=itemView.findViewById(R.id.show_feedback_doctor_btn);
            feedbackmsgview=itemView.findViewById(R.id.feedback_for_doctor_msg);
            viewAppointmentBtn=itemView.findViewById(R.id.view_appointment_desc_btn_doctor);
            newAppointmentRquestAcceptBtn=itemView.findViewById(R.id.doctor_accept_new_request_btn);
            newAppointMentRequestStatusMsgForDoctor=itemView.findViewById(R.id.request_new_appoint_for_doctor_statusMsg);
        }
    }
}