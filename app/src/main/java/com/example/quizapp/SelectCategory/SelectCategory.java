package com.example.quizapp.SelectCategory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.quizapp.QuizActivity;
import com.example.quizapp.R;



import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class SelectCategory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private CategoryInfo categoryInfo;
    private RecyclerViewAdapter recycleViewAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<CategoryInfo> categoryList = new ArrayList<>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            categoryList.clear();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_select_category);
            swipeRefreshLayout = findViewById(R.id.SwipeRefreshLayout);
            progressDialog = new ProgressDialog(this);
            recyclerView = findViewById(R.id.recycleView);
            linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            loadData();
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadData();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    String id;
    private void loadData() {
        progressDialog.setTitle(R.string.app_name);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        String url = "https://opentdb.com/api_category.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray triviaCategories = response.getJSONArray("trivia_categories");
                            for (int i = 0; i < triviaCategories.length(); i++) {
                                id = triviaCategories.getJSONObject(i).getString("id");
                                String categoryName = triviaCategories.getJSONObject(i).getString("name");
                                categoryInfo = new CategoryInfo(id, categoryName);
                                categoryList.add(categoryInfo);
                            }
                            recycleViewAdapter = new RecyclerViewAdapter( categoryList,SelectCategory.this);
                            recyclerView.setAdapter(recycleViewAdapter);
                            progressDialog.dismiss();
                        } catch (Exception e) {
                            Toast.makeText(SelectCategory.this, e.toString(), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(com.android.volley.VolleyError error) {
                        Toast.makeText(SelectCategory.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
        queue.add(jsonObjectRequest);
    }

    public void startQuizActivity(int position) {

        Intent intent = new Intent(getApplicationContext(), QuizActivity.class);

        intent.putExtra("id", categoryList.get(position).id);
        startActivity(intent);
    }
}
