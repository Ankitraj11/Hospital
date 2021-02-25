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

public class MedicineDescDialog extends AppCompatDialogFragment {
    private EditText dieases1;
    private EditText dieases2;
    private EditText dieases3;
    private EditText dieases4;
    private EditText medicine1;
    private EditText medicine2;
    private EditText medicine3;
    private EditText medicine4;
    private DatabaseReference ref;
    private FirebaseUser user;
    private String medicineStatus="not_expired";
    private Button prescribeMedicineBtn;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.medicine_desc, null);
        dieases1 = view.findViewById(R.id.dieases1);
        medicine1 = view.findViewById(R.id.medicine1);
        dieases2=view.findViewById(R.id.dieases2);
        dieases3=view.findViewById(R.id.dieases3);
        dieases4=view.findViewById(R.id.dieases4);
        medicine2=view.findViewById(R.id.medicine2);
        medicine3=view.findViewById(R.id.medicine3);
        medicine4=view.findViewById(R.id.medicine4);


        prescribeMedicineBtn = view.findViewById(R.id.prescribe_medicine);
        ref= FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        prescribeMedicineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = getArguments();


                final String patientidfromMyappointmentActivity = bundle.getString("patientid");

                String medicineName1 = medicine1.getText().toString();
                String medicineName2 = medicine2.getText().toString();
                String medicineName3 = medicine3.getText().toString();
                String medicineName4 = medicine4.getText().toString();
                String dieasesName1 = dieases1.getText().toString();
                String dieasesName2 = dieases2.getText().toString();
                String dieasesName3 = dieases3.getText().toString();
                String dieasesName4 = dieases4.getText().toString();

                medicine1.setText("");
                medicine2.setText("");
                medicine3.setText("");
                medicine4.setText("");
                dieases1.setText("");
                dieases2.setText("");
                dieases3.setText("");
                dieases4.setText("");

                final String medicineId = ref.child("medicine_description").child(user.getUid()).child(patientidfromMyappointmentActivity)
                        .push().getKey();
                final MedicineModel medicineModel = new MedicineModel(medicineId,medicineStatus, user.getUid(), patientidfromMyappointmentActivity,medicineName1,medicineName2,medicineName3,medicineName4,dieasesName1,dieasesName2,dieasesName3,dieasesName4);
                ref.child("medicine_description").child(user.getUid())
                        .child(patientidfromMyappointmentActivity).child(medicineId)
                        .setValue(medicineModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ref.child("medicine_description").child(patientidfromMyappointmentActivity)
                                .child(user.getUid()).child(medicineId)
                                .setValue(medicineModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(getActivity(), "Medicine Send", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {


                    }
                });

            }
        });
        builder.setTitle("Give medicine").setView(view).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });
        return builder.create();
    }
}







