package com.nova.simple.ui.nauta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.nova.simple.R;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayoutMediator;
import com.nova.simple.adapter.TabAdapter;
import com.nova.simple.databinding.FragmentNautaBinding;

public class NautaFragment extends Fragment {

    private FragmentNautaBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNautaBinding.inflate(inflater, container, false);
        TabAdapter adapter = new TabAdapter(getChildFragmentManager(), getLifecycle());
        adapter.addFragment(new LoginFragment());
        adapter.addFragment(new PortalUsuarioFragment());

        binding.viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        binding.viewPager.setAdapter(adapter);
        TabLayoutMediator tabMediator =
                new TabLayoutMediator(
                        binding.tablayout,
                        binding.viewPager,
                        (tab, position) -> {
                            switch (position) {
                                case 0:
                                    tab.setText(getString(R.string.title_login));
                                    break;
                                case 1:
                                    tab.setText(getString(R.string.title_portal_user));
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
