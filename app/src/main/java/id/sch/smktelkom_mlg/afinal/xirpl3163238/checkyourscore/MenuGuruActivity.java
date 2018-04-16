package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_guru);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mohon Tunggu");
        progressDialog.show();
        navigationView = findViewById(R.id.nav_view);
        rvMapel = findViewById(R.id.rvMapelGuru);
        headerview = navigationView.getHeaderView(0);
        tvNamaGuru = headerview.findViewById(R.id.tvNamaGuru);
        tvEmailGuru = headerview.findViewById(R.id.tvEmailGuru);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("Guru").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    tvNamaGuru.setText(task.getResult().getString("Nama"));
                }
            }
        });
        firestore.collection("Mapel").whereEqualTo("UID Guru", mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        MapelClass m = new MapelClass();
                        m.setNama(documentSnapshot.getString("Nama"));
                        m.setKelas(documentSnapshot.getString("Kelas"));
                        m.setIconResource(getResources().getIdentifier(documentSnapshot.getString("Icon"), "drawable", getPackageName()));
                        mapelList.add(m);
                    }
                    mapelAdapter = new MapelAdapter(MenuGuruActivity.this, mapelList);
                    mapelAdapter.notifyDataSetChanged();
                    rvMapel.setLayoutManager(new GridLayoutManager(MenuGuruActivity.this, 3));
                    rvMapel.setAdapter(mapelAdapter);
                    progressDialog.hide();
                }
            }
        });

        tvEmailGuru.setText(mAuth.getCurrentUser().getEmail());
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_logOut) {
            mAuth.signOut();
            Intent i = new Intent(MenuGuruActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
