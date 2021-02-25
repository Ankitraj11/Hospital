package com.example.hospital;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AppointTimeDateByDocDialog extends AppCompatDialogFragment {
    private EditText time;
    private EditText date;
    private EditText desc;
    private Button giveTimeDdateBtn;
    private DatabaseReference ref;
    private String patientidfronAppointmentadapter;
    private ProgressBar progressBar;
    private FirebaseUser user;
    String appointmentStatus="pending";
    String patientid;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.time_date_appoint_by_doc,null);
        ref= FirebaseDatabase.getInstance().getReference();
        Bundle bundle=getArguments();
        if(bundle!=null)
        {
            patientid=bundle.getString("patientid");



        }
        user= FirebaseAuth.getInstance().getCurrentUser();
        time=view.findViewById(R.id.write_time);
        date=view.findViewById(R.id.write_date);
        desc=view.findViewById(R.id.write_desc);
        giveTimeDdateBtn=view.findViewById(R.id.udpate_timedate_btn);
        progressBar=view.findViewById(R.id.time_date_progress);

        giveTimeDdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String times=time.getText().toString();
                String dates=date.getText().toString();
                String descs=desc.getText().toString();
                time.setText("");
                date.setText("");
                desc.setText("");

                if(patientid!=null)
                {
                    final String key=ref.child("appointment_description").child(user.getUid())
                            .child(patientid).push().getKey();
                    final AppointmentDescModel appointmentDescModel=new AppointmentDescModel(appointmentStatus,times,dates,descs,key,user.getUid(),patientid);
                    ref.child("appointment_description").child(user.getUid()).child(patientid)
                            .child(key).setValue(appointmentDescModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            ref.child("appointment_description").child(patientid).child(user.getUid())
                                    .child(key).setValue(appointmentDescModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getActivity(),"appointment given",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });


                }

            }
        });
        builder.setView(view).setTitle("Give Appointment Info");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });

        return builder.create();
    }
}
