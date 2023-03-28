package com.home.server;

import java.io.Serializable;

public class SocketData implements Serializable {
    //根据本地日志修改此值
    private static long serialVersionUID = -6013989611474208605L;

    //如果客户端不设定此值， 则发送对象由服务端决定
    public static final int SEND_US = 0;
    public static final int SEND_ONE = 1;
    public static final int SEND_ALL = 2;
    public static final int SAVE = 3;

    public static final int CLIENT_LOGIN_TYPE = 0;
    public static final int CLIENT_REGISTER_TYPE = 1;
    public static final int CLIENT_STRING_MEG_TYPE = 2;
    public static final int CLIENT_FILE_TYPE = 3;

    public static final int SERVER_LOGIN_TYPE = 0;
    public static final int SERVER_LOGIN_SUCCESSFUL = 0;
    public static final int SERVER_LOGIN_FAILED_NOT_USER = 1;
    public static final int SERVER_LOGIN_FAILED_ERROR_PASS = 2;

    public static final int SERVER_REGISTER_TYPE = 1;
    public static final int SERVER_REGISTER_SUCCESSFUL = 1;
    public static final int SERVER_REGISTER_FAILED_HAS_USER = 0;

    public static final int SERVER_STRING_MEG_TYPE = 2;

    public static final int SERVER_FILE_TYPE = 3;
    public static final int SERVER_FILE_SAVE_SUCCESSFUL = 0;
    public static final int SERVER_FILE_SAVE_FAILED = 1;

    private int mType;
    private int mSendType;
    private String mContent;
    private byte[] mFile;

    public SocketData() {
        super();
    }

    public SocketData(int type, String mContent) {
        super();
        this.mType = type;
        this.mContent = mContent;
    }

    public SocketData(int type, String mContent, int sendType) {
        super();
        this.mType = type;
        this.mContent = mContent;
        this.mSendType = sendType;
    }

    public SocketData(int type, String mContent, int sendType, byte[] file) {
        super();
        this.mType = type;
        this.mContent = mContent;
        this.mSendType = sendType;
        this.mFile = file;
    }

    public int getType() {
        return mType;
    }

    public String getContent() {
        return mContent;
    }

    public int getSendType() { 
        return mSendType;
    }

    public byte[] getFile() {
        return mFile;
    }

    @Override
    public String toString() {
        return "SocketData{" +
                "mType=" + mType +
                ", mSendType=" + mSendType +
                ", mContent='" + mContent + '\'' +
                ", mFile=" + (mFile!=null ? mFile.length : 0) +
                '}';
    }
}
