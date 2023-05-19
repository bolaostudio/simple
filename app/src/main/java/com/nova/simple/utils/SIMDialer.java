package com.nova.simple.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.List;

public class SIMDialer {
    
    private static final int REQUEST_PHONE_CALL = 20;

    public static void call(Context context, String phoneNumber, int simSlot) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", simSlot);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    (Activity) context,
                    new String[] {Manifest.permission.CALL_PHONE},
                    REQUEST_PHONE_CALL);
        } else {
            context.startActivity(intent);
        }
    }

    public static void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults,
            Context context,
            Intent intent) {
        if (requestCode == REQUEST_PHONE_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                context.startActivity(intent);
            } else {
                Toast.makeText(
                                context,
                                "Permiso de llamada telefónica denegado",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
