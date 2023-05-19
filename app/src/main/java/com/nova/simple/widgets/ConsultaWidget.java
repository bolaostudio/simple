package com.nova.simple.widgets;

import android.Manifest;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.nova.simple.R;

public class ConsultaWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(
            Context context, AppWidgetManager appWidgetManager, @NonNull int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            RemoteViews views =
                    new RemoteViews(context.getPackageName(), R.layout.layout_consulta_widget);

            // TODO: ir al uso de datos
            Intent apn = new Intent();
            apn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                apn.setAction(android.provider.Settings.ACTION_DATA_USAGE_SETTINGS);
            }
            PendingIntent pendingApn =
                    PendingIntent.getActivity(context, 0, apn, PendingIntent.FLAG_IMMUTABLE);

            // TODO: consultar saldo
            Intent saldo = new Intent("android.intent.action.CALL");
            saldo.setData(Uri.parse("tel:*222" + Uri.encode("#")));
            PendingIntent pendingSaldo =
                    PendingIntent.getActivity(context, 0, saldo, PendingIntent.FLAG_IMMUTABLE);

            // TODO: consultar bonos
            Intent bonos = new Intent("android.intent.action.CALL");
            bonos.setData(Uri.parse("tel:*222*266" + Uri.encode("#")));
            PendingIntent pendingBonos =
                    PendingIntent.getActivity(context, 0, bonos, PendingIntent.FLAG_IMMUTABLE);

            // TODO: consultar datos
            Intent datos = new Intent("android.intent.action.CALL");
            datos.setData(Uri.parse("tel:*222*328" + Uri.encode("#")));
            PendingIntent pendingDatos =
                    PendingIntent.getActivity(context, 0, datos, PendingIntent.FLAG_IMMUTABLE);

            // TODO: consultar adelanta
            Intent adelanta = new Intent("android.intent.action.CALL");
            adelanta.setData(Uri.parse("tel:*222*233" + Uri.encode("#")));
            PendingIntent pendingAdelanta =
                    PendingIntent.getActivity(context, 0, adelanta, PendingIntent.FLAG_IMMUTABLE);
            // permisos
            views.setOnClickPendingIntent(R.id.consultaAdelanta, pendingAdelanta);
            views.setOnClickPendingIntent(R.id.configurarApn, pendingApn);
            views.setOnClickPendingIntent(R.id.consultaSaldo, pendingSaldo);
            views.setOnClickPendingIntent(R.id.consultaBonos, pendingBonos);
            views.setOnClickPendingIntent(R.id.consultaDatos, pendingDatos);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "¡Conseda el permiso de llamada!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
