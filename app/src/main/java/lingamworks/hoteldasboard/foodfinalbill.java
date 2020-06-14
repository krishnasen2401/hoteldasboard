package lingamworks.hoteldasboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lingamworks.hoteldasboard.adapter.finalbill;
import lingamworks.hoteldasboard.data.fbiller;

public class foodfinalbill extends AppCompatActivity {
    TextView tt1,t2,t3,t4,t5;
    RadioButton b1,b2;
    Button b12;
String table,dateToStr,foodlistname;
int cost;
    TextView t1;
List<fbiller> fooditemslist;
finalbill adapter;
 private RecyclerView mRecyclerView;
private RecyclerView.LayoutManager mLayoutManager;
    ImageView pb;
String host;
    public void paid(View v){
        if(b1.isChecked()){
            Senddata(this, dateToStr, table,foodlistname,cost, "cash");
        }
        if(b2.isChecked()){
            Senddata(this, dateToStr, table,foodlistname,cost, "card");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodfinalbill);
        Intent i=getIntent();
        b1=findViewById(R.id.Cash);
        b2=findViewById(R.id.Card);
        tt1=findViewById(R.id.finalname);
        t2=findViewById(R.id.Pricefinal);
        t3=findViewById(R.id.qtyfinal);
        t4=findViewById(R.id.totalfinal);
        t5=findViewById(R.id.typefinal);
        host=getString(R.string.localhost);
        foodlistname="";
        b12=findViewById(R.id.button6);
        pb=findViewById(R.id.imageViewfinal);
        Glide.with(this)
                .load(R.drawable.pizza2)
                .into(pb);
        pb.setVisibility(View.VISIBLE);
        TextView table1=findViewById(R.id.finalbilltable);
        table=i.getStringExtra("ftable");
        table1.setText("BIll ORDER FOR -"+table);
        t1=findViewById(R.id.Totalfprice);
        cost=0;
        fooditemslist=new LinkedList<>();
        mRecyclerView=findViewById(R.id.rvFFT);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        dateToStr = format.format(today);
        loadcurrenttables(table,this);
    }
    public void Senddata(final Context context, final String fdate, final String ftable, final String flist, final int price, final String mode){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = host+"/android/foodhistoryinsert.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        response = response.trim();
                        Log.i("response",response);
                        if(response.equals("\"Entry successfully created\"")){
                            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                            alertDialog.setTitle("Completed Transaction");
                            alertDialog.setMessage("WHERE YOU WANT TO GO NEXT");
                            alertDialog.setCancelable(false);
                            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "HOME", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(getApplicationContext(), loginmain.class);
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
                params.put("date", fdate);
                params.put("tableno", ftable);
                params.put("flist", flist);
                params.put("fprice", String.valueOf(price));
                params.put("mode", mode);
                return params;
            }
        };

        queue.add(stringRequest);
    }
    public void loadcurrenttables(String table, final Context context){
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

                            for(int i1=0;i1<fooditemslist.size();i1++){
                                if(i1==0)
                                    foodlistname=fooditemslist.get(i1).getFname()+"-"+fooditemslist.get(i1).getQty();
                                else
                                    foodlistname=foodlistname+","+fooditemslist.get(i1).getFname()+"-"+fooditemslist.get(i1).getQty();
                            cost=cost+fooditemslist.get(i1).getQty()*fooditemslist.get(i1).getPrice();
                            }
                            adapter=new finalbill(fooditemslist,context,mRecyclerView);
                            mRecyclerView.setAdapter(adapter);
                            t1.setText("Total payable cost -"+cost);
                            pb.setVisibility(View.GONE);
                            visible1();
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
    public void visible1(){
        tt1.setVisibility(View.VISIBLE);
        t1.setVisibility(View.VISIBLE);
        t2.setVisibility(View.VISIBLE);
        t3.setVisibility(View.VISIBLE);
        t4.setVisibility(View.VISIBLE);
        t5.setVisibility(View.VISIBLE);
        b1.setVisibility(View.VISIBLE);
        b2.setVisibility(View.VISIBLE);
        b12.setVisibility(View.VISIBLE);
    }
}
