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
        final EditText usernameEditText = (EditText)view.findViewById(R.id.username);
        final EditText passwordEditText = (EditText)view.findViewById(R.id.password);

        Button loginButton = (Button)view.findViewById(R.id.login_logout_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = keyValueStore.getStringForKey(KeyValueStore.KEY_SERVER_TOKEN);
                if (token.isEmpty()) {
                    String username = usernameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    if (!username.isEmpty() && !password.isEmpty()) {
                        activity.showProgressDialog("Attempting login with server");
                        attemptServerLogin(username, password);
                    } else {
                        activity.showAlert("Oops", "Please fill in Username and Password fields");
                    }
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

    /**
     *  Server Login
     */

    private void attemptServerLogin(String username, String password) {
        UserAuthCoordinator coordinator = new UserAuthCoordinator("", getActivity(), this);
        coordinator.attemptLogin(username, password);
    }

    /**
     *  Auth Interface
     */

    @Override
    public void onFinished(BoolResponse response) {
        SettingsActivity activity = (SettingsActivity)getActivity();
        activity.hideProgressDialog();
        activity.showAlert("Login Result:", response.getUserMessage());
    }
}
