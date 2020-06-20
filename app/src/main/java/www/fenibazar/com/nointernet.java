package www.fenibazar.com;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class nointernet extends AppCompatActivity {

    RelativeLayout relativeLayout;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nointernet);
        button=(Button)findViewById(R.id.btn);
        relativeLayout=(RelativeLayout)findViewById(R.id.Relative);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(nointernet.this,firstpage.class);
                startActivity(intent);
                finish();
            }
        });

        if(!isConnected(www.fenibazar.com.nointernet.this))
        {
            relativeLayout.setVisibility(View.VISIBLE);
        }
        else{
            Intent intent = new Intent(getApplicationContext(),firstpage.class);
            startActivity(intent);
            finish();
        }
    }

    public boolean isConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=cm.getActiveNetworkInfo();
        if (networkInfo!=null && networkInfo.isConnectedOrConnecting()){
            android.net.NetworkInfo wifi=cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile=cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile!=null && mobile.isConnectedOrConnecting()) ||
                    (wifi!=null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        }
        else return false;
    }


}
