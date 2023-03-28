package com.home.server.util;

import java.sql.ResultSet;
import java.io.File;
import java.io.FileOutputStream;

import com.home.server.sql.SqlManager;
import com.home.server.SocketData;

public class MessageUtil {

    MessageCallback mCallback;
    private SqlManager mSqlManager;

    public MessageUtil(MessageCallback callback) {
        mCallback = callback;
        mSqlManager = SqlManager.getInstance();
    }

    public void processLogin(String user, String password) {
        String loginSql = "select password from account where user = '" + user +"'";
        printLog(loginSql , LogUtil.DEBUG);
        try {
            ResultSet res = mSqlManager.select(loginSql);
            if (!res.isBeforeFirst()) {
                printLog("not found user", LogUtil.DEBUG);
                if (mCallback != null) {
                    mCallback.processComplete(SocketData.SERVER_LOGIN_FAILED_NOT_USER);
                }
                return;
            }
            while (res.next()) {
                if (res.getString("password").equals(password)) {
                    printLog("login successful",LogUtil.INFO);
                    if (mCallback != null) {
                        mCallback.processComplete(SocketData.SERVER_LOGIN_SUCCESSFUL);
                    }
                    return;
                }
            }
        } catch (Exception e) {
            printLog("login failed exception = " + e.toString(),LogUtil.ERROR);
        }
        printLog("login failed",LogUtil.INFO);
        if (mCallback != null) {
            mCallback.processComplete(SocketData.SERVER_LOGIN_FAILED_ERROR_PASS);
        }
    }

    public void processRegister(String user, String password) {
        String selectUserSql = "select password from account where user = '" + user +"'";
        printLog(selectUserSql , LogUtil.DEBUG);
        try {
            ResultSet res = mSqlManager.select(selectUserSql);
            if (!res.isBeforeFirst()) {
                printLog("register not found user", LogUtil.DEBUG);
                String insertSql = "insert into account (user, password) values ('"+user+"', '"+password+"')";
                int insertRes = mSqlManager.update(insertSql);
                if (insertRes >= 0) {
                    printLog("register successful",LogUtil.INFO);
                    if (mCallback != null) {
                        mCallback.processComplete(SocketData.SERVER_REGISTER_SUCCESSFUL);
                    }
                } else {
                    printLog("register failed",LogUtil.INFO);
                    if (mCallback != null) {
                        mCallback.processComplete(SocketData.SERVER_REGISTER_FAILED_HAS_USER);
                    }
                }
            } else {
                printLog("register failed user is exist", LogUtil.DEBUG);
                if (mCallback != null) {
                    mCallback.processComplete(SocketData.SERVER_REGISTER_FAILED_HAS_USER);
                }
            }
        } catch (Exception e) {
            printLog("register select failed exception = " + e.toString(),LogUtil.ERROR);
        }
    }

    public void processStrMessage(String str) {
        if (mCallback != null) {
            mCallback.processComplete(SocketData.SERVER_STRING_MEG_TYPE, str);
        }
    }

    public void processFileMessage(String fileName, byte[] imgData) {
        try {
            File file = new File(fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(imgData, 0 , imgData.length);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            printLog("write img file failed :" + e.toString() ,LogUtil.DEBUG);
            if (mCallback != null) {
                mCallback.processComplete(SocketData.SERVER_FILE_SAVE_FAILED);
            }
        }
        if (mCallback != null) {
            mCallback.processComplete(SocketData.SERVER_FILE_SAVE_SUCCESSFUL);
        }
    }

    public interface MessageCallback {
        void processComplete(int type);
        void processComplete(int type, String msg);
    }

    private void printLog(String str, int level) {
        LogUtil.printLog("MessageUtil : " + str, level);
    }
}
