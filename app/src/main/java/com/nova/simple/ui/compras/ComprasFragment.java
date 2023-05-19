package com.nova.simple.ui.compras;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayoutMediator;
import com.nova.simple.R;
import com.nova.simple.adapter.TabAdapter;
import com.nova.simple.databinding.FragmentComprasBinding;

public class ComprasFragment extends Fragment {

    private FragmentComprasBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentComprasBinding.inflate(inflater, container, false);
        TabAdapter adapter = new TabAdapter(getChildFragmentManager(), getLifecycle());
        adapter.addFragment(new PlanesFragment());
        adapter.addFragment(new PaquetesFragment());
        adapter.addFragment(new AmigoFragment());

        binding.viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        binding.viewPager.setAdapter(adapter);
        TabLayoutMediator tabMediator =
                new TabLayoutMediator(
                        binding.tablayout,
                        binding.viewPager,
                        (tab, position) -> {
                            switch (position) {
                                case 0:
                                    tab.setText(getString(R.string.title_tab_plan));
                                    break;
                                case 1:
                                    tab.setText(getString(R.string.title_tab_paquete));
                                    break;
                                case 2:
                                    tab.setText(getString(R.string.title_tab_amigo));
                                    break;
                            }
                        });
        tabMediator.attach();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
