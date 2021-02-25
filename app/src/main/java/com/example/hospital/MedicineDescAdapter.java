package com.example.hospital;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicineDescAdapter extends RecyclerView.Adapter<MedicineDescAdapter.ViewHolder> {
    List<MedicineModel> medicineModelList;
    String medicineid;
    String patientid;
    String doctorid;
    private DatabaseReference ref;
    private FirebaseUser user;
    public MedicineDescAdapter(List<MedicineModel> medicineModelList) {
        this.medicineModelList = medicineModelList;
    }

    @NonNull
    @Override
    public MedicineDescAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_medicine_desc,null);
        user= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MedicineDescAdapter.ViewHolder holder, int position) {
        final MedicineModel medicineModel=medicineModelList.get(position);
        medicineid=medicineModel.getMedicineid();
        patientid=medicineModel.getMedicineReceiverid();
        doctorid=medicineModel.getMedicineSenderid();
        holder.medicineName1.setText(medicineModel.getMedicine1());
        holder.medicineName2.setText(medicineModel.getMedicine2());
        holder.medicineName3.setText(medicineModel.getMedicine3());
        holder.medicineName4.setText(medicineModel.getMedicine4());
        holder.dieasesName1.setText(medicineModel.getDieases1());
        holder.dieasesName2.setText(medicineModel.getDieases2());
        holder.dieasesName3.setText(medicineModel.getDieases3());
        holder.dieasesName4.setText(medicineModel.getDieases4());
        holder.medicineStatus.setText(medicineModel.getMedicineStatus());
        ref.child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String profession=snapshot.child("profession").getValue().toString();
                if(profession.equals("Patient"))
                {

                    holder.chnageMedicineStatusCheckbox.setVisibility(View.INVISIBLE);
                    holder.okBtn.setVisibility(View.INVISIBLE);
                    holder.okBtn.setEnabled(false);


                }
                else {

                    if(medicineModel.getMedicineStatus().equals("expired"))
                    {

                        holder.chnageMedicineStatusCheckbox.setVisibility(View.INVISIBLE);
                        holder.okBtn.setVisibility(View.INVISIBLE);
                        holder.okBtn.setEnabled(false);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Map<String,Object> map=new HashMap<>();
                map.put("medicineStatus","expired");
                if(holder.chnageMedicineStatusCheckbox.isChecked())
                {
                    ref.child("medicine_description").child(user.getUid()).child(patientid).child(medicineid)
                            .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            ref.child("medicine_description").child(patientid).child(user.getUid()).child(medicineid)
                                    .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    ref.child("no_of_medicine").child(doctorid)
                                            .child(patientid).child("medicine_no").setValue("1")
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    ref.child("no_of_medicine").child(patientid)
                                                            .child(doctorid).child("medicine_no").setValue("1")
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    holder.chnageMedicineStatusCheckbox.setVisibility(View.INVISIBLE);
                                                                    holder.okBtn.setVisibility(View.INVISIBLE);
                                                                    holder.okBtn.setEnabled(false);
                                                                    Toast.makeText(view.getContext(),"Medicine updated",Toast.LENGTH_SHORT).show();

                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                        }
                                                    });
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
        return medicineModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView medicineName1;
        private TextView dieasesName1;
        private TextView dieasesName2;
        private TextView dieasesName3;
        private TextView dieasesName4;
        private TextView medicineName2;
        private TextView medicineName3;
        private TextView medicineName4;
        private CheckBox chnageMedicineStatusCheckbox;
        private Button okBtn;
        private TextView medicineStatus;
        private Button medicineFeedbackBtn;
        private TextView medicineFeedbackMsg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            medicineName1=itemView.findViewById(R.id.single_medicine1);
            medicineName2=itemView.findViewById(R.id.single_medicine2);
            medicineName3=itemView.findViewById(R.id.single_medicine3);
            medicineName4=itemView.findViewById(R.id.single_medicine4);
            dieasesName1=itemView.findViewById(R.id.single_dieases1);
            dieasesName2=itemView.findViewById(R.id.single_dieases2);
            dieasesName3=itemView.findViewById(R.id.single_dieases3);
            dieasesName4=itemView.findViewById(R.id.single_dieases4);
            chnageMedicineStatusCheckbox=itemView.findViewById(R.id.change_medicine_status_checkbox);
            okBtn=itemView.findViewById(R.id.ok_btn);
            medicineStatus=itemView.findViewById(R.id.single_medicine_status);

            medicineFeedbackMsg=itemView.findViewById(R.id.medicine_feedback_msg);


        }
    }
}

