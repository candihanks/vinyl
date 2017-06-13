package com.carltaylordev.recordlisterandroidclient.UserInterface.Settings;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.carltaylordev.recordlisterandroidclient.KeyValueStore;
import com.carltaylordev.recordlisterandroidclient.R;
import com.carltaylordev.recordlisterandroidclient.UserInterface.BaseActivity;
import com.carltaylordev.recordlisterandroidclient.Validator;


public class ServerSettingsFragment extends Fragment {
    /**
     * Constructors
     */

    public ServerSettingsFragment() {
    }

    public static ServerSettingsFragment newInstance() {
        return new ServerSettingsFragment();
    }

    /**
     *  Fragment LifeCycle
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.server_settings_fragment, container, false);
        final KeyValueStore keyValueStore = new KeyValueStore(getActivity());
        SettingsActivity activity = (SettingsActivity) getActivity();

        setupEditTexts(rootView, keyValueStore, activity);
        setUpLoginButton(rootView, keyValueStore);
        return rootView;
    }

    /**
     *  Setup
     */

    private void setupEditTexts(View view, final KeyValueStore keyValueStore, final SettingsActivity activity) {
        final EditText baseUrlEditText = (EditText) view.findViewById(R.id.base_url_edit_text);
        baseUrlEditText.setText(keyValueStore.getStringForKey(KeyValueStore.KEY_BASE_SERVER_URL));

        Button saveButton = (Button) view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = baseUrlEditText.getText().toString();
                if (Validator.isValidUrl(url)) {
                    keyValueStore.setStringForKey(KeyValueStore.KEY_BASE_SERVER_URL, baseUrlEditText.getText().toString());
                    activity.showToast("Saved");
                } else {
                    activity.showAlert("Oops", "URL not valid");
                }
            }
        });
    }

    private void setUpLoginButton(View view, final KeyValueStore keyValueStore) {
        Button loginButton = (Button)view.findViewById(R.id.login_logout_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = keyValueStore.getStringForKey(KeyValueStore.KEY_SERVER_TOKEN);
                if (token.isEmpty()) {
                    // login
                } else {
                    keyValueStore.setStringForKey(KeyValueStore.KEY_SERVER_TOKEN, "");
                }
            }
        });

        String token = keyValueStore.getStringForKey(KeyValueStore.KEY_SERVER_TOKEN);
        if (token.isEmpty()) {
            loginButton.setText("Server Login");
        } else {
            loginButton.setText("Clear Server Token");
        }
    }
}
