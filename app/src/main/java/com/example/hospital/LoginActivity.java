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
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText writeEmail;
    private EditText writePassword;
    private Button loginBtn;
    private Button dontHaveAccoutnBtn;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.login_progress);
        writeEmail=findViewById(R.id.write_login_email);
        writePassword=findViewById(R.id.write_login_password);
        dontHaveAccoutnBtn=findViewById(R.id.dont_have_account_btn);
        loginBtn=findViewById(R.id.login_btn);
        dontHaveAccoutnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                final AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
                String email=writeEmail.getText().toString();
                final String password=writePassword.getText().toString();


                auth.signInWithEmailAndPassword(email,password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                progressBar.setVisibility(View.INVISIBLE);
                                builder.setMessage("login successful");
                                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);

                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                builder.show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        builder.setMessage(e.getMessage());
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