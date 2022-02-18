package com.example.yeschefuserapp.adapter;

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
import com.example.yeschefuserapp.utility.AdvancedFilterTags;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyExpandableListAdapter extends BaseExpandableListAdapter{
    private final Context context;
    private final List<String> selections;
    private final Map<String,List<String>> categories;
    private final IMyExpandableList iMyExpandableList;

    private final Map<String,List<MaterialCardView>> cards=new HashMap<>();
    private final AdvancedFilterTags advancedFilterTags=new AdvancedFilterTags(.0,0,0,null,null,null,null);

    private List<String> tags;
    private List<String> difficulty;
    private List<String> courseType;
    private List<String> cuisineType;

    private int caloriesCount=0;
    private int prepTimeCount=0;

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
        return Objects.requireNonNull(categories.get(selections.get(i))).get(i1);
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

        RecyclerView cardList = view.findViewById(R.id.gridView);

        List<MaterialCardView> cardToSendToCardAdapter=cards.get(selections.get(i));

        //Create an AdvancedFilterTags object
        CardAdapter adapter = new CardAdapter(view.getContext(), categories.get(selections.get(i)), (card, item) -> {
            switch (selections.get(i)) {
                case "Tags":
                    if (cards.get("Tags") == null) {
                        List<MaterialCardView> temp = new ArrayList<>();
                        temp.add(card);
                        cards.put("Tags", temp);
                        card.setStrokeColor(0xffff0000);
                    } else if (cards.get("Tags").contains(card)) {
                        card.setStrokeColor(0xffffff);
                        cards.get("Tags").remove(card);
                    } else {
                        card.setStrokeColor(0xffff0000);
                        cards.get("Tags").add(card);
                    }
                    break;
                case "Calories":
                    if (caloriesCount == 0) {
                        if (cards.get("Calories") == null) {
                            List<MaterialCardView> temp = new ArrayList<>();
                            temp.add(card);
                            cards.put("Calories", temp);
                        } else {
                            cards.get("Calories").add(card);
                        }
                        card.setStrokeColor(0xffff0000);
                        caloriesCount++;
                    } else if (caloriesCount == 1) {
                        if (cards.get("Calories").contains(card)) {
                            cards.get("Calories").clear();
                            card.setStrokeColor(0xffffff);
                            caloriesCount--;
                        } else {
                            cards.get("Calories").get(0).setStrokeColor(0xffffff);
                            cards.get("Calories").clear();
                            card.setStrokeColor(0xffff0000);
                            cards.get("Calories").add(card);
                            Toast.makeText(context, "Select one option", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case "Difficulty":
                    if (cards.get("Difficulty") == null) {
                        List<MaterialCardView> temp = new ArrayList<>();
                        temp.add(card);
                        cards.put("Difficulty", temp);
                        card.setStrokeColor(0xffff0000);
                    } else if (cards.get("Difficulty").contains(card)) {
                        card.setStrokeColor(0xffffff);
                        cards.get("Difficulty").remove(card);
                    } else {
                        card.setStrokeColor(0xffff0000);
                        cards.get("Difficulty").add(card);
                    }
                    break;
                case "Preparation Time":
                    if (prepTimeCount == 0) {
                        if (cards.get("Preparation Time") == null) {
                            List<MaterialCardView> temp = new ArrayList<>();
                            temp.add(card);
                            cards.put("Preparation Time", temp);
                        } else {
                            cards.get("Preparation Time").add(card);
                        }
                        card.setStrokeColor(0xffff0000);
                        prepTimeCount++;
                    } else if (prepTimeCount == 1) {
                        if (cards.get("Preparation Time").contains(card)) {
                            cards.get("Preparation Time").clear();
                            card.setStrokeColor(0xffffff);
                            prepTimeCount--;
                        } else {
                            cards.get("Preparation Time").get(0).setStrokeColor(0xffffff);
                            cards.get("Preparation Time").clear();
                            card.setStrokeColor(0xffff0000);
                            cards.get("Preparation Time").add(card);
                            Toast.makeText(context, "Select one option", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case "Course Type":
                    if (cards.get("Course Type") == null) {
                        List<MaterialCardView> temp = new ArrayList<>();
                        temp.add(card);
                        cards.put("Course Type", temp);
                        card.setStrokeColor(0xffff0000);
                    } else if (cards.get("Course Type").contains(card)) {
                        card.setStrokeColor(0xffffff);
                        cards.get("Course Type").remove(card);
                    } else {
                        card.setStrokeColor(0xffff0000);
                        cards.get("Course Type").add(card);
                    }
                    break;
                case "Cuisine Type":
                    if (cards.get("Cuisine Type") == null) {
                        List<MaterialCardView> temp = new ArrayList<>();
                        temp.add(card);
                        cards.put("Cuisine Type", temp);
                        card.setStrokeColor(0xffff0000);
                    } else if (cards.get("Cuisine Type").contains(card)) {
                        card.setStrokeColor(0xffffff);
                        cards.get("Cuisine Type").remove(card);
                    } else {
                        card.setStrokeColor(0xffff0000);
                        cards.get("Cuisine Type").add(card);
                    }
                    break;
            }
            //Create an AdvancedFilterTags object
            switch (selections.get(i)) {
                case "Tags":
                    if (advancedFilterTags.getTags() == null) {
                        tags = new ArrayList<>();
                    } else {
                        tags = advancedFilterTags.getTags();
                    }
                    tags.add(item);
                    advancedFilterTags.setTags(tags);
                    break;
                case "Calories": {
                    Matcher matcher = Pattern.compile("\\d+").matcher(item);

                    List<Integer> numbers = new ArrayList<>();
                    while (matcher.find()) {
                        numbers.add(Integer.valueOf(matcher.group()));
                    }
                    advancedFilterTags.setMaxCalories((double) numbers.get(1));
                    advancedFilterTags.setMinCalories((double) numbers.get(0));
                    break;
                }
                case "Difficulty":
                    if (advancedFilterTags.getDifficulty() == null) {
                        difficulty = new ArrayList<>();
                    } else {
                        difficulty = advancedFilterTags.getDifficulty();
                    }
                    difficulty.add(item);
                    advancedFilterTags.setDifficulty(difficulty);
                    break;
                case "Preparation Time": {
                    Matcher matcher = Pattern.compile("\\d+").matcher(item);

                    List<Integer> prepTime = new ArrayList<>();
                    while (matcher.find()) {
                        prepTime.add(Integer.valueOf(matcher.group()));
                    }
                    int minPrepTime = prepTime.get(0);
                    int maxPrepTime = prepTime.get(1);
                    if (minPrepTime == 1 && maxPrepTime == 10) {
                        advancedFilterTags.setMaxPrepTime(600);
                        advancedFilterTags.setMinPrepTime(60);
                    } else if (minPrepTime == 10 && maxPrepTime == 30) {
                        advancedFilterTags.setMaxPrepTime(1800);
                        advancedFilterTags.setMinPrepTime(600);
                    } else if (minPrepTime == 0 && maxPrepTime == 5 && prepTime.get(2) == 1) {
                        advancedFilterTags.setMaxPrepTime(3600);
                        advancedFilterTags.setMinPrepTime(1800);
                    } else if (minPrepTime == 1 && maxPrepTime == 5) {
                        advancedFilterTags.setMaxPrepTime(18000);
                        advancedFilterTags.setMinPrepTime(3600);
                    } else if (minPrepTime == 5 && maxPrepTime == 10) {
                        advancedFilterTags.setMaxPrepTime(36000);
                        advancedFilterTags.setMinPrepTime(18000);
                    }
                    break;
                }
                case "Course Type":
                    if (advancedFilterTags.getCourseType() == null) {
                        courseType = new ArrayList<>();
                    } else {
                        courseType = advancedFilterTags.getCourseType();
                    }
                    courseType.add(item);
                    advancedFilterTags.setCourseType(courseType);
                    break;
                case "Cuisine Type":
                    if (advancedFilterTags.getCuisineType() == null) {
                        cuisineType = new ArrayList<>();
                    } else {
                        cuisineType = advancedFilterTags.getCuisineType();
                    }
                    cuisineType.add(item);
                    advancedFilterTags.setCuisineType(cuisineType);
                    break;
            }
            iMyExpandableList.onItemClick(advancedFilterTags);
        }, cardToSendToCardAdapter);
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
