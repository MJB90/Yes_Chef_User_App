package com.example.yeschefuserapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yeschefuserapp.R;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private List<String> child=new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;
    private CardClickListener cardClickListener;

    public CardAdapter(Context context, List<String> child, CardClickListener cardClickListener) {
        this.context=context;
        this.child=child;
        this.inflater=LayoutInflater.from(context);
        this.cardClickListener=cardClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.tile,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item=child.get(position);
        holder.text.setText(child.get(position));
        holder.card.setOnClickListener(view -> cardClickListener.onItemClick(item, holder.card));
    }

    @Override
    public int getItemCount() {
        return child.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView text;
        ImageView image;
        CardView card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text=itemView.findViewById(R.id.child_item);
            image=itemView.findViewById(R.id.categoryImage);
            card=itemView.findViewById(R.id.categoryCard);
        }
    }

    public interface CardClickListener {
        void onItemClick(String item, CardView card);
    }
}
