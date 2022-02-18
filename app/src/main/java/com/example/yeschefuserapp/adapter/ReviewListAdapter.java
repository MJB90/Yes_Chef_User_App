package com.example.yeschefuserapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.model.UserReview;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.MyReviewViewHolder> {
    private final int resourceId;
    private final List<UserReview> mUserReviews;

    public ReviewListAdapter(int resourceId, List<UserReview> mUserReviews) {
        this.resourceId = resourceId;
        this.mUserReviews = mUserReviews;
    }

    @NonNull
    @Override
    public MyReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View reviewView = inflater.inflate(this.resourceId,parent,false);
        return new MyReviewViewHolder(reviewView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyReviewViewHolder holder, int position) {
        UserReview userReview = mUserReviews.get(position);
        //TODO to change profile image to real image
        holder.profileImage.setImageResource(R.drawable.yes_chef);

        holder.username.setText(userReview.getUserEmail());
        holder.ratingBar.setRating(userReview.getRating().floatValue());

        //TODO Once review Date in model, change review date to real date.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy");
        String reviewDate = formatter.format(userReview.getReviewDateTime());
        holder.dateReview.setText(reviewDate);
        holder.reviewDesc.setText(userReview.getDescription());
    }


    @Override
    public int getItemCount() {
        if(mUserReviews!=null)
        return mUserReviews.size();
        return 0;
    }

    public static class MyReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView username;
        RatingBar ratingBar;
        TextView dateReview;
        TextView reviewDesc;

        public MyReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.user_name);
            ratingBar = itemView.findViewById(R.id.view_rating_bar);
            dateReview = itemView.findViewById(R.id.review_date);
            reviewDesc= itemView.findViewById(R.id.review_desc);

        }
    }
}
