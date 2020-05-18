package lingamworks.hoteldasboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.List;

import lingamworks.hoteldasboard.adapter.historyfood;
import lingamworks.hoteldasboard.data.finalbilldata;
import lingamworks.hoteldasboard.database.maincreate;

public class foodhistory extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    List<finalbilldata> fooditems;
    historyfood adapter;
    String host;
    ImageView pb;
    public void exit(View v){
    AlertDialog alertDialog = new AlertDialog.Builder(foodhistory.this).create();
    alertDialog.setTitle("Exit");
    alertDialog.setMessage("WHERE YOU WANT TO GO NEXT");
    alertDialog.setCancelable(false);
    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "HOME", new DialogInterface.OnClickListener(){
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent i = new Intent(getApplicationContext(), loginmain.class);
            Log.i("context tracker",getApplicationContext().getClass().getName());
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
    });
    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Ongoing Orders", new DialogInterface.OnClickListener(){

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent i = new Intent(getApplicationContext(), Biller.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
    });
    alertDialog.show();
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodhistory);
        host=getString(R.string.localhost);
        setTitle("Orders History");
        fooditems=new ArrayList<>();
        fooditems=new ArrayList<>();
        pb=findViewById(R.id.imageViewhis);
        Glide.with(this)
                .load(R.drawable.pizza2)
                .into(pb);
        pb.setVisibility(View.VISIBLE);
        mRecyclerView=findViewById(R.id.rvFHIS);
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        foodhistoryList();
    }
    private void foodhistoryList() {
        JsonArrayRequest req = new JsonArrayRequest(host+"/android/foodhistory.php",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //   Log.d(TAG, response.toString());

                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject fb = (JSONObject) response
                                        .get(i);

                                finalbilldata  fbd= new finalbilldata(fb.getString("date"),fb.getString("tableno"),fb.getString("flist"),Integer.parseInt(fb.getString("fprice")),fb.getString("mode"));
                                fooditems.add(fbd);
                            }

                            //creating custom adapter object
                            adapter = new historyfood(fooditems, getApplicationContext(),mRecyclerView);
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
