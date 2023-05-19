package com.nova.simple.ui.nauta;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.preference.PreferenceManager;
import com.nova.simple.R;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.nova.simple.NovaApplication;
import com.nova.simple.databinding.FragmentPortalUsuarioBinding;

import com.nova.simple.utils.Pref;
import cu.suitetecsa.sdk.nauta.domain.model.NautaUser;
import cu.suitetecsa.sdk.nauta.domain.service.NautaClient;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PortalUsuarioFragment extends Fragment {

    private FragmentPortalUsuarioBinding binding;
    private NautaClient client;
    private Executor executor;
    private NavController navigate;
    private String errorMessage;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPortalUsuarioBinding.inflate(inflate, container, false);
        NovaApplication app = (NovaApplication) getActivity().getApplicationContext();
        client = app.getNautaClient();

        // --> Autocomplete clave transferir saldo
        SharedPreferences sp_autocomplete =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean active = sp_autocomplete.getBoolean("autocomplete", false);
        if (active) {
            binding.autoCompleteUsuario.setText(sp_autocomplete.getString("email", "").toString());
        } else {
            binding.autoCompleteUsuario.getText().clear();
        }

        navigate = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        // executor
        executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> loadCaptcha());

        // recharge cpatcha
        binding.inputCaptcha.setEndIconOnClickListener(
                view -> {
                    executor.execute(() -> loadCaptcha());
                });

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
        // buttom connect
        binding.buttomConnect.setOnClickListener(
                view -> {
                    String usuario = binding.autoCompleteUsuario.getText().toString().trim();
                    String password = binding.editTextPassword.getText().toString().trim();
                    String captcha = binding.editTextCaptcha.getText().toString().trim();
                    if (usuario.isEmpty() && password.isEmpty() && captcha.isEmpty()) {
                        Toast.makeText(getActivity(), "¡Hay campos vacíos!", Toast.LENGTH_LONG)
                                .show();
                    } else {
                        executor.execute(() -> connect(usuario, password, captcha));
                    }
                });
        return binding.getRoot();
    }

    private void connect(String usuario, String password, String captcha) {
        try {
            InetAddress.getByName("www.portal.nauta.cu");

            client.setCredentials(usuario, password);
            NautaUser result = client.login(captcha);

            getActivity()
                    .runOnUiThread(
                            () -> {
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("time", result.getTime());
                                map.put("account", result.getUserName());
                                map.put("saldo", result.getCredit());
                                map.put("tipo", result.getAccountType());
                                map.put("bloqueo", result.getBlockingDate());
                                map.put("delete", result.getDateOfElimination());

                                Pref.save(getActivity(), "USER_INFO", map);

                                // info
                                navigate.navigate(R.id.navigation_info_user);
                            });
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = e.getMessage();
            getActivity()
                    .runOnUiThread(
                            () -> {
                                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG)
                                        .show();
                            });
        }
    }

    private void loadCaptcha() {
        try {
            byte[] byteArray = client.getCaptchaImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            getActivity().runOnUiThread(() -> updateCaptchaImage(bitmap));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCaptchaImage(Bitmap captchaBitmap) {
        binding.imageCaptcha.setImageBitmap(captchaBitmap);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
