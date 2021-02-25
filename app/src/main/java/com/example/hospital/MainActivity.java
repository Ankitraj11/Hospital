package com.example.hospital;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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

public class MainActivity extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseAuth auth;
    private List<PatientUserModel> patientUserModelList;
    private PatientListAdapter patientListAdapter;
    private RecyclerView recyclerView;
    private List<DoctorUserModel> doctorUserModelList;
    private DoctorListAdapter doctorListAdapter;

    private DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user=FirebaseAuth.getInstance().getCurrentUser();
        auth=FirebaseAuth.getInstance();

        recyclerView =findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager=new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        ref= FirebaseDatabase.getInstance().getReference();
        if(user!=null)
        {
            fetchuserlist();
        }
        else{






            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void fetchuserlist() {

        ref.child("Users").child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()&& snapshot.hasChild("profession"))
                        {

                            String currentUserprofession=snapshot.child("profession").getValue().toString();
                            if(currentUserprofession.equals("Doctor"))
                            {
                                patientUserModelList=new ArrayList<>();

                                patientListAdapter=new PatientListAdapter(patientUserModelList);
                                recyclerView.setAdapter(patientListAdapter);
                                ref.child("Users").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                        if(snapshot.exists())
                                        {
                                            String userprofession=snapshot.child("profession").getValue().toString();
                                            if(userprofession.equals("Patient"))
                                            {

                                                PatientUserModel patientUserModel=snapshot.getValue(PatientUserModel.class);
                                                patientUserModelList.add(patientUserModel);

                                                patientListAdapter.notifyDataSetChanged();


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
                            if(currentUserprofession.equals("Patient"))
                            {
                                doctorUserModelList=new ArrayList<>();
                                doctorListAdapter=new DoctorListAdapter(doctorUserModelList);
                                recyclerView.setAdapter(doctorListAdapter);

                                ref.child("Users").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        if(snapshot.exists())
                                        {

                                            String profession=snapshot.child("profession").getValue().toString();
                                            if(profession.equals("Doctor"))
                                            {

                                                DoctorUserModel doctorUserModel=snapshot.getValue(DoctorUserModel.class);
                                                doctorUserModelList.add(doctorUserModel);

                                                doctorListAdapter.notifyDataSetChanged();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.three_dot,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.log_out)
        {
            auth.signOut();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }

        if(item.getItemId()==R.id.update_account)
        {

            startActivity(new Intent(MainActivity.this,UpdateProfileActivity.class));

        }

        if(item.getItemId()==R.id.my_appointment)
        {

            startActivity(new Intent(MainActivity.this,MyAppointmentActivity.class));

        }
        if(item.getItemId()==R.id.requested_appointment)
        {

            startActivity(new Intent(MainActivity.this,AppointmentRequestActivity.class));

        }
        if(item.getItemId()==R.id.my_account)
        {

            startActivity(new Intent(MainActivity.this,AccountActivity.class));

        }
        return true;
    }
}