package com.example.yeschefuserapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.utility.AdvancedFilterTags;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private List<String> child;
    private CardClickListener cardClickListener;

    private List<MaterialCardView> cards;

    private LayoutInflater inflater;

    public CardAdapter(Context context, String parent,List<String> child,CardClickListener cardClickListener,List<MaterialCardView> cards) {
        this.child=child;
        this.inflater=LayoutInflater.from(context);
        this.cardClickListener=cardClickListener;
        this.cards=cards;
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
        holder.card.setOnClickListener(view ->{
            cardClickListener.onItemClick(holder.card,item);
        });
        if (cards!=null)
        {
            for (MaterialCardView c: cards){
                ViewGroup viewGroup = ((ViewGroup)c.getChildAt(0));
                String getName = ((TextView)viewGroup.getChildAt(1)).getText().toString();
                if (child.get(position).equals(getName)){
                    holder.card.setStrokeColor(0xffff0000);
                }
            }
        }
    }

    public interface CardClickListener{
        void onItemClick(MaterialCardView card,String text);
    }


    @Override
    public int getItemCount() {
        return child.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView text;
        ImageView image;
        MaterialCardView card;
        MaterialCardView selectedCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text=itemView.findViewById(R.id.child_item);
            image=itemView.findViewById(R.id.categoryImage);
            card=itemView.findViewById(R.id.categoryCard);
        }
    }
}
