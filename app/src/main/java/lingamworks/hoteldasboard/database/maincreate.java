package lingamworks.hoteldasboard.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lingamworks.hoteldasboard.data.fbiller;
import lingamworks.hoteldasboard.data.finalbilldata;

public class maincreate extends SQLiteOpenHelper {
    Context context;
    public static final String DATABASE_NAME = "order";
    public static final String TABLE_Name_Order = "currentorder";
    public static final String ORDER_COLUMN_TABLE="tablename";
    public static final String ORDER_COLUMN_FID = "fname";
    public static final String ORDER_COLUMN_Price = "fprice";
    public static final String ORDER_COLUMN_QTY = "qty";
    public maincreate(Context context) {
        super(context, DATABASE_NAME , null, 1);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + TABLE_Name_Order + " ("+ORDER_COLUMN_TABLE+" Varchar(45),"+ORDER_COLUMN_FID+" Varchar(45),"+ORDER_COLUMN_Price+" Integer,"+ORDER_COLUMN_QTY+" Integer);");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you can implement here migration process
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Name_Order);
        this.onCreate(db);
    }
    public void AddFood(fbiller fb){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cn=new ContentValues();
        cn.put(ORDER_COLUMN_TABLE,fb.getTable());
        cn.put(ORDER_COLUMN_FID,fb.getFname());
        cn.put(ORDER_COLUMN_Price,fb.getPrice());
        cn.put(ORDER_COLUMN_QTY,fb.getQty());
        db.insert(TABLE_Name_Order, null, cn);
        cn.clear();
        db.close();
    }
    public List<fbiller> fbillerList(String tableno) {
        List<fbiller> fbillerLinkedList = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM currentorder WHERE tablename='"+tableno+"'", null);
        fbiller fbillerfood;

        if (cursor.moveToFirst()) {
            do {
                fbillerfood = new fbiller();
                Log.i("row", String.valueOf(cursor.getString(cursor.getColumnIndex(ORDER_COLUMN_TABLE))));
                fbillerfood.setFname(cursor.getString(cursor.getColumnIndex(ORDER_COLUMN_FID)));
                fbillerfood.setPrice(cursor.getInt(cursor.getColumnIndex(ORDER_COLUMN_Price)));
                fbillerfood.setQty(cursor.getInt(cursor.getColumnIndex(ORDER_COLUMN_QTY)));
                fbillerLinkedList.add(fbillerfood);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return fbillerLinkedList;
    }
    public void clearfood(String fname){
        SQLiteDatabase db = this.getWritableDatabase();
        SharedPreferences prefs=context.getSharedPreferences("tables",Context.MODE_PRIVATE);
        db.delete(TABLE_Name_Order, ORDER_COLUMN_TABLE +"='"+prefs.getString("edittable","-1")+"' and "+ORDER_COLUMN_FID+"='"+fname+"'",null);
        db.close();
    }
    public void editfood(String fname,int qty){
        SQLiteDatabase db = this.getWritableDatabase();
        SharedPreferences prefs=context.getSharedPreferences("tables",Context.MODE_PRIVATE);
        ContentValues cv = new ContentValues();
        cv.put(ORDER_COLUMN_QTY,qty);
        db.update(TABLE_Name_Order,cv,ORDER_COLUMN_TABLE +"='"+prefs.getString("edittable","-1")+"' AND "+ORDER_COLUMN_FID+"='"+fname+"'",null);
        db.close();
    }
    public void clear(String tableno) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Name_Order, ORDER_COLUMN_TABLE +"='"+tableno+"'",null);
        db.close();
    }
}
