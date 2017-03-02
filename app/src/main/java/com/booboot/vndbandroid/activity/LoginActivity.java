package com.booboot.vndbandroid.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.booboot.vndbandroid.BuildConfig;
import com.booboot.vndbandroid.R;
import com.booboot.vndbandroid.api.Cache;
import com.booboot.vndbandroid.bean.vndb.Links;
import com.booboot.vndbandroid.bean.vndbandroid.Theme;
import com.booboot.vndbandroid.util.Callback;
import com.booboot.vndbandroid.util.SettingsManager;
import com.booboot.vndbandroid.util.Utils;
import com.crashlytics.android.Crashlytics;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static boolean autologin = true;
    private Button loginButton;
    private EditText loginUsername;
    private EditText loginPassword;
    private ProgressBar progressBar;
    private TextView signupTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Theme.THEMES.get(SettingsManager.getTheme(this)).getNoActionBarStyle());
        setContentView(R.layout.login);

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Utils.getThemeColor(this, R.attr.colorPrimaryDark));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setBackgroundColor(getResources().getColor(android.R.color.transparent, getTheme()));
        } else {
            window.getDecorView().setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
        window.getDecorView().setVisibility(View.GONE);

        signupTextView = (TextView) findViewById(R.id.signupTextView);
        Utils.setTextViewLink(this, signupTextView, Links.VNDB_REGISTER, signupTextView.getText().toString().indexOf("Sign up here"), signupTextView.getText().toString().length());
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        loginUsername = (EditText) findViewById(R.id.loginUsername);
        loginPassword = (EditText) findViewById(R.id.loginPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        String savedUsername = SettingsManager.getUsername(this);
        String savedPassword = SettingsManager.getPassword(this);
        boolean credentialsSaved = savedUsername != null && !savedUsername.isEmpty() && savedPassword != null;
        if (credentialsSaved) {
            /* Filling the inputs with saved values (for appearance's sake) */
            loginUsername.setText(savedUsername);
            loginPassword.setText(savedPassword);
        }

        if (autologin && credentialsSaved) {
            disableAll();
            new Thread() {
                public void run() {
                    login();
                }
            }.start();
        } else {
            enableAll();
        }

        autologin = false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loginButton) {
            SettingsManager.setUsername(this, loginUsername.getText().toString());
            SettingsManager.setPassword(this, loginPassword.getText().toString());
            disableAll();
            login();
        }
    }

    private void login() {
        Cache.loadFromCache(this);

        if (Cache.loadedFromCache) {
            VNTypeFragment.refreshOnInit = true;
            addInfoToCrashlytics();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        } else {
            Cache.loadData(LoginActivity.this, new Callback() {
                @Override
                protected void config() {
                    addInfoToCrashlytics();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }, getCallback());
        }
    }

    private void addInfoToCrashlytics() {
        if (BuildConfig.DEBUG) return;
        Crashlytics.setUserName(SettingsManager.getUsername(this));
        try {
            int theme = Integer.parseInt(SettingsManager.getTheme(this));
            String[] themes = getResources().getStringArray(R.array.background_pref_titles);
            if (theme >= 0 && theme < themes.length) {
                Crashlytics.setString("THEME", themes[theme]);
            }
        } catch (NumberFormatException nfe) {
        }
        Crashlytics.setBool("HIDE RECOMMENDATIONS IN WISHLIST", SettingsManager.getHideRecommendationsInWishlist(this));
        Crashlytics.setBool("VN DETAILS BLURRED BACKGROUND", SettingsManager.getCoverBackground(this));
        Crashlytics.setBool("IN-APP BROWSER ENABLED", SettingsManager.getInAppBrowser(this));
        Crashlytics.setBool("SHOW NSFW BY DEFAULT", SettingsManager.getNSFW(this));
        int sort = SettingsManager.getSort(this);
        if (sort >= 0 && sort < Cache.SORT_OPTIONS.length) {
            Crashlytics.setString("SORT", Cache.SORT_OPTIONS[sort]);
        }
        Crashlytics.setBool("REVERSE SORT", SettingsManager.getReverseSort(this));
        Crashlytics.setBool("SPOIL ME IF FINISHED", SettingsManager.getSpoilerCompleted(this));
        int spoilerLevel = SettingsManager.getSpoilerLevel(this);
        String[] spoilerLevels = getResources().getStringArray(R.array.spoiler_pref_titles);
        if (spoilerLevel >= 0 && spoilerLevel < spoilerLevels.length) {
            Crashlytics.setString("DEFAULT SPOILER LEVEL", spoilerLevels[spoilerLevel]);
        }
    }

    public void enableAll() {
        progressBar.setVisibility(View.INVISIBLE);
        loginUsername.setEnabled(true);
        loginPassword.setEnabled(true);
        loginButton.setText(R.string.sign_in);
        loginButton.setEnabled(true);
    }

    public void disableAll() {
        /* Disabling the inputs */
        loginUsername.setEnabled(false);
        loginPassword.setEnabled(false);
        loginButton.setText(R.string.signing_in);
        loginButton.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    private Callback getCallback() {
        return new Callback() {
            @Override
            public void config() {
                if (Cache.pipeliningError) return;
                Cache.pipeliningError = true;
                Callback.showToast(LoginActivity.this, message);
                enableAll();
                if (Cache.countDownLatch != null) Cache.countDownLatch.countDown();
            }
        };
    }
}
