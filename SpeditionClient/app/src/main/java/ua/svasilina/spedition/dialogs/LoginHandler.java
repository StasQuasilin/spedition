package ua.svasilina.spedition.dialogs;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import ua.svasilina.spedition.utils.LoginUtil;
import ua.svasilina.spedition.utils.db.OnDone;

import static ua.svasilina.spedition.constants.Keys.TOKEN;
import static ua.svasilina.spedition.constants.Keys.USER;

public class LoginHandler extends Handler {

    private final LoginUtil loginUtil;
    private final OnDone onLogin;
    public LoginHandler(LoginUtil loginUtil, OnDone onLogin) {
        this.loginUtil = loginUtil;
        this.onLogin = onLogin;
    }

    @Override
    public void dispatchMessage(@NonNull Message msg) {
        final Bundle data = msg.getData();
        if (data.containsKey(TOKEN)){
            loginUtil.saveToken(data.getString(TOKEN));
            if (onLogin != null){
                onLogin.done();
            }
        } else if (data.containsKey(USER)){
            loginUtil.saveUser(data.getString(USER));
        }
    }
}
