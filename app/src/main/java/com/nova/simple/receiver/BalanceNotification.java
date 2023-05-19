package com.nova.simple.receiver;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import android.widget.RemoteViews;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;
import com.nova.simple.R;

public class BalanceNotification extends BroadcastReceiver {

    public static final String CHANNEL_ID = "Balance";

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sp_save = PreferenceManager.getDefaultSharedPreferences(context);
        String paquete = sp_save.getString("paquete", "0 MB");
        String lte = sp_save.getString("lte", "0 MB");
        String nacional = sp_save.getString("nacional", "0 MB");
        String vence = sp_save.getString("venceDat", "0 días");

        RemoteViews content =
                new RemoteViews(context.getPackageName(), R.layout.layout_notification_balance);
        content.setTextViewText(R.id.textNotifPaquete, paquete);
        content.setTextViewText(R.id.textNotifLte, lte);
        content.setTextViewText(R.id.textNotifNacional, nacional);
        content.setTextViewText(R.id.textNotifVence, vence);

        // Channel Notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID, "Balances", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Mostrar estado de sus datos en las notificaciones");
            channel.enableVibration(false);
            channel.setShowBadge(false);
            channel.setSound(null, null);
            NotificationManager notiManager = context.getSystemService(NotificationManager.class);
            notiManager.createNotificationChannel(channel);
        }
        // Crea y muestra la notificación
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_stat_logo_app)
                        .setContentTitle(context.getString(R.string.title_balance))
                        .setContent(content)
                        .setShowWhen(false)
                        .setSound(null)
                        .setOngoing(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, builder.build());
    }
}
