package lingamworks.hoteldasboard.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import lingamworks.hoteldasboard.R;
import lingamworks.hoteldasboard.data.finalbilldata;

public class historyfood extends RecyclerView.Adapter<historyfood.ViewHolder> {
    private List<finalbilldata> mlists;
    private Context mContext1;
    private RecyclerView mRecyclerV1;
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView hdate,htable,hlist,hmode,hprice;
        public View layout;
        public ViewHolder(View v) {
            super(v);
            layout=v;
            hdate=v.findViewById(R.id.hdate);
            htable=v.findViewById(R.id.htable);
            hlist=v.findViewById(R.id.hFlist);
            hprice=v.findViewById(R.id.hfprice);
            hmode=v.findViewById(R.id.hmode);
        }
    }
    public void add(int position, finalbilldata food) {
        mlists.add(position, food);
        notifyItemInserted(position);
    }
    public historyfood(List<finalbilldata> myDataset, Context context, RecyclerView recyclerView) {
        mlists = myDataset;
        mContext1 = context;
        mRecyclerV1 = recyclerView;
    }
    public historyfood.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType){
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =inflater.inflate(R.layout.row_food_history, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    public void onBindViewHolder(historyfood.ViewHolder holder, final int position){
        final finalbilldata fb=mlists.get(position);
        if(position%2==0)
            holder.layout.setBackgroundResource(R.drawable.cellbg1);
        else
            holder.layout.setBackgroundResource(R.drawable.cellbg2);
        holder.hdate.setText("Date and time:- "+fb.getDate());
        holder.htable.setText("Table :- "+fb.getTable());
        String temp[]=fb.getFlist().split(",");
        String newtemp=null;
        for(int i11=0;i11<temp.length;i11++){
            if(i11==0)
                newtemp=temp[i11];
            else
                newtemp=newtemp+"\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t "+temp[i11];
        }
        holder.hlist.setText("items ordered :- "+newtemp);
        holder.hprice.setText("Total Price = "+String.valueOf(fb.getPrice()));
        holder.hmode.setText("Paid in "+fb.getMode());
    }
    public int getItemCount(){
        return  mlists.size();}
}
