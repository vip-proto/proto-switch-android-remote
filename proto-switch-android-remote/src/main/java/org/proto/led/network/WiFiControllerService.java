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

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.proto.led.dto.ControllerDto;
import org.proto.led.dto.DimmableLightDto;
import org.proto.led.dto.LightDto;
import org.proto.led.dto.RgbLightDto;
import org.proto.led.storage.Storage;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class WiFiControllerService extends Service {


    public WiFiControllerService() {
    }

    public static final String MESSAGE_FROM_CONTROLLER = "org.proto.led.network.WiFiControllerService.MESSAGE_FROM_CONTROLLER";
    public static final String TAG = "WiFiControllerService";

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    static String UDP_BROADCAST = "UDPBroadcast";
    private DatagramSocket socket;


    private void listenAndWaitAndThrowIntent(InetAddress broadcastIP, Integer port) throws Exception {
        byte[] recvBuf = new byte[400];
        if (socket == null || socket.isClosed()) {
            socket = new DatagramSocket(port);
            socket.setSoTimeout(5000);
            socket.setBroadcast(true);
        }
        DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
        Log.i(TAG, "Waiting for UDP broadcast");
        socket.receive(packet);

        String senderIP = packet.getAddress().getHostAddress();
        parseMessage(recvBuf, senderIP);
        String message = new String(packet.getData()).trim();
        Log.i(TAG, "Got UDB broadcast from " + senderIP + ", message: " + message);

        socket.close();
    }

    protected void parseMessage(byte[] b, String senderIp) {
        if (!isValidSignature(b)) {
            return;
        }

        int crc32 = b[4] + (b[5] << 8) + (b[6] << 16) + (b[7] << 24);
        int length = b[8] + (b[9] << 8);
        int udp_port = b[10] + (b[11] << 8);
        String controllerName = getControllerName(b);
        int numberOfLights = b[44];
        int numberOfChannels = b[45];
        int protocolVersion = b[46];
        ArrayList<LightDto> lights = parseLights(b, numberOfLights);
        ControllerDto controllerDto = createController(senderIp, b);
        controllerDto.setNumberOfChanels(numberOfChannels);
        controllerDto.setLedLights(lights);
        controllerDto.setName(controllerName);
        for (LightDto light : lights) {
            light.setControllerDto(controllerDto);
            Storage.updateLights(this, light);
        }
    }

    private ArrayList<LightDto> parseLights(byte[] b, int numberOfLights) {
        int startOfLight = 48;
        ArrayList<LightDto> lights = new ArrayList<>();
        for (int i = 0; i < numberOfLights; i++) {
            String lightName = new String(b, startOfLight, 32).trim();
            byte lightType = b[startOfLight + 32];
            LightDto parsedLight = null;
            switch (lightType) {
                case 1: // ON/OFF
                    LightDto lightDto = new LightDto();
                    lightDto.setChannel(b[startOfLight + 32 + 1]);
                    lightDto.setOn(b[startOfLight + 32 + 2] > 0);
                    parsedLight = lightDto;
                    startOfLight = startOfLight + 32 + 2 + 1;
                    break;
                case 2: // RGB
                    RgbLightDto rgbLightDto = new RgbLightDto();
                    rgbLightDto.setRedChannel(b[startOfLight + 32 + 1]);
                    rgbLightDto.setRedValue(b[startOfLight + 32 + 2]);
                    rgbLightDto.setGreenChannel(b[startOfLight + 32 + 3]);
                    rgbLightDto.setGreenValue(b[startOfLight + 32 + 4]);
                    rgbLightDto.setBlueChannel(b[startOfLight + 32 + 5]);
                    rgbLightDto.setBlueValue(b[startOfLight + 32 + 6]);
                    rgbLightDto.setOn(rgbLightDto.getRedValue() > 0 || rgbLightDto.getGreenValue() > 0 || rgbLightDto.getBlueValue() > 0);
                    parsedLight = rgbLightDto;
                    startOfLight = startOfLight + 32 + 6 + 1;
                    break;
                case 3: // DIMMABLE
                    DimmableLightDto dimmableLightDto = new DimmableLightDto();
                    dimmableLightDto.setChannel(b[startOfLight + 32 + 1]);
                    dimmableLightDto.setOn(b[startOfLight + 32 + 1 + 1] > 0);
                    dimmableLightDto.setIntensity(b[startOfLight + 32 + 1 + 1]);
                    parsedLight = dimmableLightDto;
                    startOfLight = startOfLight + 32 + 2 + 1;
                    break;
            }
            parsedLight.setName(lightName);
            lights.add(parsedLight);
        }
        return lights;
    }

    private String getControllerName(byte[] b) {
        return new String(b, 12, 32).trim();
    }

    private boolean isValidSignature(byte[] bytes) {
        if (bytes[0] == (byte) 0x82 && bytes[1] == 0x19 && bytes[2] == 0x12 && bytes[3] == 0x16) {
            return true;
        } else {
            return false;
        }
    }

    private ControllerDto createController(String senderIp, byte[] bytes) {
        ControllerDto controllerDto = new ControllerDto();
        controllerDto.setIpAddress(senderIp);
        return controllerDto;
    }

    Thread UDPBroadcastThread;

    void startListenForUDPBroadcast() {
        UDPBroadcastThread = new Thread(new Runnable() {
            public void run() {
                try {
                    InetAddress broadcastIP = InetAddress.getByName("192.168.1.101"); //172.16.238.42 //192.168.1.255
                    Integer port = 6000;
                    while (shouldRestartSocketListen) {
                        try {
                            listenAndWaitAndThrowIntent(broadcastIP, port);

                        } catch (SocketTimeoutException e) {
                            Log.d("UDP", "timeout");
                        }
                    }
                    //if (!shouldListenForUDPBroadcast) throw new ThreadDeath();
                } catch (Exception e) {
                    Log.i(TAG, "no longer listening for UDP broadcasts cause of error " + e.getMessage());
                }
            }
        });
        UDPBroadcastThread.start();
    }

    private Boolean shouldRestartSocketListen = true;

    void stopListen() {
        shouldRestartSocketListen = false;
        socket.close();
    }


    @Override
    public void onDestroy() {
        stopListen();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        shouldRestartSocketListen = true;
        startListenForUDPBroadcast();
        Log.i("UDP", "Service started");
        return START_STICKY;
    }
}
