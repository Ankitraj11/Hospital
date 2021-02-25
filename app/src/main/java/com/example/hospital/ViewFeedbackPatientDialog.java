package com.example.hospital;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

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

import java.util.ArrayList;
import java.util.List;

public class ViewFeedbackPatientDialog extends AppCompatDialogFragment {
    private RecyclerView recyclerView;
    private List<MedicineFeedbackModel> medicineFeedbackModelList;
    private FeedbackPatientAdapter adapter;
    private FirebaseUser user;
    private DatabaseReference ref;
    String doctorid;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.feedback_list_patient,null);
        recyclerView=view.findViewById(R.id.patient_feedback_list_recyclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        medicineFeedbackModelList=new ArrayList<>();
        adapter=new FeedbackPatientAdapter( medicineFeedbackModelList);
        recyclerView.setAdapter(adapter);

        ref= FirebaseDatabase.getInstance().getReference();
        user= FirebaseAuth.getInstance().getCurrentUser();
        Bundle bundle=getArguments();
        if(bundle!=null)
        {
            doctorid=bundle.getString("doctorid");


        }

        showpatientfeedbackList();


        builder.setView(view).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return builder.create();
    }

    private void showpatientfeedbackList() {

        ref.child("feedback").child(user.getUid()).child(doctorid)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        if(snapshot.exists())
                        {
                            MedicineFeedbackModel medicineFeedbackModel=snapshot.getValue(MedicineFeedbackModel.class);
                            medicineFeedbackModelList.add(medicineFeedbackModel);
                            adapter.notifyDataSetChanged();
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
