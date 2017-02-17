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
import java.util.List;

/**
 * Created by Predrag on 10.2.2017..
 */

public class GroupDimmableDto extends DimmableLightDto implements GroupLight {
    private List<LightDto> lights = new ArrayList<>();

    @Override
    public List<LightDto> getLights() {
        return lights;
    }

    @Override
    public void setLights(List<LightDto> lights) {
        this.lights = lights;
        if (lights != null) {
            int max = 0;
            for (Light light : lights) {
                if (light instanceof DimmableLightDto) {
                    DimmableLightDto dimmableLightDto = (DimmableLightDto) light;
                    max = Math.max(max, dimmableLightDto.getIntensity());
                }
            }
            super.setIntensity(max);
        }
    }

    @Override
    public void setOn(boolean on) {
        super.setOn(on);
        copyToLights();
    }

    @Override
    public void setIntensity(int intensity) {
        super.setIntensity(intensity);
        copyToLights();
    }

    @Override
    public boolean isOn() {
        boolean on = false;
        if (lights != null) {
            for (Light light : lights) {
                on = on || light.isOn();
            }
        }
        return on;
    }



    private void copyToLights() {
        for (Light light : lights) {
            DimmableLightDto dimmableLightDto = (DimmableLightDto) light;
            dimmableLightDto.setIntensity(super.getIntensity());
            dimmableLightDto.setOn(super.isOn());
        }
    }
}
