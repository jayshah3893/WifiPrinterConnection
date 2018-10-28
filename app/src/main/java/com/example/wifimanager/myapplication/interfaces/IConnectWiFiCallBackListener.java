package com.example.wifimanager.myapplication.interfaces;

import java.io.OutputStream;
import java.net.Socket;


public interface IConnectWiFiCallBackListener {

    void callBackListener(String ip,int port,Socket socket, OutputStream outputStream);
}
