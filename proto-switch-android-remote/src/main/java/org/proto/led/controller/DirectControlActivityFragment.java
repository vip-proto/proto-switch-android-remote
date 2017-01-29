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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

//import com.larswerkman.holocolorpicker.ColorPicker;
//import com.larswerkman.holocolorpicker.SaturationBar;
//import com.larswerkman.holocolorpicker.ValueBar;

import com.google.gson.Gson;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

import org.proto.led.dto.RgbLightDto;

/**
 * A placeholder fragment containing a simple view.
 */
public class DirectControlActivityFragment extends DialogFragment /*implements ColorPicker.OnColorChangedListener*/  {


    private TextView textView;
    private RgbLightDto rgbLightDto;
    private String TAG = "DirectControlActivityFr";
    private static final String SELECTED_COLOR_KEY = "selected_color";
    private OnFragmentInteractionListener mListener;
    int selectedColor = 0;


    public DirectControlActivityFragment(){

    }



//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_direct_controll, container, false);
//    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach");
    }

    private void setupChildren(View rootView) {


        //-----------------------------------
        textView = (TextView) rootView.findViewById(R.id.textView);
        final ColorPicker picker = (ColorPicker) rootView.findViewById(R.id.picker);
//        SVBar svBar = (SVBar) rootView.findViewById(R.id.svbar);
        final SaturationBar saturationBar = (SaturationBar) rootView.findViewById(R.id.saturationbar);
        saturationBar.setSaturation(1);
        final ValueBar valueBar = (ValueBar) rootView.findViewById(R.id.valuebar);
        saturationBar.setOnSaturationChangedListener(new SaturationBar.OnSaturationChangedListener() {
            @Override
            public void onSaturationChanged(int saturation) {
//                valueBar.setColor(saturationBar.getColor());
//                colorChanged(valueBar.getColor());
//                picker.setNewCenterColor(valueBar.getColor());
                Log.i(TAG, "1 saturation change" +  saturation  +  String.format("#%06X", (0xFFFFFF & saturationBar.getColor())));
            }
        });
        valueBar.setValue(1);
        valueBar.setOnValueChangedListener(new ValueBar.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {
                Log.i(TAG, "2 value change " +  value  +  String.format("#%06X", (0xFFFFFF & value)));
//                colorChanged(value);
//                picker.setNewCenterColor(value);
            }
        });

        Button okButton = (Button) rootView.findViewById(R.id.color_picker_ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorSelected(selectedColor);
            }
        });

//        picker.addSVBar(svBar);
        picker.addSaturationBar(saturationBar);
        picker.addValueBar(valueBar);

//To get the color
//        picker.getColor();


//To set the old selected color u can do it like this
//        picker.setOldCenterColor(picker.getColor());
// adds listener to the colorpicker which is implemented
//in the activity
//        picker.setOnColorChangedListener(this);


//to turn of showing the old color
        picker.setShowOldCenterColor(false);
        picker.setTouchAnywhereOnColorWheelEnabled(true);
        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                Log.i(TAG, "3 color change " +  String.format("#%06X", (0xFFFFFF & color)));
                Log.i(TAG, "color from get: "+  String.format("#%06X", (0xFFFFFF & color)) + " Pic " +String.format("#%06X", (0xFFFFFF & picker.getColor())) + " val " + String.format("#%06X", (0xFFFFFF & valueBar.getColor())) + " sat " + String.format("#%06X", (0xFFFFFF & saturationBar.getColor())));
//                saturationBar.setColor(color);
//                valueBar.setColor(saturationBar.getColor());
//                colorChanged(valueBar.getColor());
//                if (picker.getColor() == valueBar.getColor() ) {
//                    Log.i(TAG, "picker==value");
//                    colorChanged(valueBar.getColor());
//                }else if(valueBar.getColor() == saturationBar.getColor()){
//                    Log.i(TAG, "valueBar==saturationBar");
//                    colorChanged(valueBar.getColor());
//                } else if(picker.getColor() ==saturationBar.getColor() ){
//                    Log.i(TAG, "picker==saturationBar");
//                    colorChanged(picker.getColor());
//                }
                colorChanged(saturationBar.getColor());
                Log.i(TAG,"----------------");
            }
        });
        picker.setOnColorSelectedListener(new ColorPicker.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                colorChanged(color);
            }

        });

        picker.setColor(rgbLightDto.calculateColor());

