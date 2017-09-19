package nl.devolksbank.nlhelpu.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import nl.devolksbank.nlhelpu.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Button button = (Button) findViewById(R.id.buttonCloseSplashscreen);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Explicitly call the back pressed method to avoid a new create of the activity without the intent extra data
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Log.d("DocPartViewActivity", "on-back-pressed");
        Intent intent = new Intent();
        setResult(1, intent);
        finish();
    }
}
