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

package org.proto.led.dto;

import android.graphics.Color;

/**
 * Created by Predrag Milutinovic on 20.3.2016..
 */
public class RgbLightDto extends DimmableLightDto {


    private int redChannel;
    private int greenChannel;
    private int blueChannel;
    private int redValue;
    private int greenValue;
    private int blueValue;

    public RgbLightDto() {
        setType(RGB_LIGHT);
    }

    public int getRedChannel() {
        return redChannel;
    }

    public void setRedChannel(int redChannel) {
        this.redChannel = redChannel;
    }

    public int getGreenChannel() {
        return greenChannel;
    }

    public void setGreenChannel(int greenChannel) {
        this.greenChannel = greenChannel;
    }

    public int getBlueChannel() {
        return blueChannel;
    }

    public void setBlueChannel(int blueChannel) {
        this.blueChannel = blueChannel;
    }

    public int getRedValue() {
        return redValue;
    }

    public int getCalculatedRedValue() {
        return redValue * getIntensity() / 64 * (isOn()?1:0);
    }

    public void setRedValue(int redValue) {
        this.redValue = redValue;
    }

    public int getGreenValue() {
        return greenValue;
    }

    public int getCalculatedGreenValue() {
        return greenValue * getIntensity() / 64 * (isOn() ? 1 : 0) * 46 / 64;
    }


    public void setGreenValue(int greenValue) {
        this.greenValue = greenValue;
    }

    public int getBlueValue() {
        return blueValue;
    }

    public int getCalculatedBlueValue() {
        return blueValue * getIntensity() / 64 * (isOn() ? 1 : 0) * 33 / 64;
    }

    public void setBlueValue(int blueValue) {
        this.blueValue = blueValue;
    }


    public int calculateColor() {
        return Color.rgb(redValue * 4 * getIntensity() / 64, greenValue * 4 * getIntensity() / 64, blueValue * 4 * getIntensity() / 64);
    }

    public void setColor(int color) {
        int newRed = Color.red(color);
        int newGreen = Color.green(color);
        int newBlue = Color.blue(color);

        int max = newRed > newGreen ? newRed : newGreen;
        max = max > newBlue ? max : newBlue;

        setIntensity(max / 4);


        if (getIntensity() != 0) {
            setRedValue(newRed / 4 * 64 / getIntensity());
            setGreenValue(newGreen / 4 * 64 / getIntensity());
            setBlueValue(newBlue / 4 * 64 / getIntensity());
        }
    }

    public DimmableLightDto toDimmableLightDto() {
        DimmableLightDto dimmableLightDto = new DimmableLightDto();
        dimmableLightDto.setOn(isOn());
        dimmableLightDto.setControllerDto(getControllerDto());
        dimmableLightDto.setIntensity(getIntensity());
        dimmableLightDto.setChannel(getChannel());
        dimmableLightDto.setName(getName());
        return dimmableLightDto;
    }

    public LightDto toLightDto() {
        LightDto lightDto = new LightDto();
        lightDto.setOn(isOn());
        lightDto.setControllerDto(getControllerDto());
        lightDto.setChannel(getChannel());
        lightDto.setName(getName());
        return lightDto;
    }


}
