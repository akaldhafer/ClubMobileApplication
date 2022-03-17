package com.example.myclub.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.myclub.Encryption.SymmtCrypto;
import com.example.myclub.R;

import com.example.myclub.activities.AdminHomeActivity;
import com.example.myclub.activities.AdvisorHomeActivity;
import com.example.myclub.activities.MainActivity;
import com.example.myclub.activities.StudentHomeActivity;
import com.example.myclub.register.RegisterActivity;
import com.example.myclub.studentAPI.StudentModule;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LoginActivity extends Activity {
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    EditText emailEdit, passEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEdit = findViewById(R.id.edit_email_login);
        passEdit = findViewById(R.id.edit_password_login);

    }
    public void loginEmail(String email, String password)
    {

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>()
                {
                    @Override
                    public void onSuccess(AuthResult authResult)
                    {
                        if(firebaseAuth.getCurrentUser()!=null) {
                            FirebaseFirestore.getInstance().collection("StudentData")
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for(int i = 0; i< task.getResult().size();i++){
                                            try {
                                                //decrypt the data
                                                SymmtCrypto symmtCrypto = new SymmtCrypto();
                                                // fetch the data
                                                String studentID = task.getResult().getDocuments().get(i).getString("studentID");
                                                String studentName = symmtCrypto.decrypt(task.getResult().getDocuments().get(i).getString("studentName"));
                                                String studentEmail = symmtCrypto.decrypt(task.getResult().getDocuments().get(i).getString("studentEmail"));
                                                String studentPassword = symmtCrypto.decrypt(task.getResult().getDocuments().get(i).getString("studentPassword"));
                                                String isAdvisor = task.getResult().getDocuments().get(i).getString("isAdvisor");
                                                String token = task.getResult().getDocuments().get(i).getString("token");
                                                ArrayList<String> clubList =(ArrayList<String>) task.getResult().getDocuments().get(i).get("clubList");

                                                if(studentEmail.equals(email) && isAdvisor.equals("admin")){
                                                    Toast.makeText(LoginActivity.this, "Welcome Back !",Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
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
                                                else if(studentEmail.equals(email) && isAdvisor.equals("advisor")){
                                                    Toast.makeText(LoginActivity.this, "Welcome Back !",Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(LoginActivity.this, AdvisorHomeActivity.class);
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
                                                else if(studentEmail.equals(email) && isAdvisor.equals("student")){
                                                    Toast.makeText(LoginActivity.this, "Welcome Back !",Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(LoginActivity.this, StudentHomeActivity.class);
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
                                                else{
                                                    Toast.makeText(LoginActivity.this, "Please register again",Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Toast.makeText(LoginActivity.this, "Please register again",Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Please register again",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                 }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(LoginActivity.this, "Wrong email or password !",Toast.LENGTH_LONG).show();

                    }
                });
    }

    public void onClickLoginButton(View view) {
        String email = emailEdit.getText().toString().trim();
        String password = passEdit.getText().toString().trim();

                if (TextUtils.isEmpty(email))
                {
                    emailEdit.setError("Please enter a valid email");
                    return;
                }

                if (TextUtils.isEmpty(password))
                {
                    passEdit.setError("Please enter password");
                    return;
                }
                else
                {
                    loginEmail(email, password);
                }
    }

    public void onClickNewAccountButton(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
