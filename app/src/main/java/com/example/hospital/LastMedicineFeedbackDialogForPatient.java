package com.example.hospital;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class LastMedicineFeedbackDialogForPatient extends AppCompatDialogFragment {
    private EditText writeMedicineFeedback;
    private Button sendFeedbackBtn;
    private FirebaseUser user;
    String  doctorid;
    int radioid;
    String feedbackSign;
    private CheckBox negativeCheckBox;
    private CheckBox positiveCheckBox;
    private DatabaseReference ref;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.medicine_feedback_dialog_for_patient, null);
        ref = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        writeMedicineFeedback = view.findViewById(R.id.medicine_feedback_dialog_msg);
        negativeCheckBox = view.findViewById(R.id.negative_feedback_check_box);
        positiveCheckBox = view.findViewById(R.id.positive_feedback_check_box);


        sendFeedbackBtn = view.findViewById(R.id.send_medicine_feedback_dialog_btn);
        sendFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String feedback = writeMedicineFeedback.getText().toString();
                writeMedicineFeedback.setText("");

                if (!positiveCheckBox.isChecked() && !negativeCheckBox.isChecked()) {
                    Toast.makeText(getActivity(), "please choose one options", Toast.LENGTH_SHORT).show();
                }
                else if(positiveCheckBox.isChecked() && negativeCheckBox.isChecked())
                {
                    Toast.makeText(getActivity(), "you can select only one option", Toast.LENGTH_SHORT).show();
                }
                else {

                    if(positiveCheckBox.isChecked() )
                    {
                        feedbackSign="positive";
                    }
                    if(negativeCheckBox.isChecked())
                    {
                        feedbackSign="negative";
                    }
                    sendFeedbacktodocotr(feedback, feedbackSign);
                }
            }
        });

        builder.setView(view).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        return builder.create();
    }
    private void sendFeedbacktodocotr(String feedback,String feedbacksign) {
        Bundle bundle=getArguments();
        if(bundle!=null)
        {

            doctorid=bundle.getString("doctorid");
        }



        final String feedbackid=ref.child("feedback").child(user.getUid()).child(doctorid)
                .push().getKey();
        final MedicineFeedbackModel medicineFeedbackModel=new MedicineFeedbackModel(feedback,feedbacksign,user.getUid(),doctorid,feedbackid);
        ref.child("feedback").child(user.getUid())
                .child(doctorid).child(feedbackid).setValue(medicineFeedbackModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ref.child("feedback").child(doctorid)
                                .child(user.getUid()).child(feedbackid).setValue(medicineFeedbackModel)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(getActivity(),"feedback send",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                });


    }
}
