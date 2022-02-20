package com.example.yeschefuserapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yeschefuserapp.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private final List<String> child;
    private final CardClickListener cardClickListener;

    private final List<MaterialCardView> cards;

    private final LayoutInflater inflater;

    public CardAdapter(Context context, List<String> child, CardClickListener cardClickListener, List<MaterialCardView> cards) {
        this.child = child;
        this.inflater = LayoutInflater.from(context);
        this.cardClickListener = cardClickListener;
        this.cards = cards;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.tile, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = child.get(position);
        holder.text.setText(child.get(position));
        holder.card.setOnClickListener(view -> cardClickListener.onItemClick(holder.card, item));
        if (cards != null) {
            for (MaterialCardView c : cards) {
                String getName = ((TextView) c.getChildAt(0)).getText().toString();
                if (child.get(position).equals(getName)) {
                    holder.card.setStrokeColor(0xffff0000);
                }
            }
        }
    }

    public interface CardClickListener {
        void onItemClick(MaterialCardView card, String text);
    }


    @Override
    public int getItemCount() {
        return child.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        MaterialCardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.child_item);
            card = itemView.findViewById(R.id.categoryCard);
        }
    }
}
