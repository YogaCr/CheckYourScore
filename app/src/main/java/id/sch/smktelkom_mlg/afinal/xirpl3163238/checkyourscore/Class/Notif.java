package id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.Class;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.R;
import id.sch.smktelkom_mlg.afinal.xirpl3163238.checkyourscore.activity.MapelActivity;

/**
 * Created by Sakata Yoga on 16/05/2018.
 */

public class Notif {
    public static final int NOTIFICATION_ID = 10;
    static ListenerRegistration listenerRegistration;

    public static void getNotif(final Context context) {
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
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
                                    Intent in = new Intent(context, MapelActivity.class);
                                    in.putExtra("UniqueCode", task.getResult().getId());
                                    in.putExtra("FromNotif", true);
                                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    PendingIntent intent = PendingIntent.getActivity(context, 0, in, PendingIntent.FLAG_ONE_SHOT);

                                    builder = new NotificationCompat.Builder(context).setContentTitle("Mapel " + task.getResult().getString("Nama") + " telah diupdate").setContentText("Silahkan dicek").setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(true).setContentIntent(intent);
                                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
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

    public static void unReg() {

        listenerRegistration.remove();
    }
}
