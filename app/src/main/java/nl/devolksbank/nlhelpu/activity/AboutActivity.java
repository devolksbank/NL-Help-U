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

    public static final String EMAIL = "info@nlhelpu.nl";

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

        final Button button2 = (Button) findViewById(R.id.buttonFeedback);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Start send email to specific address activity
                sendMail();
            }

            private void sendMail() {
                Log.d("AboutActivity", "Sending mail");
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/html");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{EMAIL});
            }
        });
    }
}
