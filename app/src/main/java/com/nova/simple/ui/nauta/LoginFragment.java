package com.nova.simple.ui.nauta;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.elevation.SurfaceColors;
import com.nova.simple.NovaApplication;
import com.nova.simple.databinding.FragmentLoginBinding;

import com.nova.simple.databinding.LayoutBottomSheetConnectedBinding;
import com.nova.simple.utils.Pref;
import cu.suitetecsa.sdk.nauta.domain.service.NautaClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import com.nova.simple.R;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    private String errorMessage;
    private NautaClient client;
    Executor executor;
    private NavController navigate;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        // tiempo en la cuenta
        String tiempo = Pref.load(getActivity(), "USER_INFO", "time", "00:00:00");
        binding.textTiempo.setText(tiempo);

        // --> Autocomplete clave transferir saldo
        SharedPreferences sp_autocomplete =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean active = sp_autocomplete.getBoolean("autocomplete", false);
        if (active) {
            binding.autoCompleteUsuario.setText(sp_autocomplete.getString("email", "").toString());
        } else {
            binding.autoCompleteUsuario.getText().clear();
        }

        //
        navigate = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);

        // import Api nautaclient to Application
        NovaApplication app = (NovaApplication) getActivity().getApplicationContext();
        client = app.getNautaClient();

        // cargar tipo de correo
        final String[] email = getResources().getStringArray(R.array.email);
        final ArrayList<String> maindb = new ArrayList<>(Arrays.asList(email));
        final ArrayAdapter<String> adpt =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line);
        binding.autoCompleteUsuario.setAdapter(adpt);
        binding.autoCompleteUsuario.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() >= 0 && !s.toString().contains("@")) {
                            adpt.clear();
                            for (String domain : maindb) {
                                adpt.add(s + domain);
                            }
                        }
                    }

                    @Override
                    public void beforeTextChanged(
                            CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
                });

        // connect login nauta
        binding.buttomLogin.setOnClickListener(
                view -> {
                    String usuario = binding.autoCompleteUsuario.getText().toString().trim();
                    String password = binding.editTextPassword.getText().toString().trim();
                    executor = Executors.newSingleThreadExecutor();
                    executor.execute(() -> connect(usuario, password));
                });

        return binding.getRoot();
    }

    private void connect(String user, String password) {
        try {
            client.setCredentials(user, password);
            client.connect();

            // TODO: Cargar información cuando hace login
            getActivity()
                    .runOnUiThread(
                            () -> {
                                navigate.navigate(R.id.nav_nauta_conectado);
                            });
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = e.getMessage();
            getActivity()
                    .runOnUiThread(
                            () ->
                                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG)
                                            .show());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
