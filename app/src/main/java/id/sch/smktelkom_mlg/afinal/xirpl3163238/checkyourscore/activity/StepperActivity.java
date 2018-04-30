package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.R;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.SliderAdapter;

public class StepperActivity extends AppCompatActivity {
    public int[] slideViewPagers = {
            R.color.Slide1,
            R.color.Slide2,
            R.color.Slide3
    };
    String pref_key = "first_install";
    SharedPreferences sharedPreferences;
    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private TextView[] mDots;

    private SliderAdapter sliderAdapter;

    private Button btnNext;
    private Button btnPreview;

    private int mCurrentPage;
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            mCurrentPage = i;
            if (i == 0) {
                btnNext.setEnabled(true);
                btnPreview.setEnabled(false);
                btnPreview.setVisibility(View.INVISIBLE);

                btnNext.setText("Next");
                btnPreview.setText("");
            } else if (i == mDots.length - 1) {
                btnNext.setEnabled(true);
                btnPreview.setEnabled(true);
                btnPreview.setVisibility(View.VISIBLE);
                btnNext.setText("Finish");
                btnPreview.setText("Back");
            } else {
                btnNext.setEnabled(true);
                btnPreview.setEnabled(true);
                btnPreview.setVisibility(View.VISIBLE);

                btnNext.setText("Next");
                btnPreview.setText("Back");
            }
            mSlideViewPager.setBackgroundColor(getResources().getColor(slideViewPagers[i]));
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepper);
        sharedPreferences = getSharedPreferences(pref_key, MODE_PRIVATE);

        if (!sharedPreferences.getBoolean(pref_key, true)) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        mSlideViewPager = findViewById(R.id.slideViewPager);
        mDotLayout = findViewById(R.id.dotsLayout);

        btnNext = findViewById(R.id.next);
        btnPreview = findViewById(R.id.preview);

        sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);

        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (btnNext.getText().toString().equals("Next")) {
                    mSlideViewPager.setCurrentItem(mCurrentPage + 1);
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(pref_key, false);
                    editor.apply();
                    Intent i = new Intent(StepperActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        btnPreview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mSlideViewPager.setCurrentItem(mCurrentPage - 1);
            }
        });
    }

    public void addDotsIndicator(int position) {
        mDots = new TextView[3];
        mDotLayout.removeAllViews();
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.Transparan));
            mDotLayout.addView(mDots[i]);
        }
        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.Putih));
        }
    }
}
