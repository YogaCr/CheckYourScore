package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore;


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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.Class.MapelClass;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.activity.BuatMapelActivity;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.adapter.MapelAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuGuruFragment extends Fragment {

    FirebaseAuth mAuth;
    RecyclerView rvMapel;
    List<MapelClass> mapelList = new ArrayList<>();
    MapelAdapter mapelAdapter;
    FirebaseFirestore firestore;
    ProgressDialog progressDialog;
    TextView tvMenuGuruNone;

    public MenuGuruFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu_guru, container, false);
        // Inflate the layout for this fragment
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Harap Tunggu");

        progressDialog.setCanceledOnTouchOutside(false);

        rvMapel = v.findViewById(R.id.rvMapelGuru);
        tvMenuGuruNone = v.findViewById(R.id.tvMenuGuruNone);
        tvMenuGuruNone.setVisibility(View.VISIBLE);
        mapelAdapter = new MapelAdapter(getContext(), mapelList, true);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FloatingActionButton fab = v.findViewById(R.id.fabMenuGuru);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), BuatMapelActivity.class);
                startActivity(i);
            }
        });
        setHasOptionsMenu(true);
        return v;
    }

    void getData() {
        tvMenuGuruNone.setVisibility(View.VISIBLE);
        mapelList.clear();
        mapelAdapter.notifyDataSetChanged();

        progressDialog.show();
        firestore.collection("Mapel").whereEqualTo("UID Guru", mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        tvMenuGuruNone.setVisibility(View.INVISIBLE);
                        MapelClass m = new MapelClass();
                        m.setNama(documentSnapshot.getString("Nama"));
                        m.setKelas(documentSnapshot.getString("Kelas"));
                        if (documentSnapshot.contains("Sampul")) {
                            m.setUrlSampul(documentSnapshot.getString("Sampul"));
                        }
                        m.setIconResource(getResources().getIdentifier(documentSnapshot.getString("Icon"), "drawable", getContext().getPackageName()));
                        m.setUniqueCode(documentSnapshot.getId());
                        mapelList.add(m);
                    }
                    mapelAdapter = new MapelAdapter(getContext(), mapelList, true);
                    mapelAdapter.notifyDataSetChanged();
                    rvMapel.setLayoutManager(new LinearLayoutManager(getContext()));
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
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_refresh, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
