package com.example.hospital;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

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

public class CommentActivity extends AppCompatActivity {
    private CircleImageView circleImageView;
    private TextView name;
    String docotrid;
    private List<CommentModel> commentModelList;
    private CommentAdapter commentAdapter;
    private RecyclerView commentRecyclerview;
    private DatabaseReference ref;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        name=findViewById(R.id.comment_profile_name);

        docotrid=getIntent().getStringExtra("doctorid");
        circleImageView=findViewById(R.id.comment_profile_image);
        ref= FirebaseDatabase.getInstance().getReference();
        user= FirebaseAuth.getInstance().getCurrentUser();
        commentRecyclerview=findViewById(R.id.comment_recyclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(CommentActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentRecyclerview.setLayoutManager(layoutManager);

        commentModelList=new ArrayList<>();
        commentAdapter =new CommentAdapter(commentModelList);
        commentRecyclerview.setAdapter(commentAdapter);
        showDoctor();


        ref.child("comments").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if(snapshot.exists())
                {
                    CommentModel commentModel=snapshot.getValue(CommentModel.class);

                    if(commentModel.getReceiverid().equals(docotrid)) {

                        commentModelList.add(commentModel);
                        commentAdapter.notifyDataSetChanged();
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

    private void showDoctor() {



        ref.child("Users").child(docotrid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DoctorUserModel doctorUserModel=snapshot.getValue(DoctorUserModel.class);
                String names=doctorUserModel.getName();
                String image=doctorUserModel.getImage();
                Picasso.get().load(image).into(circleImageView);
                name.setText(names);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}