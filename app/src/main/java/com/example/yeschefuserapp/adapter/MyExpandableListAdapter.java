package com.example.yeschefuserapp.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yeschefuserapp.R;

import java.util.List;
import java.util.Map;

public class MyExpandableListAdapter extends BaseExpandableListAdapter{
    private Context context;
    private List<String> selections;
    private Map<String,List<String>> categories;
    CardAdapter adapter;
    RecyclerView cardList;

    public MyExpandableListAdapter(Context context, List<String> selections,Map<String,List<String>> categories){
        this.context=context;
        this.selections=selections;
        this.categories=categories;
    }

    @Override
    public int getGroupCount() {
        return categories.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return selections.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return categories.get(selections.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (view==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.filter_item, null);
        }
        TextView categories=view.findViewById(R.id.categories);
        categories.setTypeface(null, Typeface.BOLD_ITALIC);
        categories.setText(getGroup(i).toString());
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view==null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filter_child_item,viewGroup,false);
        }
        cardList=view.findViewById(R.id.gridView);
        adapter=new CardAdapter(view.getContext(), categories.get(selections.get(i)),(itemName,card)-> {
            //Clicking on a card
            Toast.makeText(context, itemName, Toast.LENGTH_SHORT).show();
            card.setRotationY(0f);
            card.animate().rotationY(90f).setDuration(200).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    card.setRotationY(270f);
                    card.animate().rotationY(360f).setListener(null);
                }
            });
        });
        GridLayoutManager gridLayoutManager=new GridLayoutManager(view.getContext(),3,GridLayoutManager.VERTICAL,false);
        cardList.setLayoutManager(gridLayoutManager);
        cardList.setAdapter(adapter);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
