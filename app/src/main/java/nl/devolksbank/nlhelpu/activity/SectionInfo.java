package nl.devolksbank.nlhelpu.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import nl.devolksbank.nlhelpu.R;
import nl.devolksbank.nlhelpu.model.SectionModel;
import nl.devolksbank.nlhelpu.util.SectionsProvider;

public class SectionInfo extends AppCompatActivity {

    private int selectedSectionId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SectionInfo", "on-create");
        setContentView(R.layout.activity_section_info);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        selectedSectionId = intent.getIntExtra(MainActivity.SELECTED_SECTION_ID, selectedSectionId);
        Log.d("SectionInfo", "selected sectionId: " + selectedSectionId);

        SectionsProvider sectionsProvider = new SectionsProvider();
        SectionModel sectionModel = sectionsProvider.getSectionById(getApplicationContext(), selectedSectionId);

        setLocalizedTitle(sectionModel);

        setLocalizedContent(sectionModel);
    }

    private void setLocalizedTitle(final SectionModel sectionModel) {
        if (-1 != sectionModel.getNameResourceId()) {
            setTitle(sectionModel.getNameResourceId());
        } else {
            setTitle(sectionModel.getName());
        }
    }

    private void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span)
    {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                Log.d("SectionInfo", "Opening url: " + span.getURL());
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(span.getURL()));
                startActivity(i);
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    private void setLocalizedContent(final SectionModel sectionModel) {
        if (-1 != sectionModel.getDetailsResourceId()) {
            String[] contentSections = getResources().getStringArray(sectionModel.getDetailsResourceId());

            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.sectionInfoLayout);

            for (String sectionContent : contentSections) {
                Log.d("SectionInfo", "Adding section: " + sectionContent);

                CharSequence sequence = Html.fromHtml(sectionContent);
                SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
                URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
                for(URLSpan span : urls) {
                    makeLinkClickable(strBuilder, span);
                }

                TextView sectionText = new TextView(this);

                sectionText.setMovementMethod(LinkMovementMethod.getInstance());

                sectionText.setText(strBuilder);
                sectionText.setGravity(Gravity.START);
                sectionText.setPadding(20, 20, 20, 20); // LTRB
                sectionText.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                        LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                        LinearLayoutCompat.LayoutParams.WRAP_CONTENT, 1f));
                linearLayout.addView(sectionText);
            }
        } else {
            // skip
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Explicitly call the back pressed method to avoid a new create of the activity without the intent extra data
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Log.d("SectionInfo", "on-back-pressed");
        Intent intent = new Intent();
        intent.putExtra(MainActivity.SELECTED_SECTION_ID, selectedSectionId);
        setResult(1, intent);
        finish();
    }
}
