package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.Class.MapelClass;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.Class.Notif;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.R;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.activity.MapelActivity;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.adapter.MapelAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuSiswaFragment extends Fragment {

    public static final int NOTIFICATION_ID = 10;
    FirebaseAuth mAuth;
    TextView tvSiswaNone;
    RecyclerView rvMapelSiswa;
    List<MapelClass> mapelList = new ArrayList<>();
    MapelAdapter mapelAdapter;
    FirebaseFirestore firestore;
    EditText etKodeMapel;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    ListenerRegistration listenerRegistration;

    public MenuSiswaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_menu_siswa, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Harap Tunggu");

        progressDialog.setCanceledOnTouchOutside(false);
        mAuth = FirebaseAuth.getInstance();
        rvMapelSiswa = view.findViewById(R.id.rvMapelSiswa);
        firestore = FirebaseFirestore.getInstance();
        tvSiswaNone = view.findViewById(R.id.tvMenuSiswaNone);
        tvSiswaNone.setVisibility(View.VISIBLE);
        builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        View alertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_tambah_mapel_siswa, null);
        builder.setView(alertView);
        etKodeMapel = alertView.findViewById(R.id.etSiswaTambahMapel);
        etKodeMapel.setText("");
        alertDialog = builder.create();
        alertView.findViewById(R.id.btnSiswaTambahMapel).setOnClickListener(new View.OnClickListener() {
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
                                Toast.makeText(getContext(), "Anda sudah bergabung", Toast.LENGTH_SHORT).show();
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
                                            Intent intent = new Intent(getContext(), MapelActivity.class);
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


        FloatingActionButton fab = view.findViewById(R.id.fabMenuSiswa);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
            }
        });
        Notif.getNotif(getContext());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
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
                                m.setIconResource(getResources().getIdentifier(task.getResult().getString("Icon"), "drawable", getContext().getPackageName()));
                                m.setUniqueCode(task.getResult().getId());
                                mapelList.add(m);
                                mapelAdapter = new MapelAdapter(getContext(), mapelList, false);
                                mapelAdapter.notifyDataSetChanged();
                                rvMapelSiswa.setLayoutManager(new LinearLayoutManager(getContext()));
                                rvMapelSiswa.setAdapter(mapelAdapter);
                                tvSiswaNone.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
                progressDialog.hide();

            }
        });
        if (mapelList.size() == 0)
            tvSiswaNone.setVisibility(View.VISIBLE);
    }

//    void getNotif() {
//        listenerRegistration = firestore.collection("JoinSiswa").whereEqualTo("UID", mAuth.getCurrentUser().getUid()).whereEqualTo("Notif", true).addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
//                for (DocumentSnapshot ds : documentSnapshots) {
//                    final String mapel = ds.getString("Mapel");
//                    firestore.collection("Mapel").document(mapel).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            if (task.isSuccessful()) {
//                                if (task.getResult().exists()) {
//                                    NotificationCompat.Builder builder;
//                                    Intent in = new Intent(getContext(), MapelActivity.class);
//                                    in.putExtra("UniqueCode", task.getResult().getId());
//                                    in.putExtra("FromNotif", true);
//                                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                    PendingIntent intent = PendingIntent.getActivity(getContext(), 0, in, PendingIntent.FLAG_ONE_SHOT);
//
//                                    builder = new NotificationCompat.Builder(getContext()).setContentTitle("Mapel " + task.getResult().getString("Nama") + " telah diupdate").setContentText("Silahkan dicek").setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(true).setContentIntent(intent);
//                                    NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
//                                    Notification notification = builder.build();
//                                    notification.flags = Notification.FLAG_AUTO_CANCEL;
//                                    notificationManager.notify(NOTIFICATION_ID, builder.build());
//                                    firestore.collection("JoinSiswa").whereEqualTo("Mapel", mapel).whereEqualTo("UID", mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                            if (task.isSuccessful()) {
//                                                if (task.getResult().size() > 0) {
//                                                    for (DocumentSnapshot ds : task.getResult()) {
//                                                        Map<String, Object> map = new HashMap<>();
//                                                        map.put("Notif", false);
//                                                        firestore.collection("JoinSiswa").document(ds.getId()).update(map);
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    });
//                                }
//                            }
//                        }
//                    });
//
//                }
//            }
//        });
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuRefresh) {
            getData();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_refresh, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
