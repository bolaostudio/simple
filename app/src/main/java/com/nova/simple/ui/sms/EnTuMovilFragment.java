package com.nova.simple.ui.sms;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.elevation.SurfaceColors;
import com.nova.simple.R;
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

public class EnTuMovilFragment extends Fragment {

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
                        getContext(),
                        binding.recyclerView,
                        (view, position) -> {
                            if (position == 0) {
                                pelota();
                            } else if (position == 1) {
                                horoscopo();
                            } else if (position == 2) {
                                cubadebate();
                            } else if (position == 3) {
                                prensa_latina();
                            } else if (position == 4) {
                                granma();
                            } else if (position == 5) {
                                dhl();
                            } else if (position == 6) {
                                frases_marti();
                            } else if (position == 7) {
                                mlb();
                            } else if (position == 8) {
                                clima();
                            } else if (position == 9) {
                                vuelos();
                            } else if (position == 10) {
                                futbol();
                            } else if (position == 11) {
                                embajadas();
                            } else if (position == 12) {
                                tasaCambio();
                            } else if (position == 13) {
                                apagon();
                            }
                        }));

        return binding.getRoot();
    }

    private void loadItems() {
        item.add(
                new GridView(
                        getString(R.string.title_pelota), null, R.drawable.ic_en_tu_movil_pelota));
        item.add(
                new GridView(
                        getString(R.string.title_horoscopo),
                        null,
                        R.drawable.ic_en_tu_movil_horoscopo));
        item.add(
                new GridView(
                        getString(R.string.title_cubadebate),
                        null,
                        R.drawable.ic_en_tu_movil_cubadebate));
        item.add(
                new GridView(
                        getString(R.string.title_prensa_latina),
                        null,
                        R.drawable.ic_en_tu_movil_prensa_latina));
        item.add(
                new GridView(
                        getString(R.string.title_granma), null, R.drawable.ic_en_tu_movil_granma));
        item.add(new GridView(getString(R.string.title_dhl), null, R.drawable.ic_en_tu_movil_dhl));
        item.add(
                new GridView(
                        getString(R.string.title_marti), null, R.drawable.ic_en_tu_movil_marti));
        item.add(new GridView(getString(R.string.title_mlb), null, R.drawable.ic_en_tu_movil_mlb));
        item.add(
                new GridView(
                        getString(R.string.title_clima), null, R.drawable.ic_en_tu_movil_tiempo));
        item.add(
                new GridView(
                        getString(R.string.title_vuelos), null, R.drawable.ic_en_tu_movil_vuelos));
        item.add(
                new GridView(
                        getString(R.string.title_futbol), null, R.drawable.ic_en_tu_movil_futbol));
        item.add(
                new GridView(
                        getString(R.string.title_embajada),
                        null,
                        R.drawable.ic_en_tu_movil_embajada));
        item.add(
                new GridView(
                        getString(R.string.title_cambio), null, R.drawable.ic_en_tu_movil_cambio));
        item.add(
                new GridView(
                        getString(R.string.title_electricidad),
                        null,
                        R.drawable.ic_emp_electrica_24px));
    }

    private void pelota() {
        CharSequence[] items = getResources().getStringArray(R.array.pelota);
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(getActivity());
        dialog.setTitle(getString(R.string.title_pelota));
        dialog.setItems(
                items,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dia, int which) {
                        switch (which) {
                            case 0:
                                SendSMS.to(getContext(), "8888", "PELOTA");
                                break;
                            case 1:
                                SendSMS.to(getContext(), "8888", "PELOTA POS");
                                break;
                        }
                    }
                });
        dialog.setPositiveButton(getString(R.string.positive_buttom), null);
        dialog.show();
    }

    private void horoscopo() {
        CharSequence[] items = getResources().getStringArray(R.array.horozcopo);
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(getActivity());
        dialog.setTitle(getString(R.string.title_horoscopo));
        dialog.setItems(
                items,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dia, int which) {
                        switch (which) {
                            case 0:
                                SendSMS.to(getContext(), "8888", "ARIES");
                                break;
                            case 1:
                                SendSMS.to(getContext(), "8888", "TAURO");
                                break;
                            case 2:
                                SendSMS.to(getContext(), "8888", "GEMINIS");
                                break;
                            case 3:
                                SendSMS.to(getContext(), "8888", "CANCER");
                                break;
                            case 4:
                                SendSMS.to(getContext(), "8888", "LEO");
                                break;
                            case 5:
                                SendSMS.to(getContext(), "8888", "VIRGO");
                                break;
                            case 6:
                                SendSMS.to(getContext(), "8888", "LIBRA");
                                break;
                            case 7:
                                SendSMS.to(getContext(), "8888", "SCORPIO");
                                break;
                            case 8:
                                SendSMS.to(getContext(), "8888", "SAGITARIO");
                                break;
                            case 9:
                                SendSMS.to(getContext(), "8888", "CAPRICORNIO");
                                break;
                            case 10:
                                SendSMS.to(getContext(), "8888", "ACUARIO");
                                break;
                            case 11:
                                SendSMS.to(getContext(), "8888", "PISCIS");
                                break;
                        }
                    }
                });
        dialog.setPositiveButton(getString(R.string.positive_buttom), null);
        dialog.show();
    }

    private void tasaCambio() {
        CharSequence[] items = getResources().getStringArray(R.array.cambio);
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(getActivity());
        dialog.setTitle(getString(R.string.title_cambio));
        dialog.setItems(
                items,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dia, int which) {
                        switch (which) {
                            case 0:
                                SendSMS.to(getContext(), "8888", "EUR");
                                break;
                            case 1:
                                SendSMS.to(getContext(), "8888", "USD");
                                break;
                            case 2:
                                SendSMS.to(getContext(), "8888", "MXN");
                                break;
                            case 3:
                                SendSMS.to(getContext(), "8888", "CAD");
                                break;
                            case 4:
                                SendSMS.to(getContext(), "8888", "GBP");
                                break;
                            case 5:
                                SendSMS.to(getContext(), "8888", "GPY");
                                break;
                            case 6:
                                SendSMS.to(getContext(), "8888", "CHF");
                                break;
                            case 7:
                                SendSMS.to(getContext(), "8888", "DKK");
                                break;
                            case 8:
                                SendSMS.to(getContext(), "8888", "NOK");
                                break;
                            case 9:
                                SendSMS.to(getContext(), "8888", "SEK");
                                break;
                        }
                    }
                });
        dialog.setPositiveButton(getString(R.string.positive_buttom), null);
        dialog.show();
    }

    private void cubadebate() {
        CharSequence[] items = getResources().getStringArray(R.array.periodicos);
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(getActivity());
        dialog.setTitle(getString(R.string.title_cubadebate));
        dialog.setItems(
                items,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dia, int which) {
                        switch (which) {
                            case 0:
                                SendSMS.to(getContext(), "8100", "CUBADEBATE");
                                break;
                            case 1:
                                SendSMS.to(getContext(), "8888", "CUBADEBATE BAJA");
                                break;
                        }
                    }
                });
        dialog.setPositiveButton(getString(R.string.positive_buttom), null);
        dialog.show();
    }

    private void prensa_latina() {
        CharSequence[] items = getResources().getStringArray(R.array.periodicos);
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(getActivity());
        dialog.setTitle(getString(R.string.title_prensa_latina));
        dialog.setItems(
                items,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dia, int which) {
                        switch (which) {
                            case 0:
                                SendSMS.to(getContext(), "8100", "PL");
                                break;
                            case 1:
                                SendSMS.to(getContext(), "8888", "PL BAJA");
                                break;
                        }
                    }
                });
        dialog.setPositiveButton(getString(R.string.positive_buttom), null);
        dialog.show();
    }

    private void granma() {
        CharSequence[] items = getResources().getStringArray(R.array.periodicos);
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(getActivity());
        dialog.setTitle(getString(R.string.title_granma));
        dialog.setItems(
                items,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dia, int which) {
                        switch (which) {
                            case 0:
                                SendSMS.to(getContext(), "8100", "GRANMA");
                                break;
                            case 1:
                                SendSMS.to(getContext(), "8888", "GRANMA BAJA");
                                break;
                        }
                    }
                });
        dialog.setPositiveButton(getString(R.string.positive_buttom), null);
        dialog.show();
    }

    private void dhl() {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        custom = LayoutBottomSheetServiciosBinding.inflate(LayoutInflater.from(getContext()));
        dialog.setContentView(custom.getRoot());
        dialog.getWindow().setNavigationBarColor(SurfaceColors.SURFACE_1.getColor(getActivity()));

        // info
        custom.mTextTitle.setText(getString(R.string.title_dhl));
        custom.mtextDescription.setText("Inserte el código de rastreo de su paquete");
        custom.inputServicios.setHint("Código de rastreo");
        custom.buttomConsulta.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String code = custom.editServicios.getText().toString().trim();
                        if (code.isEmpty()) {
                            Toast.makeText(getActivity(), "Faltan caracteres", Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            SendSMS.to(getActivity(), "8888", "DHL " + code);
                        }
                        dialog.dismiss();
                    }
                });

        dialog.show();
    }

    private void frases_marti() {
        CharSequence[] items = getResources().getStringArray(R.array.periodicos);
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(getActivity());
        dialog.setTitle(getString(R.string.title_marti));
        dialog.setItems(
                items,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dia, int which) {
                        switch (which) {
                            case 0:
                                SendSMS.to(getContext(), "8100", "MARTI");
                                break;
                            case 1:
                                SendSMS.to(getContext(), "8888", "MARTI BAJA");
                                break;
                        }
                    }
                });
        dialog.setPositiveButton(getString(R.string.positive_buttom), null);
        dialog.show();
    }

    private void mlb() {
        CharSequence[] items = getResources().getStringArray(R.array.pelota);
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(getActivity());
        dialog.setTitle(getString(R.string.title_mlb));
        dialog.setItems(
                items,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dia, int which) {
                        switch (which) {
                            case 0:
                                SendSMS.to(getContext(), "8888", "MLB");
                                break;
                            case 1:
                                SendSMS.to(getContext(), "8888", "MLB POS");
                                break;
                        }
                    }
                });
        dialog.setPositiveButton(getString(R.string.positive_buttom), null);
        dialog.show();
    }

    private void clima() {
        CharSequence[] items = getResources().getStringArray(R.array.tiempo);
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(getActivity());
        dialog.setTitle(getString(R.string.title_clima));
        dialog.setItems(
                items,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dia, int which) {
                        switch (which) {
                            case 0:
                                SendSMS.to(getContext(), "8888", "VLC");
                                break;
                            case 1:
                                SendSMS.to(getContext(), "8888", "GTM");
                                break;
                            case 2:
                                SendSMS.to(getContext(), "8888", "HLG");
                                break;
                            case 3:
                                SendSMS.to(getContext(), "8888", "ART");
                                break;
                            case 4:
                                SendSMS.to(getContext(), "8888", "GRM");
                                break;
                            case 5:
                                SendSMS.to(getContext(), "8888", "LHA");
                                break;
                            case 6:
                                SendSMS.to(getContext(), "8888", "MAY");
                                break;
                            case 7:
                                SendSMS.to(getContext(), "8888", "IJU");
                                break;
                            case 8:
                                SendSMS.to(getContext(), "8888", "CMG");
                                break;
                            case 9:
                                SendSMS.to(getContext(), "8888", "CFG");
                                break;
                            case 10:
                                SendSMS.to(getContext(), "8888", "PRL");
                                break;
                            case 11:
                                SendSMS.to(getContext(), "8888", "SCU");
                                break;
                            case 12:
                                SendSMS.to(getContext(), "8888", "SSP");
                                break;
                            case 13:
                                SendSMS.to(getContext(), "8888", "LTU");
                                break;
                            case 14:
                                SendSMS.to(getContext(), "8888", "MTZ");
                                break;
                            case 15:
                                SendSMS.to(getContext(), "8888", "CAV");
                                break;
                        }
                    }
                });
        dialog.setPositiveButton(getString(R.string.positive_buttom), null);
        dialog.show();
    }

    private void vuelos() {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        custom = LayoutBottomSheetServiciosBinding.inflate(LayoutInflater.from(getContext()));
        dialog.setContentView(custom.getRoot());
        dialog.getWindow().setNavigationBarColor(SurfaceColors.SURFACE_1.getColor(getActivity()));

        // info
        custom.mTextTitle.setText(getString(R.string.title_vuelos));
        custom.mtextDescription.setText(
                "Información de los vuelos de entrada y salida en el Aeropuerto José Martí");
        custom.inputServicios.setHint("Número de vuelo");
        custom.buttomConsulta.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String code = custom.editServicios.getText().toString().trim();
                        if (code.isEmpty()) {
                            Toast.makeText(getActivity(), "Faltan caracteres", Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            SendSMS.to(getActivity(), "8888", "VUELO " + code);
                        }
                        dialog.dismiss();
                    }
                });

        dialog.show();
    }

    private void futbol() {
        CharSequence[] items = getResources().getStringArray(R.array.futbol);
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(getActivity());
        dialog.setTitle(getString(R.string.title_futbol));
        dialog.setItems(
                items,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dia, int which) {
                        switch (which) {
                            case 0:
                                SendSMS.to(getContext(), "8888", "BundesLiga");
                                break;
                            case 1:
                                SendSMS.to(getContext(), "8888", "Champion");
                                break;
                            case 2:
                                SendSMS.to(getContext(), "8888", "Copa del Rey");
                                break;
                            case 3:
                                SendSMS.to(getContext(), "8888", "La Liga");
                                break;
                            case 4:
                                SendSMS.to(getContext(), "8888", "Liga 1");
                                break;
                            case 5:
                                SendSMS.to(getContext(), "8888", "Serie A");
                                break;
                            case 6:
                                SendSMS.to(getContext(), "8888", "Premier");
                                break;
                        }
                    }
                });
        dialog.setPositiveButton(getString(R.string.positive_buttom), null);
        dialog.show();
    }

    private void embajadas() {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        custom = LayoutBottomSheetServiciosBinding.inflate(LayoutInflater.from(getContext()));
        dialog.setContentView(custom.getRoot());
        dialog.getWindow().setNavigationBarColor(SurfaceColors.SURFACE_1.getColor(getActivity()));

        // info
        custom.mTextTitle.setText(getString(R.string.title_embajada));
        custom.mtextDescription.setText(
                "Información de la embajada en Cuba, dirección, horarios, etc. Los países con <<ñ>> se escriben con <<nn>>. Ejemplo: España > Espanna");
        custom.inputServicios.setHint("País");
        custom.buttomConsulta.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String code = custom.editServicios.getText().toString().trim();
                        if (code.isEmpty()) {
                            Toast.makeText(getActivity(), "Faltan caracteres", Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            SendSMS.to(getActivity(), "8888", "EMBAJADA " + code);
                        }
                        dialog.dismiss();
                    }
                });

        dialog.show();
    }

    private void apagon() {
        CharSequence[] items = getResources().getStringArray(R.array.apagon);
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(getActivity());
        dialog.setTitle(getString(R.string.title_electricidad));
        dialog.setItems(
                items,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dia, int which) {
                        switch (which) {
                            case 0:
                                SendSMS.to(getContext(), "8888", "APAGON B1");
                                break;
                            case 1:
                                SendSMS.to(getContext(), "8888", "APAGON B2");
                                break;
                            case 2:
                                SendSMS.to(getContext(), "8888", "APAGON B3");
                                break;
                            case 3:
                                SendSMS.to(getContext(), "8888", "APAGON B4");
                                break;
                        }
                    }
                });
        dialog.setPositiveButton(getString(R.string.positive_buttom), null);
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        nav.setVisibility(View.VISIBLE);
    }
}
