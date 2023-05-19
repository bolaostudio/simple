package com.nova.simple.ui.compras;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import androidx.core.net.UriCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.elevation.SurfaceColors;
import com.nova.simple.R;
import com.nova.simple.adapter.ItemsAdapter;
import com.nova.simple.adapter.ItemsClickListener;
import com.nova.simple.databinding.FragmentAmigoBinding;
import com.nova.simple.databinding.LayoutBottomSheetPlanAmigoBinding;
import com.nova.simple.model.GridView;
import com.nova.simple.model.TypeView;

import com.nova.simple.utils.SIMDialer;
import java.util.ArrayList;

public class AmigoFragment extends Fragment {

    private FragmentAmigoBinding binding;
    private LayoutBottomSheetPlanAmigoBinding dbinding;

    private ItemsAdapter adapter;
    private ArrayList<TypeView> item = new ArrayList<>();
    SharedPreferences sp_sim;
    String simslot;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAmigoBinding.inflate(inflater, container, false);

        // sim slot
        sp_sim = PreferenceManager.getDefaultSharedPreferences(getActivity());
        simslot = sp_sim.getString("sim_preference", "0");

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
                                        SIMDialer.call(
                                                getActivity(),
                                                "*222*264" + Uri.encode("#"),
                                                Integer.parseInt(simslot));
                                        break;
                                    case 1:
                                        SIMDialer.call(
                                                getActivity(),
                                                "*133*4*3" + Uri.encode("#"),
                                                Integer.parseInt(simslot));
                                        break;
                                    case 2:
                                        activar_plan_amigo();
                                        break;
                                    case 3:
                                        desactivar_plan_amigo();
                                        break;
                                    case 4:
                                        agregar_plan_amigo();
                                        break;
                                    case 5:
                                        eliminar_plan_amigo();
                                        break;
                                }
                            }
                        }));

        return binding.getRoot();
    }

    private void loadItems() {
        item.add(
                new GridView(
                        getString(R.string.title_amigo_consulta),
                        getString(R.string.subtitle_amigo_consulta),
                        R.drawable.ic_consulta_amigo_24px));
        item.add(
                new GridView(
                        getString(R.string.title_amigo_lista),
                        getString(R.string.subtitle_amigo_lista),
                        R.drawable.ic_lista_amigo_24px));
        item.add(
                new GridView(
                        getString(R.string.subtitle_amigo_activar),
                        getString(R.string.subtitle_amigo_activar),
                        R.drawable.ic_activa_amigo_24px));
        item.add(
                new GridView(
                        getString(R.string.title_amigo_desactivar),
                        getString(R.string.subtitle_amigo_desactivar),
                        R.drawable.ic_desactiva_amigo_24px));
        item.add(
                new GridView(
                        getString(R.string.title_amigo_agregar),
                        getString(R.string.subtitle_amigo_agregar),
                        R.drawable.ic_agrega_amigo_24px));
        item.add(
                new GridView(
                        getString(R.string.title_amigo_elimina),
                        getString(R.string.subtitle_amigo_elimina),
                        R.drawable.ic_elimina_amigo_24px));
    }

    private void activar_plan_amigo() {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.getWindow().setNavigationBarColor(SurfaceColors.SURFACE_0.getColor(getActivity()));
        dbinding = LayoutBottomSheetPlanAmigoBinding.inflate(LayoutInflater.from(getActivity()));
        dialog.setContentView(dbinding.getRoot());

        // info
        dbinding.textTitle.setText(getString(R.string.title_amigo_activar));
        dbinding.textPrecio.setText(getString(R.string.activar_amigo_precio));
        dbinding.textMessage.setText(getString(R.string.activar_amigo));
        dbinding.inputAmigo.setVisibility(View.GONE);

        // positive buttom
        dbinding.buttomBuy.setOnClickListener(
                view -> {
                    SIMDialer.call(
                            getActivity(),
                            "*133*4*1*1" + Uri.encode("#"),
                            Integer.parseInt(simslot));
                });

        // negative buttom
        dbinding.buttomCancel.setOnClickListener(
                view -> {
                    dialog.dismiss();
                });
        dialog.show();
    }

    private void desactivar_plan_amigo() {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.getWindow().setNavigationBarColor(SurfaceColors.SURFACE_0.getColor(getActivity()));
        dbinding = LayoutBottomSheetPlanAmigoBinding.inflate(LayoutInflater.from(getActivity()));
        dialog.setContentView(dbinding.getRoot());

        dbinding.textTitle.setText(getString(R.string.title_amigo_desactivar));
        dbinding.textPrecio.setText(getString(R.string.desactivar_amigo_precio));
        dbinding.textMessage.setText(getString(R.string.desactivar_amigo));
        dbinding.inputAmigo.setVisibility(View.GONE);

        dbinding.buttomBuy.setOnClickListener(
                view -> {
                    SIMDialer.call(
                            getActivity(),
                            "*133*4*1*2" + Uri.encode("#"),
                            Integer.parseInt(simslot));
                });

        // cancelar
        dbinding.buttomCancel.setOnClickListener(
                view -> {
                    dialog.dismiss();
                });
        dialog.show();
    }

    private void agregar_plan_amigo() {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.getWindow().setNavigationBarColor(SurfaceColors.SURFACE_0.getColor(getActivity()));
        dbinding = LayoutBottomSheetPlanAmigoBinding.inflate(LayoutInflater.from(getActivity()));
        dialog.setContentView(dbinding.getRoot());

        dbinding.textTitle.setText(getString(R.string.title_amigo_agregar));
        dbinding.textPrecio.setText(getString(R.string.agregar_amigo_precio));
        dbinding.textMessage.setText(getString(R.string.agregar_amigo));

        dbinding.buttomBuy.setOnClickListener(
                view -> {
                    String numero = dbinding.editTextAmigo.getText().toString().trim();
                    if (numero.isEmpty()) {
                        Toast.makeText(
                                        getActivity(),
                                        getString(R.string.toast_error_campos_vacios),
                                        Toast.LENGTH_LONG)
                                .show();
                    } else {
                        SIMDialer.call(
                                getActivity(),
                                "*133*4*2*1" + numero + Uri.encode("#"),
                                Integer.parseInt(simslot));
                    }
                });

        // cancelar
        dbinding.buttomCancel.setOnClickListener(
                view -> {
                    dialog.dismiss();
                });
        dialog.show();
    }

    private void eliminar_plan_amigo() {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.getWindow().setNavigationBarColor(SurfaceColors.SURFACE_0.getColor(getActivity()));
        dbinding = LayoutBottomSheetPlanAmigoBinding.inflate(LayoutInflater.from(getActivity()));
        dialog.setContentView(dbinding.getRoot());

        dbinding.textTitle.setText(getString(R.string.title_amigo_elimina));
        dbinding.textPrecio.setText(getString(R.string.eliminar_amigo_precio));
        dbinding.textMessage.setText(getString(R.string.eliminar_amigo));

        dbinding.buttomBuy.setOnClickListener(
                view -> {
                    String numero = dbinding.editTextAmigo.getText().toString().trim();
                    if (numero.isEmpty()) {
                        Toast.makeText(
                                        getActivity(),
                                        getString(R.string.toast_error_campos_vacios),
                                        Toast.LENGTH_LONG)
                                .show();
                    } else {
                        SIMDialer.call(
                                getActivity(),
                                "*133*4*2*2" + numero + Uri.encode("#"),
                                Integer.parseInt(simslot));
                    }
                });

        // cancelar
        dbinding.buttomCancel.setOnClickListener(
                view -> {
                    dialog.dismiss();
                });
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
