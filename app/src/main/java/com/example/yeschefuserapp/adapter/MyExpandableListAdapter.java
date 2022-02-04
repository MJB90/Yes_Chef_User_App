package com.example.yeschefuserapp.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yeschefuserapp.R;

import java.util.List;
import java.util.Map;

public class MyExpandableListAdapter extends BaseExpandableListAdapter{
    private Context context;
    private List<String> selections;
    private Map<String,List<String>> categories;
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
        return categories.get(selections.get(i)).size();
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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.filter_child_item, null);
        }
        // TODO: add images later
        //ImageView image=view.findViewById(R.id.image_child_item);

        TextView item = (TextView) view.findViewById(R.id.child_item);
        String word=getChild(i,i1).toString();
        item.setText(word);
        int la=1+1;
        item.setOnClickListener(v->{
            Toast.makeText(v.getContext(),getChild(i,i1).toString(),Toast.LENGTH_LONG ).show();
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
