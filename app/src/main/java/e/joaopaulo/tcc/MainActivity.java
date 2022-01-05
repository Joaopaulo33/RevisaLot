package e.joaopaulo.tcc;

import android.content.Intent;
import android.support.graphics.drawable.AnimationUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Animation upToDown,downToTop;
    Button button;
    ImageView imageView;
    LinearLayout linearLayout;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        upToDown = AnimationUtils.loadAnimation(this,R.anim.updown);
        downToTop = AnimationUtils.loadAnimation(this,R.anim.downtop);

        button = (Button) findViewById(R.id.button);
        button.setAnimation(downToTop);

        imageView = (ImageView) findViewById(R.id.imagem);
        imageView.setAnimation(upToDown);

        linearLayout = (LinearLayout) findViewById(R.id.line1);
        linearLayout.setAnimation(downToTop);

        textView= (TextView) findViewById(R.id.login);
        textView.setAnimation(upToDown);


    }

}
