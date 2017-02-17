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

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.proto.led.controller.R;
import org.proto.led.dto.DimmableLightDto;
import org.proto.led.dto.GroupDimmableDto;
import org.proto.led.dto.GroupLight;
import org.proto.led.dto.GroupLightDto;
import org.proto.led.dto.GroupRgbLightDto;
import org.proto.led.dto.Light;
import org.proto.led.dto.LightDto;
import org.proto.led.dto.RgbLightDto;
import org.proto.led.groups.GroupsListActivity;
import org.proto.led.lightlist.fragment.LightsListFragment;
import org.proto.led.network.WiFiControllerService;
import org.proto.led.network.WifiController;
import org.proto.led.storage.Storage;

import java.util.ArrayList;

public class LightsListActivity extends AppCompatActivity implements LightsListFragment.OnFragmentInteractionListener {


    private ListView listView;
    private LightsListAdapter lightsListAdapter;

    private static String TAG = "LightsListActivity";
    private LightsListFragment lightsListFragment;
    private BroadcastReceiver receiver;
    private ArrayList<Integer> mSelectedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lights_list);
        lightsListFragment = getLightsFragment();
        Intent intent = new Intent(this, WiFiControllerService.class);
        startService(intent);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(WiFiControllerService.INTENT_LIGHTS_UPDATED));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            refreshListFromStorage();
            Log.d("receiver", "Got message: ");
        }
    };

    private void refreshListFromStorage() {
        lightsListFragment = getLightsFragment();
        if (lightsListFragment.isAdded()) {
            lightsListFragment.getView().setVisibility(View.VISIBLE);
        }
        getLightsFragment().displayData(Storage.loadLights(this));
    }

    private LightsListFragment getLightsFragment() {
        return (LightsListFragment) getSupportFragmentManager().findFragmentById(R.id.lights_list_fragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshListFromStorage();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lights, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_lights_create_group:
                alertMultipleChoiceItems();
                return true;
            case R.id.menu_lights_discover:
                WifiController.startDiscovery(this);
                return true;
            case R.id.menu_lights_groups:
                displayGroups();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
//        toast(light.getName() + " " + light.isOn() + " " + lightDescription);

    }

    @Override
    public void onLightLiveChange(LightDto light) {

        WifiController.setLight(this, light);

    }

    @Override
    public void onDeleteLight(LightDto deletedLight) {
        Storage.deleteLight(this, deletedLight);
        refreshListFromStorage();
    }

    private void toast(String s) {
        Log.d(TAG, "toast: " + s);

        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this, s, duration);
        toast.show();
    }


    private void displayGroups() {
        Intent intent = new Intent(this, GroupsListActivity.class);
        startActivity(intent);
    }

    private String[] getLightNames() {
        ArrayList<Light> lightDtos = Storage.loadLights(this);
        String[] strings = new String[lightDtos.size()];
        for (int i = 0; i < lightDtos.size(); i++) {
            strings[i] = lightDtos.get(i).getName();
        }
        return strings;
    }

    public void alertMultipleChoiceItems() {

        // where we will store or remove selected items
        mSelectedItems = new ArrayList<Integer>();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // set the dialog title
        builder.setTitle("Choose One or More")

                // specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive call backs when items are selected
                // R.array.choices were set in the resources res/values/strings.xml
                .setMultiChoiceItems(getLightNames(), null, new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        if (isChecked) {
                            // if the user checked the item, add it to the selected items
                            mSelectedItems.add(which);
                        } else if (mSelectedItems.contains(which)) {
                            // else if the item is already in the array, remove it
                            mSelectedItems.remove(Integer.valueOf(which));
                        }

                        // you can also add other codes here,
                        // for example a tool tip that gives user an idea of what he is selecting
                        // showToast("Just an example description.");
                    }

                })

                // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        // user clicked OK, so save the mSelectedItems results somewhere
                        // here we are trying to retrieve the selected items indices
                        String selectedIndex = "";
                        for (Integer i : mSelectedItems) {
                            selectedIndex += i + ", ";
                        }
                        alertEditTextKeyboardShown();
                        toast("Selected index: " + selectedIndex);

                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // removes the AlertDialog in the screen
                    }
                })

                .show();

    }

    public void alertEditTextKeyboardShown() {

        // creating the EditText widget programatically
        final EditText editText = new EditText(this);

        // create the AlertDialog as final
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Group name")
                .setTitle("Group")
                .setView(editText)

                // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        createAndStoreGroup(editText.getText().toString());
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // removes the AlertDialog in the screen
                    }
                })
                .create();

        // set the focus change listener of the EditText
        // this part will make the soft keyboard automaticall visible
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

        dialog.show();

    }

    private void createAndStoreGroup(String groupName) {
        ArrayList<Light> lightDtos = Storage.loadLights(this);
        ArrayList<LightDto> selectedLights = new ArrayList<>();

        int lightCount = 0;
        int dimmableCount = 0;
        for (Integer mSelectedItem : mSelectedItems) {
            Light lightDto = lightDtos.get(mSelectedItem);
            selectedLights.add((LightDto) lightDto);
            if (!(lightDto instanceof RgbLightDto)) {
                if (lightDto instanceof DimmableLightDto) {
                    dimmableCount++;
                } else {
                    lightCount++;
                }
            }
        }

        GroupLight groupLight;
        if (lightCount > 0) {
            GroupLightDto groupLight1 = new GroupLightDto();
            groupLight1.setName(groupName);
            groupLight = groupLight1;
        } else if (dimmableCount > 0) {
            GroupDimmableDto groupDimmableDto = new GroupDimmableDto();
            groupDimmableDto.setName(groupName);
            groupLight = groupDimmableDto;
        } else {
            GroupRgbLightDto groupRgbLightDto = new GroupRgbLightDto();
            groupRgbLightDto.setName(groupName);
            groupLight = groupRgbLightDto;
        }


        groupLight.setLights(selectedLights);
        Storage.updateGroupLights(this, groupLight);
        ArrayList<GroupLight> groupLights = Storage.loadGroupLights(this);
        Log.i(TAG, "Loaded groups " + groupLights);
    }


}
