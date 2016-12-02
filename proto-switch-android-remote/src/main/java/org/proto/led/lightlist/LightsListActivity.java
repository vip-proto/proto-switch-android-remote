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

import android.app.Fragment;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.proto.led.controller.R;
import org.proto.led.dto.DimmableLightDto;
import org.proto.led.dto.LightDto;
import org.proto.led.dto.RgbLightDto;
import org.proto.led.lightlist.fragment.LightsListFragment;
import org.proto.led.network.WifiController;
import org.proto.led.storage.Storage;

import java.util.ArrayList;

public class LightsListActivity extends AppCompatActivity implements LightsListFragment.OnFragmentInteractionListener {


    private ListView listView;
    private LightsListAdapter lightsListAdapter;

    private static String TAG = "LightsListActivity";
    private LightsListFragment lightsListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lights_list);
        lightsListFragment = (LightsListFragment) getSupportFragmentManager().findFragmentById(R.id.lights_list_fragment);

    }

    @Override
    protected void onResume() {
        super.onResume();
        lightsListFragment.displayData(Storage.loadLights(this));
    }

    @Override
    public void onLightChange(LightDto light) {
        onLightLiveChange(light);
        Storage.updateLights(this, light);
        //logs
        String lightDescription = "";
        if (light instanceof RgbLightDto) {
            RgbLightDto rgbLightDto = (RgbLightDto) light;
            lightDescription = "R" + rgbLightDto.getCalculatedRedValue() + " G" + rgbLightDto.getCalculatedGreenValue() + " B" + rgbLightDto.getCalculatedBlueValue();
        } else if (light instanceof DimmableLightDto) {
            DimmableLightDto dimmableLightDto = (DimmableLightDto) light;
            lightDescription = "D" + dimmableLightDto.getIntensity();
        }
        toast(light.getName() + " " + light.isOn() + " " + lightDescription);

    }

    @Override
    public void onLightLiveChange(LightDto light) {

        WifiController.setLight(this, light);

    }

    private void toast(String s) {
        Log.d(TAG, "toast: " + s);

        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this, s, duration);
        toast.show();
    }
}
