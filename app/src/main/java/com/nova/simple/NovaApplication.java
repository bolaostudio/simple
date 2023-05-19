package com.nova.simple;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import com.jesusd0897.crashreporter.model.CrashReporterHandler;
import cu.suitetecsa.sdk.nauta.data.repository.DefaultNautaSession;
import cu.suitetecsa.sdk.nauta.data.repository.JSoupNautaScrapper;
import cu.suitetecsa.sdk.nauta.domain.service.NautaClient;

public class NovaApplication extends Application {

    NautaClient client;

    @Override
    public void onCreate() {
        super.onCreate();
        // tema
        ThemeManager.applyTheme(this);

        // crash reporter
        Thread.setDefaultUncaughtExceptionHandler(new CrashReporterHandler(this));

        // api nauta suitetecsa
        client = new NautaClient(new JSoupNautaScrapper(new DefaultNautaSession()));
    }

    public NautaClient getNautaClient() {
        return client;
    }
}
