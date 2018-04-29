package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.R;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.fragment.EditBabFragment;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.fragment.IsiNilaiGuruFragment;

public class IsiNilaiGuruActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isi_nilai_guru);

        Toolbar toolbar = findViewById(R.id.toolbarNilai);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_input_nilai, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.inputnilaiSave:
                return false;
            case R.id.inputnilaiDelete:
                return false;
        }
        return super.onOptionsItemSelected(item);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new IsiNilaiGuruFragment();
                case 1:
                    return new EditBabFragment();
                default:
                    return new IsiNilaiGuruFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
