package com.example.hospital;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedbackDialogForDoctor extends AppCompatDialogFragment {
    private CircleImageView circleImageView;
    private TextView name;
    private FeedbackPatientAdapter feedbackPatientAdapter;
    private List<MedicineFeedbackModel> medicineFeedbackModelList;
    private RecyclerView recyclerView;
    private TextView message;
    private DatabaseReference ref;
    private FirebaseUser user;
    String patientid;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater  inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.medicine_feedback_for_doctor,null);
        circleImageView=view.findViewById(R.id.doctor_feedback_image);
        recyclerView=view.findViewById(R.id.doctor_feedback_recyclerview);
        medicineFeedbackModelList=new ArrayList<>();
        feedbackPatientAdapter=new FeedbackPatientAdapter(medicineFeedbackModelList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(feedbackPatientAdapter);
        user= FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference();
        Bundle bundle=getArguments();
        if(bundle!=null)
        {
            patientid=bundle.getString("patientid");


        }

        displayfeedback();

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });
        builder.setView(view);
        return builder.create();
    }

    private void displayfeedback() {
        ref.child("feedback").child(user.getUid())
                .child(patientid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                MedicineFeedbackModel medicineFeedbackModel=snapshot.getValue(MedicineFeedbackModel.class);
                medicineFeedbackModelList.add(medicineFeedbackModel);
                feedbackPatientAdapter.notifyDataSetChanged();




                ref.child("Users").child(patientid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        PatientUserModel patientUserModel=snapshot.getValue(PatientUserModel.class);
                        String image=patientUserModel.getImage();
                        Picasso.get().load(image).into(circleImageView);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

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
