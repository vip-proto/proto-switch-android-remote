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

import org.proto.led.dto.DimmableLightDto;
import org.proto.led.dto.LightDto;
import org.proto.led.dto.RgbLightDto;

/**
 * Created by Predrag on 26.11.2016..
 */

public class WifiController {

    public static void setLight(Context context, LightDto light) {
        // reset
        byte[] command = new byte[light.getControllerDto().getNumberOfChanels() + 1];
        command[0] = (byte) 128;

        for (int i = 1; i < command.length; i++) {
            command[i] = 64;
        }

        if (light instanceof RgbLightDto) {
            RgbLightDto rgbLightDto = (RgbLightDto) light;
            command[rgbLightDto.getRedChannel() + 1] = (byte) rgbLightDto.getCalculatedRedValue();
            command[rgbLightDto.getGreenChannel() + 1] = (byte) rgbLightDto.getCalculatedGreenValue();
            command[rgbLightDto.getBlueChannel() + 1] = (byte) rgbLightDto.getCalculatedBlueValue();
        } else if (light instanceof DimmableLightDto) {
            DimmableLightDto dimmableLightDto = (DimmableLightDto) light;
            command[dimmableLightDto.getChannel() + 1] = (byte) dimmableLightDto.getCalculatedIntensity();
        } else if (light instanceof LightDto) {
            command[light.getChannel() + 1] = (byte) (light.isOn() ? 63 : 0);

        }
        String ip = light.getControllerDto().getIpAddress();
        new MakeUDPRequest(ip, 6000, command, context).execute();
    }

    public static void startDiscovery(Context context){
        byte[] command = new byte[2];
        //  0x8f 0x1f
        command[0] = (byte) 0x8f;
        command[1] = 0x1f;
        new MakeUDPBroadcastRequest(6000, command, context).execute();
    }

    public static void setAllLights(Context context, LightDto... allLights) {
    //TODO: treba da se napravi da ih sve setuje odjednom po kontroleru. mozda bi gornja metoda mogla da vraca niz komandi koji bi se dodatno azurirao kad se naidje na svetlo na istom kontroleru.
        for (LightDto light : allLights) {
            setLight(context, light);
        }
    }

//    public void sendCommandToController(RgbLightDto... selectedLight) {
//        // reset
//        byte[] command = new byte[7];
//        command[0] = (byte) 128;
//
////        int[] values = new int[9];
//        for (int i = 1; i < command.length; i++) {
//            command[i] = 64;
//
//        }
//        for (RgbLightDto ledDto : selectedLight) {
//            command[ledDto.getRedChannel() + 1] = (byte) ledDto.getRedValue();
//            command[ledDto.getGreenChannel() + 1] = (byte) ledDto.getGreenValue();
//            command[ledDto.getBlueChannel() + 1] = (byte) ledDto.getBlueValue();
//        }
//        String ip = "192.168.1.221";
////        String ip = selectedLight;
////        String ip = "192.168.1.51";
////        Log.i(TAG, "command " + command);
//        new MakeUDPRequest(ip, 6000, command, getActivity()).execute();
//    }
}
