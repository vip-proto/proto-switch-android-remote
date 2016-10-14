package org.proto.led.network;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

public class MakeUDPRequest extends AsyncTask<String, Void, String> {

    private static final String TAG = "MakeUDPRequest";
    protected static final int READ_TIMEOUT_MILLIS = 10000;
    protected static final int CONNECTION_TIMEOUT_MILLIS = 15000;
    public static final int TIMEOUT_STATUS = 408;

    private String address;
    private int port;
    private byte[] message;
    private Context context;

    private int httpStatus;

    public MakeUDPRequest(String address, int port, byte[] message, Context context) {
        this.address = address;
        this.port = port;
        this.message = message;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... urls) {

        // params comes from the execute() call: params[0] is the url.

        sendUDP();
        return null;

    }


    private void sendUDP() {

        try {
            InetAddress IPAddress = InetAddress.getByName(address);
//            InetAddress IPAddress = getBroadcastAddress();
            DatagramSocket client_socket = new DatagramSocket(port);
            DatagramPacket send_packet = new DatagramPacket(message, message.length, IPAddress, port);
            client_socket.send(send_packet);
            client_socket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // }
    }

    InetAddress getBroadcastAddress() throws IOException {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        // handle null somehow

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }







}
