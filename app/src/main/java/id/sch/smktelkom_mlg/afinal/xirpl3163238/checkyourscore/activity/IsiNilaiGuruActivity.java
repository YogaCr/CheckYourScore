package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.R;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.fragment.EditBabFragment;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.fragment.IsiNilaiGuruFragment;

public class IsiNilaiGuruActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    String uniqueCode, idBab;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isi_nilai_guru);
        firestore = FirebaseFirestore.getInstance();
        uniqueCode = getIntent().getStringExtra("uniqueCode");
        idBab = getIntent().getStringExtra("idBab");
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

    void hapusBab() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Yakin mau menghapus?");
        builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firestore.collection("Mapel").document(uniqueCode).collection("Bab").document(idBab).delete();
                firestore.collection("JoinSiswa").whereEqualTo("Mapel", uniqueCode).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot ds : task.getResult()) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("Notif", true);
                                firestore.collection("JoinSiswa").document(ds.getId()).update(map);
                            }
                        }
                    }
                });
                Intent i = new Intent(IsiNilaiGuruActivity.this, MapelGuruActivity.class);
                i.putExtra("UniqueCode", uniqueCode);
                startActivity(i);
                finish();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.create().dismiss();
            }
        });
        builder.show();
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
                hapusBab();
                finish();
                return true;
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
