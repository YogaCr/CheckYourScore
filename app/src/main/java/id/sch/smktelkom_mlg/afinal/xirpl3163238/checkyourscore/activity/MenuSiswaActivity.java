package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.activity;

import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

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

public class MenuSiswaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth mAuth;
    TextView tvNamaSiswa, tvEmailSiswa;
    RecyclerView rvMapelSiswa;
    List<MapelClass> mapelList = new ArrayList<>();
    MapelAdapter mapelAdapter;
    FirebaseFirestore firestore;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_siswa);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mohon Tunggu");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbarsiswa);
        setSupportActionBar(toolbar);
        tvNamaSiswa = findViewById(R.id.tvNamaSiswa);
        tvEmailSiswa = findViewById(R.id.tvEmailSiswa);
        rvMapelSiswa = findViewById(R.id.rvMapelSiswa);
        firestore = FirebaseFirestore.getInstance();

        FloatingActionButton fab = findViewById(R.id.fabMenuSiswa);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        getData();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    void getData() {
        mapelList.clear();
        progressDialog.show();
        firestore.collection("JoinSiswa").whereEqualTo("UID", mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                                              @Override
                                                                                                              public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                                                  if (task.isSuccessful()) {
                                                                                                                      for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                                                                                                          firestore.collection("Mapel").document(documentSnapshot.getString("Mapel")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                              @Override
                                                                                                                              public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                                  Toast.makeText(MenuSiswaActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                                                                                                                                  MapelClass m = new MapelClass();
                                                                                                                                  m.setNama(task.getResult().getString("Nama"));
                                                                                                                                  m.setKelas(task.getResult().getString("Kelas"));
                                                                                                                                  if (task.getResult().contains("Sampul")) {
                                                                                                                                      m.setUrlSampul(task.getResult().getString("Sampul"));
                                                                                                                                  }
                                                                                                                                  m.setIconResource(getResources().getIdentifier(task.getResult().getString("Icon"), "drawable", getPackageName()));
                                                                                                                                  m.setUniqueCode(task.getResult().getId());
                                                                                                                                  mapelList.add(m);
                                                                                                                                  mapelAdapter = new MapelAdapter(MenuSiswaActivity.this, mapelList);
                                                                                                                                  mapelAdapter.notifyDataSetChanged();
                                                                                                                                  rvMapelSiswa.setLayoutManager(new LinearLayoutManager(MenuSiswaActivity.this));
                                                                                                                                  rvMapelSiswa.setAdapter(mapelAdapter);
                                                                                                                              }
                                                                                                                          });

                                                                                                                          progressDialog.hide();
                                                                                                                      }
                                                                                                                  }

                                                                                                              }
                                                                                                          }

        );
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.menuRefresh:
                getData();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
