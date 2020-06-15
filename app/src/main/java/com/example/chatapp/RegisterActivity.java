package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText name, email, password;
    private DatabaseReference mRootRef;
    private Button registerButton;
    private FirebaseAuth mAuth;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.nameEditTextRegAct);
        email = findViewById(R.id.emailEditTextRegAct);
        password = findViewById(R.id.passwordEditTextRegAct);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        registerButton = findViewById(R.id.registerButtonRegisterAct);
        mAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(this);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtName = name.getText().toString();
                String txtEmail = email.getText().toString();
                String txtPassword = password.getText().toString();

                registerUser(txtName, txtEmail, txtPassword);

            }
        });
    }
    private void registerUser(final String Name, final String Email, String Password) {
        if ( !validatePassword() | !validateEmail()) {
            return;
        }
        pd.setMessage("Please Wait");
        pd.show();
        mAuth.createUserWithEmailAndPassword(Email, Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("name", Name);
                map.put("email", Email);
                map.put("imageurl","default");
                map.put("id", mAuth.getCurrentUser().getUid());
                mRootRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), "Welcome to ChatApp", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private Boolean validateEmail() {
        String val = email.getText().toString();
        String expression = "^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(val);
        if (val.isEmpty()) {
            email.setError("Field Can't be empty");
            return false;
        } else if (matcher.matches()) {
            email.setError(null);
            return true;
        } else {
            email.setError("Enter Valid Email Address");
            return false;
        }
    }

    private Boolean validatePassword() {
        String val = password.getText().toString();
        if (val.isEmpty()) {
            password.setError("Field Can't be empty");
            return false;
        } else if (val.length() < 6) {
            password.setError("Password too short");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }
}
