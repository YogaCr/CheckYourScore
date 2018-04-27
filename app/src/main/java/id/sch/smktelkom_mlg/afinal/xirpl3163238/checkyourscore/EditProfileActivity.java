package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    StorageReference storageReference;
    private TextView tvNamaPengguna;
    private ImageButton EditProfilGambar, GantiNamaPengguna, SelesaiNamaPengguna;
    private EditText EditNamaPengguna;
    private CircleImageView gambarprofil;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firestore = FirebaseFirestore.getInstance();
        EditProfilGambar = findViewById(R.id.btnEditProfilGambar);
        GantiNamaPengguna = findViewById(R.id.GantiNamaPengguna);
        SelesaiNamaPengguna = findViewById(R.id.SelesaiNamaPengguna);
        tvNamaPengguna = findViewById(R.id.tvNamaPengguna);
        EditNamaPengguna = findViewById(R.id.EditNamaPengguna);
        gambarprofil = findViewById(R.id.GambarProfil);
        GantiNamaPengguna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditNamaPengguna.setInputType(InputType.TYPE_CLASS_TEXT);
                SelesaiNamaPengguna.setVisibility(View.VISIBLE);
                GantiNamaPengguna.setVisibility(View.INVISIBLE);
            }
        });
        SelesaiNamaPengguna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditNamaPengguna.setInputType(InputType.TYPE_NULL);
                SelesaiNamaPengguna.setVisibility(View.INVISIBLE);
                GantiNamaPengguna.setVisibility(View.VISIBLE);
            }
        });
        EditProfilGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PilihGambar();
            }
        });
    }

    @SuppressLint("NewApi")
    private void PilihGambar() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            if (filePath != null) {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading ...");
                progressDialog.show();

                StorageReference reference = storageReference.child("images/fotoProfil/" + UUID.randomUUID().toString());
                reference.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();

                                try {
                                    Bitmap bitmap;
                                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                                    gambarprofil.setImageBitmap(bitmap);
                                    Toast.makeText(EditProfileActivity.this, "Berhasil mengubah gambar", Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(EditProfileActivity.this, "Gagal mengubah gambar" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage("Uploaded" + (int) +progress + "%");
                            }
                        });


            }

        }
    }
}
