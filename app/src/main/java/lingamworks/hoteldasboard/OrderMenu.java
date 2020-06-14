package lingamworks.hoteldasboard;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import lingamworks.hoteldasboard.adapter.neCListad;
import lingamworks.hoteldasboard.data.categoriesd;

public class OrderMenu extends AppCompatActivity {
    neCListad adapter;
    List<categoriesd> heroList;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    String host;
    ImageView pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_menu);
        pb=findViewById(R.id.imageView);
        Glide.with(this)
                .load(R.drawable.pizza2)
                .into(pb);
        pb.setVisibility(View.VISIBLE);
        host=getString(R.string.localhost);
        heroList=new LinkedList<>();
        mRecyclerView=findViewById(R.id.rvFood);
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        mRecyclerView.addItemDecoration(itemDecor);
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        loadHeroList();
    }
    private void loadHeroList() {
        JsonArrayRequest req = new JsonArrayRequest(host+"/android/hotelcatre.php",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //   Log.d(TAG, response.toString());

                        try {
                            Log.i("hello","retrieveing");
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);


                                categoriesd hero = new categoriesd(person.getString("category"));
                                heroList.add(hero);
                            }
                            //creating custom adapter object
                            adapter = new neCListad(heroList, getApplicationContext(),mRecyclerView);
                            //adding the adapter to listview
                            mRecyclerView.setAdapter(adapter);
                            pb.setVisibility(View.GONE);
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
}
