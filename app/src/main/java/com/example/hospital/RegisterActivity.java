package com.example.hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText writeEmail;
    private EditText writePassword;
    private Button reisterBtn;
    private FirebaseAuth auth;
    private ProgressBar registraionProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth=FirebaseAuth.getInstance();
        writeEmail=findViewById(R.id.write_email);
        writePassword=findViewById(R.id.write_password);
        reisterBtn=findViewById(R.id.register_btn);
        registraionProgress=findViewById(R.id.registration_progress);
        reisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                registraionProgress.setVisibility(View.VISIBLE);
                String email=writeEmail.getText().toString();
                String password=writePassword.getText().toString();
                auth.createUserWithEmailAndPassword(email,password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                registraionProgress.setVisibility(View.INVISIBLE);
                                AlertDialog.Builder builder=new AlertDialog.Builder(RegisterActivity.this);
                                builder.setCancelable(false);
                                builder.setMessage("Regsitration successful");
                                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                    }
                                });
                                builder.show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        registraionProgress.setVisibility(View.INVISIBLE);
                        AlertDialog.Builder builder=new AlertDialog.Builder(RegisterActivity.this);
                        builder.setCancelable(false);
                        builder.setMessage("Regsitration successful");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                            }
                        });
                        builder.show();
                    }
                });
            }
        });
    }
}