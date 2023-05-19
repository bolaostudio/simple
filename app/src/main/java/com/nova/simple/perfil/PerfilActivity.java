package com.nova.simple.perfil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.Toast;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.elevation.SurfaceColors;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.nova.simple.R;
import com.nova.simple.activity.MainActivity;
import com.nova.simple.databinding.ActivityPerfilBinding;
import com.nova.simple.databinding.LayoutNumberQrBinding;
import com.nova.simple.preferences.NovaEditTextPreference;
import java.io.File;

public class PerfilActivity extends AppCompatActivity {

    private ActivityPerfilBinding binding;
    private SaveInage imageSave;
    private Bitmap bitmap;
    private LayoutNumberQrBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPerfilBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(R.string.title_perfil);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getWindow().setNavigationBarColor(SurfaceColors.SURFACE_0.getColor(this));

        // --> inflate preference fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frame_perfil, new PerfilFragment())
                    .commit();
        }

        // --> preferences
        SharedPreferences perfilPreference = PreferenceManager.getDefaultSharedPreferences(this);
        String nombre = perfilPreference.getString("nombre", "").toString();
        if (nombre.isEmpty()) {
            binding.mCollapsing.setTitle(getString(R.string.title_perfil));
        } else {
            binding.mCollapsing.setTitle(perfilPreference.getString("nombre", "").toString());
        }

        // --> srlect imagen
        imageSave =
                new SaveInage(this)
                        .setDirectoryName("perfil")
                        .setFileName("mi-perfil.png")
                        .setExternal(true);
        File file = imageSave.getFile();
        if (file.exists()) {
            Bitmap bitmap = imageSave.load();
            binding.mImagePerfil.setImageBitmap(bitmap);
            // notification bar
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow()
                        .getDecorView()
                        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else {
            binding.mImagePerfil.setImageResource(R.drawable.ic_person_40px);
        }
        binding.fab.setOnClickListener(
                view -> {
                    getPicture.launch("image/*");
                });
    }

    public static class PerfilFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle arg0, String arg1) {
            setPreferencesFromResource(R.xml.preferences_perfil, arg1);

            // cargar nombre al perfil
            NovaEditTextPreference nombre = findPreference("nombre");
            if (nombre != null) {
                nombre.setCustomHint(getString(R.string.hint_perfil_nombre));
            }
            nombre.setOnPreferenceChangeListener(
                    new Preference.OnPreferenceChangeListener() {
                        @Override
                        public boolean onPreferenceChange(Preference pref, Object value) {
                            ((CollapsingToolbarLayout) getActivity().findViewById(R.id.mCollapsing))
                                    .setTitle(value.toString());
                            return false;
                        }
                    });

            // BottomSheetPreference numero de telefono
            NovaEditTextPreference edpPrefernce = findPreference("numero");
            if (edpPrefernce != null) {
                edpPrefernce.setCustomHint(getString(R.string.hint_perfil_numero));
            }

            // BottomSheetPreference Password transferencia
            NovaEditTextPreference pass = findPreference("password");
            if (pass != null) {
                pass.setCustomHint(getString(R.string.hint_perfil_password));
            }

            // BottomSheetPreference cuenta nauta
            NovaEditTextPreference editPreference = findPreference("email");
            if (editPreference != null) {
                editPreference.setCustomHint(getString(R.string.hint_perfil_email));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_perfil, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.delete:
                delete_image();
                break;
            case R.id.qr:
                code_qr();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void code_qr() {
        SharedPreferences sp_numero = PreferenceManager.getDefaultSharedPreferences(this);
        String numero = sp_numero.getString("numero", null);
        if (numero == null) {
            Snackbar.make(
                            binding.coordinator,
                            getString(R.string.toast_code_qr_null),
                            Snackbar.LENGTH_LONG)
                    .show();
        } else if (numero.equals("")) {
            Snackbar.make(
                            binding.coordinator,
                            getString(R.string.toast_code_qr_null),
                            Snackbar.LENGTH_LONG)
                    .show();
        } else {
            bottom_sheet_qr();
        }
    }

    private void bottom_sheet_qr() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        bind = LayoutNumberQrBinding.inflate(LayoutInflater.from(this));
        dialog.getWindow().setNavigationBarColor(SurfaceColors.SURFACE_1.getColor(this));
        dialog.setContentView(bind.getRoot());
        // --> preferences
        SharedPreferences numero = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap =
                    barcodeEncoder.encodeBitmap(
                            numero.getString("numero", "").toString(),
                            BarcodeFormat.QR_CODE,
                            400,
                            400);
            bind.mCodeQr.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        bind.mButtomShareQr.setOnClickListener(
                view -> {
                    String bitmapPath =
                            MediaStore.Images.Media.insertImage(
                                    getContentResolver(), bitmap, "mi-code-QR", null);
                    if (bitmapPath == null) {
                        Toast.makeText(this, "Aquí no hay nada que compartir", Toast.LENGTH_LONG)
                                .show();
                    } else {
                        Uri bitmapUri = Uri.parse(bitmapPath);
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/png");
                        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                        startActivity(Intent.createChooser(intent, "Compartir"));
                    }
                });
        dialog.show();
    }

    private void delete_image() {
        imageSave.deleteFile();
        binding.mImagePerfil.setImageResource(R.drawable.ic_person_40px);
    }

    ActivityResultLauncher<String> getPicture =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    new ActivityResultCallback<Uri>() {
                        @Override
                        public void onActivityResult(Uri uri) {
                            if (uri != null) {
                                try {
                                    ImageDecoder.Source source = null;
                                    if (android.os.Build.VERSION.SDK_INT
                                            >= android.os.Build.VERSION_CODES.P) {
                                        source =
                                                ImageDecoder.createSource(
                                                        PerfilActivity.this.getContentResolver(),
                                                        uri);
                                    }
                                    Bitmap bitmap = null;
                                    if (android.os.Build.VERSION.SDK_INT
                                            >= android.os.Build.VERSION_CODES.P) {
                                        bitmap = ImageDecoder.decodeBitmap(source);
                                    }
                                    imageSave.save(bitmap);
                                    File imageFile = imageSave.getFile();
                                    if (imageFile.exists()) {
                                        Bitmap loadedBitmap = imageSave.load();
                                        binding.mImagePerfil.setImageBitmap(loadedBitmap);
                                    } else {
                                        Toast.makeText(
                                                        getApplicationContext(),
                                                        getString(R.string.error_save_image),
                                                        Toast.LENGTH_LONG)
                                                .show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
}
