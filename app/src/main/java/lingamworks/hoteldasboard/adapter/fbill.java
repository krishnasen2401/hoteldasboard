package lingamworks.hoteldasboard.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

import lingamworks.hoteldasboard.Biller;
import lingamworks.hoteldasboard.R;
import lingamworks.hoteldasboard.data.fbiller;
import lingamworks.hoteldasboard.database.maincreate;

public class fbill extends RecyclerView.Adapter<fbill.ViewHolder> {
    private List<fbiller> mlists;
    private Context mContext1;
    private RecyclerView mRecyclerV1;
    private maincreate dbhelper;
    String flag;
    int quantity;
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        public TextView fname111,fprice111,fqty111,ftprice111;
        public View layout;
        public ViewHolder(View v) {
            super(v);
            layout=v;
            fname111=v.findViewById(R.id.fname1);
            fprice111=v.findViewById(R.id.fprice1);
            fqty111=v.findViewById(R.id.fqty1);
            ftprice111=v.findViewById(R.id.ftprice);
            Log.i("status",flag);
            if(flag=="enable") {
                v.setOnCreateContextMenuListener(this);
            }else
            {}
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem Edit = menu.add(Menu.NONE, 1, 1, "Edit");
            MenuItem Delete = menu.add(Menu.NONE, 2, 2, "Remove");
            Delete.setOnMenuItemClickListener(test);
            Edit.setOnMenuItemClickListener(test);
        }
        final MenuItem.OnMenuItemClickListener test=new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                dbhelper=new maincreate(mContext1);
                switch (item.getItemId()){
                    case 1:Toast.makeText(mContext1,"Editing:-"+fname111.getText()+"",Toast.LENGTH_SHORT).show();
                    LayoutInflater inflater = LayoutInflater.from(mContext1);
                    View alertLayout = inflater.inflate(R.layout.alert_add_items, null);
                    AlertDialog alertDialog = new AlertDialog.Builder(mContext1).create();
                    final EditText quantit = alertLayout.findViewById(R.id.alertInput);
                    alertDialog.setTitle(fname111.getText()+"-"+fprice111.getText());
                    alertDialog.setMessage("Enter the Quantity");
                    alertDialog.setView(alertLayout);
                    alertDialog.setCancelable(false);
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Add", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(quantit.getText().toString().isEmpty()){
                                    error();
                                }
                                else {
                                    quantity = Integer.parseInt(quantit.getText().toString());
                                    dbhelper.editfood(fname111.getText().toString(), quantity);
                                    fqty111.setText(String.valueOf(quantity));
                                    ftprice111.setText(String.valueOf(quantity * Integer.parseInt(fprice111.getText().toString())));
                                    SharedPreferences fp = mContext1.getSharedPreferences("tables",Context.MODE_PRIVATE);
                                    fbiller fb=new fbiller(fname111.getText().toString(),Integer.parseInt(fprice111.getText().toString()),quantity,fp.getString("table","-1"));
                                    mlists.set(getAdapterPosition(),fb);
                                    notifyItemChanged(getAdapterPosition(),fb);
                                } }
                        });
                        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //nothing to be done here required when back button is set false i.e setscancelable false
                            }
                        });
                        alertDialog.show();
                    break;
                    case 2:Toast.makeText(mContext1,"Removed-"+fname111.getText(),Toast.LENGTH_SHORT).show();
                    dbhelper.clearfood(fname111.getText().toString());
                    mlists.remove(getAdapterPosition());
                    mRecyclerV1.removeViewAt(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), mlists.size());
                        notifyDataSetChanged();
                    break;
                }
                return false;
            }
        };
    }
    public void add(int position, fbiller ListData){
        mlists.add(position,ListData);
        notifyItemInserted(position);
    }
    public fbill(List<fbiller> myDataset, Context context, RecyclerView recyclerView,String flag) {
        mlists = myDataset;
        mContext1 = context;
        mRecyclerV1 = recyclerView;
        this.flag=flag;
    }
    public fbill.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType){
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =inflater.inflate(R.layout.row_fbill, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    public void onBindViewHolder(ViewHolder holder, final int position){
        final fbiller fnames1=mlists.get(position);
        if(position%2==0)
            holder.layout.setBackgroundResource(R.drawable.cellbg1);
        else
            holder.layout.setBackgroundResource(R.drawable.cellbg2);
        holder.fname111.setText(fnames1.getFname());
        holder.fprice111.setText(String.valueOf(fnames1.getPrice()));
        holder.fqty111.setText(String.valueOf(fnames1.getQty()));
        holder.ftprice111.setText(String.valueOf(fnames1.getQty()*fnames1.getPrice()));
    }
    public int getItemCount(){
        return  mlists.size();}
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
