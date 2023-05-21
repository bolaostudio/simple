package com.nova.simple.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.SwitchPreferenceCompat;
import com.google.android.material.elevation.SurfaceColors;
import com.nova.simple.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import com.nova.simple.ThemeManager;
import com.nova.simple.databinding.ActivitySettingsBinding;
import com.nova.simple.receiver.BalanceNotification;
import com.nova.simple.services.FloatingWindow;
import java.util.Locale;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setNavigationBarColor(SurfaceColors.SURFACE_0.getColor(this));

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, new SettingsFragment())
                    .commit();
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            // Si no tienes el permiso, solicítalo al usuario
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                        this, new String[] {Manifest.permission.POST_NOTIFICATIONS}, 0);
            }
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat
            implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);

            // cargar tema predeterminado
            ListPreference theme = getPreferenceManager().findPreference("tema_preference");
            if (theme.getValue() == null) {
                theme.setValue(ThemeManager.Mode.DEFAULT.name());
            }
            theme.setOnPreferenceChangeListener(
                    (preference, newValue) -> {
                        ThemeManager.applyTheme(ThemeManager.Mode.valueOf((String) newValue));
                        return true;
                    });

            // SwitchPreferenceCompat burbuja flotante
            SwitchPreferenceCompat floatingWindow =
                    (SwitchPreferenceCompat) findPreference("floating");

            SwitchPreferenceCompat notification = getPreferenceManager().findPreference("notifi");
            assert notification != null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notification.setEnabled(true);
            } else {
                notification.setEnabled(false);
                notification.setSummary("No compatible");
            }

            // Cargar lenguaje en la app (Español o Ingles)
            ListPreference idiomaPref = findPreference("language");
            if (idiomaPref != null) {
                idiomaPref.setOnPreferenceChangeListener(
                        (preference, newValue) -> {
                            String idioma = (String) newValue;
                            Locale newLocale = new Locale(idioma);
                            Locale.setDefault(newLocale);
                            Configuration config = new Configuration();
                            config.locale = newLocale;
                            Resources res = Objects.requireNonNull(getActivity()).getResources();
                            res.updateConfiguration(config, getResources().getDisplayMetrics());
                            getActivity().recreate();
                            return true;
                        });
            }

            SwitchPreferenceCompat autocomplete =
                    (SwitchPreferenceCompat) findPreference("autocomplete");
        }

        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String rootKey) {
            if (rootKey.equals("floating")) {
                boolean isChecked = sharedPreferences.getBoolean("floating", false);
                if (isChecked) {
                    if (!canDrawOverlay()) {
                        requestOverlayPermission();
                    } else {
                        startServiceFloating();
                    }
                } else {
                    stopServiceFloating();
                }
            }

            // TODO: Notificacion con valor de los datos
            if (rootKey.equals("notifi")) {
                boolean active = sharedPreferences.getBoolean("notifi", false);
                if (active) {
                    getActivity()
                            .sendBroadcast(new Intent(getActivity(), BalanceNotification.class));
                } else {
                    NotificationManagerCompat notificationManager =
                            NotificationManagerCompat.from(getActivity());
                    notificationManager.cancel(1);
                }
            }
        }

        private void setLocale(String language) {
            Locale locale = new Locale(language);
            Configuration config = new Configuration();
            config.locale = locale;
            getActivity()
                    .getResources()
                    .updateConfiguration(config, getResources().getDisplayMetrics());
        }

        private boolean canDrawOverlay() {

            return Settings.canDrawOverlays(getContext());
        }

        private void requestOverlayPermission() {
            Intent intent =
                    new Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getActivity().getPackageName()));
            activityResult.launch(intent);
        }

        private void startServiceFloating() {
            // Inicia el Service
            Intent intent = new Intent(getActivity(), FloatingWindow.class);
            getActivity().startService(intent);
        }

        private void stopServiceFloating() {
            // Stop el Service
            Intent intent = new Intent(getActivity(), FloatingWindow.class);
            getActivity().stopService(intent);
        }

        ActivityResultLauncher<Intent> activityResult =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {
                                if (canDrawOverlay()) {
                                    startServiceFloating();
                                } else {
                                    Toast.makeText(
                                                    getActivity(),
                                                    "La aplicación necesita permiso para dibujar sobre otras aplicaciones",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        });

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen()
                    .getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onStart() {
            super.onStart();
            getPreferenceScreen()
                    .getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onStop() {
            super.onStop();
            Objects.requireNonNull(getPreferenceScreen().getSharedPreferences())
                    .unregisterOnSharedPreferenceChangeListener(this);
        }
    }
}
