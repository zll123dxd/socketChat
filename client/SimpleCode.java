package com.example.testapp.javaTest.option;

import com.example.testapp.R;
import com.example.testapp.javaTest.tool.SocketData;
import com.example.testapp.javaTest.tool.SocketManager;

public class ChatActivity extends Activity {
    SocketManager.HanderCallback mCallback =  new SocketManager.HanderCallback() {
        @Override
        public void onReceiver(SocketData data) {
            //message callback
        }
    };

    SocketManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        manager = SocketManager.getInstance();
        manager.registerHandlerCallback(mCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.unRegisterHanlderCallback(mCallback);
    }

    private void sendMessage() {
        String input = "test";
        SocketData data = new SocketData(SocketData.CLIENT_STRING_MEG_TYPE, input, SocketData.SEND_ALL);
        manager.setMessage(data);
    }
}
