package com.nova.simple.ui.nauta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.nova.simple.NovaApplication;
import com.nova.simple.R;
import com.nova.simple.databinding.LayoutPortalUsuarioInfoBinding;
import com.nova.simple.utils.Pref;

import cu.suitetecsa.sdk.nauta.domain.service.NautaClient;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PortalUsuarioInfo extends Fragment {

    private LayoutPortalUsuarioInfoBinding binding;
    private NautaClient client;
    private Executor executor;
    private String errorMessage;
    private BottomNavigationView nav;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = LayoutPortalUsuarioInfoBinding.inflate(inflater, container, false);

        // hide bottomnavigationview
        nav = Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_nav_view);
        nav.setVisibility(View.GONE);

        NovaApplication app = (NovaApplication) getActivity().getApplicationContext();
        client = app.getNautaClient();
        executor = Executors.newSingleThreadExecutor();

        String tiempo = Pref.load(getActivity(), "USER_INFO", "time", null);
        String account = Pref.load(getActivity(), "USER_INFO", "account", null);

        String saldo = Pref.load(getActivity(), "USER_INFO", "saldo", null);
        String tipo = Pref.load(getActivity(), "USER_INFO", "tipo", null);
        String bloqueo = Pref.load(getActivity(), "USER_INFO", "bloqueo", null);
        String elimina = Pref.load(getActivity(), "USER_INFO", "delete", null);

        binding.textTiempo.setText(tiempo);
        binding.textCuenta.setText(account);
        binding.textSaldoCuenta.setText(saldo);
        binding.textTypeAccount.setText(tipo);
        binding.textBloqueoCuenta.setText(bloqueo);
        binding.textDeleteCuenta.setText(elimina);

        // recargar con QR
        binding.inputRecargaCuenta.setEndIconOnClickListener(
                view -> {
                    ScanOptions scanner = new ScanOptions();
                    scanner.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
                    scanner.setPrompt(getString(R.string.message_scanner_qr_code));
                    scanner.setOrientationLocked(true);
                    barcodeLauncher.launch(scanner);
                });

        // buttom recargar
        binding.buttomRecargar.setOnClickListener(
                view -> {
                    executor.execute(
                            () -> {
                                try {
                                    String balance =
                                            binding.buttomRecargar.getText().toString().trim();
                                    client.toUpBalance(balance);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    errorMessage = e.getMessage();
                                    getActivity()
                                            .runOnUiThread(
                                                    () -> {
                                                        Toast.makeText(
                                                                        getActivity(),
                                                                        errorMessage,
                                                                        Toast.LENGTH_LONG)
                                                                .show();
                                                    });
                                }
                            });
                });

        // buttom transferir
        binding.buttomTransferir.setOnClickListener(
                view -> {
                    executor.execute(
                            () -> {
                                try {
                                    String strMonto =
                                            binding.editTextMonto.getText().toString().trim();
                                    float monto = Float.parseFloat(strMonto);
                                    String email =
                                            binding.editTextCuenta.getText().toString().trim();
                                    client.transferBalance(monto, email);

                                    // confirmacion
                                    getActivity()
                                            .runOnUiThread(
                                                    () -> {
                                                        Toast.makeText(
                                                                        getActivity(),
                                                                        "¡Transferencia exitosa!",
                                                                        Toast.LENGTH_LONG)
                                                                .show();
                                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    errorMessage = e.getMessage();
                                    getActivity()
                                            .runOnUiThread(
                                                    () -> {
                                                        Toast.makeText(
                                                                        getActivity(),
                                                                        errorMessage,
                                                                        Toast.LENGTH_LONG)
                                                                .show();
                                                    });
                                }
                            });
                });

        // cambiar contraseña
        binding.buttomPassword.setOnClickListener(
                view -> {
                    executor.execute(
                            () -> {
                                try {
                                    String nueva =
                                            binding.editTextPassword.getText().toString().trim();
                                    client.changePassword(nueva);
                                    getActivity()
                                            .runOnUiThread(
                                                    () -> {
                                                        Toast.makeText(
                                                                        getActivity(),
                                                                        "¡Cambio de contraseña exitoso!",
                                                                        Toast.LENGTH_LONG)
                                                                .show();
                                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    errorMessage = e.getMessage();
                                    getActivity()
                                            .runOnUiThread(
                                                    () -> {
                                                        Toast.makeText(
                                                                        getContext(),
                                                                        errorMessage,
                                                                        Toast.LENGTH_LONG)
                                                                .show();
                                                    });
                                }
                            });
                });
        return binding.getRoot();
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher =
            registerForActivityResult(
                    new ScanContract(),
                    result -> {
                        if (result.getContents() == null) {
                            // ---> cancelado
                        } else {
                            String code = result.getContents().toString();
                            binding.editTextRecargaCuenta.setText(code);
                        }
                    });

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        nav.setVisibility(View.VISIBLE);
        // Pref.remove(getActivity(), "USER_INFO");
    }
}
