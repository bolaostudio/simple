package com.nova.simple.model;

public class HeadView implements TypeView {

    private String header;

    public HeadView(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    @Override
    public int getViewType() {
        return HeadView.VIEW_HEADER;
    }
}
