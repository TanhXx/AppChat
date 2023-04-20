package com.example.schnunltdd;

import static com.example.schnunltdd.Chat.receiverImg;
import static com.example.schnunltdd.Chat.senderImg;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class messagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Modelclass> messagesAdapterList;
    int ITEM_SEND = 1;
    int ITEM_RECEIVE = 2;

    // Add selectedUserId variable
    private String selectedUserId;

    public messagesAdapter(Context context, ArrayList<Modelclass> messagesAdapterList) {
        this.context = context;
        this.messagesAdapterList = messagesAdapterList;
        this.selectedUserId = selectedUserId;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout,parent,false);
            return  new senderViewHolder(view);
        }else {
            View view  = LayoutInflater.from(context).inflate(R.layout.receiver_layout,parent,false);
            return  new receiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Modelclass messages = messagesAdapterList.get(position);
        Log.d("messagesAdapter", "messagesAdapterList size: " + messagesAdapterList.size() + ", position: " + position);


        if (holder.getClass() == senderViewHolder.class){
            senderViewHolder viewHolder = (senderViewHolder) holder;
            viewHolder.msgtxt.setText(messages.getMessagess());
            Picasso.get().load(senderImg).into(viewHolder.circleImageView);
        } else {
            receiverViewHolder viewHolder1 = (receiverViewHolder) holder;
            viewHolder1.msgtxt.setText(messages.getMessagess());
            Picasso.get().load(receiverImg).into(viewHolder1.circleImageView);
        }
    }


    @Override
    public int getItemCount() {
        if (messagesAdapterList == null) {
            return 0;
        } else {
            return messagesAdapterList.size();
        }


    }

    @Override
    public int getItemViewType(int position) {
        Modelclass messages = messagesAdapterList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderid())){
            return ITEM_SEND;
        }else {
            return ITEM_RECEIVE;
        }
    }

    class  senderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView msgtxt;

        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.senderimg);
            msgtxt = itemView.findViewById(R.id.sendertext);

        }
    }

    class  receiverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView msgtxt;

        public receiverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.receimg);
            msgtxt = itemView.findViewById(R.id.recetext);
        }
    }
}
