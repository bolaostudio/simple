package com.nova.simple.adapter;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public class ItemsClickListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector gesture;
    private ClickListener click;

    public ItemsClickListener(Context context, RecyclerView recycler, ClickListener click) {
        this.click = click;
        gesture =
                new GestureDetector(
                        context,
                        new GestureDetector.SimpleOnGestureListener() {
                            @Override
                            public boolean onSingleTapUp(MotionEvent arg0) {
                                View child = recycler.findChildViewUnder(arg0.getX(), arg0.getY());
                                if (child != null && click != null) {
                                    click.onClick(child, recycler.getChildAdapterPosition(child));
                                    return true;
                                }
                                return false;
                            }
                        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return gesture.onTouchEvent(e);
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {}

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}

    public interface ClickListener {
        void onClick(View view, int position);
    }
}
