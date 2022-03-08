package com.example.myclub.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myclub.R;
import com.example.myclub.postAPI.PostModule;
import com.example.myclub.postAPI.PostSendData;
import com.example.myclub.postAPI.PostViewMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.sql.Timestamp;
import java.util.ArrayList;

public class CreatePostActivity extends Activity {
    private String studentID="", studentEmail="",studentPassword="",studentName="", isAdvisor="";
    ArrayList<String> clubList= new ArrayList<>();
    EditText edTitle, edDesc;
    private String sImageUri;
    private ImageView imageView;
    public Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference storageReference;
    private StorageTask uploadtask;
    private static final String TAG = "createPost";

    ProgressDialog mProgressDialog;
//    PostSendData postSendData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post_activity);

        //get the current user info
        studentID = getIntent().getStringExtra("studentID");
        studentEmail = getIntent().getStringExtra("email");
        studentPassword = getIntent().getStringExtra("password");
        isAdvisor = getIntent().getStringExtra("role");
        studentName = getIntent().getStringExtra("name");
        clubList = getIntent().getStringArrayListExtra("clublist");

        //link to ui elements
        edTitle = findViewById(R.id.edPostTitle);
        edDesc = findViewById(R.id.edPostDesc);

        imageView = (ImageView) findViewById(R.id.pickImage);


        storageReference = FirebaseStorage.getInstance().getReference("PostImages");

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait, Sending Post Form..");

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(CreatePostActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CreatePostActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                } else {
                    PickerImage();
                }
            }
        });

    }
    private void uploadFile(){

        if(imageUri != null){
            Log.d(TAG, "uploadfile: getLastPathSegment type " + imageUri.getLastPathSegment());
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("Uploading the image...");
            pd.show();
            StorageReference fileReference =
                    storageReference.child(imageUri.getLastPathSegment());
            uploadtask = fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    pd.dismiss();
                                    sImageUri = uri.toString();
                                    Log.d(TAG, "uploadFile: url will be upload " + sImageUri);
                                    checkSignUpDetails(sImageUri);
                                    Toast.makeText(CreatePostActivity.this, "Image Upload successful", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreatePostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0* snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                            pd.setMessage("Progress: "+ (int) progress + "%");
                        }
                    });

        }else{
            Toast.makeText(this, "No Image selected", Toast.LENGTH_LONG).show();
        }
    }
    private void initCreateEvent( String postID, String postTitle, String postDescription,
                                  String postImgUrl, String clubID,String advisorEmail,
                                  String isApproved, String token) {
        mProgressDialog.show();
//        postSendData.onSuccessUpdate(CreatePostActivity.this, postID,postTitle,postDescription,
//                 postImgUrl,clubID, advisorEmail,
//                 isApproved,token);
        PostModule postModule = new PostModule(postID, postTitle, postDescription, postImgUrl, clubID, advisorEmail,isApproved, token);

        FirebaseFirestore.getInstance().collection("PostData").document(token).set(postModule, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            onUpdateSuccess("Post sent successfully!");
                        }else{
                            onUpdateFailure("Something went wrong!");
                        }
                    }
                });
    }
    private void checkSignUpDetails(String imageuri) {
        String Status = "pending";
        String title = edTitle.getText().toString().trim();
        String Description = edDesc.getText().toString().trim();

        Log.d(TAG, "checkdetails: url before upload " + imageuri);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String id = timestamp.toString().trim();

        if(!TextUtils.isEmpty(title)
                && !TextUtils.isEmpty(Description) && !TextUtils.isEmpty(imageuri)){
            System.out.println(id+title+Description+clubList.get(0)+studentEmail+Status+id);
            initCreateEvent(id, title, Description, imageuri, clubList.get(0).toString(),studentEmail,Status, id );
        }else{
            if(TextUtils.isEmpty(title)){
                edTitle.setError("Event Title is required");
                return;
            }if (TextUtils.isEmpty(Description)){
                edDesc.setError("Event Description is required");
                return;
            }
            if (TextUtils.isEmpty(imageuri)){
                Toast.makeText(CreatePostActivity.this, "Image is Required", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                imageUri = result.getUri();
                Log.d(TAG, "onActivityResult: url is " + imageUri);
                Picasso.with(CreatePostActivity.this).load(result.getUri()).fit().into(imageView);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d(TAG, "onActivityResult: " + error.getMessage().toString());
            }
        }
    }
    private void PickerImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(CreatePostActivity.this);

    }


    public void onCreatePostClick(View view) {
        if(uploadtask != null && uploadtask.isInProgress()){
            Toast.makeText(CreatePostActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
        }
        else{

            uploadFile();
        }

    }

    public void onClickViewPostButton(View view) {

        Intent intent = new Intent(CreatePostActivity.this, ViewClubActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("studentID", studentID);
        intent.putExtra("email", studentEmail);
        intent.putExtra("password",studentPassword);
        intent.putExtra("name", studentName);
        intent.putExtra("advisorEmail",studentEmail);
        intent.putExtra("role", isAdvisor);
        intent.putStringArrayListExtra("clublist", clubList);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CreatePostActivity.this, AdvisorHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("studentID", studentID);
        intent.putExtra("email", studentEmail);
        intent.putExtra("password",studentPassword);
        intent.putExtra("name", studentName);
        intent.putExtra("role", isAdvisor);
        intent.putStringArrayListExtra("clublist", clubList);
        startActivity(intent);
        finish();
    }

    public void onUpdateFailure(String message) {
        mProgressDialog.dismiss();
        Toast.makeText(CreatePostActivity.this, message, Toast.LENGTH_LONG).show();
    }

    public void onUpdateSuccess(String message) {
        mProgressDialog.dismiss();
        Toast.makeText(CreatePostActivity.this, message, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(CreatePostActivity.this, AdvisorHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("studentID", studentID);
        intent.putExtra("email", studentEmail);
        intent.putExtra("password",studentPassword);
        intent.putExtra("name", studentName);
        intent.putExtra("role", isAdvisor);
        intent.putStringArrayListExtra("clublist", clubList);
        startActivity(intent);
        finish();

    }
}
