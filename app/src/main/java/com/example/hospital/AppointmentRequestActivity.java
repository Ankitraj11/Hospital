package com.example.hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

public class AppointmentRequestActivity extends AppCompatActivity {
    FirebaseRecyclerAdapter<PatientUserModel, RequestedPatientViewHolder> patientapter;
    FirebaseRecyclerAdapter<DoctorUserModel, RequestedDoctorViewHolder> doctoradapter;
    private DatabaseReference ref;
    private FirebaseUser user;
    private RecyclerView recyclerView;
    String currentUserProfession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_request);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(AppointmentRequestActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView=findViewById(R.id.appointment_request_recyclerview);
        recyclerView.setLayoutManager(layoutManager);
        ref= FirebaseDatabase.getInstance().getReference();
        user= FirebaseAuth.getInstance().getCurrentUser();
        ref.child("Users").child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        currentUserProfession=snapshot.child("profession").getValue().toString();
                        if(currentUserProfession.equals("Doctor"))
                        {

                            FirebaseRecyclerOptions<PatientUserModel> options=new FirebaseRecyclerOptions.Builder<PatientUserModel>()
                                    .setQuery(ref.child("appointment_request").child(user.getUid()),PatientUserModel.class).build();


                            patientapter=new FirebaseRecyclerAdapter<PatientUserModel, RequestedPatientViewHolder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull final RequestedPatientViewHolder holder, final int position, @NonNull PatientUserModel model) {
                                    final  String list_user_id=getRef(position).getKey();
                                    DatabaseReference getRef=getRef(position).child("request_type").getRef();
                                    getRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            if(snapshot.exists())
                                            {
                                                String requestTpe=snapshot.getValue().toString();
                                                if(requestTpe.equals("received"))
                                                {
                                                    ref.child("Users").child(list_user_id)
                                                            .addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                    PatientUserModel patientUserModel=snapshot.getValue(PatientUserModel.class);
                                                                    String image=patientUserModel.getImage();
                                                                    String name=patientUserModel.getName();
                                                                    String profession=patientUserModel.getProfession();
                                                                    Picasso.get().load(image).into(holder.circleImageView);
                                                                    holder.Name.setText(name);
                                                                    holder.Profession.setText(profession);

                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            acceptAppointmentforDoctor(list_user_id);
                                        }
                                    });
                                    holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            rejectAppointmentForDoctor(list_user_id);
                                        }
                                    });



                                }

                                @NonNull
                                @Override
                                public RequestedPatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                    View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_requested_patient,null);
                                    return new RequestedPatientViewHolder(view);
                                }
                            };recyclerView.setAdapter(patientapter);
                            patientapter.notifyDataSetChanged();
                            patientapter.startListening();




                        }
                        if(currentUserProfession.equals("Patient")) {

                            FirebaseRecyclerOptions<DoctorUserModel> options = new FirebaseRecyclerOptions.Builder<DoctorUserModel>()
                                    .setQuery(ref.child("appointment_request").child(user.getUid()), DoctorUserModel.class).build();

                            doctoradapter = new FirebaseRecyclerAdapter<DoctorUserModel, RequestedDoctorViewHolder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull final RequestedDoctorViewHolder holder, final int position, @NonNull DoctorUserModel model) {

                                    final    String  list_user_id=getRef(position).getKey();
                                    DatabaseReference getref=getRef(position).child("request_type").getRef();
                                    getref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            if(snapshot.exists())
                                            {
                                                String requestType=snapshot.getValue().toString();
                                                if(requestType.equals("received"))
                                                {
                                                    ref.child("Users").child(list_user_id)
                                                            .addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                    DoctorUserModel doctorUserModel=snapshot.getValue(DoctorUserModel.class);
                                                                    String Name=doctorUserModel.getName();
                                                                    String image=doctorUserModel.getImage();
                                                                    String profession=doctorUserModel.getProfession();
                                                                    String speciality=doctorUserModel.getSpeciality();
                                                                    Picasso.get().load(image).into(holder.circleImageView);
                                                                    holder.name.setText(Name);
                                                                    holder.profession.setText(profession);
                                                                    holder.speciality.setText(speciality);
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            acceptAppointforPatient(list_user_id);
                                        }
                                    });

                                    holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            rejectAppoinmentforPatient(list_user_id);
                                        }
                                    });
                                }

                                @NonNull
                                @Override
                                public RequestedDoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                    View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.single_requested_doctor,null);
                                    return new RequestedDoctorViewHolder(view);
                                }
                            };
                            recyclerView.setAdapter(doctoradapter);
                            doctoradapter.notifyDataSetChanged();;
                            doctoradapter.startListening();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




    }

    private void rejectAppoinmentforPatient(final String list_user_id) {
        ref.child("appointment_request").child(user.getUid())
                .child(list_user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                ref.child("appointment_request").child(list_user_id)
                        .child(user.getUid()).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
    }

    private void acceptAppointforPatient(final String list_user_id) {
        ref.child("my_appointment").child(user.getUid())
                .child(list_user_id).child("status")
                .setValue("saved").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ref.child("my_appointment").child(list_user_id)
                        .child(user.getUid()).child("status")
                        .setValue("saved").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        ref.child("appointment_request").child(user.getUid())
                                .child(list_user_id).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        ref.child("appointment_request").child(list_user_id)
                                                .child(user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                                    }
                                });
                    }
                });
            }
        });
    }

    private void rejectAppointmentForDoctor(final String list_user_id) {

        ref.child("appointment_request").child(user.getUid())
                .child(list_user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                ref.child("appointment_request").child(list_user_id)
                        .child(user.getUid()).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
    }

    private void acceptAppointmentforDoctor(final String list_user_id ) {

        ref.child("my_appointment").child(user.getUid())
                .child(list_user_id).child("my_appointment")
                .setValue("saved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            ref.child("my_appointment").child(list_user_id)
                                    .child(user.getUid()).child("my_appointment")
                                    .setValue("saved")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                ref.child("appointment_request").child(user.getUid())
                                                        .child(list_user_id).removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful())
                                                                {
                                                                    ref.child("appointment_request").child(list_user_id)
                                                                            .child(user.getUid()).removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {

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

    public class RequestedDoctorViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView circleImageView;
        private  TextView name;
        private  TextView profession;
        private  TextView speciality;
        private Button rejectBtn;
        private  Button acceptBtn;
        public RequestedDoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.appointment_request_doctor_image);
            name=itemView.findViewById(R.id.appointment_request_doctor_name);
            profession=itemView.findViewById(R.id.appointment_request_doctor_profession);
            speciality=itemView.findViewById(R.id.appointment_request_doctor_speciality);
            rejectBtn=itemView.findViewById(R.id.appointment_request_doctor_reject_btn);
            acceptBtn=itemView.findViewById(R.id.appointment_request_doctor_accept_btn);
        }
    }

    public  class RequestedPatientViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView circleImageView;
        private TextView Name;
        private TextView Profession;
        private Button acceptBtn;
        private Button rejectBtn;
        public RequestedPatientViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.appointment_request_patient_image);
            Name=itemView.findViewById(R.id.appointment_request_patient_name);
            Profession=itemView.findViewById(R.id.appointment_request_patient_profession);
            acceptBtn=itemView.findViewById(R.id.appointment_request_patient_accept_btn);
            rejectBtn=itemView.findViewById(R.id.appointment_request_patient_reject_btn);
        }
    }

}