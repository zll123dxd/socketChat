package com.example.testapp.javaTest.tool;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class SocketManager {
    private static SocketManager socketManager;
    private Socket socket;
    private ArrayList<HanderCallback> mCallbackList;
    private ObjectOutputStream objOut;

    public SocketManager() {
        mCallbackList = new ArrayList<HanderCallback>();
        new Thread(){
            @Override
            public void run() {
                try {
                    socket = new Socket("192.168.112.27", 20000);
                    new Thread(new ClientThread(socket, mHandler)).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static SocketManager getInstance() {
        if (socketManager == null) {
            socketManager = new SocketManager();
        }
        return socketManager;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setMessage(SocketData data) {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (objOut == null) {
                        objOut = new ObjectOutputStream(socket.getOutputStream());
                    }
                    objOut.writeObject(data);
                    objOut.flush();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    public void close() {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (socket != null) {
                        socket.close();
                        socket = null;
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void registerHandlerCallback(HanderCallback callback) {
        if (callback != null) {
            mCallbackList.add(callback);
        }
    }

    public void unRegisterHanlderCallback(HanderCallback callback) {
            mCallbackList.remove(callback);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                onReceiver((SocketData) msg.obj);
            }
        }
    };

    private void onReceiver(SocketData data) {
        if (mCallbackList != null) {
            for (HanderCallback callback : mCallbackList) {
                callback.onReceiver(data);
            }
        }
    }

    public interface HanderCallback {
        void onReceiver(SocketData data);
    }
}
