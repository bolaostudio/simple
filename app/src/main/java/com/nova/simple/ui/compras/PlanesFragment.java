package com.nova.simple.ui.compras;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.elevation.SurfaceColors;
import com.nova.simple.R;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import com.nova.simple.adapter.ItemsAdapter;
import com.nova.simple.adapter.ItemsClickListener;
import com.nova.simple.databinding.FragmentPlanesBinding;
import com.nova.simple.databinding.LayoutBottomSheetComprasBinding;
import com.nova.simple.model.GridView;
import com.nova.simple.model.HeadView;
import com.nova.simple.model.TypeView;
import com.nova.simple.utils.SIMDialer;
import java.util.ArrayList;

public class PlanesFragment extends Fragment {

    private FragmentPlanesBinding binding;
    LayoutBottomSheetComprasBinding bsheet;
    private ItemsAdapter adapter;
    private ArrayList<TypeView> item = new ArrayList<>();
    private SharedPreferences preferences;

    SharedPreferences sp_sim;
    String simslot;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPlanesBinding.inflate(inflater, container, false);

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
                                        // Header
                                        break;
                                    case 1:
                                        compra_plan_basico();
                                        break;
                                    case 2:
                                        compra_plan_medio();
                                        break;
                                    case 3:
                                        compra_plan_extra();
                                        break;
                                    case 4:
                                        // header
                                        break;
                                    case 5:
                                        compra_paquete_a();
                                        break;
                                    case 6:
                                        compra_paquete_b();
                                        break;
                                    case 7:
                                        compra_paquete_c();
                                        break;
                                    case 8:
                                        // header
                                        break;
                                    case 9:
                                        compra_mensajeria();
                                        break;
                                    case 10:
                                        compra_diaria();
                                        break;
                                }
                            }
                        }));

        // tarifa por consumo
        SharedPreferences sp_tarifa =
                getActivity().getSharedPreferences("tarifa", Context.MODE_PRIVATE);
        if (!sp_tarifa.contains("isChecked")) {
            SharedPreferences.Editor editor = sp_tarifa.edit();
            editor.putInt("isChecked", -1);
            editor.apply();
        }
        
        int selectedButtom = -1;
        try {
            selectedButtom = sp_tarifa.getInt("isChecked", -1);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        if (selectedButtom != -1) {
            binding.toggleGroup.check(selectedButtom);
        }

        binding.toggleGroup.addOnButtonCheckedListener(
                (group, checkedId, isChecked) -> {
                    if (isChecked) {
                        sp_tarifa.edit().putInt("isChecked", checkedId).apply();
                        if (ContextCompat.checkSelfPermission(
                                        getActivity(), Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(
                                            getActivity(),
                                            getString(R.string.message_permissions_denied),
                                            Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            switch (checkedId) {
                                case R.id.buttomDisable:
                                    SIMDialer.call(
                                            getActivity(),
                                            "*133*1*1*2" + Uri.encode("#"),
                                            Integer.parseInt(simslot));
                                    break;
                                case R.id.buttomEnable:
                                    SIMDialer.call(
                                            getActivity(),
                                            "*133*1*1*1" + Uri.encode("#"),
                                            Integer.parseInt(simslot));
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                });

        return binding.getRoot();
    }

    private void loadItems() {
        item.add(new HeadView(getString(R.string.title_categoria_combos)));
        item.add(
                new GridView(
                        getString(R.string.title_plan_basico),
                        getString(R.string.subtitle_plan_basico),
                        R.drawable.ic_paquetes_20px));
        item.add(
                new GridView(
                        getString(R.string.title_plan_medio),
                        getString(R.string.subtitle_plan_medio),
                        R.drawable.ic_paquetes_20px));
        item.add(
                new GridView(
                        getString(R.string.title_plan_extra),
                        getString(R.string.subtitle_plan_extra),
                        R.drawable.ic_paquetes_20px));
        item.add(new HeadView(getString(R.string.title_categoria_lte)));
        item.add(
                new GridView(
                        getString(R.string.title_lte_a),
                        getString(R.string.subtitle_lte),
                        R.drawable.ic_lte_20px));
        item.add(
                new GridView(
                        getString(R.string.title_lte_b),
                        getString(R.string.subtitle_lte),
                        R.drawable.ic_lte_20px));
        item.add(
                new GridView(
                        getString(R.string.title_lte_c),
                        getString(R.string.subtitle_lte),
                        R.drawable.ic_lte_20px));
        item.add(new HeadView(getString(R.string.title_categoria_bolsas)));
        item.add(
                new GridView(
                        getString(R.string.title_mensajeria),
                        getString(R.string.subtitle_mensajeria),
                        R.drawable.ic_mensajeria_20px));
        item.add(
                new GridView(
                        getString(R.string.title_diaria),
                        getString(R.string.subtitle_diaria),
                        R.drawable.ic_diaria_20px));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void compra_plan_basico() {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.getWindow().setNavigationBarColor(SurfaceColors.SURFACE_0.getColor(getActivity()));
        bsheet = LayoutBottomSheetComprasBinding.inflate(LayoutInflater.from(getActivity()));
        dialog.setContentView(bsheet.getRoot());

        // info
        bsheet.textTitle.setText(getString(R.string.subtitle_plan_basico));
        bsheet.textPrecio.setText(getString(R.string.plan_basico_precio));
        bsheet.textPaquete.setText(getString(R.string.plan_basico_paquete));
        bsheet.textLte.setText(getString(R.string.plan_basico_lte));
        bsheet.textNacional.setText(getString(R.string.paquetes_nacional));
        bsheet.textVoz.setText(getString(R.string.plan_basico_voz));
        bsheet.textSms.setText(getString(R.string.plan_basico_sms));
        bsheet.textVemcimiento.setText(getString(R.string.plan_basico_vence));

        // positive buttom
        bsheet.buttomBuy.setOnClickListener(
                view -> {
                    SIMDialer.call(
                            getActivity(),
                            "*133*5*1*1" + Uri.encode("#"),
                            Integer.parseInt(simslot));
                });

        // negative buttom
        bsheet.buttomCancel.setOnClickListener(
                view -> {
                    dialog.dismiss();
                });
        dialog.show();
    }

    private void compra_plan_medio() {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.getWindow().setNavigationBarColor(SurfaceColors.SURFACE_0.getColor(getActivity()));
        bsheet = LayoutBottomSheetComprasBinding.inflate(LayoutInflater.from(getActivity()));
        dialog.setContentView(bsheet.getRoot());

        // info
        bsheet.textTitle.setText(getString(R.string.subtitle_plan_medio));
        bsheet.textPrecio.setText(getString(R.string.plan_medio_precio));
        bsheet.textPaquete.setText(getString(R.string.plan_medio_paquete));
        bsheet.textLte.setText(getString(R.string.plan_medio_lte));
        bsheet.textNacional.setText(getString(R.string.paquetes_nacional));
        bsheet.textVoz.setText(getString(R.string.plan_medio_voz));
        bsheet.textSms.setText(getString(R.string.plan_medio_sms));
        bsheet.textVemcimiento.setText(getString(R.string.plan_medio_vence));

        // positive buttom
        bsheet.buttomBuy.setOnClickListener(
                view -> {
                    SIMDialer.call(
                            getActivity(),
                            "*133*5*2*1" + Uri.encode("#"),
                            Integer.parseInt(simslot));
                });

        // negative buttom
        bsheet.buttomCancel.setOnClickListener(
                view -> {
                    dialog.dismiss();
                });
        dialog.show();
    }

    private void compra_plan_extra() {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.getWindow().setNavigationBarColor(SurfaceColors.SURFACE_0.getColor(getActivity()));
        bsheet = LayoutBottomSheetComprasBinding.inflate(LayoutInflater.from(getActivity()));
        dialog.setContentView(bsheet.getRoot());

        // info
        bsheet.textTitle.setText(getString(R.string.subtitle_plan_extra));
        bsheet.textPrecio.setText(getString(R.string.plan_extra_precio));
        bsheet.textPaquete.setText(getString(R.string.plan_extra_paquete));
        bsheet.textLte.setText(getString(R.string.plan_extra_lte));
        bsheet.textNacional.setText(getString(R.string.paquetes_nacional));
        bsheet.textVoz.setText(getString(R.string.plan_extra_voz));
        bsheet.textSms.setText(getString(R.string.plan_extra_sms));
        bsheet.textVemcimiento.setText(getString(R.string.plan_extra_vence));

        // positive buttom
        bsheet.buttomBuy.setOnClickListener(
                view -> {
                    SIMDialer.call(
                            getActivity(),
                            "*133*5*3*1" + Uri.encode("#"),
                            Integer.parseInt(simslot));
                });

        // negative buttom
        bsheet.buttomCancel.setOnClickListener(
                view -> {
                    dialog.dismiss();
                });
        dialog.show();
    }

    private void compra_paquete_a() {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.getWindow().setNavigationBarColor(SurfaceColors.SURFACE_0.getColor(getActivity()));
        bsheet = LayoutBottomSheetComprasBinding.inflate(LayoutInflater.from(getActivity()));
        dialog.setContentView(bsheet.getRoot());

        // info
        bsheet.textTitle.setText(getString(R.string.title_categoria_lte));
        bsheet.textPrecio.setText(getString(R.string.paquete_a_precio));
        bsheet.textPaquete.setVisibility(View.GONE);
        bsheet.textLte.setText(getString(R.string.paquete_a_lte));
        bsheet.textNacional.setText(getString(R.string.paquetes_nacional));
        bsheet.textVoz.setVisibility(View.GONE);
        bsheet.textSms.setVisibility(View.GONE);
        bsheet.textVemcimiento.setText(getString(R.string.paquete_a_vence));

        // positive buttom
        bsheet.buttomBuy.setOnClickListener(
                view -> {
                    SIMDialer.call(
                            getActivity(),
                            "*133*1*4*1*1" + Uri.encode("#"),
                            Integer.parseInt(simslot));
                });

        // negative buttom
        bsheet.buttomCancel.setOnClickListener(
                view -> {
                    dialog.dismiss();
                });
        dialog.show();
    }

    private void compra_paquete_b() {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.getWindow().setNavigationBarColor(SurfaceColors.SURFACE_0.getColor(getActivity()));
        bsheet = LayoutBottomSheetComprasBinding.inflate(LayoutInflater.from(getActivity()));
        dialog.setContentView(bsheet.getRoot());

        // info
        bsheet.textTitle.setText(getString(R.string.title_categoria_lte));
        bsheet.textPrecio.setText(getString(R.string.paquete_b_precio));
        bsheet.textPaquete.setVisibility(View.GONE);
        bsheet.textLte.setText(getString(R.string.paquete_b_lte));
        bsheet.textNacional.setText(getString(R.string.paquetes_nacional));
        bsheet.textVoz.setVisibility(View.GONE);
        bsheet.textSms.setVisibility(View.GONE);
        bsheet.textVemcimiento.setText(getString(R.string.paquete_b_vence));

        // positive buttom
        bsheet.buttomBuy.setOnClickListener(
                view -> {
                    SIMDialer.call(
                            getActivity(),
                            "*133*1*4*2*1" + Uri.encode("#"),
                            Integer.parseInt(simslot));
                });

        // negative buttom
        bsheet.buttomCancel.setOnClickListener(
                view -> {
                    dialog.dismiss();
                });
        dialog.show();
    }

    private void compra_paquete_c() {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.getWindow().setNavigationBarColor(SurfaceColors.SURFACE_0.getColor(getActivity()));
        bsheet = LayoutBottomSheetComprasBinding.inflate(LayoutInflater.from(getActivity()));
        dialog.setContentView(bsheet.getRoot());

        // info
        bsheet.textTitle.setText(getString(R.string.title_categoria_lte));
        bsheet.textPrecio.setText(getString(R.string.paquete_c_precio));
        bsheet.textPaquete.setText(getString(R.string.paquete_c_paquete));
        bsheet.textLte.setText(getString(R.string.paquete_c_lte));
        bsheet.textNacional.setText(getString(R.string.paquetes_nacional));
        bsheet.textVoz.setVisibility(View.GONE);
        bsheet.textSms.setVisibility(View.GONE);
        bsheet.textVemcimiento.setText(getString(R.string.paquete_c_vence));

        // positive buttom
        bsheet.buttomBuy.setOnClickListener(
                view -> {
                    SIMDialer.call(
                            getActivity(),
                            "*133*1*4*3*1" + Uri.encode("#"),
                            Integer.parseInt(simslot));
                });

        // negative buttom
        bsheet.buttomCancel.setOnClickListener(
                view -> {
                    dialog.dismiss();
                });
        dialog.show();
    }

    private void compra_mensajeria() {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.getWindow().setNavigationBarColor(SurfaceColors.SURFACE_0.getColor(getActivity()));
        bsheet = LayoutBottomSheetComprasBinding.inflate(LayoutInflater.from(getActivity()));
        dialog.setContentView(bsheet.getRoot());

        // info
        bsheet.textTitle.setText(getString(R.string.subtitle_mensajeria));
        bsheet.textPrecio.setText(getString(R.string.mensajeria_precio));
        bsheet.textPaquete.setText(getString(R.string.mensajeria_paquete));
        bsheet.textLte.setVisibility(View.GONE);
        bsheet.textNacional.setVisibility(View.GONE);
        bsheet.textVoz.setVisibility(View.GONE);
        bsheet.textSms.setVisibility(View.GONE);
        bsheet.textVemcimiento.setText(getString(R.string.mensajeria_vence));

        // positive buttom
        bsheet.buttomBuy.setOnClickListener(
                view -> {
                    SIMDialer.call(
                            getActivity(),
                            "*133*1*2*1" + Uri.encode("#"),
                            Integer.parseInt(simslot));
                });

        // negative buttom
        bsheet.buttomCancel.setOnClickListener(
                view -> {
                    dialog.dismiss();
                });
        dialog.show();
    }

    private void compra_diaria() {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.getWindow().setNavigationBarColor(SurfaceColors.SURFACE_0.getColor(getActivity()));
        bsheet = LayoutBottomSheetComprasBinding.inflate(LayoutInflater.from(getActivity()));
        dialog.setContentView(bsheet.getRoot());

        // info
        bsheet.textTitle.setText(getString(R.string.subtitle_diaria));
        bsheet.textPrecio.setText(getString(R.string.diaria_precio));
        bsheet.textPaquete.setVisibility(View.GONE);
        bsheet.textLte.setText(getString(R.string.diaria_paquete));
        bsheet.textNacional.setVisibility(View.GONE);
        bsheet.textVoz.setVisibility(View.GONE);
        bsheet.textSms.setVisibility(View.GONE);
        bsheet.textVemcimiento.setText(getString(R.string.diaria_vence));

        // positive buttom
        bsheet.buttomBuy.setOnClickListener(
                view -> {
                    SIMDialer.call(
                            getActivity(),
                            "*133*1*3*1" + Uri.encode("#"),
                            Integer.parseInt(simslot));
                });

        // negative buttom
        bsheet.buttomCancel.setOnClickListener(
                view -> {
                    dialog.dismiss();
                });
        dialog.show();
    }
}
