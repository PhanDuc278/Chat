package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    Button btn_login;
    ProgressBar progressBar ;
    FirebaseAuth auth;
    TextView forgot_password;
    FirebaseUser user ;
    TextView txt_SignUp ;

    String getEmail , getPass ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        progressBar.setVisibility(View.INVISIBLE);

        email.setText(getEmail);
        password.setText(getPass);

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        btn_login.setOnClickListener(view->{
            String txt_email = email.getText().toString();
            String txt_password = password.getText().toString();

            if(txt_email.isEmpty()){
                email.setError("Email is empty !");
            }
            else if(!txt_email.matches(Util.emailPattern)){
                email.setError("Email is invalid !");
            }
            else if(txt_password.isEmpty()){
                password.setError("Pass is empty !");
            }
            else {
                loginWithEmailAndPassWord(txt_email , txt_password);
                progressBar.setVisibility(View.VISIBLE);
            }

        });

        txt_SignUp.setOnClickListener(view->{
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });


    }

    private void loginWithEmailAndPassWord(String txt_email, String txt_password) {
        auth.signInWithEmailAndPassword(txt_email , txt_password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initViews() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        forgot_password = findViewById(R.id.forgot_password);
        txt_SignUp = findViewById(R.id.txt_SignUp);
        progressBar = findViewById(R.id.progress_Login);
        getEmail = getIntent().getStringExtra("_email");
        getPass = getIntent().getStringExtra("_pass");
    }

}