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

package org.proto.led.lightlist;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;



import org.proto.led.controller.R;
import org.proto.led.dto.DimmableLightDto;
import org.proto.led.dto.LightDto;
import org.proto.led.dto.RgbLightDto;

import java.util.ArrayList;

import es.dmoral.coloromatic.ColorOMaticDialog;
import es.dmoral.coloromatic.IndicatorMode;
import es.dmoral.coloromatic.OnColorSelectedListener;
import es.dmoral.coloromatic.colormode.ColorMode;

import static android.R.attr.duration;

/**
 * Created by Predrag on 16.10.2016..
 */

public class LightsListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<LightDto> data = new ArrayList<LightDto>();
    private static LayoutInflater inflater=null;
    public LightsListAdapter(Activity a) {
        activity = a;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.light_line, null);

        ToggleButton toggleButton = (ToggleButton) vi.findViewById(R.id.light_line_toggle_button);
        SeekBar seekBar = (SeekBar) vi.findViewById(R.id.light_line_seek_bar);
        final Button colorButton = (Button)vi.findViewById(R.id.light_line_color_button);
        final LightDto light = (LightDto) getItem(position);
        // Setting all values in listview
        toggleButton.setText(light.getName());
        toggleButton.setTextOn(light.getName());
        toggleButton.setTextOff(light.getName());
        toggleButton.setChecked(light.isOn());
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToggleButton button = (ToggleButton) v;
                if(button.isChecked()!=light.isOn()){
                    light.setOn(button.isChecked());
                    onUpdate(light);
                }
            }
        });
//        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                light.setOn(isChecked);
//
//            }
//        });
        if(light instanceof DimmableLightDto){
            final DimmableLightDto dimmableLightDto = (DimmableLightDto) light;
            seekBar.setProgress(dimmableLightDto.getIntensity());
            seekBar.setVisibility(View.VISIBLE);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(fromUser) {
                        dimmableLightDto.setIntensity(progress);
                        if (dimmableLightDto instanceof RgbLightDto) {
                            RgbLightDto rgbLightDto = (RgbLightDto) dimmableLightDto;
                            setColorOnButton(colorButton, rgbLightDto.calculateColor());
                        }
                        onLiveUpdate(dimmableLightDto);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    dimmableLightDto.setIntensity(seekBar.getProgress());
                    onUpdate(dimmableLightDto);
                    notifyDataSetChanged();
                }
            });
        } else {
            seekBar.setVisibility(View.INVISIBLE);
        }
        if(light instanceof RgbLightDto){
            final RgbLightDto rgbLightDto = (RgbLightDto) light;
            colorButton.setVisibility(View.VISIBLE);
            colorButton.setBackgroundResource(R.drawable.tags_rounded_corners);
            colorButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getColor(rgbLightDto.calculateColor(), rgbLightDto);
                }
            });
            setColorOnButton(colorButton, rgbLightDto.calculateColor());
        } else {
            colorButton.setVisibility(View.INVISIBLE);
        }

        return vi;
    }

    private void setColorOnButton(Button colorButton, int color){
        GradientDrawable drawable = (GradientDrawable) colorButton.getBackground();
        drawable.setColor(color);
    }

    public ArrayList<LightDto> getData() {
        return data;
    }

    private void getColor(int color, final RgbLightDto rgbLightDto){
        int[] colors = new int[4];

         new ColorOMaticDialog.Builder()
                .initialColor(color)
                .colorMode(ColorMode.RGB) // RGB, ARGB, HVS
                .indicatorMode(IndicatorMode.DECIMAL) // HEX or DECIMAL; Note that using HSV with IndicatorMode.HEX is not recommended
                .onColorSelected(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(@ColorInt int i) {
//                        rgbLightDto.setColor(i);
//                        onLiveUpdate(rgbLightDto);

                        rgbLightDto.setColor(i);
                        notifyDataSetChanged();
                        onUpdate(rgbLightDto);
                    }
                })
                .showColorIndicator(true) // Default false, choose to show text indicator showing the current color in HEX or DEC (see images) or not
                .create()
                .show(((AppCompatActivity) activity).getSupportFragmentManager(), "ColorOMaticDialog");
//        ColorPickerDialogBuilder
//                .with(activity)
//                .setTitle("Choose color")
//                .initialColor(color)
//                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
//                .density(12)
//                .lightnessSliderOnly()
//                .setOnColorSelectedListener(new OnColorSelectedListener() {
//                    @Override
//                    public void onColorSelected(int selectedColor) {
//                        rgbLightDto.setColor(selectedColor);
//                        onLiveUpdate(rgbLightDto);
//                    }
//                })
//                .setPositiveButton("ok", new ColorPickerClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
//                        rgbLightDto.setColor(selectedColor);
//                        notifyDataSetChanged();
//                        onUpdate(rgbLightDto);
//                    }
//                })
//                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .build()
//                .show();
    }


    public void onUpdate(LightDto updatedLight){


    }

    public void onLiveUpdate(LightDto updatedLight){


    }
}
