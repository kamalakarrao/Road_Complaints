package com.example.roadcomplaints;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roadcomplaints.adapter.RecyclerViewAdapter;
import com.example.roadcomplaints.model.Complaints;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button complain;
    private FirebaseAuth mAuth;
    private boolean isAgent = false;

    RecyclerView recyclerView;

    DatabaseReference databaseReference;

    ProgressDialog progressDialog;

    List<Complaints> complaintsList = new ArrayList<>();


    RecyclerViewAdapter adapter;

    ConstraintLayout c_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));


        complain = (Button) findViewById(R.id.complain);

        c_layout = (ConstraintLayout) findViewById(R.id.c_layout);

        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("complaints");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot data) {
                complaintsList.clear();
                for (DataSnapshot dataSnapshot : data.getChildren()) {
                    try {
                        String complaintId = dataSnapshot.getKey();
                        String description = dataSnapshot.child("description").getValue().toString();
                        String status = dataSnapshot.child("status").getValue().toString();
                        String image = dataSnapshot.child("image_url").getValue().toString();
                        String agentId = "";
                        Complaints complaints = new Complaints(complaintId, image, description, status, agentId);

                        complaintsList.add(complaints);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (complaintsList.size()<1){
                    recyclerView.setVisibility(View.GONE);
                }else {
                    recyclerView.setVisibility(View.VISIBLE);
                }


                adapter = new RecyclerViewAdapter(MainActivity.this, complaintsList);

                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

// Write a message to the database
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("message");
//
//        myRef.setValue("Testing!");
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Snackbar.make(view, "Logged Out", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
*/  Intent intent = new Intent(MainActivity.this,RegisterComplant.class);
                startActivity(intent);

              //  FirebaseAuth.getInstance().signOut();
            }
        });
    }

    private String getUuid(){
       return FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
    }

    private boolean checkIfAgent(){

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("UserId").child(getUuid()).child("type");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if (dataSnapshot.getValue().toString().equals("Agent")){
                        isAgent = true;
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //myRef.setValue("Hello, World!");
        return isAgent;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
       // updateUI(currentUser);
        if (currentUser==null){
            Toast.makeText(this, "Currentuser is null", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
        }else {

            Toast.makeText(this, "Logged in Agent", Toast.LENGTH_SHORT).show();
            if (isAgent){

            }else {
                Toast.makeText(this, "Logged in as Citizen", Toast.LENGTH_SHORT).show();


            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {

            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(getIntent());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
