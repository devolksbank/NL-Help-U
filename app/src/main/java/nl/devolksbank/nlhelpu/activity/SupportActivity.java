package nl.devolksbank.nlhelpu.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import nl.devolksbank.nlhelpu.R;
import nl.devolksbank.nlhelpu.util.BegeleiderDataSource;

public class SupportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SupportActivity", "on-create");
        setContentView(R.layout.activity_support);

        setTitle(getString(R.string.support_title));

        final BegeleiderDataSource dataSource = new BegeleiderDataSource(getApplicationContext());

        dataSource.getBegeleiderEmail();

        final EditText inputName = (EditText) findViewById(R.id.input_name);
        final EditText inputEmail = (EditText) findViewById(R.id.input_email);
        final EditText inputPhone = (EditText) findViewById(R.id.input_phone);
        final EditText inputNotes = (EditText) findViewById(R.id.input_notes);

        inputName.setText(dataSource.getBegeleiderName());
        inputEmail.setText(dataSource.getBegeleiderEmail());
        inputPhone.setText(dataSource.getBegeleiderPhone());
        inputNotes.setText(dataSource.getBegeleiderNotes());

        inputName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                Log.d("SupportActivity", "after-text-changed: inputName");
                dataSource.updateBegeleider(inputName.getText().toString(), inputEmail.getText().toString(), inputPhone.getText().toString(), inputNotes.getText().toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        inputEmail.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                Log.d("SupportActivity", "after-text-changed: inputEmail");
                dataSource.updateBegeleider(inputName.getText().toString(), inputEmail.getText().toString(), inputPhone.getText().toString(), inputNotes.getText().toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        inputPhone.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                Log.d("SupportActivity", "after-text-changed: inputPhone");
                dataSource.updateBegeleider(inputName.getText().toString(), inputEmail.getText().toString(), inputPhone.getText().toString(), inputNotes.getText().toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        inputNotes.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                Log.d("SupportActivity", "after-text-changed: inputNotes");
                dataSource.updateBegeleider(inputName.getText().toString(), inputEmail.getText().toString(), inputPhone.getText().toString(), inputNotes.getText().toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        // TODO: make email clickable
        // TODO: validate email(format)
        // TODO: validate phone(format)
        // TODO: make phone callable
        // TODO: make visible of saving settings?
        // TODO: save settings on edit
        // TODO: print the saved settings in the MainActivity when present: message otherwise
    }
}
