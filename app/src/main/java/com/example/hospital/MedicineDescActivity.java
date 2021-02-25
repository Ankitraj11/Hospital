package com.example.hospital;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MedicineDescActivity extends AppCompatActivity {
    private List<MedicineModel> medicineModelList;
    private  MedicineDescAdapter medicineDescAdapter;
    private RecyclerView recyclerView;
    private FirebaseUser user;
    private DatabaseReference ref;
    String currentUserProfession;
    String patientid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_desc);
        ref= FirebaseDatabase.getInstance().getReference();
        user= FirebaseAuth.getInstance().getCurrentUser();
        LinearLayoutManager layoutManager=new LinearLayoutManager(MedicineDescActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView=findViewById(R.id.medicine_desc_recyclerview);
        recyclerView.setLayoutManager(layoutManager);

        ref.child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUserProfession = snapshot.child("profession").getValue().toString();
                if (currentUserProfession.equals("Doctor")) {

                    medicineModelList = new ArrayList<>();
                    medicineDescAdapter = new MedicineDescAdapter(medicineModelList);
                    recyclerView.setAdapter(medicineDescAdapter);
                    patientid = getIntent().getStringExtra("patientid");
                    if (!TextUtils.isEmpty(patientid)) {
                        ref.child("medicine_description").child(user.getUid()).child(patientid)
                                .addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        if (snapshot.exists()) {
                                            MedicineModel medicineModel = snapshot.getValue(MedicineModel.class);
                                            if (medicineModel.getMedicineSenderid().equals(user.getUid())
                                                    && medicineModel.getMedicineReceiverid().equals(patientid)) {

                                                medicineModelList.add(medicineModel);
                                                medicineDescAdapter.notifyDataSetChanged();


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

                    }
                }

                if(currentUserProfession.equals("Patient")) {
                    final String doctorid = getIntent().getStringExtra("doctorid");
                    if (!TextUtils.isEmpty(doctorid)) {
                        ref.child("medicine_description").child(user.getUid())
                                .child(doctorid).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                if (snapshot.exists()) {

                                    MedicineModel medicineModel = snapshot.getValue(MedicineModel.class);
                                    if (medicineModel.getMedicineSenderid().equals(doctorid) && medicineModel.getMedicineReceiverid().equals(user.getUid())) {

                                        medicineModelList = new ArrayList<>();
                                        medicineDescAdapter = new MedicineDescAdapter(medicineModelList);
                                        recyclerView.setAdapter(medicineDescAdapter);
                                        medicineModelList.add(medicineModel);
                                        medicineDescAdapter.notifyDataSetChanged();
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


                    }
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }




}
