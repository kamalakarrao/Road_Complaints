package com.example.roadcomplaints;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Button loginbutton;
    EditText editTextEmail , editTextPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        loginbutton = (Button) findViewById(R.id.loginbutton);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String email = editTextEmail.getText().toString();
               String password = editTextPassword.getText().toString();


               mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()) {

                       Log.d("ALERT", "signInWithEmail:success");
                       FirebaseUser user = mAuth.getCurrentUser();
                       if (user!=null){
                           // Write a message to the database
                           // Write a message to the database
                           FirebaseDatabase database = FirebaseDatabase.getInstance();
                           DatabaseReference myRef = database.getReference().child("UserId").child(getUuid());

                           myRef.child("uid").setValue(getUuid());
                           myRef.child("type").setValue("citizen");
                           myRef.child("email").setValue(getEmail());

                           finish();
                           Toast.makeText(LoginActivity.this, user.getEmail(), Toast.LENGTH_SHORT).show();
                       }
                   }else {
                           Toast.makeText(LoginActivity.this, "Please Enter Valid Details", Toast.LENGTH_SHORT).show();
                       }
                   }

               });

              /*  mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("ALERT", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                   // updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("ALERT", "signInWithEmail:failure", task.getException());

                                    Toast.makeText(LoginActivity.this, "Auth FAiled", Toast.LENGTH_SHORT).show();


                                   // updateUI(null);
                                }

                                // ...
                            }
                        });
*/
            }
        });
    }



    private String getUuid(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
    }
    private String getEmail(){
        return FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
    }



}
