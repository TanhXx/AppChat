package com.example.schnunltdd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {
    RecyclerView messageAdapter;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    TextView receivername;
    CardView imgsend;
    EditText edtnoidung;
    CircleImageView profile;
    String receiverimg,reciverUid,receiverName;
    public static String senderImg;
    public static String receiverImg;
    String SenderUID;
    String senderRoom, receiverRoom;
    ArrayList<Modelclass> messagesArraylist;
    messagesAdapter messagesadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();



        receiverName = getIntent().getStringExtra("name");
        receiverimg = getIntent().getStringExtra("reciverImg");
        reciverUid = getIntent().getStringExtra("reciverUid");

        messagesArraylist = new ArrayList<>();
        messageAdapter = findViewById(R.id.msgadapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messagesadapter = new messagesAdapter(Chat.this, messagesArraylist);
        messageAdapter.setLayoutManager(linearLayoutManager);
        messageAdapter.setAdapter(messagesadapter);





        receivername = findViewById(R.id.recivername);
        profile = findViewById(R.id.profilerg0);

//        if(receiverimg != null && !receiverimg.isEmpty()){
//            Picasso.get().load(receiverimg).into(profile);
//            receivername.setText(""+receiverName);
//        }else {
//            receivername.setText(""+ receiverName);
//            profile.setImageResource(R.drawable.userdk);
//        }
        Picasso.get().load(receiverimg).into(profile);
        receivername.setText(""+receiverName);

        

        SenderUID = firebaseAuth.getUid();
        senderRoom = SenderUID + reciverUid;
        receiverRoom = reciverUid + SenderUID;

        DatabaseReference reference = database.getReference().child("Users").child(firebaseAuth.getUid());
        DatabaseReference chatref = database.getReference().child("chats").child(senderRoom).child("messages");

        chatref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArraylist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Modelclass messages = dataSnapshot.getValue(Modelclass.class);
                    messagesArraylist.add(messages);
                }
                messagesadapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                senderImg = snapshot.child("photoUrl").getValue().toString();
//                DataSnapshot photoSnapshot = snapshot.child("photoUrl");
////                if (photoSnapshot.exists()) {
////                    senderImg = photoSnapshot.getValue(String.class);
////                } else {
////                    Toast.makeText(Chat.this, "Error: photoUrl does not exist in the snapshot.", Toast.LENGTH_SHORT).show();
////                }
                receiverImg = receiverimg;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        imgsend  = findViewById(R.id.imgsend);
        edtnoidung = findViewById(R.id.edtnoidung);



        imgsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = edtnoidung.getText().toString();
                if(message.isEmpty()){
                    Toast.makeText(Chat.this, "Ban Chua Nhap Noi Dung Tin Nhan", Toast.LENGTH_SHORT).show();
                }
                edtnoidung.setText("");
                Date date = new Date();
                Modelclass messagess = new Modelclass(message, SenderUID,date.getTime());
                database= FirebaseDatabase.getInstance();
                database.getReference().child("chats").child(senderRoom).child("messages").push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        database.getReference().child("chats").child(receiverRoom).child("messages").push().setValue(messagess).
                                addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                    }
                });
            }
        });

    }
}