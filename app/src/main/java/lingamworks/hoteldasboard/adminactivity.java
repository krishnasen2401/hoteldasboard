package lingamworks.hoteldasboard;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class adminactivity extends AppCompatActivity {
public void pending(View v){
    Intent i=new Intent(this,Biller.class);
    startActivity(i);
}
public void orderhistory(View v){
    Intent i=new Intent(this,foodhistory.class);
    startActivity(i);
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminactivity);
    }
}
