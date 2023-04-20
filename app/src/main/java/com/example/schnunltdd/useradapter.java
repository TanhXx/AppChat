package com.example.schnunltdd;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class useradapter extends RecyclerView.Adapter<useradapter.viewholder> {
    MainActivity mainActivity;
    ArrayList<Users> usersArrayList;


    public useradapter(MainActivity mainActivity, ArrayList<Users> usersArrayList) {
        this.mainActivity=mainActivity;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public useradapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.user,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull useradapter.viewholder holder, int position) {
        Users users = usersArrayList.get(position);
        holder.txtname.setText(users.getUserName());
        holder.txtstt.setText(users.getStatus());
        if (users.photoUrl != null && !users.getPhotoUrl().isEmpty()) {
            Picasso.get().load(users.getPhotoUrl()).into(holder.userimg);
        } else {
            holder.userimg.setImageResource(R.drawable.userdk);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, Chat.class);
                intent.putExtra("name", users.getUserName());
                intent.putExtra("reciverImg", users.getPhotoUrl());
                intent.putExtra("reciverUid", users.getUserId());
                mainActivity.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {

        return usersArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        CircleImageView userimg;
        TextView txtname,txtstt;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            userimg = itemView.findViewById(R.id.userimg);
            txtname = itemView.findViewById(R.id.username);
            txtstt = itemView.findViewById(R.id.userstatus);

        }
    }
}
