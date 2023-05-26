package com.nova.simple.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.SwitchPreferenceCompat;
import com.nova.simple.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import com.nova.simple.ThemeManager;
import com.nova.simple.databinding.ActivitySettingsBinding;
import com.nova.simple.preferences.NovaListPreference;
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

        // load fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, new SettingFragment())
                    .commit();
        }
        
        // permiso de notificación
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            // Si no tienes el permiso, solicítalo al usuario
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                        this, new String[] {Manifest.permission.POST_NOTIFICATIONS}, 0);
            }
        }
    }

    public static class SettingFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);

            // cambiar tema de la aplicación
            NovaListPreference theme = findPreference("tema_preference");
            if (theme.getValue() == null) {
                theme.setValue(ThemeManager.Mode.system.name());
            }
            theme.setOnPreferenceChangeListener(
                    (preference, value) -> {
                        ThemeManager.applyTheme(ThemeManager.Mode.valueOf((String) value));
                        return true;
                    });

            // cargar idioma de la app
            NovaListPreference language = findPreference("language");
            if (language != null) {
                language.setOnPreferenceChangeListener(
                        (preference, value) -> {
                            String idioma = (String) value;
                            Locale newLocale = new Locale(idioma);
                            Locale.setDefault(newLocale);
                            Configuration config = new Configuration();
                            config.locale = newLocale;
                            Resources resource =
                                    Objects.requireNonNull(getActivity().getResources());
                            resource.updateConfiguration(
                                    config, getResources().getDisplayMetrics());
                            getActivity().recreate();
                            return true;
                        });
            }

            // floating
            SwitchPreferenceCompat switchPref = findPreference("floating");
            switchPref.setOnPreferenceChangeListener(
                    (preference, newValue) -> {
                        boolean isChecked = (Boolean) newValue;
                        if (isChecked) {
                            if (!canDrawOverlay()) {
                                Intent intent =
                                        new Intent(
                                                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                                Uri.parse(
                                                        "package:"
                                                                + getActivity().getPackageName()));
                                activityResult.launch(intent);
                            } else {
                                startServiceFloating();
                            }
                        } else {
                            stopServiceFloating();
                        }
                        return true;
                    });

            // autocompletado
            SwitchPreferenceCompat autocomplete = findPreference("autocomplete");
            autocomplete.setOnPreferenceChangeListener(
                    (preference, newValue) -> {
                        boolean isChecked = (Boolean) newValue;

                        return true;
                    });

            // notification
            SwitchPreferenceCompat notification = findPreference("notifi");
            notification.setOnPreferenceChangeListener(
                    (preference, newValue) -> {
                        boolean isChecked = (Boolean) newValue;
                        if (isChecked) {
                            getActivity()
                                    .sendBroadcast(
                                            new Intent(getActivity(), BalanceNotification.class));
                        } else {
                            NotificationManagerCompat notificationManager =
                                    NotificationManagerCompat.from(getActivity());
                            notificationManager.cancel(1);
                        }
                        return true;
                    });
        }

        // comprobar permiso de superposicion
        private boolean canDrawOverlay() {
            return Settings.canDrawOverlays(getContext());
        }

        // iniciar servicio
        private void startServiceFloating() {
            Intent intent = new Intent(getActivity(), FloatingWindow.class);
            getActivity().startService(intent);
        }

        // pausar servicio
        private void stopServiceFloating() {
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
