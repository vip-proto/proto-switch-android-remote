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

package org.proto.led.groups;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.proto.led.controller.R;
import org.proto.led.dto.GroupLight;
import org.proto.led.dto.Light;
import org.proto.led.dto.LightDto;
import org.proto.led.lightlist.fragment.LightsListFragment;
import org.proto.led.network.WifiController;
import org.proto.led.storage.Storage;

import java.util.ArrayList;

public class GroupsListActivity extends AppCompatActivity implements LightsListFragment.OnFragmentInteractionListener {

    private LightsListFragment lightsListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_list);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshListFromStorage();
    }

    private LightsListFragment getLightsFragment() {
        return (LightsListFragment) getSupportFragmentManager().findFragmentById(R.id.groups_list_fragment);
    }

    private void refreshListFromStorage() {
        lightsListFragment = getLightsFragment();
        if (lightsListFragment.isAdded()) {
            lightsListFragment.getView().setVisibility(View.VISIBLE);
        }
        ArrayList<GroupLight> groupLights = Storage.loadGroupLights(this);
        ArrayList<Light> lights = new ArrayList<>();
        for (GroupLight groupLight : groupLights) {
            lights.add(groupLight);
        }
        getLightsFragment().displayData(lights);
    }

    @Override
    public void onLightChange(LightDto light) {
        onLightLiveChange(light);
        GroupLight groupLight = (GroupLight) light;
        Storage.updateLights(this, groupLight.getLights().toArray(new LightDto[groupLight.getLights().size()]));

    }


    @Override
    public void onLightLiveChange(LightDto light) {
        GroupLight groupLight = (GroupLight) light;
        LightDto[] lightDtos = groupLight.getLights().toArray(new LightDto[groupLight.getLights().size()]);
        WifiController.setAllLights(this, lightDtos);
    }

    @Override
    public void onDeleteLight(LightDto deletedLight) {
        GroupLight groupLight = (GroupLight) deletedLight;
        Storage.deleteGroupLights(this, groupLight);
        refreshListFromStorage();
    }
}
