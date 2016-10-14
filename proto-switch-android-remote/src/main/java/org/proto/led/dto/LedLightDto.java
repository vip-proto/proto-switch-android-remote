package org.proto.led.dto;

/**
 * Created by Predrag Milutinovic on 20.3.2016..
 */
public class LedLightDto {

    private ControllerDto controllerDto;
    private String name;
    private boolean on;
    private int redChannel;
    private int greenChannel;
    private int blueChannel;
    private int redValue;
    private int greenValue;
    private int blueValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public ControllerDto getControllerDto() {
        return controllerDto;
    }

    public void setControllerDto(ControllerDto controllerDto) {
        this.controllerDto = controllerDto;
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

    public void setRedValue(int redValue) {
        this.redValue = redValue;
    }

    public int getGreenValue() {
        return greenValue;
    }

    public void setGreenValue(int greenValue) {
        this.greenValue = greenValue;
    }

    public int getBlueValue() {
        return blueValue;
    }

    public void setBlueValue(int blueValue) {
        this.blueValue = blueValue;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }
}
