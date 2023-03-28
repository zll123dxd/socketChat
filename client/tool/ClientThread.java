package com.example.testapp.javaTest.tool;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.Socket;

public class ClientThread implements Runnable{
    private Handler mHandler;
    private LocalObjectInputStream input;

    public ClientThread(Socket socket, Handler handler) throws IOException {
        mHandler = handler;
        input = new LocalObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        try {
            SocketData data = null;
            while ((data = (SocketData) input.readObject()) != null) {
                    Message message = new Message();
                    message.what = 0;
                    message.obj = data;
                    mHandler.sendMessage(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
