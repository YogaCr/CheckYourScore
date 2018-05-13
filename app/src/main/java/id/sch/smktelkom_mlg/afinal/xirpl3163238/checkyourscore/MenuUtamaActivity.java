package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MenuUtamaActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String pref_key = "IS_GURU";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_menu:
                    if (sharedPreferences.getBoolean(pref_key, true)) {
                        getSupportActionBar().setTitle("Menu Guru");
                        return loadFragment(new MenuGuruFragment());
                    } else {
                        getSupportActionBar().setTitle("Menu Siswa");
                        return loadFragment(new MenuSiswaFragment());
                    }
                case R.id.navigation_profil:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utama);
        sharedPreferences = getSharedPreferences(pref_key, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(pref_key, true)) {
            getSupportActionBar().setTitle("Menu Guru");
            loadFragment(new MenuGuruFragment());
        } else {
            getSupportActionBar().setTitle("Menu Siswa");
            loadFragment(new MenuSiswaFragment());
        }
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        } else {
            return false;
        }
    }

}
