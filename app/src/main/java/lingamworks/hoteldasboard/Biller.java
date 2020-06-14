package lingamworks.hoteldasboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lingamworks.hoteldasboard.adapter.fbill;
import lingamworks.hoteldasboard.data.fbiller;
import lingamworks.hoteldasboard.database.maincreate;

public class Biller extends AppCompatActivity implements  AdapterView.OnItemSelectedListener
{
    SharedPreferences prefs;
    maincreate dbhelper;
    fbill adaper1;
    String selected,host;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<String> currentables;
    List<fbiller> fooditemslist;
    Spinner t2;
    Intent i;
    int counter;
    String orderstatus,table;
    ArrayAdapter<String> spinnerArrayAdapter;
    SharedPreferences.Editor editor;
    ImageView pb;
    Button b123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biller);
        i=getIntent();
        table=null;
        b123=findViewById(R.id.killerbutton);
        pb=findViewById(R.id.imageViewbill);
        Glide.with(this)
                .load(R.drawable.pizza2)
                .into(pb);
        host=getString(R.string.localhost);
        prefs = this.getSharedPreferences("tables",Context.MODE_PRIVATE);
        t2=findViewById(R.id.restoreTable);
        counter=i.getIntExtra("counter", -1);
        mRecyclerView=findViewById(R.id.rvBiller);
        orderstatus=null;
        dbhelper=new maincreate(this);
        currentables=new ArrayList<>();
        fooditemslist=new LinkedList<>();
        spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currentables);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        t2.setAdapter(spinnerArrayAdapter);
        t2.setOnItemSelectedListener(this);
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        if(counter!=-1)
        {
            pb.setVisibility(View.GONE);
            orderstatus="hidden";
            table=prefs.getString("table","-1");
            TextView textView=findViewById(R.id.customsetter);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setTextSize(18);
            textView.setText("Current Order For Table No-"+i.getStringExtra("table"));
          t2.setVisibility(View.GONE);
            Button b1=findViewById(R.id.killerbutton);
            b1.setText("Confirm order");
           customsetter(i.getStringExtra("table"));
        }else{
            b123.setVisibility(View.INVISIBLE);
            loadcurrenttables();
            pb.setVisibility(View.VISIBLE);
            setTitle("Pending Order and Current");
        }
    }
    public void Paid(View v){
        if(orderstatus=="hidden"){
            Senddata(this,fooditemslist);
            Intent i = new Intent(this, loginmain.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();

        }
        else{
            Intent i = new Intent(this, foodfinalbill.class);
            i.putExtra("ftable",selected);
            startActivity(i);
            finish();
        }
    }
private void customsetter(String godfood){
        fooditemslist.clear();
        editor = prefs.edit();
        editor.putString("edittable", godfood);
        editor.commit();
        fooditemslist=dbhelper.fbillerList(godfood);
        if(fooditemslist.isEmpty()){
            AlertDialog alertDialog = new AlertDialog.Builder(Biller.this).create();
            alertDialog.setTitle("ERROR");
            alertDialog.setMessage("You havent selected any items");
            alertDialog.setIcon(R.drawable.warning);
            alertDialog.setCancelable(false);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
            });
            alertDialog.show();
        }
        adaper1=new fbill(fooditemslist,this,mRecyclerView,"enable");
        mRecyclerView.setAdapter(adaper1);
}

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        pb.setVisibility(View.VISIBLE);
        selected=parent.getItemAtPosition(position).toString();
        fooditemslist.clear();
        editor = prefs.edit();
        editor.putString("edittable", selected);
        editor.commit();
        adaper1=new fbill(fooditemslist,this,mRecyclerView,"disable");
        loadcurrenttables(selected);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
    public void Senddata(final Context context, List<fbiller> fb) {
        Gson gson=new Gson();
        final String newDataArray=gson.toJson(fb);
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = host+"/android/currentorderinsert.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = response.trim();
                        Log.i("response",response);
                        dbhelper.clear(table);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("Response1",error.toString());
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("tableno",table);
                params.put("dataarray", newDataArray);
                return params;
            }
        };

        queue.add(stringRequest);
    }
    public void loadcurrenttables(){
            JsonArrayRequest req = new JsonArrayRequest(host+"/android/fetchcurrent.php?status=table",
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            //   Log.d(TAG, response.toString());

                            try {
                                for (int i = 0; i < response.length(); i++) {

                                    JSONObject fb = (JSONObject) response
                                            .get(i);
                                    currentables.add(fb.getString("tablename"));
                                }
                                //add adapter here
                                if(currentables.isEmpty()){
                                    pb.setVisibility(View.GONE);
                                    AlertDialog alertDialog = new AlertDialog.Builder(Biller.this).create();
                                    alertDialog.setTitle("ERROR");
                                    alertDialog.setMessage("There 0 Current Orders");
                                    alertDialog.setCancelable(false);
                                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent i = new Intent(getApplicationContext(), adminactivity.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(i);
                                            finish();
                                        }
                                    });
                                    alertDialog.show();
                                }
                                t2.setAdapter(spinnerArrayAdapter);
                                b123.setVisibility(View.VISIBLE);
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
    public void loadcurrenttables(String table){
        JsonArrayRequest req = new JsonArrayRequest(host+"/android/fetchcurrent.php?status=currenttable&current="+table,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //   Log.d(TAG, response.toString());

                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject fb = (JSONObject) response
                                        .get(i);
                                fbiller fbb=new fbiller(fb.getString("fname"),Integer.parseInt(fb.getString("fprice")),Integer.parseInt(fb.getString("qty")),"tabele");
                                fooditemslist.add(fbb);
                            }
                            //add adapter here
                            pb.setVisibility(View.GONE);
                            mRecyclerView.setAdapter(adaper1);
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