//adding onChangeListeners to bars
//		opacityBar.setOnOpacityChangeListener(new OpacityBar.OnOpacityChangedListener() {
//												  @Override
//												  public void onOpacityChanged(int opacity) {
//
//												  }
//											  });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View rootView = layoutInflater.inflate(R.layout.fragment_direct_controll, null);

        setupChildren(rootView);

        return new AlertDialog.Builder(getActivity())
                .setView(rootView)
                .create();
    }


    private void colorChanged(int color) {
        selectedColor = color;
        String hexColor = String.format("#%06X", (0xFFFFFF & color));
        Log.i(TAG, "colorChanged: " + hexColor);
        rgbLightDto.setColor(color);
        textView.setText(hexColor + " " + rgbLightDto.getRedValue() + " " + rgbLightDto.getGreenValue() + " " + rgbLightDto.getBlueValue());
        mListener.onLiveUpdateRGB(rgbLightDto);
    }
    private void colorSelected(int color) {
        String hexColor = String.format("#%06X", (0xFFFFFF & color));
        Log.i(TAG, "colorSelected: " + hexColor);
        rgbLightDto.setColor(color);
        textView.setText(hexColor + " " + rgbLightDto.getRedValue() + " " + rgbLightDto.getGreenValue() + " " + rgbLightDto.getBlueValue());
        mListener.onUpdateRGB(rgbLightDto);
        dismiss();
    }

//				valueBar.setOnValueChangeListener(new NumberPicker.OnValueChangeListener …)
//				saturationBar.setOnSaturationChangeListener(new OnSaturationChangeListener …)


//    @Override
//    public void onColorChanged(int color) {
//
//    }

    public static DirectControlActivityFragment newInstance(RgbLightDto rgbLightDto) {
        Bundle args = new Bundle();
        Gson gson = new Gson();
        String json = gson.toJson(rgbLightDto);
        args.putString(SELECTED_COLOR_KEY, json);

        DirectControlActivityFragment dialog = new DirectControlActivityFragment();
        dialog.setArguments(args);
        dialog.setRgbLightDto(rgbLightDto);
        return dialog;
    }

    public void setmListener(OnFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    public void setRgbLightDto(RgbLightDto rgbLightDto) {
        this.rgbLightDto = rgbLightDto;
    }


    public static class Builder {

        private Context context;
        private RgbLightDto selectedColor;
        private String tag;
        private OnFragmentInteractionListener onFragmentInteractionListener;


        public Builder setSelectedColor( RgbLightDto selectedColor) {
            this.selectedColor = selectedColor;
            return this;
        }

        public Builder(@NonNull Context context) {
            this.context = context;
        }

        public Builder setOnFragmentInteractionListener(OnFragmentInteractionListener onFragmentInteractionListener) {
            this.onFragmentInteractionListener = onFragmentInteractionListener;
            return this;
        }

        public Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }

        protected DirectControlActivityFragment build() {
            DirectControlActivityFragment dialog = DirectControlActivityFragment.newInstance(selectedColor);
            dialog.setmListener(onFragmentInteractionListener);
            return dialog;
        }

        public DirectControlActivityFragment show() {
            DirectControlActivityFragment dialog = build();
            dialog.show(resolveContext(context).getFragmentManager(), tag == null ? String.valueOf(System.currentTimeMillis()) : tag);
            return dialog;
        }

        protected Activity resolveContext(Context context) {
            if (context instanceof Activity) {
                return (Activity) context;
            } else if (context instanceof ContextWrapper) {
                return resolveContext(((ContextWrapper) context).getBaseContext());
            }
            return null;
        }
    }

    public interface OnFragmentInteractionListener {
        void onUpdateRGB(RgbLightDto updatedLight);

        void onLiveUpdateRGB(RgbLightDto updatedLight);
    }

}
