package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.Class.MapelClass;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.R;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.adapter.MapelAdapter;

public class MenuGuruActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth mAuth;
    TextView tvNamaGuru, tvEmailGuru;
    NavigationView navigationView;
    View headerview;
    RecyclerView rvMapel;
    List<MapelClass> mapelList = new ArrayList<>();
    MapelAdapter mapelAdapter;
    FirebaseFirestore firestore;
    ProgressDialog progressDialog;
    ImageView ivProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_guru);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Harap Tunggu");

        progressDialog.setCanceledOnTouchOutside(false);
        navigationView = findViewById(R.id.nav_view);

        rvMapel = findViewById(R.id.rvMapelGuru);
        findViewById(R.id.tvMenuGuruNone).setVisibility(View.VISIBLE);
        headerview = navigationView.getHeaderView(0);
        tvNamaGuru = headerview.findViewById(R.id.tvNamaGuru);
        tvEmailGuru = headerview.findViewById(R.id.tvEmailGuru);
        ivProfil = headerview.findViewById(R.id.ivProfilGuru);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        tvNamaGuru.setText(mAuth.getCurrentUser().getDisplayName());
        mapelAdapter = new MapelAdapter(MenuGuruActivity.this, mapelList, true);

        tvEmailGuru.setText(mAuth.getCurrentUser().getEmail());
        getData();
        getGambar();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuGuruActivity.this, BuatMapelActivity.class);
                startActivity(i);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    void getData() {
        findViewById(R.id.tvMenuGuruNone).setVisibility(View.VISIBLE);
        mapelList.clear();
        mapelAdapter.notifyDataSetChanged();

        progressDialog.show();
        firestore.collection("Mapel").whereEqualTo("UID Guru", mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        findViewById(R.id.tvMenuGuruNone).setVisibility(View.INVISIBLE);
                        MapelClass m = new MapelClass();
                        m.setNama(documentSnapshot.getString("Nama"));
                        m.setKelas(documentSnapshot.getString("Kelas"));
                        if (documentSnapshot.contains("Sampul")) {
                            m.setUrlSampul(documentSnapshot.getString("Sampul"));
                        }
                        m.setIconResource(getResources().getIdentifier(documentSnapshot.getString("Icon"), "drawable", getPackageName()));
                        m.setUniqueCode(documentSnapshot.getId());
                        mapelList.add(m);
                    }
                    mapelAdapter = new MapelAdapter(MenuGuruActivity.this, mapelList, true);
                    mapelAdapter.notifyDataSetChanged();
                    rvMapel.setLayoutManager(new LinearLayoutManager(MenuGuruActivity.this));
                    rvMapel.setAdapter(mapelAdapter);

                }
                progressDialog.hide();

            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menuRefresh:
                getData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onResume() {
        super.onResume();
        tvNamaGuru.setText(mAuth.getCurrentUser().getDisplayName());
        getGambar();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_logOut) {
            mAuth.signOut();
            SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("IS_GURU");
            editor.apply();
            Intent i = new Intent(MenuGuruActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
            return true;
        } else if (id == R.id.nav_Profil) {
            Intent x = new Intent(MenuGuruActivity.this, EditProfileActivity.class);
            startActivity(x);
            return true;
        } else if (id == R.id.nav_tentang) {
            Intent x2 = new Intent(MenuGuruActivity.this, AboutActivity.class);
            startActivity(x2);
            return true;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void getGambar() {
        if (mAuth.getCurrentUser().getPhotoUrl() == null) {
            ivProfil.setImageResource(R.drawable.icon_profil);
        } else {
            Glide.with(this).load(mAuth.getCurrentUser().getPhotoUrl()).apply(new RequestOptions().centerCrop()).into(ivProfil);
        }
    }
}
