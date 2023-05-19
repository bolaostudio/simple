package com.nova.simple.preferences;

import android.content.Context;
import android.util.AttributeSet;
import androidx.preference.Preference;
import com.nova.simple.R;
import androidx.preference.CheckBoxPreference;

public class NovaColorPreference extends Preference {

    public NovaColorPreference(Context context, AttributeSet attr) {
        super(context, attr);
        setLayoutResource(R.layout.layout_preference_colors);
     //   setWidgetLayoutResource(R.layout.layout_preference_colors);
    }
}
