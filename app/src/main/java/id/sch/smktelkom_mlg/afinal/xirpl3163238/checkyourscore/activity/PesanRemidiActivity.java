package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.R;

public class PesanRemidiActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    Intent i;
    String uniqueCode, idMapel, url;
    TextView tvPesan;
    Button btnLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_remidi);
        tvPesan = findViewById(R.id.tvPesanRemidi);
        btnLink = findViewById(R.id.btnLinkRemidi);
        firestore = FirebaseFirestore.getInstance();
        i = getIntent();
        btnLink.setVisibility(View.INVISIBLE);
        btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(in);
            }
        });
        uniqueCode = i.getStringExtra("UniqueCode");
        idMapel = i.getStringExtra("BabId");
        firestore.collection("Mapel").document(uniqueCode).collection("Bab").document(idMapel).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                tvPesan.setText(task.getResult().getString("PesanRemidi"));
                if (task.getResult().contains("URLRemidi")) {
                    url = task.getResult().getString("URLRemidi");
                    btnLink.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
