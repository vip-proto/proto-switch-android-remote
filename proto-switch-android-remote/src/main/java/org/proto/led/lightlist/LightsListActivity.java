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

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import org.proto.led.controller.R;
import org.proto.led.dto.LightDto;
import org.proto.led.dto.RgbLightDto;
import org.proto.led.storage.Storage;

import java.util.ArrayList;

public class LightsListActivity extends AppCompatActivity {


    private ListView listView;
    private LightsListAdapter lightsListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lights_list);
        setupChildren();
        displayData();
    }

    private void setupChildren() {
        listView = (ListView) findViewById(R.id.lights_list_list);


        // Getting adapter by passing xml data ArrayList
        lightsListAdapter =new LightsListAdapter(this);
        listView.setAdapter(lightsListAdapter);
    }

    private void displayData() {
        lightsListAdapter.getData().clear();
        ArrayList<LightDto> rgbLightDtos = Storage.loadLights();
        lightsListAdapter.getData().addAll(rgbLightDtos);

    }

}
