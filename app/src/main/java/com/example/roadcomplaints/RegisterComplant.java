package com.example.roadcomplaints;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

public class RegisterComplant extends AppCompatActivity {
    Button btn_send,btn_capture;
    EditText edt_description;

    //source
    private Uri mImageUri = null;

    private static final  int GALLERY_REQUEST =1;

    private static final int CAMERA_REQUEST_CODE=1;


    private ImageButton mProfileImage;
    Bitmap photo;
    private static final int CAMERA_REQ = 1;
    StorageReference mstorageRef;
    String userId;
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
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


//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, CAMERA_REQ);
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

        final String title_val = edt_description.getText().toString().trim();

        if( mImageUri != null){

            StorageReference filepath = mStorage.child("Blog_Images").child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                  // Uri downloadUrl =taskSnapshot.getDownloadUrl();
//
//                    DatabaseReference newPost = mDatabase.push();
//                    newPost.child("title").setValue(title_val);
//                    newPost.child("desc").setValue(desc_val);
//                    newPost.child("image").setValue(downloadUrl.toString());
//
//
//                    mProgress.dismiss();
//                    startActivity(new Intent(PostActivity.this,MainActivity.class));
                }
            });
        }


    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
//        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){
//
//
//
//            mImageUri = data.getData();
//            //String path =getRealPathFromURI(mImageUri);
//            //Log.d("Image url",path);
//           // Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
////            mSelectImage.setImageURI(mImageUri);
//
////            CropImage.activity(mImageUri)
////                    .setGuidelines(CropImageView.Guidelines.ON)
////                    .setAspectRatio(1,1)
////                    .start(this);
//
//
//
//
//        /* Bitmap mImageUri1 = (Bitmap) data.getExtras().get("data");
//         mSelectImage.setImageBitmap(mImageUri1);
//
//          Toast.makeText(this, "Image saved to:\n" +
//                  data.getExtras().get("data"), Toast.LENGTH_LONG).show();
//
//
//*/
//
//
//
//        }
//
////        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
////            CropImage.ActivityResult result = CropImage.getActivityResult(data);
////            if (resultCode == RESULT_OK) {
////                Uri resultUri = result.getUri();
////
////                mSelectImage.setImageURI(resultUri);
////                mImageUri = resultUri;
////
////            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
////                Exception error = result.getError();
////            }
////        }
//
//
//    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }



    public void submit(){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] b = stream.toByteArray();
        StorageReference storageReference =FirebaseStorage.getInstance().getReference().child("documentImages").child("noplateImg");
        //StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile_images").child(userID);
        storageReference.putBytes(b).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

//                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Task<Uri> downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                Toast.makeText(RegisterComplant.this, "uploaded", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterComplant.this,"failed",Toast.LENGTH_LONG).show();


            }
        });

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQ && resultCode ==RESULT_OK) {

            photo = (Bitmap) data.getExtras().get("data");
            submit();
        }
    }


}
