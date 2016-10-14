package org.proto.led.dto;

import java.util.ArrayList;

/**
 * Created by Predrag Milutinovic on 19.3.2016..
 */
public class ControllerDto {
	private String                 name;
	private ArrayList<LedLightDto> ledLights = new ArrayList<LedLightDto>(  );
	private String ipAddress;
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

	public ArrayList<LedLightDto> getLedLights() {
		return ledLights;
	}

	public void setLedLights(ArrayList<LedLightDto> ledLights) {
		this.ledLights = ledLights;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
}
