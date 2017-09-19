package nl.devolksbank.nlhelpu.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import nl.devolksbank.nlhelpu.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setTitle(R.string.about_header);

        TextView textView = (TextView) findViewById(R.id.textAbout);
        textView.setText(R.string.about_activity_content);

        final Button button = (Button) findViewById(R.id.buttonOpenSplashScreen);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Open the support activity
                Log.d("AboutActivity", "Open splashscreen");
                Intent i = new Intent(getApplicationContext(), SplashActivity.class);
                startActivity(i);
            }
        });
    }
}
