package lingamworks.hoteldasboard.adapter;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import lingamworks.hoteldasboard.R;
import lingamworks.hoteldasboard.data.fbiller;
import lingamworks.hoteldasboard.data.fooditems;
import lingamworks.hoteldasboard.database.maincreate;

public class cListad extends RecyclerView.Adapter<cListad.ViewHolder> {
    maincreate helper;
    private List<fooditems> mlists;
    private Context mContext1;
    private RecyclerView mRecyclerV1;
    SharedPreferences prefs;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView fooditems, price,fdisk;
        public View layout;
        public Button b1;
        public ImageView v1;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            fooditems = v.findViewById(R.id.fooditems);
            price = v.findViewById(R.id.foodprices);
            b1=v.findViewById(R.id.button4);
            v1=v.findViewById(R.id.fimageView);
            fdisk=v.findViewById(R.id.fdescription);
        }
    }

    public void add(int position, fooditems ListData) {
        mlists.add(position, ListData);
        notifyItemInserted(position);
    }

    public cListad(List<fooditems> myDataset, Context context, RecyclerView recyclerView) {
        mlists = myDataset;
        mContext1 = context;
        mRecyclerV1 = recyclerView;
    }

    public cListad.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                 int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.row_iteams, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        prefs=mContext1.getSharedPreferences("tables",Context.MODE_PRIVATE);
        final fooditems foodnames1 = mlists.get(position);
        if(position%2==0)
            holder.layout.setBackgroundResource(R.drawable.cellbg1);
        else
            holder.layout.setBackgroundResource(R.drawable.cellbg2);
        holder.fooditems.setText(foodnames1.getName());
        holder.price.setText(foodnames1.getPrice());
        String desc=foodnames1.getDescription();
        if(desc.isEmpty())
            holder.fdisk.setVisibility(View.GONE);
        else {
            holder.fdisk.setVisibility(View.VISIBLE);
            holder.fdisk.setText(foodnames1.getDescription());
        }String host=mContext1.getString(R.string.localhost);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(mContext1)
                .load(host+foodnames1.getImagelocal())
                .apply(requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE))
                .apply(requestOptions.skipMemoryCache(true))
                .apply(new RequestOptions().override(Target.SIZE_ORIGINAL))
                .into(holder.v1);
        holder.b1.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(final View v) {
                LayoutInflater inflater = LayoutInflater.from(v.getContext());
                helper =new maincreate(v.getContext());
                View alertLayout = inflater.inflate(R.layout.alert_add_items, null);
                final EditText quantit = alertLayout.findViewById(R.id.alertInput);
                AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                alertDialog.setTitle(foodnames1.getName()+"-"+foodnames1.getPrice());
                alertDialog.setMessage("Enter the Quantity");
                alertDialog.setView(alertLayout);
                alertDialog.setCancelable(false);
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Add", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(quantit.getText().toString().isEmpty())
                            error();
                        else {
                            int quantity = Integer.parseInt(quantit.getText().toString());
                            fbiller fb = new fbiller(foodnames1.getName(), Integer.parseInt(foodnames1.getPrice()), quantity, prefs.getString("table", "-1"));
                            helper.AddFood(fb);
                        }
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //nothing to be done here required when back button is set false i.e setscancelable false
                    }
                });
                alertDialog.show();
            }
        });
    }

        public int getItemCount () {
            return mlists.size();
        }
        public void error(){
            AlertDialog alertDialog = new AlertDialog.Builder(mContext1).create();
            alertDialog.setTitle("Error");
            alertDialog.setIcon(R.drawable.warning);
            alertDialog.setMessage("You have not input the number");
            alertDialog.setCancelable(false);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            alertDialog.show();
        }
    }
