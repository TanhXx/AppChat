package com.example.schnunltdd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DangNhap extends AppCompatActivity {
    private TextView tv,txtdangky;
    private Button btndangnhap;
    private EditText edttk, edtmk;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        auth = FirebaseAuth.getInstance();
        AnhXa();
        AnimatioAlpha();
        TaoTk();
        Dangnhap();



    }

    private void Dangnhap() {
        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = edttk.getText().toString().trim();
                String Password = edtmk.getText().toString().trim();

                auth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(DangNhap.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DangNhap.this,MainActivity.class);
                                startActivity(intent);

                        }else {
                            Toast.makeText(DangNhap.this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    private void TaoTk() {
        txtdangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DangNhap.this, DangKy.class);
                startActivity(intent);
            }
        });
    }

    private void AnimatioAlpha() {
        Animation BlinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink);
        tv.startAnimation(BlinkAnimation);
    }

    private void AnhXa() {
        txtdangky = findViewById(R.id.txtdangky);
        tv = findViewById(R.id.textView);
        btndangnhap = findViewById(R.id.btndangnhap);
        edttk = findViewById(R.id.edttaikhoan);
        edtmk = findViewById(R.id.edtmatkhau);
    }



}