package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    ImageView img_backLogin;
    EditText edt_NameRegister , edt_EmailRegister , edt_PassRegister , edt_ConfirmPassRegister;
    Button btn_Register ;
    TextView txt_back_Login ;
    ProgressBar progress_Register ;
    String name , email , pass , confirm_Pass ;
    FirebaseAuth auth ;
    DatabaseReference reference ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();

        img_backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this , LoginActivity.class));
            }
        });

        btn_Register.setOnClickListener(view->{
            name            = edt_NameRegister.getText().toString().trim();
            email           = edt_EmailRegister.getText().toString().trim();
            pass            = edt_PassRegister.getText().toString().trim();
            confirm_Pass    = edt_ConfirmPassRegister.getText().toString().trim();

            if(name.isEmpty()){
                edt_NameRegister.setError("User name is empty !");
            }else if(email.isEmpty()){
                edt_EmailRegister.setError("Email is empty !");
            }else if(!email.matches(Util.emailPattern)){
                edt_EmailRegister.setError("Email is invalid !");
            }else if(pass.isEmpty()){
                edt_PassRegister.setError("Password is empty !");
            }else if (pass.length()<6){
                edt_PassRegister.setError("Password be greater than 6 characters!");
            } else if(confirm_Pass.isEmpty()){
                edt_ConfirmPassRegister.setError("Confirm password is empty !");
            }else if (!pass.matches(confirm_Pass)){
                edt_ConfirmPassRegister.setError("Password is not match !");
            }else {
                registerUser(name , email , pass);
                progress_Register.setVisibility(View.VISIBLE);
            }
        });


    }

    private void registerUser(String name, String email, String pass) {
        auth.createUserWithEmailAndPassword(email , pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", name);
                            hashMap.put("imageURL", Util.img_URL);
                            hashMap.put("status", "offline");
                            hashMap.put("search", name.toLowerCase());

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        intent.putExtra("_email" , email);
                                        intent.putExtra("_pass" , pass);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void initViews() {
        auth = FirebaseAuth.getInstance();
        img_backLogin = findViewById(R.id.img_backLogin);
        edt_NameRegister = findViewById(R.id.edt_NameRegister);
        edt_EmailRegister = findViewById(R.id.edt_EmailRegister);
        edt_PassRegister = findViewById(R.id.edt_PassRegister);
        edt_ConfirmPassRegister = findViewById(R.id.edt_ConfirmPassRegister);
        btn_Register = findViewById(R.id.btn_Register);
        txt_back_Login = findViewById(R.id.txt_back_Login);
        progress_Register = findViewById(R.id.progress_Register);
    }

    @Override
    protected void onStart() {
        super.onStart();
        progress_Register.setVisibility(View.INVISIBLE);
    }
}