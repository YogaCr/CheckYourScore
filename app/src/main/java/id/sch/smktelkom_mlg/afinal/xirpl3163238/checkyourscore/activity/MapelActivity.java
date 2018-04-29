package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.SubtitleCollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.R;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.fragment.Statistik;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.fragment.TugasFragment;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.fragment.UlanganFragment;

/**
 * Created by Ferlina Firdausi on 28/03/2018.
 */

public class MapelActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    String uniqueCode;
    Intent i;
    ImageView ivIcon, ivSampul;
    Toolbar toolbar;
    TabLayout tabLayout;
    SubtitleCollapsingToolbarLayout ctl;
    NestedScrollView nestedScrollView;
    ProgressDialog progressDialog;
    Double KKM;
    FirebaseAuth mAuth;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapel);
        mAuth = FirebaseAuth.getInstance();
        tabLayout = findViewById(R.id.tabs);
        tabLayout.getTabAt(0).setIcon(R.drawable.icon_graph);
        tabLayout.getTabAt(1).setIcon(R.drawable.icon_nilai);
        tabLayout.getTabAt(2).setIcon(R.drawable.icon_nilaiulangan);
        nestedScrollView = findViewById(R.id.nested);
        nestedScrollView.setFillViewport(true);
        ivIcon = findViewById(R.id.gambarmapelsiswa);
        ivSampul = findViewById(R.id.ivSampulSiswa);
        ctl = findViewById(R.id.ctl);
        toolbar = findViewById(R.id.tb);
        setSupportActionBar(toolbar);
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
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Harap Tunggu");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        firestore = FirebaseFirestore.getInstance();
        i = getIntent();
        uniqueCode = i.getStringExtra("UniqueCode");
        getData();

    }

    void getData() {
        progressDialog.show();
        firestore.collection("Mapel").document(uniqueCode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    ctl.setTitle(task.getResult().getString("Nama"));
                    KKM = task.getResult().getDouble("KKM");
                    int resDraw = getResources().getIdentifier(task.getResult().getString("Icon"), "Drawable", getPackageName());
                    ivIcon.setImageDrawable(getResources().getDrawable(resDraw));
                    if (task.getResult().contains("Sampul")) {
                        Glide.with(MapelActivity.this).load(task.getResult().getString("Sampul")).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressDialog.hide();
                                return false;
                            }
                        }).into(ivSampul);
                    } else {

                    }
                    firestore.collection("User").document(task.getResult().getString("UID Guru")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            ctl.setSubtitle(task.getResult().getString("Nama"));
                        }
                    });
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MapelActivity.this);
                    builder.setTitle("Error");
                    builder.setMessage("Gagal mengambil data");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onBackPressed();
                        }
                    });
                    builder.show();
                }
                progressDialog.hide();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mapel_siswa, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.siswa_keluar:
                keluar();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void keluar() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Yakin mau keluar?");
        builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firestore.collection("JoinSiswa").whereEqualTo("Mapel", uniqueCode).whereEqualTo("UID", mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot ds : task.getResult()) {
                            firestore.collection("JoinSiswa").document(ds.getId()).delete();
                        }
                    }
                });
                firestore.collection("Mapel").document(uniqueCode).collection("Bab").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot ds : task.getResult()) {
                            String id = ds.getId();
                            firestore.collection("Mapel").document(uniqueCode).collection("Bab").document(id).collection("Nilai").document(mAuth.getCurrentUser().getUid()).delete();
                        }
                        Intent i = new Intent(MapelActivity.this, MenuSiswaActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                });
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.create().dismiss();
            }
        });
        builder.create().show();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Statistik();
                case 1:
                    return new TugasFragment();
                case 2:
                    return new UlanganFragment();
                default:
                    return new Statistik();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
