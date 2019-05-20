package lingamworks.hoteldasboard.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import lingamworks.hoteldasboard.R;
import lingamworks.hoteldasboard.data.categoriesd;
import lingamworks.hoteldasboard.submenus;

public class neCListad extends RecyclerView.Adapter<neCListad.ViewHolder>{
    private List<categoriesd> mlists;
    private Context mContext1;
    private RecyclerView mRecyclerV1;
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView categoryid;
        public View layout;
        public ViewHolder(View v) {
            super(v);
            layout=v;
            categoryid=v.findViewById(R.id.foodlistrv);
        }
    }
    public void add(int position, categoriesd ListData){
        mlists.add(position,ListData);
        notifyItemInserted(position);
    }
    public neCListad(List<categoriesd> myDataset, Context context, RecyclerView recyclerView) {
        mlists = myDataset;
        mContext1 = context;
        mRecyclerV1 = recyclerView;
    }
    public neCListad.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType){
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =inflater.inflate(R.layout.row_category, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    public void onBindViewHolder(ViewHolder holder, final int position){
        final categoriesd cat=mlists.get(position);
        if(position%2==0){
            holder.layout.setBackgroundResource(R.drawable.cellbg1);
        }else
            holder.layout.setBackgroundResource(R.drawable.cellbg2);
        holder.categoryid.setText(cat.getCatname());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent i=new Intent(mContext1,submenus.class);
                i.putExtra("name", cat.getCatname());
                i.putExtra("counter",position);
                mContext1.startActivity(i);
            }
        });

        Log.i("testrv1", cat.getCatname());
    }
    public int getItemCount(){return  mlists.size();}
}
