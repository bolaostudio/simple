package com.nova.simple.ui.compras;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.elevation.SurfaceColors;
import com.nova.simple.R;
import com.nova.simple.adapter.ItemsAdapter;
import com.nova.simple.adapter.ItemsClickListener;
import com.nova.simple.databinding.FragmentPaquetesBinding;
import com.nova.simple.databinding.LayoutButtomSheetPaqueteBinding;
import com.nova.simple.model.GridView;
import com.nova.simple.model.HeadView;
import com.nova.simple.model.TypeView;

import com.nova.simple.utils.SIMDialer;
import java.util.ArrayList;

public class PaquetesFragment extends Fragment {

    private FragmentPaquetesBinding binding;
    private LayoutButtomSheetPaqueteBinding dbinding;

    private ItemsAdapter adapter;
    private ArrayList<TypeView> item = new ArrayList<>();

    SharedPreferences sp_sim;
    String simslot;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPaquetesBinding.inflate(inflater, container, false);

        // sim slot
        sp_sim = PreferenceManager.getDefaultSharedPreferences(getActivity());
        simslot = sp_sim.getString("sim_preference", "0");

        // recyclerview
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setNestedScrollingEnabled(false);
        adapter = new ItemsAdapter(getActivity(), item);
        binding.recyclerView.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        manager.setSpanSizeLookup(
                new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return adapter.getItemViewType(position) == HeadView.VIEW_HEADER ? 2 : 1;
                    }
                });
        binding.recyclerView.setLayoutManager(manager);
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
                                        // header
                                        break;
                                    case 1:
                                        compras_5_minutos();
                                        break;
                                    case 2:
                                        compras_10_minutos();
                                        break;
                                    case 3:
                                        compras_15_minutos();
                                        break;
                                    case 4:
                                        compras_25_minutos();
                                        break;
                                    case 5:
                                        compras_40_minutos();
                                        break;
                                    case 6:
                                        // header
                                        break;
                                    case 7:
                                        compras_20_mensajes();
                                        break;
                                    case 8:
                                        compras_50_mensajes();
                                        break;
                                    case 9:
                                        compras_50_mensajes();
                                        break;
                                    case 10:
                                        compras_120_mensajes();
                                        break;
                                }
                            }
                        }));

        // consultar minutos
        binding.buttomConsultaMinutos.setOnClickListener(
                view -> {
                    SIMDialer.call(
                            getActivity(), "*222*869" + Uri.encode("#"), Integer.parseInt(simslot));
                });

        // consultar mensajes
        binding.buttomConsultaMensajes.setOnClickListener(
                view -> {
                    SIMDialer.call(
                            getActivity(), "*222*767" + Uri.encode("#"), Integer.parseInt(simslot));
                });
        return binding.getRoot();
    }

    private void loadItems() {
        item.add(new HeadView(getString(R.string.categoria_voz)));
        item.add(
                new GridView(
                        getString(R.string.title_5_min),
                        getString(R.string.title_minutos),
                        R.drawable.ic_llamadas_unfilled_24px));
        item.add(
                new GridView(
                        getString(R.string.title_10_min),
                        getString(R.string.title_minutos),
                        R.drawable.ic_llamadas_unfilled_24px));
        item.add(
                new GridView(
                        getString(R.string.title_15_min),
                        getString(R.string.title_minutos),
                        R.drawable.ic_llamadas_unfilled_24px));
        item.add(
                new GridView(
                        getString(R.string.title_25_min),
                        getString(R.string.title_minutos),
                        R.drawable.ic_llamadas_unfilled_24px));
        item.add(
                new GridView(
                        getString(R.string.title_40_min),
                        getString(R.string.title_minutos),
                        R.drawable.ic_llamadas_unfilled_24px));

        // mensajes
        item.add(new HeadView(getString(R.string.categoria_sms)));
        item.add(
                new GridView(
                        getString(R.string.title_20_sms),
                        getString(R.string.title_mensajes),
                        R.drawable.ic_balance_sms_20px));
        item.add(
                new GridView(
                        getString(R.string.title_50_sms),
                        getString(R.string.title_mensajes),
                        R.drawable.ic_balance_sms_20px));
        item.add(
                new GridView(
                        getString(R.string.title_90_sms),
                        getString(R.string.title_mensajes),
                        R.drawable.ic_balance_sms_20px));
        item.add(
                new GridView(
                        getString(R.string.title_120_sms),
                        getString(R.string.title_mensajes),
                        R.drawable.ic_balance_sms_20px));
    }

    private void compras_5_minutos() {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.getWindow().setNavigationBarColor(SurfaceColors.SURFACE_0.getColor(getActivity()));
        dbinding = LayoutButtomSheetPaqueteBinding.inflate(LayoutInflater.from(getActivity()));
        dialog.setContentView(dbinding.getRoot());

        // info
        dbinding.textTitle.setText(getString(R.string.title_minutos));
        dbinding.textPrecio.setText(getString(R.string.minutos_5_precio));
        dbinding.textMessage.setText(getString(R.string.minutos_5));

        // positive buttom
        dbinding.buttomBuy.setOnClickListener(
                view -> {
                    SIMDialer.call(
                            getActivity(),
                            "*133*3*1*1" + Uri.encode("#"),
                            Integer.parseInt(simslot));
                });

        // negative buttom
        dbinding.buttomCancel.setOnClickListener(
                view -> {
                    dialog.dismiss();
                });
        dialog.show();
    }

    private void compras_10_minutos() {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.getWindow().setNavigationBarColor(SurfaceColors.SURFACE_0.getColor(getActivity()));
        dbinding = LayoutButtomSheetPaqueteBinding.inflate(LayoutInflater.from(getActivity()));
        dialog.setContentView(dbinding.getRoot());

        // info
        dbinding.textTitle.setText(getString(R.string.title_minutos));
        dbinding.textPrecio.setText(getString(R.string.minutos_10_precio));
        dbinding.textMessage.setText(getString(R.string.minutos_10));

        // positive buttom
        dbinding.buttomBuy.setOnClickListener(
                view -> {
                    SIMDialer.call(
                            getActivity(),
                            "*133*3*2*1" + Uri.encode("#"),
                            Integer.parseInt(simslot));
                });

        // negative buttom
        dbinding.buttomCancel.setOnClickListener(
                view -> {
                    dialog.dismiss();
                });
        dialog.show();
    }

    private void compras_15_minutos() {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.getWindow().setNavigationBarColor(SurfaceColors.SURFACE_0.getColor(getActivity()));
        dbinding = LayoutButtomSheetPaqueteBinding.inflate(LayoutInflater.from(getActivity()));
        dialog.setContentView(dbinding.getRoot());

        // info
        dbinding.textTitle.setText(getString(R.string.title_minutos));
        dbinding.textPrecio.setText(getString(R.string.minutos_15_precio));
        dbinding.textMessage.setText(getString(R.string.minutos_15));

        // positive buttom
        dbinding.buttomBuy.setOnClickListener(
                view -> {
                    SIMDialer.call(
                            getActivity(),
                            "*133*3*3*1" + Uri.encode("#"),
                            Integer.parseInt(simslot));
                });

        // negative buttom
        dbinding.buttomCancel.setOnClickListener(
                view -> {
                    dialog.dismiss();
                });
        dialog.show();
    }

    private void compras_25_minutos() {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.getWindow().setNavigationBarColor(SurfaceColors.SURFACE_0.getColor(getActivity()));
        dbinding = LayoutButtomSheetPaqueteBinding.inflate(LayoutInflater.from(getActivity()));
        dialog.setContentView(dbinding.getRoot());

        // info
        dbinding.textTitle.setText(getString(R.string.title_minutos));
        dbinding.textPrecio.setText(getString(R.string.minutos_25_precio));
        dbinding.textMessage.setText(getString(R.string.minutos_25));

        // positive buttom
        dbinding.buttomBuy.setOnClickListener(
                view -> {
                    SIMDialer.call(
                            getActivity(),
                            "*133*3*4*1" + Uri.encode("#"),
                            Integer.parseInt(simslot));
                });

        // negative buttom
        dbinding.buttomCancel.setOnClickListener(
                view -> {
                    dialog.dismiss();
                });
        dialog.show();
    }

    private void compras_40_minutos() {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.getWindow().setNavigationBarColor(SurfaceColors.SURFACE_0.getColor(getActivity()));
        dbinding = LayoutButtomSheetPaqueteBinding.inflate(LayoutInflater.from(getActivity()));
        dialog.setContentView(dbinding.getRoot());

        // info
        dbinding.textTitle.setText(getString(R.string.title_minutos));
        dbinding.textPrecio.setText(getString(R.string.minutos_40_precio));
        dbinding.textMessage.setText(getString(R.string.minutos_40));

        // positive buttom
        dbinding.buttomBuy.setOnClickListener(
                view -> {
                    SIMDialer.call(
                            getActivity(),
                            "*133*3*5*1" + Uri.encode("#"),
                            Integer.parseInt(simslot));
                });

        // negative buttom
        dbinding.buttomCancel.setOnClickListener(
                view -> {
                    dialog.dismiss();
                });
        dialog.show();
    }

    // mensajes
    private void compras_20_mensajes() {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.getWindow().setNavigationBarColor(SurfaceColors.SURFACE_0.getColor(getActivity()));
        dbinding = LayoutButtomSheetPaqueteBinding.inflate(LayoutInflater.from(getActivity()));
        dialog.setContentView(dbinding.getRoot());

        // info
        dbinding.textTitle.setText(getString(R.string.title_mensajes));
        dbinding.textPrecio.setText(getString(R.string.sms_20_precio));
        dbinding.textMessage.setText(getString(R.string.plan_sms_20));

        // positive buttom
        dbinding.buttomBuy.setOnClickListener(
                view -> {
                    SIMDialer.call(
                            getActivity(),
                            "*133*2*1*1" + Uri.encode("#"),
                            Integer.parseInt(simslot));
                });

        // negative buttom
        dbinding.buttomCancel.setOnClickListener(
                view -> {
                    dialog.dismiss();
                });
        dialog.show();
    }

    private void compras_50_mensajes() {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.getWindow().setNavigationBarColor(SurfaceColors.SURFACE_0.getColor(getActivity()));
        dbinding = LayoutButtomSheetPaqueteBinding.inflate(LayoutInflater.from(getActivity()));
        dialog.setContentView(dbinding.getRoot());

        // info
        dbinding.textTitle.setText(getString(R.string.title_mensajes));
        dbinding.textPrecio.setText(getString(R.string.sms_50_precio));
        dbinding.textMessage.setText(getString(R.string.plan_sms_50));

        // positive buttom
        dbinding.buttomBuy.setOnClickListener(
                view -> {
                    SIMDialer.call(
                            getActivity(),
                            "*133*2*2*1" + Uri.encode("#"),
                            Integer.parseInt(simslot));
                });

        // negative buttom
        dbinding.buttomCancel.setOnClickListener(
                view -> {
                    dialog.dismiss();
                });
        dialog.show();
    }

    private void compras_90_mensajes() {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.getWindow().setNavigationBarColor(SurfaceColors.SURFACE_0.getColor(getActivity()));
        dbinding = LayoutButtomSheetPaqueteBinding.inflate(LayoutInflater.from(getActivity()));
        dialog.setContentView(dbinding.getRoot());

        // info
        dbinding.textTitle.setText(getString(R.string.title_mensajes));
        dbinding.textPrecio.setText(getString(R.string.sms_90_precio));
        dbinding.textMessage.setText(getString(R.string.plan_sms_90));

        // positive buttom
        dbinding.buttomBuy.setOnClickListener(
                view -> {
                    SIMDialer.call(
                            getActivity(),
                            "*133*2*3*1" + Uri.encode("#"),
                            Integer.parseInt(simslot));
                });

        // negative buttom
        dbinding.buttomCancel.setOnClickListener(
                view -> {
                    dialog.dismiss();
                });
        dialog.show();
    }

    private void compras_120_mensajes() {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.getWindow().setNavigationBarColor(SurfaceColors.SURFACE_0.getColor(getActivity()));
        dbinding = LayoutButtomSheetPaqueteBinding.inflate(LayoutInflater.from(getActivity()));
        dialog.setContentView(dbinding.getRoot());

        // info
        dbinding.textTitle.setText(getString(R.string.title_mensajes));
        dbinding.textPrecio.setText(getString(R.string.sms_120_precio));
        dbinding.textMessage.setText(getString(R.string.plan_sms_120));

        // positive buttom
        dbinding.buttomBuy.setOnClickListener(
                view -> {
                    SIMDialer.call(
                            getActivity(),
                            "*133*2*4*1" + Uri.encode("#"),
                            Integer.parseInt(simslot));
                });

        // negative buttom
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
