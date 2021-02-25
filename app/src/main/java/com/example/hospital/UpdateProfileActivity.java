package com.example.hospital;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.egl.EGLDisplay;
import de.hdodenhof.circleimageview.CircleImageView;
public class UpdateProfileActivity extends AppCompatActivity {
    private static final int GALLERY_PICK =101 ;
    private Spinner spinner;
    private String  imagefetchformfirebasestorge;
    private CircleImageView circleImageView;
    private EditText name;
    private ProgressBar progressBar;
    private  String speciality;
    private String profession;
    private String professionfromFirebase;
    private CheckBox patientCheckBox;
    private CheckBox doctorCheckBox;

    private Button completeProfileBtn;
    private Button updateProfileBtn;
    int radiobuttonid;
    Uri imageUri;
    Task<Uri> result;
    private FirebaseUser user;
    private StorageReference sref;
    private DatabaseReference ref;
    String fetchimagefromgallery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        user= FirebaseAuth.getInstance().getCurrentUser();
        sref= FirebaseStorage.getInstance().getReference();
        ref= FirebaseDatabase.getInstance().getReference();
        updateProfileBtn=findViewById(R.id.update_profile_btn);
        circleImageView=findViewById(R.id.update_profile_image);
        name=findViewById(R.id.update_profile_name);
        spinner=findViewById(R.id.spinner);
        patientCheckBox=findViewById(R.id.patient_check_box);
        doctorCheckBox=findViewById(R.id.doctor_check_box);


        progressBar=findViewById(R.id.profile_updation_progress);

        completeProfileBtn=findViewById(R.id.complete_profile_btn);

