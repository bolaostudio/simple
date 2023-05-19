package com.nova.simple.model;

public class GridView implements TypeView {

    private String title;
    private String subtitle;
    private int icon;

    public GridView(String title, String subtitle, int icon) {
        this.title = title;
        this.subtitle = subtitle;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public int getIcon() {
        return icon;
    }

    @Override
    public int getViewType() {
        return GridView.VIEW_GRID;
    }
}
