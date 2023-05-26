package com.nova.simple.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.elevation.SurfaceColors;
import com.google.android.material.navigation.NavigationView;
import com.jesusd0897.crashreporter.model.ReportListener;
import com.jesusd0897.crashreporter.model.StyleReporter;
import com.jesusd0897.crashreporter.util.UtilKt;
import com.nova.simple.BuildConfig;
import com.nova.simple.R;
import com.nova.simple.databinding.ActivityMainBinding;
import com.nova.simple.databinding.NavHeaderMainBinding;
import com.nova.simple.perfil.PerfilActivity;
import com.nova.simple.perfil.SaveInage;
import com.nova.simple.services.FloatingWindow;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    NavController navController;
    AppBarConfiguration appBarConfiguration;

    private Locale locale = null;
    private BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        String idioma = getLocaleFromPreferences();
        setLocale(idioma);

        // navigationBarColor SurfaceColor
        getWindow().setNavigationBarColor(SurfaceColors.SURFACE_2.getColor(this));
        //   getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //  navView = findViewById(R.id.bottom_nav_view);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        // hide balances API 26 inferior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Dispositivos compatibles con sendUssdRequest
            binding.bottomNavView.getMenu().findItem(R.id.navigation_balance).setVisible(true);
        } else {
            // Dispositivos no compatibles con sendUssdRequest
            binding.bottomNavView.getMenu().findItem(R.id.navigation_balance).setVisible(false);
        }

        appBarConfiguration =
                new AppBarConfiguration.Builder(
                                R.id.navigation_home,
                                R.id.navigation_balance,
                                R.id.navigation_compras,
                                R.id.navigation_llamadas,
                                R.id.navigation_nauta)
                        .setOpenableLayout(drawerLayout)
                        .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        setupDrawerLayout();
        NavigationUI.setupWithNavController(binding.bottomNavView, navController);

        // manejar perfil desde el header
        View view = binding.navView.getHeaderView(0);
        NavHeaderMainBinding bindingHeader = NavHeaderMainBinding.bind(view);
        bindingHeader.imagePerfil.setOnClickListener(
                view1 -> {
                    startActivity(new Intent(MainActivity.this, PerfilActivity.class));
                    finish();
                });

        // load photo
        SaveInage imageSave =
                new SaveInage(this)
                        .setDirectoryName("perfil")
                        .setFileName("mi-perfil.png")
                        .setExternal(true);
        File file = imageSave.getFile();
        if (file.exists()) {
            Bitmap bitmap = imageSave.load();
            bindingHeader.imagePerfil.setImageBitmap(bitmap);
        } else {
            bindingHeader.imagePerfil.setImageResource(R.drawable.ic_perfil_40px);
        }

        // --> preferences perfil nombre
        SharedPreferences perfilPreference = PreferenceManager.getDefaultSharedPreferences(this);
        String nombre = perfilPreference.getString("nombre", "").toString();
        if (nombre.isEmpty()) {
            bindingHeader.mTextUsuario.setText(getString(R.string.usuario));
        } else {
            bindingHeader.mTextUsuario.setText(nombre);
        }

        // --> saludo
        String saludo = getSaludo();
        bindingHeader.mTextSaludo.setText(saludo);

        // crash reporter
        // Manejar el rastro de error
        ReportListener listener =
                new ReportListener() {
                    @Override
                    public void onNoNeeded() {}

                    @Override
                    public void onSendClick(String errorTrace) {
                        String appVersion = BuildConfig.VERSION_NAME;

                        Intent send = new Intent("android.intent.action.SENDTO");
                        send.putExtra(
                                "android.intent.extra.EMAIL",
                                new String[] {"simpleapp@zohomail.com"});
                        send.putExtra("android.intent.extra.SUBJECT", "BUG/SIMPLE");
                        send.putExtra("android.intent.extra.TEXT", appVersion + "\n" + errorTrace);
                        send.setType("text/plain");
                        send.setData(Uri.parse("mailto:"));
                        try {
                            startActivity(send);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelClick(String errorTrace) {
                        System.exit(0);
                    }
                };
        try {
            UtilKt.trySendCrashReport(
                    this,
                    listener,
                    "",
                    false,
                    new StyleReporter(
                            getString(R.string.title_bug_report),
                            getString(R.string.message_bug_report),
                            ContextCompat.getDrawable(this, R.drawable.ic_home_filled_24px)),
                    new String[] {""});
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.navView.setNavigationItemSelectedListener(this);
    }

    private String getSaludo() {
        String greeting;
        Calendar calendar = Calendar.getInstance();
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        if (hora >= 0 && hora < 12) {
            greeting = getString(R.string.saludo_dia);
        } else if (hora >= 12 && hora < 18) {
            greeting = getString(R.string.saludo_tarde);
        } else {
            greeting = getString(R.string.saludo_noche);
        }
        return greeting;
    }

    private String getLocaleFromPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString("language", "es");
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        switch (item.getItemId()) {
            case R.id.navigation_home:
                navController.popBackStack(R.id.navigation_home, false);
                return true;
            case R.id.navigation_servicios:
                navController.navigate(R.id.navigation_servicios);
                binding.drawerLayout.closeDrawers();
                return true;
            case R.id.navigation_entumovil:
                navController.navigate(R.id.nav_entumovil);
                binding.drawerLayout.closeDrawers();
                return true;
            case R.id.navigation_telepuntos:
                openGoogleMap();
                return true;
            case R.id.activity_settings:
                navController.navigate(R.id.nav_settings);
                binding.drawerLayout.closeDrawers();
                return true;
            case R.id.activity_invite:
                inviteFriend();
                return true;
            case R.id.activity_about:
                navController.navigate(R.id.activity_about);
                binding.drawerLayout.closeDrawers();
                return true;
        }

        return false;
    }

    private void inviteFriend() {
        new ShareCompat.IntentBuilder(this)
                .setText(getString(R.string.invitar_amigo) + getPackageName())
                .setType("text/plain")
                .setChooserTitle("Compartir:")
                .startChooser();
    }

    private void openGoogleMap() {
        startActivity(
                new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.view_telepuntos))));
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setupDrawerLayout() {
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (pref.getBoolean("floating", false)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            } else {
                Intent intent = new Intent(this, FloatingWindow.class);
                startService(intent);
            }
        }
    }

    public void onPreferenceChanged() {
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int night = AppCompatDelegate.getDefaultNightMode();
        AppCompatDelegate.setDefaultNightMode(night);
        navController.popBackStack(R.id.navigation_home, false);
        recreate();
    }
}
