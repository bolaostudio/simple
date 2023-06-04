package com.nova.simple.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import androidx.preference.PreferenceManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.elevation.SurfaceColors;
import com.nova.simple.R;
import androidx.preference.EditTextPreference;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class NovaEditTextPreference extends EditTextPreference {

    TextInputLayout inputPreference;
    TextInputEditText editTextPreference;

    public NovaEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NovaEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onClick() {

        View view =
                LayoutInflater.from(getContext())
                        .inflate(R.layout.layout_nova_edit_text_preference, null);
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(view);
        dialog.getWindow().setNavigationBarColor(SurfaceColors.SURFACE_1.getColor(getContext()));

        // Change title BottomSheetDialog
        TextView title = (TextView) view.findViewById(R.id.textTitlePreference);
        if (title != null) {
            title.setText(getTitle());
        }

        // Insert text into the TextInputEditText
        editTextPreference = view.findViewById(R.id.mEditTextPreference);
        editTextPreference.setText(getText());
        editTextPreference.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (i == EditorInfo.IME_ACTION_DONE) {
                            if (callChangeListener(textView.getText().toString())) {
                                setText(textView.getText().toString());
                            }
                            return true;
                        }
                        return false;
                    }
                });
        // Insert custom hint in the TextInputLayout
        inputPreference = view.findViewById(R.id.inputLayoutPreference);
        if (inputPreference != null) {
            inputPreference.setHint(getCustomHint());
        }

        // Positive button
        MaterialButton positive = view.findViewById(R.id.mButtomPossitivePreference);
        positive.setOnClickListener(
                v -> {
                    setText(editTextPreference.getText().toString());
                    persistString(editTextPreference.getText().toString());
                    String newText = editTextPreference.getText().toString().trim();
                    if (callChangeListener(newText)) {
                        setText(newText);
                    }
                    notifyChanged();
                    dialog.dismiss();
                });

        // Negative button
        MaterialButton negative = view.findViewById(R.id.buttomCancel);
        negative.setOnClickListener(
                v -> {
                    dialog.dismiss();
                });

        //  Show BottomSheetDialog on click
        dialog.show();
    }

    // Method to insert the custom hint
    public void setCustomHint(String hint) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences.edit().putString(getKey() + "_hint", hint).apply();
    }

    // Method to get the custom hint
    private String getCustomHint() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getContext());
        return sharedPreferences.getString(getKey() + "_hint", "");
    }
}
