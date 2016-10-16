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
import org.proto.led.dto.ControllerDto;
import org.proto.led.dto.DimmableLightDto;
import org.proto.led.dto.LightDto;
import org.proto.led.dto.RgbLightDto;

import java.util.ArrayList;

/**
 * Created by Predrag Milutinovic on 19.3.2016..
 */
public class Storage {
	public static ArrayList<ControllerDto> loadControllers(Context context) {
		ArrayList<ControllerDto> controllerDtos = new ArrayList<ControllerDto>();
		RgbLightDto rgbLightDto1 = new RgbLightDto();
		rgbLightDto1.setName( "LED 1" );
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
		controllerDto1.setName( "MockedController1" );
		controllerDto1.getLedLights().add( rgbLightDto1 );
		controllerDto1.getLedLights().add( rgbLightDto2 );
		controllerDtos.add( controllerDto1 );
		ControllerDto controllerDto2 = new ControllerDto();
		controllerDto2.setName( "MockedController2" );
		controllerDto2.getLedLights().add( rgbLightDto1 );
		controllerDto2.getLedLights().add( rgbLightDto2 );
		controllerDto2.getLedLights().add( rgbLightDto3 );
		controllerDtos.add( controllerDto2 );
		return controllerDtos;
	}

	public static ArrayList<LightDto> loadLights() {
		ArrayList<LightDto> ledLightDtos = new ArrayList<LightDto>();
		RgbLightDto ledLightDto1 = new RgbLightDto();
		ledLightDto1.setName("Daska gore");
		ledLightDto1.setRedChannel(0);
		ledLightDto1.setGreenChannel(1);
		ledLightDto1.setBlueChannel(2);
		ledLightDto1.setRedValue(50);
		ledLightDto1.setGreenValue(1);
		ledLightDto1.setBlueValue(2);
		RgbLightDto ledLightDto2 = new RgbLightDto();
		ledLightDto2.setName("Daska dole");
		ledLightDto2.setRedChannel(3);
		ledLightDto2.setGreenChannel(4);
		ledLightDto2.setBlueChannel(5);
		ledLightDto2.setRedValue(3);
		ledLightDto2.setGreenValue(40);
		ledLightDto2.setBlueValue(5);
		RgbLightDto ledLightDto3 = new RgbLightDto();
		ledLightDto3.setName("Zavesa");
		ledLightDto3.setRedChannel(6);
		ledLightDto3.setGreenChannel(7);
		ledLightDto3.setBlueChannel(8);
		ledLightDto3.setRedValue(6);
		ledLightDto3.setGreenValue(7);
		ledLightDto3.setBlueValue(30);
		ledLightDtos.add(ledLightDto1);
		ledLightDtos.add(ledLightDto2);
		ledLightDtos.add(ledLightDto3);
		LightDto ledLightDto4 = new LightDto();
		ledLightDto4.setName("Trpezarija 1");
		ledLightDtos.add(ledLightDto4);
		DimmableLightDto ledLightDto5 = new DimmableLightDto();
		ledLightDto5.setName("Trpezarija gore");
		ledLightDto5.setIntensity(32);
		ledLightDtos.add(ledLightDto5);

		return ledLightDtos;
	}

	public static void updateLights(RgbLightDto... selectedLight) {

	}
}
