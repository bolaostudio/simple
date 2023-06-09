package com.nova.simple.ui.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nova.simple.BuildConfig;
import com.nova.simple.R;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.nova.simple.adapter.ItemsAdapter;
import com.nova.simple.adapter.ItemsClickListener;
import com.nova.simple.databinding.FragmentAboutBinding;
import com.nova.simple.model.AboutView;
import com.nova.simple.model.TypeView;
import java.util.ArrayList;
import java.util.Objects;

public class AboutFragment extends Fragment {

    private FragmentAboutBinding binding;
    private ItemsAdapter adapter;
    private ArrayList<TypeView> item = new ArrayList<>();
    private BottomNavigationView nav;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAboutBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);

        // hide bottomnavigationview
        nav = Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_nav_view);
        nav.setVisibility(View.GONE);

        // version app
        String version = BuildConfig.VERSION_NAME;
        binding.buttomVersion.setText(version);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setNestedScrollingEnabled(false);
        adapter = new ItemsAdapter(getActivity(), item);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list_contributor();
        binding.recyclerView.addOnItemTouchListener(
                new ItemsClickListener(
                        getActivity(),
                        binding.recyclerView,
                        (view, position) -> {
                            if (position == 0) {
                                startActivity(
                                        new Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse("http://lnk.bio/roclahy")));
                            } else if (position == 1) {
                                startActivity(
                                        new Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse("https://t.me/OrdielVictor")));
                            } else if (position == 2) {
                                startActivity(
                                        new Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse("http://t.me/haroldadan")));
                            }
                        }));
        // donar
        binding.cardDonar.setOnClickListener(
                view -> {
                    Toast.makeText(getActivity(), "En desarrollo...", Toast.LENGTH_LONG).show();
                });

        // social app
        binding.buttomGithub.setOnClickListener(
                view -> {
                    startActivity(
                            new Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://github.com/bolaostudio/simple")));
                });
        binding.buttomTwitterSimple.setOnClickListener(
                view -> {
                    startActivity(
                            new Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://twitter.com/BolaoStudio")));
                });
        binding.buttomTelegram.setOnClickListener(
                view -> {
                    startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/simplecuba")));
                });

        // social dev
        binding.buttomTwitter.setOnClickListener(
                view -> {
                    startActivity(
                            new Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://twitter.com/esalessandrx")));
                });
        binding.buttomInstagram.setOnClickListener(
                view -> {
                    startActivity(
                            new Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://instagram.com/esalessandrx")));
                });
        return binding.getRoot();
    }

    private void list_contributor() {
        item.add(
                new AboutView(
                        getString(R.string.about_name1), getString(R.string.about_desc_name1)));
        item.add(
                new AboutView(
                        getString(R.string.about_name2), getString(R.string.about_desc_name2)));
        item.add(
                new AboutView(
                        getString(R.string.about_name3), getString(R.string.about_desc_name3)));
        item.add(
                new AboutView(getString(R.string.about_beta), getString(R.string.about_desc_beta)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        nav.setVisibility(View.VISIBLE);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.about_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem menuItem = menu.findItem(R.id.licence);
        MenuItem menTerm = menu.findItem(R.id.politicas);
        menuItem.setVisible(true);
        menTerm.setVisible(true);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.licence:
                Toast.makeText(getActivity(), "En desarrollo...", Toast.LENGTH_LONG).show();
                break;
            case R.id.politicas:
                Toast.makeText(getActivity(), "En desarrollo...", Toast.LENGTH_LONG).show();
                break;
            default:
        }

        return super.onOptionsItemSelected(item);
    }
}
