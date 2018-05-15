package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragment extends Fragment {

    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth auth;
    private ImageButton EditProfilGambar, GantiNamaPengguna, SelesaiNamaPengguna;
    private EditText EditNamaPengguna;
    private CircleImageView gambarprofil;
    private Uri filePath;

    public ProfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profil, container, false);
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firestore = FirebaseFirestore.getInstance();
        EditProfilGambar = v.findViewById(R.id.btnEditProfilGambar);
        GantiNamaPengguna = v.findViewById(R.id.GantiNamaPengguna);
        SelesaiNamaPengguna = v.findViewById(R.id.SelesaiNamaPengguna);
        EditNamaPengguna = v.findViewById(R.id.EditNamaPengguna);
        EditNamaPengguna.setInputType(InputType.TYPE_NULL);
        gambarprofil = v.findViewById(R.id.GambarProfil);
        if (auth.getCurrentUser().getPhotoUrl() == null) {
            gambarprofil.setImageResource(R.drawable.icon_profil);
        } else {
            Glide.with(this).load(auth.getCurrentUser().getPhotoUrl()).apply(new RequestOptions().centerCrop()).into(gambarprofil);
        }
        EditNamaPengguna.setText(auth.getCurrentUser().getDisplayName());

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
                if (EditNamaPengguna.getText().toString().isEmpty()) {
                    EditNamaPengguna.setError("Tolong isi nama anda");
                } else {
                    EditNamaPengguna.setInputType(InputType.TYPE_NULL);
                    SelesaiNamaPengguna.setVisibility(View.INVISIBLE);
                    GantiNamaPengguna.setVisibility(View.VISIBLE);
                    gantiNama();
                }
            }
        });
        EditProfilGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PilihGambar();
            }
        });
        return v;
    }

    void gantiNama() {
        String nama = EditNamaPengguna.getText().toString();
        Map<String, Object> data = new HashMap<>();
        data.put("Nama", nama);
        firestore.collection("User").document(auth.getCurrentUser().getUid()).update(data);
        UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder().setDisplayName(nama).build();
        auth.getCurrentUser().updateProfile(changeRequest);
        EditNamaPengguna.setFocusable(false);
    }

    @SuppressLint("NewApi")
    private void PilihGambar() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            filePath = data.getData();
//
//            if (filePath != null) {
//                final ProgressDialog progressDialog = new ProgressDialog(this);
//                progressDialog.setTitle("Uploading ...");
//                progressDialog.show();
//
//                StorageReference reference = storageReference.child("images/fotoProfil/" + UUID.randomUUID().toString());
//                reference.putFile(filePath)
//                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                progressDialog.dismiss();
//                                try {
//                                    Bitmap bitmap;
//                                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                                    gambarprofil.setImageBitmap(bitmap);
//                                    UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder().setPhotoUri(taskSnapshot.getDownloadUrl()).build();
//                                    auth.getCurrentUser().updateProfile(changeRequest);
//                                    Toast.makeText(EditProfileActivity.this, "Berhasil mengubah gambar", Toast.LENGTH_SHORT).show();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//
//
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                progressDialog.dismiss();
//                                Toast.makeText(EditProfileActivity.this, "Gagal mengubah gambar", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                                progressDialog.setMessage("Sedang mengupload");
//                            }
//                        });
//
//
//            }
//
//        }
//    }
}
