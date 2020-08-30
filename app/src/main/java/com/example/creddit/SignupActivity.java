package com.example.creddit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SignupActivity extends AppCompatActivity {

    TextView sign_login_txt;
    TextInputLayout textRegisterEmail, textRegisterPassword, textRegistercPassword;
    TextInputEditText editRegisterEmail, editRegisterPassword, editRegistercPassword;
    Button btnRegister;
    ProgressBar registerProgressBar;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    DatabaseReference mRef, mRef2;
    String uid;
    String currentDate;
    SimpleDateFormat sdf;
    int numberOfUsers;

    SharedPref sessionManager;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SharedPref(this);
        if (sessionManager.loadNightModeState() == true) {

            setTheme(R.style.darktheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_signup);

        sdf = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = sdf.format(new Date());

        sign_login_txt = findViewById(R.id.sign_login_txt);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mRef = firebaseDatabase.getReference("creddit").child("users");

        textRegisterEmail = findViewById(R.id.textRegistercUsername);
        textRegisterPassword = findViewById(R.id.textRegisterPassword);
        textRegistercPassword = findViewById(R.id.textRegistercPassword);

        editRegisterEmail = findViewById(R.id.sign_username);
        editRegisterPassword = findViewById(R.id.sign_password);
        editRegistercPassword = findViewById(R.id.sign_cpassword);

        btnRegister = findViewById(R.id.sign_btn_signup);
        registerProgressBar = findViewById(R.id.signUpProgressBar);

        FirebaseDatabase.getInstance().getReference("creddit").child("numberOfUsers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    numberOfUsers = dataSnapshot.getValue(Integer.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnRegister.setVisibility(View.GONE);
                registerProgressBar.setVisibility(View.VISIBLE);

                String email = editRegisterEmail.getText().toString().trim();
                String password = editRegisterPassword.getText().toString().trim();
                String c_password = editRegistercPassword.getText().toString().trim();

                if (email.isEmpty()) {
                    editRegisterEmail.setError("Please insert email");
                    btnRegister.setVisibility(View.VISIBLE);
                    registerProgressBar.setVisibility(View.GONE);

                } else if (password.isEmpty()) {
                    editRegisterPassword.setError("Please insert password");
                    btnRegister.setVisibility(View.VISIBLE);
                    registerProgressBar.setVisibility(View.GONE);

                } else if (c_password.isEmpty()) {
                    editRegistercPassword.setError("Please insert confirm password");
                    btnRegister.setVisibility(View.VISIBLE);
                    registerProgressBar.setVisibility(View.GONE);

                } else if (!email.matches(emailPattern)) {
                    editRegisterEmail.setError("Please enter valid email address");
                    btnRegister.setVisibility(View.VISIBLE);
                    registerProgressBar.setVisibility(View.GONE);

                } else if (password.length() < 6) {
                    editRegisterPassword.setError("Password length should be minimum of 6 digits");
                    btnRegister.setVisibility(View.VISIBLE);
                    registerProgressBar.setVisibility(View.GONE);

                } else {
                    if (password.equals(c_password)) {
                        Register(email, password);
                    } else {
                        Toast.makeText(getApplicationContext(), "Password does not matched", Toast.LENGTH_LONG).show();
                        btnRegister.setVisibility(View.VISIBLE);
                        registerProgressBar.setVisibility(View.GONE);
                    }
                }
            }
        });

        sign_login_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void Register(final String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "User Created", Toast.LENGTH_SHORT).show();
                            registerProgressBar.setVisibility(View.GONE);
                            btnRegister.setVisibility(View.VISIBLE);
                            editRegisterEmail.setText("");
                            editRegisterPassword.setText("");
                            editRegistercPassword.setText("");

                            uid = firebaseAuth.getCurrentUser().getUid();
                            mRef.child(uid).child("createdAt").setValue(currentDate);
                            mRef.child(uid).child("userNumber").setValue(numberOfUsers+1);
                            FirebaseDatabase.getInstance().getReference("creddit").child("numberOfUsers").setValue(numberOfUsers+1);

                            mRef.child(uid).child("showNSFW").setValue(0);
                            mRef.child(uid).child("blurNSFW").setValue(0);

                            firebaseAuth.signOut();

//                            String key = mRef.push().getKey();
//                            mRef2 = mRef.child(key);
//                            mRef2.child("email").setValue(email);
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "User Alerdy exist!!!", Toast.LENGTH_SHORT).show();
                                registerProgressBar.setVisibility(View.GONE);
                                btnRegister.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }

}
