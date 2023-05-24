package com.nova.simple.ui.nauta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nova.simple.NovaApplication;
import com.nova.simple.R;
import com.nova.simple.databinding.FragmentConectadoBinding;

import cu.suitetecsa.sdk.nauta.domain.service.NautaClient;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ConectadoFragment extends Fragment {

    private FragmentConectadoBinding binding;
    private NautaClient client;
    private Executor executor;
    private BottomNavigationView nav;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentConectadoBinding.inflate(inflater, container, false);

        // hide bottomnavigationview
        nav = Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_nav_view);
        nav.setVisibility(View.GONE);

        NovaApplication app = (NovaApplication) getActivity().getApplicationContext();
        client = app.getNautaClient();

        // desconectar
        binding.buttomDesconectar.setOnClickListener(
                view -> {
                    executor = Executors.newSingleThreadExecutor();
                    executor.execute(() -> disconnect());
                });

        return binding.getRoot();
    }

    private void conectado(String user, String pass) {
        /* try {
             Map<String, Object> info = client.getConnectInformation(user, pass);

        } catch (LoginException e) {
            e.printStackTrace();
        }*/
    }

    private void disconnect() {
        try {
            client.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        nav.setVisibility(View.VISIBLE);
    }
}
