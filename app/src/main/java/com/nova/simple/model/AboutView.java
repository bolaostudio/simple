package com.nova.simple.model;

public class AboutView implements TypeView {

    String title, subtitle;

    public AboutView(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    @Override
    public int getViewType() {
        return AboutView.VIEW_ABOUT;
    }
}
