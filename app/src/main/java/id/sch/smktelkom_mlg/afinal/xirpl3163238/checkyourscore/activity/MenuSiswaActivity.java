package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.activity;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.Class.MapelClass;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.R;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.adapter.MapelAdapter;

public class MenuSiswaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final int NOTIFICATION_ID = 10;
    FirebaseAuth mAuth;
    TextView tvNamaSiswa, tvEmailSiswa;
    RecyclerView rvMapelSiswa;
    NavigationView navigationView;
    View headerview;
    List<MapelClass> mapelList = new ArrayList<>();
    MapelAdapter mapelAdapter;
    FirebaseFirestore firestore;
    EditText etKodeMapel;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    ImageView ivProfil;
    ListenerRegistration listenerRegistration;

    @Override
    protected void onResume() {
        super.onResume();
        tvNamaSiswa.setText(mAuth.getCurrentUser().getDisplayName());
        getGambar();
        getData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_siswa);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Harap Tunggu");

        progressDialog.setCanceledOnTouchOutside(false);
        navigationView = findViewById(R.id.nav_view_siswa);
        headerview = navigationView.getHeaderView(0);
        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbarsiswa);
        setSupportActionBar(toolbar);
        tvNamaSiswa = headerview.findViewById(R.id.tvNmSiswa);
        tvEmailSiswa = headerview.findViewById(R.id.tvEmSiswa);
        tvNamaSiswa.setText(mAuth.getCurrentUser().getDisplayName());
        tvEmailSiswa.setText(mAuth.getCurrentUser().getEmail());
        ivProfil = headerview.findViewById(R.id.ivProfilSiswa);
        getGambar();

        rvMapelSiswa = findViewById(R.id.rvMapelSiswa);
        firestore = FirebaseFirestore.getInstance();
        findViewById(R.id.tvMenuSiswaNone).setVisibility(View.VISIBLE);
        builder = new AlertDialog.Builder(MenuSiswaActivity.this);
        builder.setCancelable(true);
        View v = LayoutInflater.from(MenuSiswaActivity.this).inflate(R.layout.layout_tambah_mapel_siswa, null);
        builder.setView(v);
        etKodeMapel = v.findViewById(R.id.etSiswaTambahMapel);
        etKodeMapel.setText("");
        alertDialog = builder.create();
        v.findViewById(R.id.btnSiswaTambahMapel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etKodeMapel.getText().toString().isEmpty()) {
                    etKodeMapel.setError("Tolong isi kode mapel");
                } else {
                    final String kode = etKodeMapel.getText().toString();
                    firestore.collection("JoinSiswa").whereEqualTo("UID", mAuth.getCurrentUser().getUid()).whereEqualTo("Mapel", kode).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.getResult().size() > 0) {
                                Toast.makeText(MenuSiswaActivity.this, "Anda sudah bergabung", Toast.LENGTH_SHORT).show();
                            } else {
                                firestore.collection("Mapel").document(kode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.getResult().exists()) {
                                            Map<String, Object> data = new HashMap<>();
                                            data.put("UID", mAuth.getCurrentUser().getUid());
                                            data.put("Mapel", task.getResult().getId());
                                            firestore.collection("JoinSiswa").add(data);
                                            firestore.collection("Mapel").document(kode).collection("Bab").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    for (DocumentSnapshot ds : task.getResult()) {
                                                        Map<String, Double> map = new HashMap<>();
                                                        map.put("Nilai", 0.0);
                                                        firestore.collection("Mapel").document(kode).collection("Bab").document(ds.getId()).collection("Nilai").document(mAuth.getCurrentUser().getUid()).set(map);
                                                    }
                                                }
                                            });
                                            Intent intent = new Intent(MenuSiswaActivity.this, MapelActivity.class);
                                            intent.putExtra("UniqueCode", kode);
                                            startActivity(intent);
                                            alertDialog.dismiss();

                                        } else {
                                            etKodeMapel.setError("Kode salah");
                                        }

                                    }
                                });
                            }

                        }
                    });
                }
            }
        });


        FloatingActionButton fab = findViewById(R.id.fabMenuSiswa);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });

        getNotif();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    void getData() {

        progressDialog.show();

        firestore.collection("JoinSiswa")
                .whereEqualTo("UID", mAuth.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                mapelList.clear();
                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                    firestore.collection("Mapel").document(documentSnapshot.getString("Mapel")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.getResult().exists()) {
                                MapelClass m = new MapelClass();
                                m.setNama(task.getResult().getString("Nama"));
                                m.setKelas(task.getResult().getString("Kelas"));
                                if (task.getResult().contains("Sampul")) {
                                    m.setUrlSampul(task.getResult().getString("Sampul"));
                                }
                                m.setIconResource(getResources().getIdentifier(task.getResult().getString("Icon"), "drawable", getPackageName()));
                                m.setUniqueCode(task.getResult().getId());
                                mapelList.add(m);
                                mapelAdapter = new MapelAdapter(MenuSiswaActivity.this, mapelList, false);
                                mapelAdapter.notifyDataSetChanged();
                                rvMapelSiswa.setLayoutManager(new LinearLayoutManager(MenuSiswaActivity.this));
                                rvMapelSiswa.setAdapter(mapelAdapter);
                                findViewById(R.id.tvMenuSiswaNone).setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
                progressDialog.hide();

            }
        });
        if (mapelList.size() == 0)
            findViewById(R.id.tvMenuSiswaNone).setVisibility(View.VISIBLE);
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

    void getGambar() {
        if (mAuth.getCurrentUser().getPhotoUrl() == null) {
            ivProfil.setImageResource(R.drawable.icon_profil);
        } else {
            Glide.with(this).load(mAuth.getCurrentUser().getPhotoUrl()).apply(new RequestOptions().centerCrop()).into(ivProfil);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_logOut:
                mAuth.signOut();
                SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("IS_GURU");
                editor.apply();
                listenerRegistration.remove();
                Intent i = new Intent(MenuSiswaActivity.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                return true;
            case R.id.nav_Profil:
                Intent x = new Intent(MenuSiswaActivity.this, EditProfileActivity.class);
                startActivity(x);
                return true;
            case R.id.nav_tentang:
                Intent in = new Intent(MenuSiswaActivity.this, AboutActivity.class);
                startActivity(in);
                return true;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void getNotif() {

        listenerRegistration = firestore.collection("JoinSiswa").whereEqualTo("UID", mAuth.getCurrentUser().getUid()).whereEqualTo("Notif", true).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentSnapshot ds : documentSnapshots) {
                    final String mapel = ds.getString("Mapel");
                    firestore.collection("Mapel").document(mapel).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    NotificationCompat.Builder builder;
                                    Intent in = new Intent(MenuSiswaActivity.this, MapelActivity.class);
                                    in.putExtra("UniqueCode", task.getResult().getId());
                                    in.putExtra("FromNotif", true);
                                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    PendingIntent intent = PendingIntent.getActivity(MenuSiswaActivity.this, 0, in, PendingIntent.FLAG_ONE_SHOT);

                                    builder = new NotificationCompat.Builder(MenuSiswaActivity.this).setContentTitle("Mapel " + task.getResult().getString("Nama") + " telah diupdate").setContentText("Silahkan dicek").setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(true).setContentIntent(intent);
                                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    Notification notification = builder.build();
                                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                                    notificationManager.notify(NOTIFICATION_ID, builder.build());
                                    firestore.collection("JoinSiswa").whereEqualTo("Mapel", mapel).whereEqualTo("UID", mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if (task.getResult().size() > 0) {
                                                    for (DocumentSnapshot ds : task.getResult()) {
                                                        Map<String, Object> map = new HashMap<>();
                                                        map.put("Notif", false);
                                                        firestore.collection("JoinSiswa").document(ds.getId()).update(map);
                                                    }
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuRefresh) {
            getData();
        }
        return super.onOptionsItemSelected(item);
    }
}
