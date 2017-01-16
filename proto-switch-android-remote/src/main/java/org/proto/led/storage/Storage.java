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

package org.proto.led.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.proto.led.controller.R;
import org.proto.led.dto.ControllerDto;
import org.proto.led.dto.DimmableLightDto;
import org.proto.led.dto.LightDto;
import org.proto.led.dto.RgbLightDto;
import org.proto.led.dto.ThemeDto;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Created by Predrag Milutinovic on 19.3.2016..
 */
public class Storage {
    private static final String TAG = "Storage";
    private static final String PREFERENCES = "PREFERENCES";

    public static void storeLights(Context context, ArrayList<LightDto> lights) {
        long start = System.currentTimeMillis();
        Gson gson = new Gson();
        String json = gson.toJson(lights);
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.preference_whole_model), json);
        editor.commit();
        long end = System.currentTimeMillis();

        Log.i(TAG, "saving lights time = " + (end - start) + "ms");

    }

    public static ArrayList<LightDto> loadLights(Context context) {
        long start = System.currentTimeMillis();
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        String json = sharedPref.getString(context.getString(R.string.preference_whole_model), "");
        Log.d(TAG, "lights json = " + json);
        Type listType = new TypeToken<ArrayList<RgbLightDto>>() {
        }.getType();

        ArrayList<RgbLightDto> lightsRow = new Gson().fromJson(json, listType);
        ArrayList<LightDto> lights = new ArrayList<LightDto>();
        Log.i(TAG, "lightsRow.size() = " + (lightsRow != null ? lightsRow.size() : "null"));
        if (lightsRow == null || lightsRow.size() == 0) {
            lights = initLights();
        } else {
            for (RgbLightDto rgbLightDto : lightsRow) {
                if (rgbLightDto.getType().equals(LightDto.RGB_LIGHT)) {
                    lights.add(rgbLightDto);
                } else if (rgbLightDto.getType().equals(LightDto.DIMMABLE_LIGHT)) {
                    lights.add(rgbLightDto.toDimmableLightDto());
                } else {
                    lights.add(rgbLightDto.toLightDto());
                }
            }
        }

        long end = System.currentTimeMillis();

        Log.i(TAG, "loading time = " + (end - start) + "ms");
        return lights;
    }

    public static ArrayList<ControllerDto> loadControllers(Context context) {
        ArrayList<ControllerDto> controllerDtos = new ArrayList<ControllerDto>();
        RgbLightDto rgbLightDto1 = new RgbLightDto();
        rgbLightDto1.setName("LED 1");
        rgbLightDto1.setRedChannel(0);
        rgbLightDto1.setGreenChannel(1);
        rgbLightDto1.setBlueChannel(2);
        RgbLightDto rgbLightDto2 = new RgbLightDto();
        rgbLightDto2.setName("LED 2");
        rgbLightDto2.setRedChannel(3);
        rgbLightDto2.setGreenChannel(4);
        rgbLightDto2.setBlueChannel(5);
        RgbLightDto rgbLightDto3 = new RgbLightDto();
        rgbLightDto3.setName("LED 3");
        rgbLightDto3.setRedChannel(6);
        rgbLightDto3.setGreenChannel(7);
        rgbLightDto3.setBlueChannel(8);
        ControllerDto controllerDto1 = new ControllerDto();
        controllerDto1.setName("MockedController1");
        controllerDto1.setIpAddress("192.168.1.221");
        controllerDto1.getLedLights().add(rgbLightDto1);
        rgbLightDto1.setControllerDto(controllerDto1);
        controllerDto1.getLedLights().add(rgbLightDto2);
        rgbLightDto2.setControllerDto(controllerDto1);
        controllerDtos.add(controllerDto1);
        ControllerDto controllerDto2 = new ControllerDto();
        controllerDto2.setName("MockedController2");
        controllerDto2.setIpAddress("192.168.1.222");
        controllerDto2.getLedLights().add(rgbLightDto1);
        controllerDto2.getLedLights().add(rgbLightDto2);
        controllerDto2.getLedLights().add(rgbLightDto3);
        rgbLightDto3.setControllerDto(controllerDto2);
        controllerDtos.add(controllerDto2);
        return controllerDtos;
    }

    public static ArrayList<LightDto> initLights() {
        ControllerDto controllerDto1 = new ControllerDto();
        controllerDto1.setName("MockedController1");
        controllerDto1.setIpAddress("192.168.1.195");
        controllerDto1.setNumberOfChanels(10);
        ArrayList<LightDto> ledLightDtos = new ArrayList<LightDto>();
        RgbLightDto ledLightDto1 = new RgbLightDto();
        ledLightDto1.setName("Daska gore");
        ledLightDto1.setRedChannel(3);
        ledLightDto1.setGreenChannel(4);
        ledLightDto1.setBlueChannel(5);
        ledLightDto1.setRedValue(50);
        ledLightDto1.setGreenValue(1);
        ledLightDto1.setBlueValue(2);
        ledLightDto1.setIntensity(50);
        ledLightDto1.setControllerDto(controllerDto1);
        RgbLightDto ledLightDto2 = new RgbLightDto();
        ledLightDto2.setName("Daska dole");
        ledLightDto2.setRedChannel(0);
        ledLightDto2.setGreenChannel(1);
        ledLightDto2.setBlueChannel(2);
        ledLightDto2.setRedValue(3);
        ledLightDto2.setGreenValue(40);
        ledLightDto2.setBlueValue(5);
        ledLightDto2.setIntensity(40);
        ledLightDto2.setControllerDto(controllerDto1);
        RgbLightDto ledLightDto3 = new RgbLightDto();
        ledLightDto3.setName("Zavesa");
        ledLightDto3.setRedChannel(6);
        ledLightDto3.setGreenChannel(7);
        ledLightDto3.setBlueChannel(8);
        ledLightDto3.setRedValue(6);
        ledLightDto3.setGreenValue(7);
        ledLightDto3.setBlueValue(30);
        ledLightDto3.setIntensity(30);
        ledLightDto3.setControllerDto(controllerDto1);
        ledLightDtos.add(ledLightDto1);
        ledLightDtos.add(ledLightDto2);
        ledLightDtos.add(ledLightDto3);
        DimmableLightDto ledLightDto4 = new DimmableLightDto();
        ledLightDto4.setName("Cvece");
        ledLightDto4.setChannel(9);
        ledLightDto4.setIntensity(63);
        ledLightDto4.setOn(true);
        ledLightDto4.setControllerDto(controllerDto1);
        ledLightDtos.add(ledLightDto4);
        // -------
//		ledLightDtos.add(ledLightDto1);
//		ledLightDtos.add(ledLightDto2);
//		ledLightDtos.add(ledLightDto3);
//		ledLightDtos.add(ledLightDto4);
//		ledLightDtos.add(ledLightDto5);
//		ledLightDtos.add(ledLightDto5);

        return ledLightDtos;
    }

    public static void updateLights(Context context, LightDto... selectedLight) {
        ArrayList<LightDto> lights = loadLights(context);

//		LinkedHashSet<LightDto> lightsSet = new LinkedHashSet<LightDto>(lights);
        for (LightDto lightDto : selectedLight) {
            int indexOf = lights.indexOf(lightDto);
            if (indexOf != -1) {
                lights.remove(indexOf);
                lights.add(indexOf, lightDto);
            } else {
                lights.add(lightDto);
            }
//			lightsSet.
//			lightsSet.add(lightDto);
        }
//		lights.clear();
//		lights.addAll(lightsSet);
        storeLights(context, lights);
    }

    public static ArrayList<ThemeDto> loadThemes(Context context) {
        ArrayList<ThemeDto> themes = new ArrayList<ThemeDto>();
        ThemeDto theme1 = new ThemeDto();
        theme1.setName("Movie");
        theme1.setLights(loadLights(context));
        themes.add(theme1);
        ThemeDto theme2 = new ThemeDto();
        theme2.setName("Party");
        theme2.setLights(loadLights(context));
        themes.add(theme2);
        return themes;
    }

    public static void addTheme(ThemeDto themeDto) {

    }

    public static void updateTheme(ThemeDto themeDto) {

    }

    public static void deleteTheme(ThemeDto themeDto) {

    }

}
