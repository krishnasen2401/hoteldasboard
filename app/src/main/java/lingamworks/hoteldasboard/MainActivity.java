package lingamworks.hoteldasboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import lingamworks.hoteldasboard.database.maincreate;


public class MainActivity extends AppCompatActivity {
SharedPreferences sharedPref ;
SharedPreferences.Editor editor;
    maincreate mv;
public void placeorder(View v){
    Intent i=new Intent(this,OrderMenu.class);
    startActivity(i);
}
    public void tester(View v){
        Intent i=new Intent(this,submenus.class);
        startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         mv=new maincreate(this);
        sharedPref = this.getSharedPreferences("tables",Context.MODE_PRIVATE);
         editor = sharedPref.edit();
        editor.putString("table", "null");
        editor.putString("edittable", "null");
        editor.commit();
        Log.i("table",sharedPref.getString("table","-1"));
        LayoutInflater inflater = LayoutInflater.from(this);
        View alertLayout = inflater.inflate(R.layout.alert_add_items, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        final EditText quantit = alertLayout.findViewById(R.id.alertInput);
        alertDialog.setTitle("INFORMATION NEEDED");
        quantit.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialog.setMessage("Enter the Table");
        alertDialog.setView(alertLayout);
        alertDialog.setCancelable(false);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Continue", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
               String table=quantit.getText().toString();
               if(table.isEmpty())
                   repeat();
               else
                editor = sharedPref.edit();
                editor.putString("table", table);
                editor.commit();
                mv.clear(table);
            }
        });
        alertDialog.show();
    }
    public void repeat(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View alertLayout = inflater.inflate(R.layout.alert_add_items, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        final EditText quantit = alertLayout.findViewById(R.id.alertInput);
        quantit.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialog.setTitle("Empty input");
        alertDialog.setIcon(R.drawable.warning);
        alertDialog.setMessage("Enter the Table");
        alertDialog.setView(alertLayout);
        alertDialog.setCancelable(false);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Continue", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String table=quantit.getText().toString();
                if(table.isEmpty())
                    repeat();
                else
                editor = sharedPref.edit();
                editor.putString("table", table);
                editor.commit();
                mv.clear(table);
            }
        });
        alertDialog.show();
    }
    }
