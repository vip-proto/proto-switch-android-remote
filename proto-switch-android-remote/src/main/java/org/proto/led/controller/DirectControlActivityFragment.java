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

package org.proto.led.controller;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

import org.proto.led.dto.ControllerDto;
import org.proto.led.dto.RgbLightDto;
import org.proto.led.network.MakeUDPRequest;
import org.proto.led.storage.Storage;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DirectControlActivityFragment extends Fragment implements ColorPicker.OnColorChangedListener {

    private Spinner selectControllerSpinner;
    private Spinner selectLightSpinner;
    private ArrayAdapter<ControllerDto> controllerArrayAdapter;
    private ArrayAdapter<RgbLightDto> lightArrayAdapter;
    private TextView textView;
    private LinearLayout selectorHolder;
    private LinearLayout offHolder;
    private RgbLightDto selectedLight;
    private String TAG = "DirectControlActivityFr";

    public DirectControlActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_direct_controll, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupChildren();
        displayData();
    }

    private void displayData() {

    }

    private void setupChildren() {
        selectorHolder = (LinearLayout) getActivity().findViewById(R.id.selectorHolder);
        offHolder = (LinearLayout) getActivity().findViewById(R.id.offHolder);
        setupSelectorButtons();
        setupOffButtons();
//        setupControllerSpinner();
//        setupLightSpinner();
        //-----------------------------------
        textView = (TextView) getActivity().findViewById(R.id.textView);
        final ColorPicker picker = (ColorPicker) getActivity().findViewById(R.id.picker);
//        SVBar svBar = (SVBar) getActivity().findViewById(R.id.svbar);
        final SaturationBar saturationBar = (SaturationBar) getActivity().findViewById(R.id.saturationbar);
        saturationBar.setSaturation(1);
        final ValueBar valueBar = (ValueBar) getActivity().findViewById(R.id.valuebar);
        saturationBar.setOnSaturationChangedListener(new SaturationBar.OnSaturationChangedListener() {
            @Override
            public void onSaturationChanged(int saturation) {
                valueBar.setColor(saturationBar.getColor());
//                picker.setNewCenterColor(valueBar.getColor());
                colorChanged(valueBar.getColor());
            }
        });
        valueBar.setValue(1);
        valueBar.setOnValueChangedListener(new ValueBar.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {
                colorChanged(value);
//                picker.setNewCenterColor(value);
            }
        });
//        picker.addSVBar(svBar);
//        picker.addSaturationBar(saturationBar);
//        picker.addValueBar(valueBar);

//To get the color
//        picker.getColor();


//To set the old selected color u can do it like this
//        picker.setOldCenterColor(picker.getColor());
// adds listener to the colorpicker which is implemented
//in the activity
        picker.setOnColorChangedListener(this);


//to turn of showing the old color
        picker.setShowOldCenterColor(false);
        picker.setTouchAnywhereOnColorWheelEnabled(true);
        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                saturationBar.setColor(color);
                valueBar.setColor(saturationBar.getColor());
                colorChanged(valueBar.getColor());
//                if (picker.getColor() == valueBar.getColor() ) {
//                    colorChanged(valueBar.getColor());
//                }else if(valueBar.getColor() == saturationBar.getColor()){
//                    colorChanged(valueBar.getColor());
//                } else if(picker.getColor() ==saturationBar.getColor() ){
//                    colorChanged(valueBar.getColor());
//                }
                Log.i(TAG, "color from get: "+  String.format("#%06X", (0xFFFFFF & color)) + " " +String.format("#%06X", (0xFFFFFF & picker.getColor())) + " " + String.format("#%06X", (0xFFFFFF & valueBar.getColor())) + " " + String.format("#%06X", (0xFFFFFF & saturationBar.getColor())));
            }
        });
//        picker.setOnColorSelectedListener(new ColorPicker.OnColorSelectedListener() {
//            @Override
//            public void onColorSelected(int color) {
//                colorChanged(color);
//            }
//        });

