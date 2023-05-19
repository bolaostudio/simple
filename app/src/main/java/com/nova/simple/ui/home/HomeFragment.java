package com.nova.simple.ui.home;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.nova.simple.R;
import com.nova.simple.databinding.FragmentHomeBinding;
import com.nova.simple.databinding.LayoutDialogPasswordBinding;
import com.nova.simple.utils.SIMDialer;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    
    SharedPreferences sp_sim;
    String simslot;

    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setHasOptionsMenu(true);

        // sim slot
        sp_sim = PreferenceManager.getDefaultSharedPreferences(getActivity());
        simslot = sp_sim.getString("sim_preference", "0");

        // Recarga saldo
        binding.buttomRecargar.setOnClickListener(
                view -> {
                    if (!validateRecarga()) {
                        return;
                    } else {
                        String code = binding.editRecarga.getText().toString().trim();
                        SIMDialer.call(
                                getActivity(),
                                "*662*" + code + Uri.encode("#"),
                                Integer.parseInt(simslot));
                    }
                });

        // scanner qr recarga
        binding.inputRecarga.setEndIconOnClickListener(
                view -> {
                    ScanOptions scanner = new ScanOptions();
                    scanner.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
                    scanner.setPrompt(getString(R.string.message_scanner_qr_code));
                    scanner.setOrientationLocked(true);
                    barcodeLauncher.launch(scanner);
                });

        // validate numero
        /*
                binding.editNumero.addTextChangedListener(new ValidationTextWatcher(binding.editNumero));
               binding.editClave.addTextChangedListener(new ValidationTextWatcher(binding.editClave));
                binding.editMonto.addTextChangedListener(new ValidationTextWatcher(binding.editMonto));
        */
        binding.imgPassword.setOnClickListener(
                view -> {
                    cambiarPassword();
                });

        // transferir saldo
        binding.buttomTransferir.setOnClickListener(
                view -> {
                    if (!validateNumero()) {
                        return;
                    }
                    if (!validateMonto()) {
                        return;
                    }
                    if (!validateClave()) {
                        return;
                    } else {
                        String numero = binding.editNumero.getText().toString();
                        String clave = binding.editClave.getText().toString();
                        String monto = binding.editMonto.getText().toString();
                        SIMDialer.call(
                                getActivity(),
                                "*234*1*" + numero + "*" + clave + "*" + monto + Uri.encode("#"),
                                Integer.parseInt(simslot));
                    }
                });

        // select contact
        binding.inputNumero.setEndIconOnClickListener(
                view -> {
                    if (ContextCompat.checkSelfPermission(
                                    getActivity(), Manifest.permission.READ_CONTACTS)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                                getActivity(),
                                new String[] {Manifest.permission.READ_CONTACTS},
                                20);
                        return;
                    } else {
                        pickContact.launch(null);
                    }
                });

        // --> Autocomplete clave transferir saldo
        SharedPreferences sp_autocomplete =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean active = sp_autocomplete.getBoolean("autocomplete", false);
        if (active) {
            binding.editClave.setText(sp_autocomplete.getString("password", "").toString());
        } else {
            binding.editClave.getText().clear();
        }

        // ---> Adelanta Saldo
        String[] monto = getResources().getStringArray(R.array.adelanta);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        getActivity(), android.R.layout.simple_dropdown_item_1line, monto);
        binding.autocomplete.setAdapter(adapter);
        binding.buttomAdalanta.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (validateAdelanta()) {
                            String monto_adelanta =
                                    binding.autocomplete
                                            .getText()
                                            .toString()
                                            .replace("25.00 CUP", "25")
                                            .replace("50.00 CUP", "50")
                                            .trim();
                            SIMDialer.call(
                                    getActivity(),
                                    "*234*3*1*" + monto_adelanta + Uri.encode("#"),
                                    Integer.parseInt(simslot));
                        }
                    }
                });

        // consulta saldo
        binding.buttomConsultaSaldo.setOnClickListener(
                view -> {
                    SIMDialer.call(
                            getActivity(), "*222" + Uri.encode("#"), Integer.parseInt(simslot));
                });

        // consulta bonos
        binding.buttomConsultaBonos.setOnClickListener(
                view -> {
                    SIMDialer.call(
                            getActivity(), "*222*266" + Uri.encode("#"), Integer.parseInt(simslot));
                });

        // consulta pospago
        binding.buttomConsultaPospago.setOnClickListener(
                view -> {
                    SIMDialer.call(
                            getActivity(), "*111" + Uri.encode("#"), Integer.parseInt(simslot));
                });

        // consulta datos
        binding.buttomConsultaDatos.setOnClickListener(
                view -> {
                    SIMDialer.call(
                            getActivity(), "*222*328" + Uri.encode("#"), Integer.parseInt(simslot));
                });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity()
                    .getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
    }

    private boolean validateNumero() {
        if (binding.editNumero.getText().toString().trim().isEmpty()) {
            binding.inputNumero.setError(getString(R.string.validate_numero_empty));
            requestFocus(binding.editNumero);
            return false;
        } else if (binding.editNumero.getText().toString().length() < 8) {
            binding.inputNumero.setError(getString(R.string.validate_numero_digitos));
            requestFocus(binding.editNumero);
            return false;
        } else {
            binding.inputNumero.setErrorEnabled(true);
            binding.inputNumero.setError(null);
        }
        return true;
    }

    private boolean validateClave() {
        if (binding.editClave.getText().toString().trim().isEmpty()) {
            binding.inputClave.setError(getString(R.string.validate_numero_empty));
            //   requestFocus(binding.editClave);
            return false;
        } else if (binding.editClave.getText().toString().length() < 4) {
            binding.inputClave.setError(getString(R.string.validate_clave_digitos));
            //    requestFocus(binding.editClave);
            return false;
        } else {
            binding.inputClave.setError(null);
            binding.inputClave.setErrorEnabled(true);
        }
        return true;
    }

    private boolean validateMonto() {
        if (binding.editMonto.getText().toString().contains(".")) {
            binding.editMonto.getText().clear();
            Toast.makeText(getActivity(), getString(R.string.toast_centavos), Toast.LENGTH_LONG)
                    .show();
            requestFocus(binding.editMonto);
            return false;
        }
        return true;
    }

    private boolean validateRecarga() {
        if (binding.editRecarga.getText().toString().trim().isEmpty()) {
            binding.inputRecarga.setError(getString(R.string.validate_numero_empty));
            return false;
        } else if (binding.editRecarga.getText().toString().trim().length() < 16) {
            binding.inputRecarga.setError(getString(R.string.validate_clave_digitos));
            return false;
        }
        return true;
    }

    private boolean validateAdelanta() {
        if (binding.autocomplete.getText().toString().isEmpty()) {
            binding.inputAdelanta.setError("Seleccione la cantidad");
            return false;
        }
        return true;
    }

    private class ValidationTextWatcher implements TextWatcher {
        private View view;

        private ValidationTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.editNumero:
                    validateNumero();
                    break;
                case R.id.editClave:
                    validateClave();
                    break;
                case R.id.editMonto:
                    validateMonto();
                    break;
                case R.id.editRecarga:
                    validateRecarga();
                    break;
                case R.id.autocomplete:
                    validateAdelanta();
                    break;
            }
        }
    }

    private void scannerNumero() {
        ScanOptions scanner = new ScanOptions();
        scanner.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        scanner.setPrompt(getString(R.string.message_scanner_numero));
        scanner.setOrientationLocked(true);
        scannerNumero.launch(scanner);
    }

    private void cambiarPassword() {
        LayoutDialogPasswordBinding dialogBinding =
                LayoutDialogPasswordBinding.inflate(LayoutInflater.from(getActivity()));
        final AlertDialog alertDialog =
                new MaterialAlertDialogBuilder(getActivity())
                        .setTitle(getString(R.string.title_cambio_password))
                        .setView(dialogBinding.getRoot())
                        .setPositiveButton(
                                getString(android.R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String passwordActual =
                                                dialogBinding
                                                        .mEditActualPass
                                                        .getText()
                                                        .toString()
                                                        .trim();
                                        String passwordNueva =
                                                dialogBinding
                                                        .mEditNewPass
                                                        .getText()
                                                        .toString()
                                                        .trim();
                                        if (passwordActual.isEmpty() || passwordNueva.isEmpty()) {
                                            Toast.makeText(
                                                            getActivity(),
                                                            getString(
                                                                    R.string.validate_numero_empty),
                                                            Toast.LENGTH_LONG)
                                                    .show();
                                        } else {
                                            SIMDialer.call(
                                                    getContext(),
                                                    "*234*2*"
                                                            + passwordActual
                                                            + "*"
                                                            + passwordNueva
                                                            + Uri.encode("#"),
                                                    Integer.parseInt(simslot));
                                        }
                                    }
                                })
                        .create();
        alertDialog.show();
    }
    // scanner codigo recarga
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher =
            registerForActivityResult(
                    new ScanContract(),
                    result -> {
                        if (result.getContents() == null) {
                            // ---> cancelado
                        } else {
                            String code = result.getContents().toString();
                            binding.editRecarga.setText(code);
                        }
                    });

    // scanner numero
    private final ActivityResultLauncher<ScanOptions> scannerNumero =
            registerForActivityResult(
                    new ScanContract(),
                    result -> {
                        if (result.getContents() == null) {
                            // ---> cancelado
                        } else {
                            String code = result.getContents().toString();
                            binding.editNumero.setText(code);
                        }
                    });

    // select contact
    private final ActivityResultLauncher<Void> pickContact =
            registerForActivityResult(
                    new ActivityResultContracts.PickContact(),
                    new ActivityResultCallback<Uri>() {
                        @Override
                        public void onActivityResult(Uri uri) {
                            if (uri != null) {
                                try {
                                    Cursor cursor =
                                            getActivity()
                                                    .getContentResolver()
                                                    .query(uri, null, null, null, null);
                                    if (cursor.getCount() > 0) {
                                        while (cursor.moveToNext()) {
                                            String id =
                                                    cursor.getString(
                                                            cursor.getColumnIndex(
                                                                    ContactsContract.Contacts._ID));
                                            String name =
                                                    cursor.getString(
                                                            cursor.getColumnIndex(
                                                                    ContactsContract.Contacts
                                                                            .DISPLAY_NAME));
                                            if (Integer.parseInt(
                                                            cursor.getString(
                                                                    cursor.getColumnIndex(
                                                                            ContactsContract
                                                                                    .Contacts
                                                                                    .HAS_PHONE_NUMBER)))
                                                    > 0) {
                                                binding.inputNumero.setHelperText(name);

                                                // phone number
                                                Cursor phoneCursor =
                                                        getActivity()
                                                                .getContentResolver()
                                                                .query(
                                                                        ContactsContract
                                                                                .CommonDataKinds
                                                                                .Phone.CONTENT_URI,
                                                                        null,
                                                                        ContactsContract
                                                                                        .CommonDataKinds
                                                                                        .Phone
                                                                                        .CONTACT_ID
                                                                                + " = ?",
                                                                        new String[] {id},
                                                                        null);
                                                while (phoneCursor.moveToNext()) {
                                                    String number =
                                                            phoneCursor.getString(
                                                                    phoneCursor.getColumnIndex(
                                                                            ContactsContract
                                                                                    .CommonDataKinds
                                                                                    .Phone.NUMBER));
                                                    String replace =
                                                            number.replace("+53", "")
                                                                    .replace(" ", "")
                                                                    .replace("(", "")
                                                                    .replace(")", "")
                                                                    .replace("#", "")
                                                                    .replace("*", "");
                                                    replace =
                                                            replace.substring(replace.length() - 8);
                                                    if (replace.startsWith("5")
                                                            && replace.length() == 8) {
                                                        binding.editNumero.setText(replace);
                                                    } else {
                                                        Toast.makeText(
                                                                        getActivity(),
                                                                        name
                                                                                + " no es un número móvil",
                                                                        Toast.LENGTH_LONG)
                                                                .show();
                                                        binding.inputNumero.setHelperText(null);
                                                        binding.editNumero.setText(null);
                                                    }
                                                }
                                                phoneCursor.close();
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.getStackTrace();
                                    binding.inputNumero.setHelperText(null);
                                    binding.editNumero.setText(null);
                                }
                            }
                        }
                    });

    @SuppressWarnings("deprecation")
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem menuItem = menu.findItem(R.id.scann);
        menuItem.setVisible(true);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scann:
                scannerNumero();
                break;
            default:
        }

        return super.onOptionsItemSelected(item);
    }
}
