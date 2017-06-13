package com.carltaylordev.recordlisterandroidclient.UserInterface.Settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.carltaylordev.recordlisterandroidclient.KeyValueStore;
import com.carltaylordev.recordlisterandroidclient.R;
import com.carltaylordev.recordlisterandroidclient.Server.UserAuthCoordinator;
import com.carltaylordev.recordlisterandroidclient.Validator;
import com.carltaylordev.recordlisterandroidclient.models.BoolResponse;


public class ServerSettingsFragment extends Fragment implements UserAuthCoordinator.Interface {

    EditText mUsernameEditText;
    EditText mPasswordEditText;
    Button mLoginButton;

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

        setupBaseUrlEditTextAndButton(rootView, keyValueStore, activity);
        setUpAuthEditTextAndButton(rootView, keyValueStore, activity);
        refreshUI();
        return rootView;
    }

    /**
     *  Setup
     */

    private void setupBaseUrlEditTextAndButton(View view, final KeyValueStore keyValueStore, final SettingsActivity activity) {
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

    private void setUpAuthEditTextAndButton(View view, final KeyValueStore keyValueStore, final SettingsActivity activity) {
        mUsernameEditText = (EditText)view.findViewById(R.id.username);
        mPasswordEditText = (EditText)view.findViewById(R.id.password);

        mLoginButton = (Button)view.findViewById(R.id.login_logout_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String baseUrl = keyValueStore.getStringForKey(KeyValueStore.KEY_BASE_SERVER_URL);
                if (baseUrl.isEmpty()) {
                    activity.showAlert("Oops", "Please set BaseURL");
                    return;
                }
                String token = keyValueStore.getStringForKey(KeyValueStore.KEY_SERVER_TOKEN);
                if (token.isEmpty()) {
                    String username = mUsernameEditText.getText().toString();
                    String password = mPasswordEditText.getText().toString();

                    if (!username.isEmpty() && !password.isEmpty()) {
                        activity.showProgressDialog("Attempting login with server");
                        attemptServerLogin(baseUrl, username, password);
                    } else {
                        activity.showAlert("Oops", "Username & Password needed");
                    }
                } else {
                    activity.showProgressDialog("Attempting to clear token with server");
                    attemptServerClearAuthToken(baseUrl, token);
                }
            }
        });
    }

    /**
     *  refreshUI
     */

    private void refreshUI() {
        mUsernameEditText.setText("");
        mPasswordEditText.setText("");

        final KeyValueStore keyValueStore = new KeyValueStore(getActivity());
        String token = keyValueStore.getStringForKey(KeyValueStore.KEY_SERVER_TOKEN);
        if (token.isEmpty()) {
            mUsernameEditText.setVisibility(View.VISIBLE);
            mPasswordEditText.setVisibility(View.VISIBLE);
            mLoginButton.setText("Server Login");
        } else {
            mLoginButton.setText("Clear Server Token");
            mUsernameEditText.setVisibility(View.GONE);
            mPasswordEditText.setVisibility(View.GONE);
        }
    }

    /**
     *  Server
     */

    private void attemptServerLogin(String baseUrl, String username, String password) {
        UserAuthCoordinator coordinator = new UserAuthCoordinator(baseUrl, getActivity(), this);
        coordinator.attemptAuthentication(username, password);
    }

    private void attemptServerClearAuthToken(String baseUrl, String token) {
        UserAuthCoordinator coordinator = new UserAuthCoordinator(baseUrl, getActivity(), this);
        coordinator.attemptClearAuthToken(token);
    }

    /**
     *  Auth Interface
     */

    @Override
    public void onFinished(BoolResponse response) {
        SettingsActivity activity = (SettingsActivity)getActivity();
        activity.hideProgressDialog();
        activity.showAlert("Result:", response.getUserMessage());
        refreshUI();
    }
}
