package com.example.schnunltdd;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class DangKy extends AppCompatActivity {
    Uri imageURI;
    private EditText edttk, edtmk,edtuser;
    private TextView tv;
    private Button btndangky;
    private TextView txtdangnhap;
    private CircleImageView rg_profileImg;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        // Ánh xạ
        tv = findViewById(R.id.textView2);
        btndangky = findViewById(R.id.btndangky);
        txtdangnhap = findViewById(R.id.txtdangnhap);
        edttk = findViewById(R.id.edttaikhoan);
        edtmk= findViewById(R.id.edtmatkhau);
        edtuser = findViewById(R.id.edtuser);
        rg_profileImg = findViewById(R.id.profilerg0);
        //Animation Alpha
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.blink);
        tv.startAnimation(animation);

        // Xử lí sự kiện cho nút đăng nhập
        txtdangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DangKy.this,DangNhap.class);
                startActivity(intent);
                finish();
            }
        });
        FirebaseStorage storage = FirebaseStorage.getInstance();
        btndangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stremail = edttk.getText().toString();
                String strpassword = edtmk.getText().toString();
                String struser = edtuser.getText().toString();
                String status = "Hey I'm Using This Application";
                FirebaseAuth auth = FirebaseAuth.getInstance();


                if(stremail.isEmpty()){
                    Toast.makeText(DangKy.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(strpassword.isEmpty()){
                    Toast.makeText(DangKy.this, "Vui lòng nhập password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(stremail.isEmpty()){
                    Toast.makeText(DangKy.this, "Vui lòng nhập user", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.createUserWithEmailAndPassword(stremail, strpassword)
                        .addOnCompleteListener(DangKy.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(DangKy.this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                                    updateuserInfo();
                                }else{
                                    Toast.makeText(DangKy.this, "Tạo tài khoản không thành công ", Toast.LENGTH_SHORT).show();
                                }
                            }

                            private void updateuserInfo() {
                                String uid = auth.getUid();
                                HashMap<String,Object> hashMap = new HashMap<>();
                                hashMap.put("uid", uid);
                                hashMap.put("email",stremail);
                                hashMap.put("status", status);
                                hashMap.put("userName",struser);
                                // Thêm ảnh vào hashMap
                                StorageReference storageReference = storage.getReference().child("Upload");
                                if (imageURI != null) {
                                    // Tạo một tham chiếu đến thư mục "images" trên Firebase Storage
                                    StorageReference storageRef = storage.getReference().child("images");
                                    // Tạo tên file ngẫu nhiên để lưu trữ ảnh trên Firebase Storage
                                    String filename = UUID.randomUUID().toString();
                                    StorageReference fileRef = storageRef.child(filename);
                                    // Tải ảnh lên Firebase Storage
                                    fileRef.putFile(imageURI)
                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    // Lấy địa chỉ URL của ảnh từ Firebase Storage
                                                    fileRef.getDownloadUrl()
                                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {
                                                                    // Thêm địa chỉ URL của ảnh vào hashMap
                                                                    hashMap.put("photoUrl", uri.toString());
                                                                    // Thêm hashMap vào database
                                                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                                                                    ref.child(uid).setValue(hashMap)
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
                                                                                    // Thành công
                                                                                }
                                                                            })
                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    // Thất bại
                                                                                }
                                                                            });
                                                                }
                                                            });
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Thất bại
                                                }
                                            });
                                } else {
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                                    ref.child(uid).setValue(hashMap)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    // Thành công
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Thất bại
                                                }
                                            });
                                }


                            }
                        });
            }
        });
        rg_profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (data != null) {
                imageURI = data.getData();
                rg_profileImg.setImageURI(imageURI);
                FirebaseStorage storage = FirebaseStorage.getInstance();
                // Lưu ảnh vào Firebase Storage
                StorageReference storageRef = storage.getReference().child("images/" + imageURI.getLastPathSegment());
                storageRef.putFile(imageURI)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Lấy URL của ảnh đã được lưu vào Firebase Storage
                                Task<Uri> downloadUrl = storageRef.getDownloadUrl();
                                downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        // Lưu URL của ảnh vào Realtime Database
                                        String uid = FirebaseAuth.getInstance().getUid();
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                                        HashMap<String,Object> hashMap = new HashMap<>();
                                        hashMap.put("photoUrl", uri.toString());
                                        ref.updateChildren(hashMap);
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Xử lý lỗi khi lưu ảnh không thành công
                            }
                        });
            }
        }
    }
}