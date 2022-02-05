package com.example.yeschefuserapp.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.model.Recipe;
import com.example.yeschefuserapp.utility.AdvancedFilterTags;
import com.example.yeschefuserapp.utility.MySingleton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private String parent;
    private List<String> child;
    private CardClickListener cardClickListener;

    private List<String> tags;
    private List<String> difficulty;
    private List<String> courseType;
    private List<String> cuisineType;

    private Context context;
    private LayoutInflater inflater;
    private final AdvancedFilterTags advancedFilterTags=new AdvancedFilterTags(.0,0,0,null,null,null,null);

    public CardAdapter(Context context, String parent,List<String> child,CardClickListener cardClickListener) {
        this.context=context;
        this.parent=parent;
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
        holder.card.setOnClickListener(view ->{
            cardClickListener.onItemClick(holder.card,item);
        });

    }

    public interface CardClickListener{
        void onItemClick(CardView card,String text);
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
}
