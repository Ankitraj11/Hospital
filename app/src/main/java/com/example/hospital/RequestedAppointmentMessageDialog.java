package com.example.hospital;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class RequestedAppointmentMessageDialog extends AppCompatDialogFragment {
    private DatabaseReference ref;
    private EditText writeMessage;
    private Button sendMessageBtn;
    String doctorid;
    private FirebaseUser user;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.requested_appointment_mesage_dialog,null);
        user= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference();
        writeMessage=view.findViewById(R.id.write_appointment_request_message);
        sendMessageBtn=view.findViewById(R.id.requested_appointment_msg_send_btn);
        Bundle bundle=getArguments();
        if(bundle!=null)
        {

            doctorid=bundle.getString("doctorid");

        }

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=writeMessage.getText().toString();
                writeMessage.setText("");
                sendAppointmentMessage(message);
            }
        });
        builder.setView(view).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        return builder.create();
    }

    private void sendAppointmentMessage(String message) {


        Map<String,Object> map1=new HashMap<>();
        map1.put("message",message);
        map1.put("request_type","send");

        final Map<String,Object> map2=new HashMap<>();
        map2.put("message",message);
        map2.put("request_type","received");


        ref.child("request_new_appointment_with_custom_message").child(user.getUid())
                .child(doctorid).setValue(map1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ref.child("request_new_appointment_with_custom_message").child(doctorid)
                                .child(user.getUid()).setValue(map2)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(getActivity(),"Request Send",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });



    }
}
