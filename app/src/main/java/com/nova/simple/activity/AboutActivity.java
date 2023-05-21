package com.nova.simple.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.Menu;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.elevation.SurfaceColors;
import com.nova.simple.BuildConfig;
import com.nova.simple.R;
import com.nova.simple.adapter.ItemsAdapter;
import com.nova.simple.adapter.ItemsClickListener;
import com.nova.simple.databinding.ActivityAboutBinding;
import com.nova.simple.model.AboutView;
import com.nova.simple.model.TypeView;
import java.util.ArrayList;

public class AboutActivity extends AppCompatActivity {

    private ActivityAboutBinding binding;
    private ItemsAdapter adapter;
    private ArrayList<TypeView> item = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.title_about);

        // navbar color
        getWindow().setNavigationBarColor(SurfaceColors.SURFACE_0.getColor(this));

        // version app
        String version = BuildConfig.VERSION_NAME;
        binding.buttomVersion.setText(version);

        //
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setNestedScrollingEnabled(false);
        adapter = new ItemsAdapter(this, item);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list_contributor();
        binding.recyclerView.addOnItemTouchListener(
                new ItemsClickListener(
                        this,
                        binding.recyclerView,
                        (view, position) -> {
                            if (position == 0) {
                                startActivity(
                                        new Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse("https://t.me/Fundadora")));
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

        // social app
        binding.buttomGithub.setOnClickListener(
                view -> {
                    startActivity(
                            new Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://github.com/esalessandrxx/simple-cuba")));
                });
        binding.buttomTwitterSimple.setOnClickListener(
                view -> {
                    startActivity(
                            new Intent(
                                    Intent.ACTION_VIEW, Uri.parse("https://twitter.com/simplecu")));
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
}
