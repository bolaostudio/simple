package com.nova.simple.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.nova.simple.databinding.LayoutAboutViewBinding;
import com.nova.simple.databinding.LayoutGridViewBinding;
import com.nova.simple.databinding.LayoutHeadViewBinding;
import com.nova.simple.model.AboutView;
import com.nova.simple.model.GridView;
import com.nova.simple.model.HeadView;
import com.nova.simple.model.TypeView;
import java.util.ArrayList;

public class ItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<TypeView> item;

    public ItemsAdapter(Context context, ArrayList<TypeView> item) {
        this.context = context;
        this.item = item;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Grid) {
            GridView view = (GridView) item.get(position);
            Grid grid = (Grid) holder;
            grid.binding.textTitle.setText(view.getTitle());
            grid.binding.textSubtitle.setText(view.getSubtitle());
            grid.binding.imageIcon.setImageResource(view.getIcon());
        } else if (holder instanceof Header) {
            HeadView view = (HeadView) item.get(position);
            Header header = (Header) holder;
            header.binding.textHeader.setText(view.getHeader());

        } else if (holder instanceof About) {
            AboutView view = (AboutView) item.get(position);
            About about = (About) holder;
            about.binding.textName.setText(view.getTitle());
            about.binding.textDescription.setText(view.getSubtitle());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == GridView.VIEW_GRID) {
            LayoutGridViewBinding binding =
                    LayoutGridViewBinding.inflate(
                            LayoutInflater.from(parent.getContext()), parent, false);
            return new Grid(binding);
        } else if (viewType == HeadView.VIEW_HEADER) {
            LayoutHeadViewBinding binding =
                    LayoutHeadViewBinding.inflate(
                            LayoutInflater.from(parent.getContext()), parent, false);
            return new Header(binding);

        } else if (viewType == AboutView.VIEW_ABOUT) {
            LayoutAboutViewBinding binding =
                    LayoutAboutViewBinding.inflate(
                            LayoutInflater.from(parent.getContext()), parent, false);
            return new About(binding);
        }
        throw new RuntimeException("Error Exception");
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    @Override
    public int getItemViewType(int position) {
        return item.get(position).getViewType();
    }

    public static class Grid extends RecyclerView.ViewHolder {

        private LayoutGridViewBinding binding;

        public Grid(LayoutGridViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class Header extends RecyclerView.ViewHolder {

        private LayoutHeadViewBinding binding;

        public Header(LayoutHeadViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class About extends RecyclerView.ViewHolder {

        private LayoutAboutViewBinding binding;

        public About(LayoutAboutViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
