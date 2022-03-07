package com.example.myclub.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.myclub.Encryption.SymmtCrypto;
import com.example.myclub.R;
import com.example.myclub.activities.StudentHomeActivity;
import com.example.myclub.constant.VariableConstant;
import com.example.myclub.login.LoginActivity;
import com.example.myclub.studentAPI.StudentModule;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;

public class RegisterActivity extends Activity {
    private EditText edEmail, edPass, edName;
    public static final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        edEmail = findViewById(R.id.edit_email_register);
        edPass = findViewById(R.id.edit_password_register);
        edName = findViewById(R.id.edit_name_register);

    }

    public void onClickRegisterButton(View view) {

        Validation();
    }
    //Checks if the field is empty or not
    private void Validation() {
        String _Name = edName.getText().toString().trim();
        String _Email = edEmail.getText().toString().trim();
        String _Password = edPass.getText().toString().trim();

        if(!TextUtils.isEmpty(_Name)
                && !TextUtils.isEmpty(_Password)
                && !TextUtils.isEmpty(_Email)){
           //send info

            registerEmail(_Email, _Password, _Name);
        }else{
            if(TextUtils.isEmpty(_Name)){
                edName.setError("Student Name is required");
                return;
            }if (TextUtils.isEmpty(_Password)){
                edPass.setError("Student password is required");
                return;
            }
            if (TextUtils.isEmpty(_Email)){
                edEmail.setError("Student Email is required");
                return;
            }

        }
    }


    public void registerEmail(String email, String password, String name)
    {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>()
                {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(AuthResult authResult)
                    {
                        //    public StudentModule(String studentID, String studentName, String studentEmail, String studentPassword, String isAdvisor, String token, ArrayList<String> clubList) {
                        String isAdvisor = "student";
                        String token = Timestamp.now().toString();
                        ArrayList<String> clubList = new ArrayList<>();
                        String Id = email;
                        SymmtCrypto symmtCrypto = new SymmtCrypto();
                        StudentModule studentModule = null;
                        try {
                            studentModule = new StudentModule(Id, symmtCrypto.encrypt(name), symmtCrypto.encrypt(email), symmtCrypto.encrypt(password)
                                    , isAdvisor, token, clubList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        FirebaseFirestore.getInstance().collection("StudentData").document(Id).set(studentModule, SetOptions.merge())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(RegisterActivity.this, "Successful !",Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(RegisterActivity.this, StudentHomeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.putExtra("studentID", Id);
                                            intent.putExtra("email", email);
                                            intent.putExtra("password",password);
                                            intent.putExtra("name", name);
                                            intent.putExtra("role", isAdvisor);
                                            startActivity(intent);
                                            finish();
                                         }
                                        else{
                                            Toast.makeText(RegisterActivity.this, "Something went wrong !",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(RegisterActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();

                    }
                });
    }


    public void onClickLoginButton(View view) {

        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