        fetchprofileformfirebase();
        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(this,R.array.doctor_speciality,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                speciality=adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                final String names=name.getText().toString();


                if(TextUtils.isEmpty(names) )
                {
                    if(TextUtils.isEmpty(names))
                    {
                        Toast.makeText(UpdateProfileActivity.this,"please choose your name",Toast.LENGTH_SHORT).show();
                    }



                }
                else {

                    if (TextUtils.isEmpty(fetchimagefromgallery)) {
                        Map<String, Object> map = new HashMap<>();
                        if (professionfromFirebase.equals("Doctor")) {

                            map.put("name",names);

                            ref.child("Users").child(user.getUid())
                                    .updateChildren(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            progressBar.setVisibility(View.INVISIBLE);
                                            AlertDialog.Builder builder=new AlertDialog.Builder(UpdateProfileActivity.this);;
                                            builder.setMessage("profile Updated");
                                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    startActivity(new Intent(UpdateProfileActivity.this,MainActivity.class));
                                                    finish();

                                                }
                                            });
                                            builder.show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    AlertDialog.Builder builder=new AlertDialog.Builder(UpdateProfileActivity.this);;
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
                        if(professionfromFirebase.equals("Patient"))
                        {
                            map.put("name",names);

                            ref.child("Users").child(user.getUid())
                                    .updateChildren(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            AlertDialog.Builder builder=new AlertDialog.Builder(UpdateProfileActivity.this);;
                                            builder.setMessage("profile Updated");
                                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    startActivity(new Intent(UpdateProfileActivity.this,MainActivity.class));

                                                    finish();
                                                }
                                            });
                                            builder.show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    AlertDialog.Builder builder=new AlertDialog.Builder(UpdateProfileActivity.this);;
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

                    }
                    else {

                        Map<String, Object> map = new HashMap<>();
                        if (professionfromFirebase.equals("Doctor")) {

                            map.put("name",names);

                            map.put("image",fetchimagefromgallery);
                            ref.child("Users").child(user.getUid())
                                    .updateChildren(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            AlertDialog.Builder builder=new AlertDialog.Builder(UpdateProfileActivity.this);;
                                            builder.setMessage("profile Updated");
                                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    startActivity(new Intent(UpdateProfileActivity.this,MainActivity.class));

                                                    finish();

                                                }
                                            });
                                            builder.show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    AlertDialog.Builder builder=new AlertDialog.Builder(UpdateProfileActivity.this);;
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
                        if(professionfromFirebase.equals("Patient"))
                        {
                            map.put("name",names);

                            map.put("image",fetchimagefromgallery);
                            ref.child("Users").child(user.getUid())
                                    .updateChildren(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            AlertDialog.Builder builder=new AlertDialog.Builder(UpdateProfileActivity.this);;
                                            builder.setMessage("profile Updated");
                                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    startActivity(new Intent(UpdateProfileActivity.this,MainActivity.class));

                                                    finish();
                                                }
                                            });
                                            builder.show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    AlertDialog.Builder builder=new AlertDialog.Builder(UpdateProfileActivity.this);;
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



                    }
                }


            }
        });

        completeProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                final String names = name.getText().toString();


                if(!doctorCheckBox.isChecked() && !patientCheckBox.isChecked())
                {
                    Toast.makeText(UpdateProfileActivity.this,"please choose  one profession",Toast.LENGTH_SHORT).show();
                }


                else {

                    if(patientCheckBox.isChecked())
                    {
                        profession=patientCheckBox.getText().toString();
                    }
                    if(doctorCheckBox.isChecked())
                    {
                        profession=doctorCheckBox.getText().toString();
                    }
                    if(patientCheckBox.isChecked() && doctorCheckBox.isChecked())
                    {
                        Toast.makeText(UpdateProfileActivity.this,"please choose only one profession",Toast.LENGTH_SHORT).show();
                    }
                }






                if(TextUtils.isEmpty(names) || TextUtils.isEmpty(profession)
                        || TextUtils.isEmpty(speciality) || TextUtils.isEmpty(fetchimagefromgallery))
                {

                    if(TextUtils.isEmpty(names))
                    {
                        name.requestFocus();
                        Toast.makeText(UpdateProfileActivity.this,"please choose your name",Toast.LENGTH_SHORT).show();

                    }
                    if(TextUtils.isEmpty(profession))
                    {
                        Toast.makeText(UpdateProfileActivity.this,"please choose your profession",Toast.LENGTH_SHORT).show();

                    }
                    if(TextUtils.isEmpty(fetchimagefromgallery))
                    {

                        Toast.makeText(UpdateProfileActivity.this,"please choose your image",Toast.LENGTH_SHORT).show();

                    }
                }
                else {

                    sref.child("Users_image").child(user.getUid()).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            if(taskSnapshot.getMetadata()!=null)
                            {
                                if(taskSnapshot.getMetadata().getReference()!=null)
                                {
                                    result=taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            imagefetchformfirebasestorge=uri.toString();

                                            if(profession.equals("Doctor"))
                                            {
                                                DoctorUserModel doctorUserModel=new DoctorUserModel(user.getUid(),imagefetchformfirebasestorge,names,profession,speciality);
                                                ref.child("Users").child(user.getUid()).setValue(doctorUserModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        AlertDialog.Builder builder=new AlertDialog.Builder(UpdateProfileActivity.this);
                                                        builder.setMessage("profile Updated");
                                                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                                Intent intent=new Intent(UpdateProfileActivity.this,MainActivity.class);
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
                                                        AlertDialog.Builder builder=new AlertDialog.Builder(UpdateProfileActivity.this);
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
                                            if(profession.equals("Patient"))
                                            {

                                                PatientUserModel patientUserModel=new PatientUserModel(names,profession,imagefetchformfirebasestorge,user.getUid());
                                                ref.child("Users").child(user.getUid())
                                                        .setValue(patientUserModel)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                progressBar.setVisibility(View.INVISIBLE);
                                                                AlertDialog.Builder builder=new AlertDialog.Builder(UpdateProfileActivity.this);
                                                                builder.setMessage("profile Updated");
                                                                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                                        Intent intent=new Intent(UpdateProfileActivity.this,MainActivity.class);
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
                                                        AlertDialog.Builder builder=new AlertDialog.Builder(UpdateProfileActivity.this);
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

                                        }
                                    });
                                }
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {



                        }
                    });

                }
            }
        });


    }

    private void fetchprofileformfirebase() {

        ref.child("Users").child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists() && snapshot.hasChild("name"))
                        {
                            professionfromFirebase=snapshot.child("profession").getValue().toString();
                            patientCheckBox.setVisibility(View.INVISIBLE);
                            doctorCheckBox.setVisibility(View.INVISIBLE);
                            patientCheckBox.setEnabled(false);
                            doctorCheckBox.setEnabled(false);
                            spinner.setVisibility(View.INVISIBLE);
                            updateProfileBtn.setVisibility(View.VISIBLE);
                            updateProfileBtn.setEnabled(true);

                            String names=snapshot.child("name").getValue().toString();
                            String images=snapshot.child("image").getValue().toString();
                            name.setText(names);
                            Picasso.get().load(images).into(circleImageView);

                        }
                        else {
                            completeProfileBtn.setVisibility(View.VISIBLE);
                            completeProfileBtn.setEnabled(true);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void chooseImage() {

        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_PICK);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_PICK && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            imageUri=data.getData();
            fetchimagefromgallery=imageUri.toString();
            Picasso.get().load(imageUri).into(circleImageView);
        }


    }
}