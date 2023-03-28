package com.home.server;


import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

import com.home.server.util.LogUtil;
import com.home.server.sql.SqlManager;

public class ChatServer {

    public static ArrayList<Socket> socketList = new ArrayList<Socket>();
    public static Map<String, Socket> socketMap = new HashMap<>();

    public static void main (String[] args) throws IOException{
        ServerSocket server = new ServerSocket(20000);
        printLog("Server Socket = " + server.toString(), LogUtil.INFO);    
        printLog("create successful",LogUtil.INFO);
        printLog("wait for client",LogUtil.INFO);
        while (true) {
            Socket socket = server.accept();
            printLog("added client, socket =" + socket.toString(),LogUtil.INFO);
            printLog("remote ip = " + socket.getRemoteSocketAddress().toString(),LogUtil.INFO);
            socketList.add(socket);
            new Thread(new ServerThread(socket)).start();
        }
    }

    private static void test() {
        printLog("------------run test-----------", LogUtil.INFO);
        sqlTest();
        printLog("------------test end-----------", LogUtil.INFO);
    }

    private static void sqlTest() {
        SqlManager sql = SqlManager.getInstance();
        sql.runTest();
    }

    private static void printLog(String str, int level) {
        LogUtil.printLog("ChatServer : " + str, level);
    }
}
