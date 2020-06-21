package www.fenibazar.com;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        progressBar=findViewById(R.id.progress_bar);
        textView=findViewById(R.id.text);

        progressBar.setMax(100);
        progressBar.setScaleY(3f);
        progressanimation();



    }

    public void progressanimation(){
        ProgressBarAnimation anim=new ProgressBarAnimation(this,progressBar,textView,0f,100f);
        anim.setDuration(4000);
        progressBar.setAnimation(anim);

    }
}
