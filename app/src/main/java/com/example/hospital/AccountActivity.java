package com.example.hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {
    private CircleImageView circleImageView;
    private TextView name;
    private TextView profession;
    private TextView speciality;
    private DatabaseReference ref;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        user= FirebaseAuth.getInstance().getCurrentUser();
        circleImageView=findViewById(R.id.account_image);
        name=findViewById(R.id.account_name);
        profession=findViewById(R.id.account_profession);
        speciality=findViewById(R.id.account_speciality);
        ref= FirebaseDatabase.getInstance().getReference();
        ref.child("Users").child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            String names=snapshot.child("name").getValue().toString();
                            String images=snapshot.child("image").getValue().toString();
                            String professions=snapshot.child("profession").getValue().toString();
                            Picasso.get().load(images).into(circleImageView);
                            name.setText(names);
                            profession.setText(professions);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}