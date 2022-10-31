package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    ImageView back_Login ;
    EditText edt_EmailResetPass ;
    Button btn_GetNewPass ;
    FirebaseAuth auth ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        auth = FirebaseAuth.getInstance();

        back_Login = findViewById(R.id.back_Login);
        edt_EmailResetPass = findViewById(R.id.edt_EmailResetPass);
        btn_GetNewPass = findViewById(R.id.btn_GetNewPass);

        back_Login.setOnClickListener(view->{
            startActivity(new Intent(ResetPasswordActivity.this , LoginActivity.class));
        });

        btn_GetNewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_EmailResetPass.getText().toString().trim();
                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ResetPasswordActivity.this , "Please check your email to reset password !" ,Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ResetPasswordActivity.this , LoginActivity.class));
                        }else {
                            Toast.makeText(ResetPasswordActivity.this , ""+ task.getException().getMessage() , Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}