package com.home.server;

import java.io.IOException;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;

import com.home.server.state.DataStateMachine;
import com.home.server.util.LogUtil;
import com.home.server.util.MessageUtil;

public class ServerThread extends DataStateMachine implements Runnable {
    private Socket mSocket = null;
    private LocalObjectInputStream input = null;
    private ObjectOutputStream output = null;
    private boolean DEBUG = true;
    private State mDefaultState;
    private State mTypeState;
    private State mProcessState;
    private State mCompletedState;
    private SocketData mSocketData;
    private SocketData mCompleteSocketData;
    private MessageUtil messageUtil;

    private String mUserName;
    private int mProcessType;

    public final int INIT_COMPLETE = 0;
    public final int COMPLETED = 1;
    
    public ServerThread(Socket socket) throws IOException {
        mSocket = socket;
        input = new LocalObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());
        mDefaultState = new DefaultState();
        mTypeState = new TypeState();
        mProcessState = new ProcessState();
        mCompletedState = new CompletedState();
        addState(mDefaultState);
        addState(mTypeState);
        addState(mProcessState);
        addState(mCompletedState);
        // init
        process(INIT_COMPLETE);
    }

    @Override
    public void run() {
        try {
            SocketData data = null;
            while ((data = readFromClient()) != null) {
                printLog(data.toString(), LogUtil.DEBUG);
                mSocketData = data;
                process(data.getType());
            }
        } catch (Exception e) {
            printLog("Server Thread : Exception is happen in run. Exception :" + e.toString(), LogUtil.ERROR);
        }
    }

    private SocketData readFromClient() {
        try {
            return (SocketData)input.readObject();
        } catch (Exception e) {
            printLog("Server Thread : Client = " + mSocket.toString() + " is Disconnected ," + e, LogUtil.ERROR);
            ChatServer.socketList.remove(mSocket);
        }
        return null;
    }

    private void checkMessage() {
    }

    private void sendToAll(SocketData data) {
        try {
            for (Socket otherSocekt : ChatServer.socketList) {
                ObjectOutputStream sOutput = new ObjectOutputStream(otherSocekt.getOutputStream());
                sOutput.writeObject(data);
                sOutput.flush();
                printLog("send to socket :" + mSocket.toString() + ", write message to us", LogUtil.DEBUG);
            }
        } catch (IOException e) {
            printLog("IOException in sendToUS", LogUtil.ERROR);
        }
    }

    private void sendToOne(String msg) {
    }

    private void sendToUs(SocketData data) {
        synchronized (mSocket) {
            try {
                output.writeObject(data);
                output.flush();
                printLog("send to socket :" + mSocket.toString() + ", write message to us", LogUtil.DEBUG);
            } catch (IOException e) {
                printLog("IOException in sendToUS", LogUtil.ERROR);
            }
        }
    }

    private class DefaultState extends State{

        public DefaultState() {
            mType = "defaultState";
        }

        public void enter() {
            printLog("enter : " + mType, LogUtil.INFO);
            messageUtil = new MessageUtil(callback);
        }

        public void process(int msg) {
            printLog("process : " + mType, LogUtil.INFO);
            switch (msg) {
               case INIT_COMPLETE:
                   transferTo(mTypeState);
                   break;
               default :
                   break;
            }
        }

        public void exit() {
            printLog("exit : " + mType, LogUtil.INFO);
        }
    }

    private class TypeState extends State{

        private String sessionType;

        public TypeState() {
            mType = "typeState";
        }

        public void enter() {
            printLog("enter : " + mType, LogUtil.INFO);
        }

        public void process(int msg) {
            printLog("process : " + mType, LogUtil.INFO);
            mProcessType = msg;
            switch (msg){
                case SocketData.CLIENT_LOGIN_TYPE:
                case SocketData.CLIENT_REGISTER_TYPE:
                case SocketData.CLIENT_STRING_MEG_TYPE:
                case SocketData.CLIENT_FILE_TYPE:
                    transferTo(mProcessState);
                    break;
                default:
                    break;
            }
        }

        public void exit() {
            printLog("exit : " + mType, LogUtil.INFO);
        }
    }

    private class ProcessState extends State{

        public ProcessState() {
            mType = "processState";
        }

        public void enter() {
            printLog("enter : " + mType, LogUtil.INFO);
            String account[];
            switch (mProcessType) {
                case SocketData.CLIENT_LOGIN_TYPE:
                    account = mSocketData.getContent().split(":");
                    if (account.length == 2) {
                        mUserName = account[0];
                        messageUtil.processLogin(account[0], account[1]);
                    }
                    break;
                case SocketData.CLIENT_REGISTER_TYPE:
                    account = mSocketData.getContent().split(":");
                    if (account.length == 2) {
                        messageUtil.processRegister(account[0], account[1]);
                    }
                case SocketData.CLIENT_STRING_MEG_TYPE:
                    messageUtil.processStrMessage(mSocketData.getContent());
                    break;
                case SocketData.CLIENT_FILE_TYPE:
                    messageUtil.processFileMessage(mSocketData.getContent(), mSocketData.getFile());
                default:
                    break;
            }

        }

        public void process(int msg) {
            printLog("process : " + mType, LogUtil.INFO);
            switch (mProcessType) {
                case SocketData.CLIENT_LOGIN_TYPE:
                    mCompleteSocketData = new SocketData(SocketData.SERVER_LOGIN_TYPE, String.valueOf(msg));
                    if (msg == SocketData.SERVER_LOGIN_SUCCESSFUL) {
                        ChatServer.socketMap.put(mUserName, mSocket);
                    }
                    break;
                case SocketData.CLIENT_REGISTER_TYPE:
                    mCompleteSocketData = new SocketData(SocketData.SERVER_REGISTER_TYPE, String.valueOf(msg));
                    break;
                case SocketData.CLIENT_STRING_MEG_TYPE:
                    mCompleteSocketData = new SocketData(SocketData.SERVER_STRING_MEG_TYPE, msgContext);
                    break;
                case SocketData.CLIENT_FILE_TYPE:
                     mCompleteSocketData = new SocketData(SocketData.SERVER_FILE_TYPE, String.valueOf(msg));
                    break;
                default:
                    break;
            }
            transferTo(mCompletedState);
        }

        public void exit() {
            printLog("exit : " + mType, LogUtil.INFO);
        }
    }

    private class CompletedState extends State{

        public CompletedState() {
            mType = "completedState";
        }

        public void enter() {
            printLog("enter : " + mType, LogUtil.INFO);
            switch (mCompleteSocketData.getType()) {
                case SocketData.SERVER_LOGIN_TYPE:
                    sendToUs(mCompleteSocketData);
                    break;
                case SocketData.SERVER_REGISTER_TYPE:
                    sendToUs(mCompleteSocketData);
                case SocketData.SERVER_STRING_MEG_TYPE:
                    if (mSocketData.getSendType() == SocketData.SEND_ALL) {
                        sendToAll(mCompleteSocketData);
                    }
                case SocketData.SERVER_FILE_TYPE:
                    if (mSocketData.getSendType() == SocketData.SAVE) {
                        sendToUs(mCompleteSocketData);
                    }
                default:
                    break;
            }
            process(COMPLETED);
        }

        public void process(int msg) {
            printLog("process : " + mType, LogUtil.INFO);
            switch (msg) {
                case COMPLETED:
                    transferTo(mTypeState);
                    break;
                default:
                    break;
            }
        }

        public void exit() {
            printLog("exit : " + mType, LogUtil.INFO);
            mSocketData = null;
            mCompleteSocketData = null;
        }
    }

    private String msgContext;

    private MessageUtil.MessageCallback callback = new MessageUtil.MessageCallback() {
        public void processComplete(int type) {
            process(type);
        }

        public void processComplete(int type, String msg) {
            msgContext = msg;
            process(type);
        }
    };

    private void printLog(String str, int level) {
        LogUtil.printLog("ServerThread : " + str, level);
    }
}
