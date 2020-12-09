package ua.svasilina.spedition.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.constants.ApiLinks;
import ua.svasilina.spedition.utils.LoginUtil;
import ua.svasilina.spedition.utils.NetworkUtil;
import ua.svasilina.spedition.utils.db.OnSyncDone;
import ua.svasilina.spedition.utils.network.Connector;

import static ua.svasilina.spedition.constants.Keys.EMPTY;
import static ua.svasilina.spedition.constants.Keys.PASSWORD;
import static ua.svasilina.spedition.constants.Keys.PHONE;
import static ua.svasilina.spedition.constants.Keys.REASON;
import static ua.svasilina.spedition.constants.Keys.STATUS;
import static ua.svasilina.spedition.constants.Keys.SUCCESS;
import static ua.svasilina.spedition.constants.Keys.TOKEN;
import static ua.svasilina.spedition.constants.Keys.USER;

//import org.json.simple.JSONObject;

public class LoginDialog extends DialogFragment {

    private Context context;
    private EditText phoneEdit;
    private EditText passwordEdit;
    private ProgressBar progressBar;
    private TextView statusView;
    private final LoginUtil loginUtil;
    private NetworkUtil networkUtil;
    private final LayoutInflater inflater;
    private boolean isAuthorize;
    private boolean waitAnswer = false;
    private OnSyncDone onLogin;

    public LoginDialog(Context context, OnSyncDone onLogin) {
        this.context = context;
        this.onLogin = onLogin;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        loginUtil = new LoginUtil(context);
        networkUtil = new NetworkUtil();
        isAuthorize = loginUtil.getToken() != null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable final Bundle savedInstanceState) {
        Log.i("Login", "Create dialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (isAuthorize) {
            final View view = inflater.inflate(R.layout.already_login, null);
            final TextView userName = view.findViewById(R.id.userName);
            final String name = loginUtil.getUserName();
            if (name != null){
                userName.setText(name.toUpperCase());
            } else {
                userName.setVisibility(View.INVISIBLE);
            }
            final Button removeToken = view.findViewById(R.id.removeToken);
            removeToken.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginUtil.removeToken();
                    dismiss();
                    showLoginDialog(context, getParentFragmentManager(), null);
                }
            });
            final Button close = view.findViewById(R.id.close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            builder.setView(view);
        } else {
            final View view = inflater.inflate(R.layout.activity_login, null);

            phoneEdit = view.findViewById(R.id.editTextPhone);
            phoneEdit.setText(getNumber());

            passwordEdit = view.findViewById(R.id.editPassword);
            final Button loginButton = view.findViewById(R.id.loginButton);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Login", "Press login button");
                    if (!waitAnswer) {
                        login();
                    }
                }
            });
            progressBar = view.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.INVISIBLE);
            statusView = view.findViewById(R.id.statusText);

            builder.setView(view);
        }

        return builder.create();
    }
    public static void showLoginDialog(Context context, FragmentManager manager, OnSyncDone onLogin){
        new LoginDialog(context, onLogin).show(manager, "Login Dialog");
    }
    private String getNumber() {
        return EMPTY;
    }

    private void login() {
        progressBar.setVisibility(View.INVISIBLE);
        waitAnswer = true;
        progressBar.setVisibility(View.VISIBLE);

        final String login = phoneEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        final JSONObject json = new JSONObject();
        try {
            json.put(PHONE, login);
            json.put(PASSWORD, Base64.encodeToString(password.getBytes(), Base64.NO_WRAP));
        } catch (JSONException ignore) {}

        final StatusHandler statusHandler = new StatusHandler(statusView, progressBar);
        final LoginHandler loginHandler = new LoginHandler(loginUtil);
        JsonObjectRequest request = new JsonObjectRequest(
            Request.Method.POST,
            ApiLinks.LOGIN,
            json,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    waitAnswer = false;
                    try {
                        final String status = response.getString(STATUS);
                        if (status.equals(SUCCESS)) {
                            String token = response.getString(TOKEN);

                            sendMessage(loginHandler, TOKEN, token);
                            if (response.has(USER)){
                                final String user = String.valueOf(response.get(USER));
                                sendMessage(loginHandler, USER, user);
                            }
                            statusHandler.removeCallbacksAndMessages(null);
                            dismiss();
                            if (onLogin != null){
                                onLogin.done();
                            }
                        } else {
                            String reason = response.getString(REASON);
                            sendMessage(statusHandler, REASON, reason);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    sendMessage(statusHandler, REASON, error.getMessage());
                }
            }
        );

        Connector.getConnector().addRequest(context, request);
    }

    private void sendMessage(Handler handler, String key, String value) {
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        Message message = new Message();
        message.setData(bundle);
        handler.sendMessage(message);
    }
}
