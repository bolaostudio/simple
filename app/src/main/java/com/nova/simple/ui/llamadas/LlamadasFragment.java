package com.nova.simple.ui.llamadas;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import com.nova.simple.adapter.ItemsAdapter;
import com.nova.simple.adapter.ItemsClickListener;
import com.nova.simple.databinding.FragmentLlamadasBinding;
import com.nova.simple.model.GridView;
import com.nova.simple.R;
import com.nova.simple.model.HeadView;
import com.nova.simple.model.TypeView;
import com.nova.simple.utils.SIMDialer;
import java.util.ArrayList;

public class LlamadasFragment extends Fragment {

    private FragmentLlamadasBinding binding;
    private ItemsAdapter adapter;
    private ArrayList<TypeView> item = new ArrayList<>();

    SharedPreferences sp_sim;
    String simslot;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLlamadasBinding.inflate(inflater, container, false);

        // sim slot
        sp_sim = PreferenceManager.getDefaultSharedPreferences(getActivity());
        simslot = sp_sim.getString("sim_preference", "0");

        // recycler
        binding.recyclerView.setHasFixedSize(true);
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
        load_items();

        binding.recyclerView.addOnItemTouchListener(
                new ItemsClickListener(
                        getActivity(),
                        binding.recyclerView,
                        new ItemsClickListener.ClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                switch (position) {
                                    case 0:
                                        pickContactAsterisc.launch(null);
                                        break;
                                    case 1:
                                        pickContactPrivado.launch(null);
                                        break;

                                    case 2:
                                        // utiles
                                        break;
                                    case 3:
                                        SIMDialer.call(
                                                getActivity(),
                                                "52642266",
                                                Integer.parseInt(simslot));
                                        break;
                                    case 4:
                                        SIMDialer.call(
                                                getActivity(),
                                                "80043434",
                                                Integer.parseInt(simslot));
                                        break;
                                    case 5:
                                        SIMDialer.call(
                                                getActivity(), "114", Integer.parseInt(simslot));

                                        break;
                                    case 6:
                                        SIMDialer.call(
                                                getActivity(), "18888", Integer.parseInt(simslot));
                                        break;
                                    case 7:
                                        SIMDialer.call(
                                                getActivity(), "118", Integer.parseInt(simslot));
                                        break;
                                    case 8:
                                        // emergencia
                                        break;
                                    case 9:
                                        SIMDialer.call(
                                                getActivity(), "103", Integer.parseInt(simslot));

                                        break;
                                    case 10:
                                        SIMDialer.call(
                                                getActivity(), "104", Integer.parseInt(simslot));
                                        break;
                                    case 11:
                                        SIMDialer.call(
                                                getActivity(), "105", Integer.parseInt(simslot));
                                        break;
                                    case 12:
                                        SIMDialer.call(
                                                getActivity(), "106", Integer.parseInt(simslot));
                                        break;
                                    case 13:
                                        SIMDialer.call(
                                                getActivity(), "107", Integer.parseInt(simslot));
                                        break;
                                }
                            }
                        }));
        return binding.getRoot();
    }

    private void load_items() {
        item.add(
                new GridView(
                        getString(R.string.title_asterisco), null, R.drawable.ic_asterisco_24px));
        item.add(new GridView(getString(R.string.title_privado), null, R.drawable.ic_privado_24px));

        // numeros utiles
        item.add(new HeadView(getString(R.string.categoria_util)));
        item.add(
                new GridView(
                        getString(R.string.title_atencion_cliente),
                        null,
                        R.drawable.ic_atencion_al_cliente_24px));
        item.add(
                new GridView(
                        getString(R.string.title_nauta_hogar),
                        null,
                        R.drawable.ic_nauta_hogar_24px));
        item.add(
                new GridView(
                        getString(R.string.title_reporte_tff),
                        null,
                        R.drawable.ic_reporte_fijo_24px));
        item.add(
                new GridView(
                        getString(R.string.title_electric),
                        null,
                        R.drawable.ic_emp_electrica_24px));
        item.add(new GridView(getString(R.string.title_quejas), null, R.drawable.ic_mobile_24px));

        // emergencia
        item.add(new HeadView(getString(R.string.categoria_emergencia)));
        item.add(
                new GridView(
                        getString(R.string.title_antidroga), null, R.drawable.ic_antidrogas_24px));
        item.add(
                new GridView(
                        getString(R.string.title_ambulancia), null, R.drawable.ic_ambulancia_24px));
        item.add(
                new GridView(
                        getString(R.string.title_bomberos), null, R.drawable.ic_bomberos_24px));
        item.add(new GridView(getString(R.string.title_policia), null, R.drawable.ic_policia_24px));
        item.add(
                new GridView(
                        getString(R.string.title_rescate_salvamento),
                        null,
                        R.drawable.ic_salvamento_maritimo_24px));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private final ActivityResultLauncher<Void> pickContactAsterisc =
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
                                                // name

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
                                                                    .replace(")", "");
                                                    replace =
                                                            replace.substring(replace.length() - 8);
                                                    if (replace.startsWith("5")
                                                            && replace.length() == 8) {
                                                        // phone
                                                        SIMDialer.call(
                                                                getActivity(),
                                                                "*99" + replace,
                                                                Integer.parseInt(simslot));
                                                    } else {
                                                        Toast.makeText(
                                                                        getActivity(),
                                                                        name + "no es un movil",
                                                                        Toast.LENGTH_LONG)
                                                                .show();
                                                    }
                                                }
                                                phoneCursor.close();
                                            }
                                        }
                                    }

                                } catch (Exception e) {
                                    e.getStackTrace();
                                }
                            }
                        }
                    });
    private final ActivityResultLauncher<Void> pickContactPrivado =
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
                                                // name

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
                                                                    .replace(")", "");
                                                    replace =
                                                            replace.substring(replace.length() - 8);
                                                    if (replace.startsWith("5")
                                                            && replace.length() == 8) {
                                                        // phone
                                                        SIMDialer.call(
                                                                getActivity(),
                                                                Uri.encode("#")
                                                                        + "31"
                                                                        + Uri.encode("#")
                                                                        + replace,
                                                                Integer.parseInt(simslot));
                                                    } else {
                                                        Toast.makeText(
                                                                        getActivity(),
                                                                        name + "no es un móvil",
                                                                        Toast.LENGTH_LONG)
                                                                .show();
                                                    }
                                                }
                                                phoneCursor.close();
                                            }
                                        }
                                    }

                                } catch (Exception e) {
                                    e.getStackTrace();
                                }
                            }
                        }
                    });
}
