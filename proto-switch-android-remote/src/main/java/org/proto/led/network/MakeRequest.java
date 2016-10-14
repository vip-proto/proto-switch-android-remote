/*
 * *****************************************************************
 *   This file is part of PROTO-SWITCH Light controller project.
 *
 *   Copyright (C) 2016 ViP-PROTO Association, http://vip-proto.com
 *   Predrag Milutinovic <pedjolino@gmail.com>
 *
 *   This program is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU General Public License as
 *   published by the Free Software Foundation; either version 2 of the
 *   License, or (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful, but
 *   WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *   General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 *   02111-1307, USA.
 *
 *   The GNU General Public License is contained in the file COPYING.
 * /
 *
 */

package org.proto.led.network;

import android.content.Context;
import android.content.pm.PackageManager;
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

public abstract class MakeRequest extends AsyncTask<String, Void, String> {

    private static final String TAG = "MakeRequest";
    protected static final int READ_TIMEOUT_MILLIS = 10000;
    protected static final int CONNECTION_TIMEOUT_MILLIS = 15000;
    public static final int TIMEOUT_STATUS = 408;

    private String requestMethod;
    private Context context;

    private int httpStatus;

    public MakeRequest(String requestMethod, Context context) {
        this.requestMethod = requestMethod;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... urls) {

        // params comes from the execute() call: params[0] is the url.
        try {
            return downloadUrl(urls[0]);
        } catch (IOException e) {
            return null;
        }
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        onResult(result);
    }

    public abstract void onResult(String result);

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT_MILLIS /* milliseconds */);
            conn.setConnectTimeout(CONNECTION_TIMEOUT_MILLIS /* milliseconds */);
            conn.setRequestMethod(requestMethod);
            conn.setDoInput(true);

            addSomethingToHeader(conn);

            // Starts the query
            conn.connect();
            httpStatus = conn.getResponseCode();
            Log.d(TAG, "The response is: " + httpStatus);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch (SocketTimeoutException e) {
            Log.e(TAG, "Login timeout");
            httpStatus = TIMEOUT_STATUS;
            return null;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private void sendUDP(String address, int port, String message)  {

        try {
            InetAddress IPAddress = InetAddress.getByName(address);
            DatagramSocket client_socket = new DatagramSocket(port, IPAddress);
            byte[] send_data = new byte[1024];
            send_data = message.getBytes();
            DatagramPacket send_packet = new DatagramPacket(send_data, message.length(), IPAddress, port);
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

    protected void addSomethingToHeader(HttpURLConnection conn) {

//		conn.setRequestProperty( context.getString( R.string.Stage2AppHttpHeader ), getVersion() );
//		if (requestMethod.equals( "POST" )) {
//			conn.setRequestProperty( "Content-Type", "application/json" );
//		}
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        return total.toString();
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    private String getVersion() {
        String version = "undetected";
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName + " (" + context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionCode + ")";

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Can not find version of app", e);
        }
        return version;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

}
