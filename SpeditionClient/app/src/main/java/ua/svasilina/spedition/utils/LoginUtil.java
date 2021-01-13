package ua.svasilina.spedition.utils;

import android.content.Context;

import java.io.File;

public class LoginUtil {
    private final StorageUtil storageUtil;
    private static final String FILE_NAME = "user_access";
    private static final String USER_NAME = "user_name";

    public LoginUtil(Context context) {
        storageUtil = new StorageUtil(context);
    }

    public void saveToken(String token) {
        storageUtil.saveData(FILE_NAME, token);
    }

    public String isLogin() {
        final String userData = storageUtil.readFile(FILE_NAME);
        if (userData != null){
            return storageUtil.readFile(USER_NAME);
        }
        return null;
    }

    public String getToken() {
        return storageUtil.readFile(FILE_NAME);
    }

    public void removeToken(){
        final FileFilter fileFilter = new FileFilter(FILE_NAME);
        final File[] files = storageUtil.getFiles(fileFilter);
        if (files != null){
            for(File file : files){
                file.delete();
            }
        }
    }

    public String getUserName() {
        return storageUtil.readFile(USER_NAME);
    }

    public void saveUser(String userName) {
        storageUtil.saveData(USER_NAME, userName);
    }
}
