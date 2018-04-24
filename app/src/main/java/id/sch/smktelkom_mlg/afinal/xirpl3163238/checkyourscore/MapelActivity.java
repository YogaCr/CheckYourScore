package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Ferlina Firdausi on 28/03/2018.
 */

public class MapelActivity extends AppCompatActivity {
    ImageView imageView;
    Toolbar toolbar;
    TabLayout tabLayout;
    CollapsingToolbarLayout ctl;
    NestedScrollView nestedScrollView;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapel);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.getTabAt(0).setIcon(R.drawable.icon_nilai);
        tabLayout.getTabAt(1).setIcon(R.drawable.icon_graph);
        nestedScrollView = findViewById(R.id.nested);
        nestedScrollView.setFillViewport(true);
        imageView = findViewById(R.id.gambarmapel);
        ctl = findViewById(R.id.ctl);
        toolbar = findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mViewPager.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new TugasFragment();
                case 1:
                    return new UlanganFragment();
                case 2:
                    return new Statistik();
                default:
                    return new TugasFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
