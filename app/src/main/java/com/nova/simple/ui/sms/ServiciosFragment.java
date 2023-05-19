package com.nova.simple.ui.sms;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.elevation.SurfaceColors;
import com.nova.simple.R;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import com.nova.simple.adapter.ItemsAdapter;
import com.nova.simple.adapter.ItemsClickListener;
import com.nova.simple.databinding.FragmentAmigoBinding;
import com.nova.simple.databinding.FragmentServiciosBinding;
import com.nova.simple.databinding.LayoutBottomSheetServiciosBinding;
import com.nova.simple.model.GridView;
import com.nova.simple.model.TypeView;
import com.nova.simple.utils.SendSMS;
import java.util.ArrayList;
import java.util.Objects;

public class ServiciosFragment extends Fragment {

    private FragmentServiciosBinding binding;
    private LayoutBottomSheetServiciosBinding custom;
    private ItemsAdapter adapter;
    private ArrayList<TypeView> item = new ArrayList<>();

    private BottomNavigationView nav;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentServiciosBinding.inflate(inflater, container, false);

        // hide bottomnavigationview
        nav = Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_nav_view);
        nav.setVisibility(View.GONE);

        // RecyclerView
        binding.recyclerView.setHasFixedSize(true);
        adapter = new ItemsAdapter(getActivity(), item);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        loadItems();

        binding.recyclerView.addOnItemTouchListener(
                new ItemsClickListener(
                        getActivity(),
                        binding.recyclerView,
                        new ItemsClickListener.ClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                switch (position) {
                                    case 0:
                                        tipo_de_red();
                                        break;
                                    case 1:
                                        SendSMS.to(getActivity(), "2266", "LTE");
                                        break;
                                    case 2:
                                        SendSMS.to(getActivity(), "2266", "SIM");
                                        break;
                                    case 3:
                                        SendSMS.to(getActivity(), "8000", "TURISMO");
                                        break;
                                    case 4:
                                        SendSMS.to(getActivity(), "8000", "CANCELAR INFOMOVIL");
                                        break;
                                    case 5:
                                        SendSMS.to(getActivity(), "2266", "TARIFA");
                                        break;
                                    case 6:
                                        SendSMS.to(getActivity(), "2266", "OFERTA");
                                        break;
                                }
                            }
                        }));

        return binding.getRoot();
    }

    private void loadItems() {
        item.add(
                new GridView(
                        getString(R.string.title_servicio_tipo_red),
                        null,
                        R.drawable.ic_signal_mobile_24px));
        item.add(
                new GridView(
                        getString(R.string.title_servicio_activar_4g),
                        null,
                        R.drawable.ic_lte_20px));
        item.add(
                new GridView(
                        getString(R.string.title_servicio_sim),
                        null,
                        R.drawable.ic_sim_unfilled_24px));
        item.add(
                new GridView(
                        getString(R.string.title_servicio_turismo),
                        null,
                        R.drawable.ic_turismo_24px));
        item.add(
                new GridView(
                        getString(R.string.title_servicio_baja_infomovil),
                        null,
                        R.drawable.ic_infomovil_24px));
        item.add(
                new GridView(
                        getString(R.string.title_servicio_tarifa),
                        null,
                        R.drawable.ic_tarifa_24px));
        item.add(
                new GridView(
                        getString(R.string.title_servicio_oferta),
                        null,
                        R.drawable.ic_oferta_24px));
    }

    private void tipo_de_red() {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        custom = LayoutBottomSheetServiciosBinding.inflate(LayoutInflater.from(getContext()));
        dialog.setContentView(custom.getRoot());
        dialog.getWindow().setNavigationBarColor(SurfaceColors.SURFACE_1.getColor(getActivity()));

        // info
        custom.mTextTitle.setText(getString(R.string.title_servicio_tipo_red));
        custom.mtextDescription.setText(
                "Escriba los primeros 8 dígitos de su IMEI, el cual podrá consultar marcando *#06#");
        custom.buttomConsulta.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String code = custom.editServicios.getText().toString().trim();
                        if (code.isEmpty()) {
                            Toast.makeText(getActivity(), "Faltan caracteres", Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            SendSMS.to(getActivity(), "2266", code);
                        }
                        dialog.dismiss();
                    }
                });

        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        nav.setVisibility(View.VISIBLE);
    }
}
