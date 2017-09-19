package nl.devolksbank.nlhelpu.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import nl.devolksbank.nlhelpu.R;
import nl.devolksbank.nlhelpu.model.SectionModel;
import nl.devolksbank.nlhelpu.util.SectionsProvider;
import nl.devolksbank.nlhelpu.viewadapter.SectionViewAdapter;

public class MainActivity extends AppCompatActivity {

    public static final String SELECTED_SECTION_ID = "nl.devolksbank.nlhelpu.SELECTED_SUBJECT";

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "on-create");
        setContentView(R.layout.activity_main);

        SharedPreferences sharedpreferences = getSharedPreferences("my-preferences", Context.MODE_PRIVATE);

        if (!sharedpreferences.contains("splash-screen-shown")) {
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putBoolean("splash-screen-shown", true);
            editor.apply(); // asynchronous storage of data

            Intent i = new Intent(this, SplashActivity.class);
            startActivity(i);
        }

        listView = (ListView) findViewById(R.id.listMainMenu);

        SectionsProvider sectionsProvider = new SectionsProvider();

        ArrayAdapter<SectionModel> adapter = new SectionViewAdapter(this, android.R.layout.simple_list_item_1, sectionsProvider.listSections(getApplicationContext()));

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        final MainActivity thisActivity = this;

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ListView Clicked item value
                SectionModel itemValue = (SectionModel) listView.getItemAtPosition(position);
                Log.i("MainActivity", "Position : " + position + ", ListItem : " + itemValue);

                Intent intent = new Intent(thisActivity, DocumentCollectionActivity.class);

                intent.putExtra(SELECTED_SECTION_ID, itemValue.getId());
                startActivity(intent);

            }

        });

        final Button button = (Button) findViewById(R.id.buttonBegeleider);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Open the support activity
                Log.d("MainActivity", "Open support activity");
                Intent i = new Intent(getApplicationContext(),SupportActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            // TODO: open about activity
            Log.d("MainActivity", "Open about");

            Intent i = new Intent(getApplicationContext(),AboutActivity.class);
            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
