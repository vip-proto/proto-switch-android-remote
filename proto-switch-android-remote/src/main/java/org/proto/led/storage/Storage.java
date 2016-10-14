package org.proto.led.storage;

import android.content.Context;
import org.proto.led.dto.ControllerDto;
import org.proto.led.dto.LedLightDto;

import java.util.ArrayList;

/**
 * Created by Predrag Milutinovic on 19.3.2016..
 */
public class Storage {
	public static ArrayList<ControllerDto> loadControllers(Context context) {
		ArrayList<ControllerDto> controllerDtos = new ArrayList<ControllerDto>();
		LedLightDto ledLightDto1 = new LedLightDto();
		ledLightDto1.setName( "LED 1" );
		ledLightDto1.setRedChannel(0);
		ledLightDto1.setGreenChannel(1);
		ledLightDto1.setBlueChannel(2);
		LedLightDto ledLightDto2 = new LedLightDto();
		ledLightDto2.setName("LED 2");
		ledLightDto2.setRedChannel(3);
		ledLightDto2.setGreenChannel(4);
		ledLightDto2.setBlueChannel(5);
		LedLightDto ledLightDto3 = new LedLightDto();
		ledLightDto3.setName("LED 3");
		ledLightDto3.setRedChannel(6);
		ledLightDto3.setGreenChannel(7);
		ledLightDto3.setBlueChannel(8);
		ControllerDto controllerDto1 = new ControllerDto();
		controllerDto1.setName( "MockedController1" );
		controllerDto1.getLedLights().add( ledLightDto1 );
		controllerDto1.getLedLights().add( ledLightDto2 );
		controllerDtos.add( controllerDto1 );
		ControllerDto controllerDto2 = new ControllerDto();
		controllerDto2.setName( "MockedController2" );
		controllerDto2.getLedLights().add( ledLightDto1 );
		controllerDto2.getLedLights().add( ledLightDto2 );
		controllerDto2.getLedLights().add( ledLightDto3 );
		controllerDtos.add( controllerDto2 );
		return controllerDtos;
	}

	public static ArrayList<LedLightDto> loadLights() {
		ArrayList<LedLightDto> ledLightDtos = new ArrayList<LedLightDto>();
		LedLightDto ledLightDto1 = new LedLightDto();
		ledLightDto1.setName("LED 1");
		ledLightDto1.setRedChannel(0);
		ledLightDto1.setGreenChannel(1);
		ledLightDto1.setBlueChannel(2);
		LedLightDto ledLightDto2 = new LedLightDto();
		ledLightDto2.setName("LED 2");
		ledLightDto2.setRedChannel(3);
		ledLightDto2.setGreenChannel(4);
		ledLightDto2.setBlueChannel(5);
		LedLightDto ledLightDto3 = new LedLightDto();
		ledLightDto3.setName("LED 3");
		ledLightDto3.setRedChannel(6);
		ledLightDto3.setGreenChannel(7);
		ledLightDto3.setBlueChannel(8);
		ledLightDtos.add(ledLightDto1);
		ledLightDtos.add(ledLightDto2);
//		ledLightDtos.add(ledLightDto3);
		return ledLightDtos;
	}

	public static void updateLights(LedLightDto... selectedLight) {

	}
}
