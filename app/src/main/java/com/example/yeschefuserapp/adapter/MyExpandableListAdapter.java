package com.example.yeschefuserapp.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.fragment.AdvancedFilterFragment;
import com.example.yeschefuserapp.utility.AdvancedFilterTags;
import com.example.yeschefuserapp.utility.MySingleton;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyExpandableListAdapter extends BaseExpandableListAdapter{
    private Context context;
    private List<String> selections;
    private Map<String,List<String>> categories;
    private CardAdapter adapter;
    private RecyclerView cardList;
    private IMyExpandableList iMyExpandableList;

    private Map<String,List<MaterialCardView>> cards=new HashMap<>();
    private final AdvancedFilterTags advancedFilterTags=new AdvancedFilterTags(.0,0,0,null,null,null,null);

    private List<String> tags;
    private List<String> difficulty;
    private List<String> courseType;
    private List<String> cuisineType;

    private int caloriesCount=0;
    private int difficultyCount=0;
    private int prepTimeCount=0;
    private int noServingCount=0;
    private int courseTypeCount=0;
    private int cuisineCount=0;

    public MyExpandableListAdapter(Context context, List<String> selections,Map<String,List<String>> categories,IMyExpandableList iMyExpandableList){
        this.context=context;
        this.selections=selections;
        this.categories=categories;
        this.iMyExpandableList=iMyExpandableList;
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

        List<MaterialCardView> cardToSendToCardAdapter=cards.get(selections.get(i));

        adapter=new CardAdapter(view.getContext(), selections.get(i) ,categories.get(selections.get(i)),(card,item)->{
            switch (selections.get(i)){
                case "Tags":
                    if (cards.get("Tags")==null)
                    {
                        List<MaterialCardView> temp=new ArrayList<>();
                        temp.add(card);
                        cards.put("Tags",temp);
                        card.setStrokeColor(0xffff0000);
                    }
                    else if (cards.get("Tags").contains(card))
                    {
                        card.setStrokeColor(0xffffff);
                        cards.get("Tags").remove(card);
                    }
                    else{
                        card.setStrokeColor(0xffff0000);
                        cards.get("Tags").add(card);
                    }
                    break;
                case "Calories":
                    if (caloriesCount==0)
                    {
                        if (cards.get("Calories")==null){
                            List<MaterialCardView> temp=new ArrayList<>();
                            temp.add(card);
                            cards.put("Calories",temp);
                        }
                        else{
                            cards.get("Calories").add(card);
                        }
                        card.setStrokeColor(0xffff0000);
                        caloriesCount++;
                    }
                    else if(caloriesCount==1){
                        if (cards.get("Calories").contains(card)){
                            cards.get("Calories").clear();
                            card.setStrokeColor(0xffffff);
                            caloriesCount--;
                        }
                        else
                        {
                            cards.get("Calories").get(0).setStrokeColor(0xffffff);
                            cards.get("Calories").clear();
                            card.setStrokeColor(0xffff0000);
                            cards.get("Calories").add(card);
                            Toast.makeText(context,"Select one option",Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case "Difficulty":
                    if (cards.get("Difficulty")==null)
                    {
                        List<MaterialCardView> temp=new ArrayList<>();
                        temp.add(card);
                        cards.put("Difficulty",temp);
                        card.setStrokeColor(0xffff0000);
                    }
                    else if (cards.get("Difficulty").contains(card))
                    {
                        card.setStrokeColor(0xffffff);
                        cards.get("Difficulty").remove(card);
                    }
                    else{
                        card.setStrokeColor(0xffff0000);
                        cards.get("Difficulty").add(card);
                    }
                    break;
                case "Preparation Time":
                    if (prepTimeCount==0)
                    {
                        if (cards.get("Preparation Time")==null){
                            List<MaterialCardView> temp=new ArrayList<>();
                            temp.add(card);
                            cards.put("Preparation Time",temp);
                        }
                        else{
                            cards.get("Preparation Time").add(card);
                        }
                        card.setStrokeColor(0xffff0000);
                        prepTimeCount++;
                    }
                    else if(prepTimeCount==1){
                        if (cards.get("Preparation Time").contains(card)){
                            cards.get("Preparation Time").clear();
                            card.setStrokeColor(0xffffff);
                            prepTimeCount--;
                        }
                        else
                        {
                            cards.get("Preparation Time").get(0).setStrokeColor(0xffffff);
                            cards.get("Preparation Time").clear();
                            card.setStrokeColor(0xffff0000);
                            cards.get("Preparation Time").add(card);
                            Toast.makeText(context,"Select one option",Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case "Course Type":
                    if (cards.get("Course Type")==null)
                    {
                        List<MaterialCardView> temp=new ArrayList<>();
                        temp.add(card);
                        cards.put("Course Type",temp);
                        card.setStrokeColor(0xffff0000);
                    }
                    else if (cards.get("Course Type").contains(card))
                    {
                        card.setStrokeColor(0xffffff);
                        cards.get("Course Type").remove(card);
                    }
                    else{
                        card.setStrokeColor(0xffff0000);
                        cards.get("Course Type").add(card);
                    }
                    break;
                case "Cuisine Type":
                    if (cards.get("Cuisine Type")==null)
                    {
                        List<MaterialCardView> temp=new ArrayList<>();
                        temp.add(card);
                        cards.put("Cuisine Type",temp);
                        card.setStrokeColor(0xffff0000);
                    }
                    else if (cards.get("Cuisine Type").contains(card))
                    {
                        card.setStrokeColor(0xffffff);
                        cards.get("Cuisine Type").remove(card);
                    }
                    else{
                        card.setStrokeColor(0xffff0000);
                        cards.get("Cuisine Type").add(card);
                    }
                    break;
            }
            //Create an AdvancedFilterTags object
            if (selections.get(i)=="Tags"){
                if (advancedFilterTags.getTags()==null) {
                    tags=new ArrayList<>();
                    tags.add(item);
                }
                else {
                    tags = advancedFilterTags.getTags();
                    tags.add(item);
                }
                advancedFilterTags.setTags(tags);
            }
            else if(selections.get(i)=="Calories"){
                advancedFilterTags.setMaxCalories(Double.parseDouble(item.substring(6,9)));
            }
            else if(selections.get(i)=="Difficulty"){
                if (advancedFilterTags.getDifficulty()==null) {
                    difficulty=new ArrayList<>();
                    difficulty.add(item);
                }
                else {
                    difficulty = advancedFilterTags.getDifficulty();
                    difficulty.add(item);
                }
                advancedFilterTags.setDifficulty(difficulty);
            }
            else if(selections.get(i)=="Preparation Time") {
                switch (item.substring(7,8)){
                    case "0":
                        advancedFilterTags.setMaxPrepTime(Integer.parseInt(item.substring(6,8))*60);
                        break;
                    default:
                        advancedFilterTags.setMaxPrepTime(Integer.parseInt(item.substring(6,7))*3600);
                        break;
                }
            }
            else if (selections.get(i)=="Course Type")
            {
                if (advancedFilterTags.getCourseType()==null) {
                    courseType=new ArrayList<>();
                    courseType.add(item);
                }
                else {
                    courseType = advancedFilterTags.getCourseType();
                    courseType.add(item);
                }
                advancedFilterTags.setCourseType(courseType);
            }
            else if (selections.get(i)=="Cuisine Type")
            {
                if (advancedFilterTags.getCuisineType()==null) {
                    cuisineType=new ArrayList<>();
                    cuisineType.add(item);
                }
                else {
                    cuisineType = advancedFilterTags.getCuisineType();
                    cuisineType.add(item);
                }
                advancedFilterTags.setCuisineType(cuisineType);
            }
            iMyExpandableList.onItemClick(advancedFilterTags);
        },cardToSendToCardAdapter);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(view.getContext(),3,GridLayoutManager.VERTICAL,false);
        cardList.setLayoutManager(gridLayoutManager);
        cardList.setAdapter(adapter);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public interface IMyExpandableList{
        void onItemClick(AdvancedFilterTags object);
    }
}
