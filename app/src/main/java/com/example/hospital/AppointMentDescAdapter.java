package com.example.hospital;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

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

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AppointMentDescAdapter extends RecyclerView.Adapter<AppointMentDescAdapter.ViewHolder> {
    List<AppointmentDescModel> appointmentDescModelList;
    private DatabaseReference ref;
    private FirebaseUser user;
    String appointmentreceiverid;
    String appointmentsenderid;
    String appointmentid;

    String status;
    int appointmentno;
    String nextAppointmentMessage="please give me your next appointment date.I need it urgently";
    public AppointMentDescAdapter(List<AppointmentDescModel> appointmentDescModelList) {
        this.appointmentDescModelList = appointmentDescModelList;
    }

    @NonNull
    @Override
    public AppointMentDescAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_appointment_desc_patient,null);
        ref= FirebaseDatabase.getInstance().getReference();
        user= FirebaseAuth.getInstance().getCurrentUser();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AppointMentDescAdapter.ViewHolder holder, int position) {

        AppointmentDescModel appointmentDescModel=appointmentDescModelList.get(position);
        String times=appointmentDescModel.getAppoinmentTime();
        appointmentid=appointmentDescModel.getAppoinmentid();
        appointmentreceiverid=appointmentDescModel.getReceiverid();
        appointmentsenderid=appointmentDescModel.getSenderid();
        String date=appointmentDescModel.getAppoinmentDate();
        String desc=appointmentDescModel.getAppoinmentDesc();
        status=appointmentDescModel.getAppointmentStatus();
        holder.appointmentDesc.setText(desc);
        holder.appointmentTime.setText(times);
        holder.appointmentDate.setText(date);
        holder.appointmentStatus.setText(appointmentDescModel.getAppointmentStatus());
        final Map<String ,Object> map=new HashMap<>();
        map.put("appointmentStatus","completed");


        ref.child("Users").child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String profession=snapshot.child("profession").getValue().toString();
                        if(profession.equals("Patient"))
                        {






                            holder.changeStatusOkBtn.setVisibility(View.INVISIBLE);
                            holder.changeStatusOkBtn.setEnabled(false);
                            holder.changeAppointmentStatusCheckbox.setVisibility(View.INVISIBLE);
                        }
                        if(profession.equals("Doctor"))
                        {







                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        ref.child("appointment_description").child(appointmentsenderid)
                .child(appointmentreceiverid).child(appointmentid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists())
                        {
                            String status=snapshot.child("appointmentStatus").getValue().toString();
                            if(status.equals("completed"))
                            {
                                holder.changeAppointmentStatusCheckbox.setVisibility(View.INVISIBLE);
                                holder.changeStatusOkBtn.setVisibility(View.INVISIBLE);
                                holder.changeStatusOkBtn.setEnabled(false);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });







        holder.changeStatusOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                if(holder.changeAppointmentStatusCheckbox.isChecked())
                {

                    ref.child("appointment_description").child(appointmentsenderid)
                            .child(appointmentreceiverid).child(appointmentid)
                            .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            ref.child("appointment_description").child(appointmentreceiverid)
                                    .child(appointmentsenderid).child(appointmentid)
                                    .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    ref.child("no_of_appointment").child(appointmentsenderid)
                                            .child(appointmentreceiverid).child("appointno").setValue("1")
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    ref.child("no_of_appointment").child(appointmentreceiverid)
                                                            .child(appointmentsenderid).child("appointno").setValue("1")
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {

                                                                    Toast.makeText(view.getContext(),"status updated",Toast.LENGTH_SHORT).show();
                                                                }
                                                            });

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });


                                    final String appointno=String.valueOf(appointmentno);




                                }
                            });



                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(view.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });


                }
                else {
                    Toast.makeText(view.getContext(),"please select checkbox",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return appointmentDescModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView appointmentTime;
        private TextView appointmentDate;
        private TextView appointmentDesc;
        private TextView appointmentStatus;
        private Button changeStatusOkBtn;
        private  Button demandNextAppointementBtn;
        private CheckBox changeAppointmentStatusCheckbox;
        private TextView nextappointmentDemandMsg;
        private Button demandAppointmentGiveBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            appointmentDate=itemView.findViewById(R.id.appoinment_date_patient);
            appointmentTime=itemView.findViewById(R.id.appoinment_time_patient);
            appointmentDesc=itemView.findViewById(R.id.appoinment_desc_patient);
            appointmentStatus=itemView.findViewById(R.id.appoinment_status_patient);
            changeAppointmentStatusCheckbox=itemView.findViewById(R.id.change_appointment_status_checkbox);
            changeStatusOkBtn=itemView.findViewById(R.id.change_status_ok_btn);



        }
    }
}
