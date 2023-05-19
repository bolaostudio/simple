package com.nova.simple.preferences;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.ListPreference;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class NovaListPreference extends ListPreference {

    public NovaListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onClick() {
        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle(getTitle());
        final CharSequence[] entries = getEntries();
        final CharSequence[] entryValues = getEntryValues();
        int selectedIndex = findIndexOfValue(getValue());
        builder.setSingleChoiceItems(
                entries,
                selectedIndex,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String value = (String) entryValues[which];
                        if (callChangeListener(value)) {
                            setValue(value);
                        }
                        dialog.dismiss();
                    }
                });

        builder.setPositiveButton(android.R.string.ok, null);
        builder.show();
    }
}
