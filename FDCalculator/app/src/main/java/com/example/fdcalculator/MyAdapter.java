package com.example.fdcalculator;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{



    Context context;
    ArrayList<FDstore> fds;

    public MyAdapter(Context context,ArrayList<FDstore> fds){
        this.context = context;
        this.fds = fds;
        if(getItemCount() == 0){
            Toast.makeText(context, "Empty!!", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_show_previous_details,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.principal.setText(""+fds.get(position).getPrincipalAmount());
        holder.intrest.setText(""+fds.get(position).getRate());
        holder.duration.setText(""+fds.get(position).getDuration());
        holder.date.setText(""+fds.get(position).getSartDate());
        holder.simpleintrest.setText(""+fds.get(position).getSimpleIntrest());
        holder.info.setText("Details :");
        holder.key.setText(""+fds.get(position).getKey());
    }

    @Override
    public int getItemCount() {
        return fds.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView principal,intrest,duration,date,simpleintrest,info,key;
        Button editbtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            principal = itemView.findViewById(R.id.principal);
            intrest = itemView.findViewById(R.id.intrest);
            duration = itemView.findViewById(R.id.duration);
            date = itemView.findViewById(R.id.date);
            simpleintrest = itemView.findViewById(R.id.simpleintrest);
            info = itemView.findViewById(R.id.info);
            editbtn = itemView.findViewById(R.id.edit_btn);
            key = itemView.findViewById(R.id.key);
            editbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(view.getContext(), "Position Clicked : "+getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(view.getContext(), "Principal : "+principal.getText(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context,EditDetailsAcitivity.class);
                    intent.putExtra("Principal",(String)principal.getText());
                    intent.putExtra("intrest",(String)intrest.getText());
                    intent.putExtra("duration",(String)duration.getText());
                    intent.putExtra("date",(String)date.getText());
                    intent.putExtra("key",(String)key.getText());
                    context.startActivity(intent);
                }
            });


        }
    }


}
