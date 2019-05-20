package lingamworks.hoteldasboard;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.LinkedList;
import java.util.List;
import lingamworks.hoteldasboard.adapter.cListad;
import lingamworks.hoteldasboard.data.categoriesd;
import lingamworks.hoteldasboard.data.fooditems;

public  class submenus extends AppCompatActivity {
    List<categoriesd> catList;
    cListad adapter;
    categoriesd d;
    List<fooditems> foodList;
    fooditems f;
    TextView v1;
    int count;
    ImageView pb1;
    Button b11,b22,b33;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    String host,position;
    public void submit(View v){
        SharedPreferences prefs= this.getSharedPreferences("tables",Context.MODE_PRIVATE);
        Intent i=new Intent(this,Biller.class);
        i.putExtra("counter",1);
        i.putExtra("table",prefs.getString("table","-1"));
        startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submenus);
        setTitle("Menu Book");
        position="first";
        count=0;
        b11=findViewById(R.id.hide1);
        b11.setVisibility(View.GONE);
        b22=findViewById(R.id.hide2);
        b22.setVisibility(View.GONE);
        b33=findViewById(R.id.hide3);
        b33.setVisibility(View.GONE);
        pb1=findViewById(R.id.imageViewsub);
        pb1.setVisibility(View.VISIBLE);
        host=getString(R.string.localhost);
        Glide.with(this)
                .load(R.drawable.pizza2)
                .into(pb1);
        host=getString(R.string.localhost);
        Intent i=getIntent();
        int counter=i.getIntExtra("counter", -1);
        catList =new LinkedList<>();
        foodList=new LinkedList<>();
        v1=findViewById(R.id.textView2);
        mRecyclerView=findViewById(R.id.fooditems1);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new cListad(foodList, getApplicationContext(),mRecyclerView);
        mRecyclerView.setAdapter(adapter);
        if(counter!=-1){
            String temp1=i.getStringExtra("name");
            loadcatList11(counter,temp1);
            count=counter;
        }else{loadcatList();}

    }
    public void previous(View v){
        count--;
            pb1.setVisibility(View.VISIBLE);
            b11.setVisibility(View.GONE);
            b22.setVisibility(View.GONE);
            d = catList.get(count);
            v1.setText(d.getCatname());
            mRecyclerView.removeAllViewsInLayout();
            loadfoodList(v1.getText().toString());

    }
    public void next(View v){
        count++;

            b22.setVisibility(View.GONE);
            b11.setVisibility(View.GONE);
            pb1.setVisibility(View.VISIBLE);
            d = catList.get(count);
            v1.setText(d.getCatname());
            mRecyclerView.removeAllViewsInLayout();
            loadfoodList(v1.getText().toString());

    }
    private void loadcatList() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest req = new JsonArrayRequest(host+"/android/hotelcatre.php",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //   Log.d(TAG, response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            Log.i("hello","retrieveing");
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);
                                categoriesd hero = new categoriesd(person.getString("category"));
                                catList.add(hero);
                            }
                            d = catList.get(0);
                            v1.setText(d.getCatname());
                            //creating custom adapter object
                            //adding the adapter to listview
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(req);
        JsonArrayRequest req1 = new JsonArrayRequest(host+"/android/fooditems.php?status=first",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            Log.i("hello","retrieveing");
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response.get(i);
                                Log.i("got it",person.getString("description"));
                                fooditems hero = new fooditems(person.getString("fname"),person.getString("fprice"),person.getString("description"),person.getString("imagelocal"));
                                foodList.add(hero);
                            }
                            adapter = new cListad(foodList, getApplicationContext(),mRecyclerView);
                            mRecyclerView.setAdapter(adapter);
                            pb1.setVisibility(View.GONE);
                            b22.setVisibility(View.VISIBLE);
                            b33.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(req1);
    }
    private void loadfoodList(String category) {
        foodList.clear();
        JsonArrayRequest req = new JsonArrayRequest(host+"/android/fooditems.php?status=current&category="+category+"",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //   Log.d(TAG, response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            Log.i("hello","retrieveing");
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response.get(i);
                                fooditems hero = new fooditems(person.getString("fname"),person.getString("fprice"),person.getString("description"),person.getString("imagelocal"));
                                foodList.add(hero);
                            }
                            mRecyclerView.removeAllViewsInLayout();
                            adapter = new cListad(foodList, getApplicationContext(),mRecyclerView);
                            mRecyclerView.setAdapter(adapter);
                            pb1.setVisibility(View.GONE);
                            if(count==catList.size()-1){
                                b11.setVisibility(View.VISIBLE);
                                b22.setVisibility(View.GONE);
                            }else if(count==0){
                                b22.setVisibility(View.VISIBLE);
                                b11.setVisibility(View.GONE);
                            }else{
                                b22.setVisibility(View.VISIBLE);
                                b11.setVisibility(View.VISIBLE);
                            }
                            b33.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(req);
    }
    private void loadcatList11(final int count,String category) {
                mRecyclerView.removeAllViews();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest req = new JsonArrayRequest(host+"/android/hotelcatre.php",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //   Log.d(TAG, response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            Log.i("hello","retrieveing");
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);
                                categoriesd hero = new categoriesd(person.getString("category"));
                                catList.add(hero);
                            }
                            d = catList.get(count);
                            v1.setText(d.getCatname());
                            //creating custom adapter object
                            //adding the adapter to listview
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(req);
        JsonArrayRequest req1 = new JsonArrayRequest(host+"/android/fooditems.php?status=current&category="+category+"",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //   Log.d(TAG, response.toString());
                        try {
                            // Parsing json array response
                            // loop through each json object
                            Log.i("hello","retrieveing");
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response.get(i);
                                fooditems hero = new fooditems(person.getString("fname"),person.getString("fprice"),"","");
                                foodList.add(hero);
                            }
                            mRecyclerView.removeAllViewsInLayout();
                            adapter = new cListad(foodList, getApplicationContext(),mRecyclerView);
                            mRecyclerView.setAdapter(adapter);
                            pb1.setVisibility(View.GONE);
                            if(count==catList.size()-1){
                                b11.setVisibility(View.VISIBLE);
                                b22.setVisibility(View.GONE);
                            }else if(count==0){
                                b22.setVisibility(View.VISIBLE);
                                b11.setVisibility(View.GONE);
                            }else{
                                b22.setVisibility(View.VISIBLE);
                                b11.setVisibility(View.VISIBLE);
                            }
                            b33.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(req1);
    }

}

