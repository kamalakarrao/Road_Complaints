package com.example.roadcomplaints;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class RegisterComplant extends AppCompatActivity {
    Button btn_send,btn_capture;
    EditText edt_description;

    //source
    private Uri mImageUri = null;

    private static final int CAMERA_REQUEST_CODE=1;


    private ImageButton mProfileImage;
    private static final int CAMERA_REQ = 1;
    StorageReference mstorageRef;
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;


    private StorageReference mStorage;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_complant);

        btn_capture = (Button) findViewById(R.id.btn_capture);
        btn_send = (Button) findViewById(R.id.btn_send);
        edt_description = (EditText) findViewById(R.id.edt_description);


        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");




        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("ProfileInfo");
        mAuth = FirebaseAuth.getInstance();

        btn_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                }
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startposting();
                Toast.makeText(RegisterComplant.this, "Update in firebase database !", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void startposting() {


        if( mImageUri != null){

            final StorageReference filepath = mStorage.child("Blog_Images").child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   //String downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                   //YOU HAVE TO WORK HERE TO GET IMAGE URL
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("ALERT", "onSuccess: uri= "+ uri.toString());
                            updateData(uri.toString());

                        }
                    });
//                   downloadUrl = taskSnapshot.getMetadata().getReference().getPath();
//                   getcompletePath(downloadUrl);
                  //  Toast.makeText(RegisterComplant.this, "Done", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    private void getcompletePath(String downloadUrl) {
        Task<Uri> storageReference = mStorage.child(downloadUrl).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Uri urlll = uri;
                Log.d("ALERT",uri.getPath().toString());
            }
        });
    }


    private void updateData(String downloadUrl) {
        Toast.makeText(RegisterComplant.this,"ff"+ downloadUrl, Toast.LENGTH_SHORT).show();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("urlll");

        myRef.setValue(downloadUrl);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_REQ && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(getApplicationContext(), photo);
             mImageUri = getImageUri(getApplicationContext(), photo);


        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}