//adding onChangeListeners to bars
//		opacityBar.setOnOpacityChangeListener(new OpacityBar.OnOpacityChangedListener() {
//												  @Override
//												  public void onOpacityChanged(int opacity) {
//
//												  }
//											  });
    }

    private void colorChanged(int color) {
        String hexColor = String.format("#%06X", (0xFFFFFF & color));
        Log.i(TAG, "colorChanged: " + hexColor);
        int redInt = Math.round(((0xFF0000 & color) >> 16) / 4);
        int greenInt = Math.round(((0x00FF00 & color) >> 8) / 4);
        int blueInt = Math.round((0x0000FF & color) / 4);
        textView.setText(hexColor + " " + redInt + " " + greenInt + " " + blueInt);
        updateColorOnSelectedLight(redInt, greenInt, blueInt);
    }

    private void setupOffButtons() {
        offHolder.removeAllViews();
        ImageButton allButton = new ImageButton(getActivity());
//        allButton.setText("");
        allButton.setImageResource(R.mipmap.off);
        allButton.setBackgroundColor(Color.TRANSPARENT);
        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnOff(Storage.loadLights().toArray(new RgbLightDto[]{}));
            }
        });
        allButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        offHolder.addView(allButton);

        ArrayList<RgbLightDto> ledLightDtos = Storage.loadLights();
        for (final RgbLightDto light : ledLightDtos) {
            ImageButton lightButton = new ImageButton(getActivity());
//            lightButton.setText("");
            lightButton.setImageResource(R.mipmap.off);
            lightButton.setBackgroundColor(Color.TRANSPARENT);
            lightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    turnOff(light);
                }
            });
            lightButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            offHolder.addView(lightButton);
        }
    }

    private void turnOff(RgbLightDto... ledLightDtos) {
        updateColor(0, 0, 0, ledLightDtos);
    }

    private void updateColorOnSelectedLight(int redInt, int greenInt, int blueInt) {
        if (selectedLight != null) {
            updateColor(redInt, greenInt, blueInt, selectedLight);
        } else {
            updateColor(redInt, greenInt, blueInt, Storage.loadLights().toArray(new RgbLightDto[]{}));
        }
    }


    private void updateColor(int redInt, int greenInt, int blueInt, RgbLightDto... selectedLight) {
        for (int i = 0; i < selectedLight.length; i++) {
            RgbLightDto lightDto = selectedLight[i];
            updateSingleLight(lightDto, redInt, greenInt, blueInt);
        }
        sendCommandToController(selectedLight);
        Storage.updateLights(selectedLight);
    }

    private void sendCommandToController(RgbLightDto... selectedLight) {
        // reset
        byte[] command = new byte[7];
        command[0] = (byte) 128;

//        int[] values = new int[9];
        for (int i = 1; i < command.length; i++) {
            command[i] = 64;

        }
        for (RgbLightDto ledDto : selectedLight) {
            command[ledDto.getRedChannel() + 1] = (byte) ledDto.getRedValue();
            command[ledDto.getGreenChannel() + 1] = (byte) ledDto.getGreenValue();
            command[ledDto.getBlueChannel() + 1] = (byte) ledDto.getBlueValue();
        }
        String ip = "192.168.1.221";
//        String ip = "192.168.1.51";
//        Log.i(TAG, "command " + command);
        new MakeUDPRequest(ip, 6000, command, getActivity()).execute();
    }


    private void updateSingleLight(RgbLightDto selectedLight, int redInt, int greenInt, int blueInt) {
        selectedLight.setRedValue(redInt);
        selectedLight.setGreenValue(greenInt);
        selectedLight.setBlueValue(blueInt);

    }


    private void setupSelectorButtons() {
        selectorHolder.removeAllViews();
        final ImageButton allButton = new ImageButton(getActivity());
//        allButton.setTextOff("");
//        allButton.setTextOn("");
        allButton.setImageResource(R.drawable.toggle_selector);
        allButton.setBackgroundColor(Color.TRANSPARENT);

//        allButton.set;
//        android:layout_centerHorizontal="true"

        allButton.setSelected(true);
        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allButton.setSelected(true);
                unselectAllButtonsExcept(v);
                selectedLight = null;
            }
        });
        allButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        selectorHolder.addView(allButton);

        ArrayList<RgbLightDto> ledLightDtos = Storage.loadLights();
        for (final RgbLightDto light : ledLightDtos) {
            final ImageButton lightButton = new ImageButton(getActivity());
//            lightButton.setText("");
//            lightButton.setTextOff("");
//            lightButton.setTextOn("");
            lightButton.setImageResource(R.drawable.toggle_selector);
            lightButton.setBackgroundColor(Color.TRANSPARENT);
            lightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lightButton.setSelected(true);
                    unselectAllButtonsExcept(v);
                    selectedLight = light;

                }
            });
            lightButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            selectorHolder.addView(lightButton);
        }
    }

    private void unselectAllButtonsExcept(View clickedButton) {
        for (int i = 0; i < selectorHolder.getChildCount(); i++) {

            View childAtI = selectorHolder.getChildAt(i);
            if (!clickedButton.equals(childAtI)) {
                ImageButton imageButton = (ImageButton) childAtI;
                imageButton.setSelected(false);
            }
        }
    }
//				valueBar.setOnValueChangeListener(new NumberPicker.OnValueChangeListener …)
//				saturationBar.setOnSaturationChangeListener(new OnSaturationChangeListener …)

//    private void setupControllerSpinner() {
//        selectControllerSpinner = (Spinner) getActivity().findViewById(R.id.color_setter_select_controller_spinner);
//        selectControllerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ControllerDto selectedController = (ControllerDto) parent.getItemAtPosition(position);
//                lightArrayAdapter.clear();
//                lightArrayAdapter.addAll(selectedController.getLedLights());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                lightArrayAdapter.clear();
//            }
//        });
//
//        if (controllerArrayAdapter == null) {
//            int item = android.R.layout.simple_spinner_item;
//
//            controllerArrayAdapter = new ArrayAdapter<ControllerDto>(getActivity(), item);
//            controllerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//            controllerArrayAdapter.addAll(Storage.loadControllers(getActivity()));
//            selectControllerSpinner.setAdapter(controllerArrayAdapter);
//        }
//    }
//
//    private void setupLightSpinner() {
//        selectLightSpinner = (Spinner) getActivity().findViewById(R.id.color_setter_select_light_spinner);
//        selectLightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        if (lightArrayAdapter == null) {
//            int item = android.R.layout.simple_spinner_item;
//
//            lightArrayAdapter = new ArrayAdapter<LedLightDto>(getActivity(), item);
//            lightArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//            selectLightSpinner.setAdapter(lightArrayAdapter);
//        }
//    }

    @Override
    public void onColorChanged(int color) {

    }
}
