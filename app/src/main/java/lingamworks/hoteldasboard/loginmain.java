package lingamworks.hoteldasboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class loginmain extends AppCompatActivity {
public void Customlogin(View V){
    Intent i=new Intent(this,MainActivity.class);
    startActivity(i);
}
public void adminlogin(View v){
    LayoutInflater inflater = LayoutInflater.from(this);
    View alertLayout = inflater.inflate(R.layout.alert_add_items, null);
    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
    final EditText quantit = alertLayout.findViewById(R.id.alertInput);
    quantit.setInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_VARIATION_PASSWORD);
    alertDialog.setTitle("INFORMATION NEEDED");
    alertDialog.setMessage("Enter Passcode");
    alertDialog.setView(alertLayout);
    alertDialog.setCancelable(false);
    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Enter", new DialogInterface.OnClickListener(){
        @Override
        public void onClick(DialogInterface dialog, int which) {
           String passcode=quantit.getText().toString();
            if(passcode.equals("1234")){
                Intent i=new Intent(loginmain.this,adminactivity.class);
                startActivity(i);
            }else{
                error();
            }
        }
    });
    alertDialog.show();
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginmain);
    }
    public void error(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Error");
        alertDialog.setIcon(R.drawable.warning);
        alertDialog.setMessage("Wrong Passcode Entered");
        alertDialog.setCancelable(false);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Back", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }
}
