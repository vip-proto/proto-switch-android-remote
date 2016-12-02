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

import java.util.ArrayList;

/**
 * Created by Predrag Milutinovic on 19.3.2016..
 */
public class ControllerDto {
	private String                 name;
	private ArrayList<RgbLightDto> ledLights = new ArrayList<RgbLightDto>(  );
	private String ipAddress;
	public String getName() {
		return name;
	}
	private int numberOfChanels;

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	public ArrayList<RgbLightDto> getLedLights() {
		return ledLights;
	}

	public void setLedLights(ArrayList<RgbLightDto> ledLights) {
		this.ledLights = ledLights;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getNumberOfChanels() {
		return numberOfChanels;
	}

	public void setNumberOfChanels(int numberOfChanels) {
		this.numberOfChanels = numberOfChanels;
	}
}
