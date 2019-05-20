package lingamworks.hoteldasboard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.BreakIterator;
import java.util.List;
import lingamworks.hoteldasboard.R;
import lingamworks.hoteldasboard.data.fbiller;

public class finalbill extends RecyclerView.Adapter<finalbill.ViewHolder> {
    private List<fbiller> mlists;
    private Context mContext1;
    private RecyclerView mRecyclerV1;
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView fname111,fprice111,fqty111,ftprice111;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout=v;
            fname111=v.findViewById(R.id.fname1);
            fprice111=v.findViewById(R.id.fprice1);
            fqty111=v.findViewById(R.id.fqty1);
            ftprice111=v.findViewById(R.id.ftprice);
        }
        }
    public void add(int position, fbiller food) {
        mlists.add(position, food);
        notifyItemInserted(position);
    }
    public finalbill(List<fbiller> myDataset, Context context, RecyclerView recyclerView) {
        mlists = myDataset;
        mContext1 = context;
        mRecyclerV1 = recyclerView;
    }
    public finalbill.ViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType){
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =inflater.inflate(R.layout.row_fbill, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    public void onBindViewHolder(ViewHolder holder, final int position){
        if(position%2==0)
            holder.layout.setBackgroundResource(R.drawable.cellbg1);
        else
            holder.layout.setBackgroundResource(R.drawable.cellbg2);
        final fbiller fnames1=mlists.get(position);
        holder.fname111.setText(fnames1.getFname());
        holder.fprice111.setText(String.valueOf(fnames1.getPrice()));
        holder.fqty111.setText(String.valueOf(fnames1.getQty()));
        holder.ftprice111.setText(String.valueOf(fnames1.getQty()*fnames1.getPrice()));
    }
    public int getItemCount(){
        return  mlists.size();}
}
