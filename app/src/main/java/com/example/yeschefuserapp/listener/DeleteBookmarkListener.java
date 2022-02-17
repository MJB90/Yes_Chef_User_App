package com.example.yeschefuserapp.listener;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.yeschefuserapp.adapter.BookmarkAdapter;
import com.example.yeschefuserapp.context.UserContext;
import com.example.yeschefuserapp.utility.MySingleton;

import java.util.HashMap;
import java.util.Map;

public class DeleteBookmarkListener implements View.OnClickListener {
    private static final String TAG = "DeleteBookmarkListener";

    private final Context context;
    private final UserContext userContext;
    private final BookmarkAdapter adapter;
    private final int pos;

    public DeleteBookmarkListener(Context context, BookmarkAdapter adapter, int pos) {
        this.context = context;
        this.userContext = new UserContext(context);
        this.adapter = adapter;
        this.pos = pos;
    }

    @Override
    public void onClick(View view) {
        deleteBookmark();
    }

    private void deleteBookmark() {
        StringRequest objectRequest = new StringRequest(
                Request.Method.DELETE,
                String.format("http://10.0.2.2:8090/api/user/delete_bookmark/%s/%s", this.userContext.getEmail(), this.adapter.getRecipe(pos).getId()),
                response -> {
                    Toast.makeText(context, "Delete successfully", Toast.LENGTH_LONG).show();
                    this.adapter.removeRecipe(pos);
                    this.adapter.notifyItemRemoved(pos);
                },
                error -> {
                    Log.e(TAG, "Delete bookmark failed", error);
                    Toast.makeText(context, "Delete failed", Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Authorization", "Bearer " + userContext.getToken());
                return headerMap;
            }
        };
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_TIMEOUT_MS));
        MySingleton.getInstance(context).addToRequestQueue(objectRequest);
    }
}
