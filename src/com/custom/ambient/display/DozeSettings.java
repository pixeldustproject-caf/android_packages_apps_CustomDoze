/*
 * Copyright (C) 2015 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.custom.ambient.display;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.view.MenuItem;

public class DozeSettings extends PreferenceActivity implements OnPreferenceChangeListener {

    private Context mContext;

    private SwitchPreference mAoDPreference;
    private SwitchPreference mAmbientDisplayPreference;
    private SwitchPreference mHandwavePreference;
    private SwitchPreference mPocketPreference;
    private SwitchPreference mBatteryPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.doze_settings);
        mContext = getApplicationContext();

        final ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mAoDPreference =
            (SwitchPreference) findPreference(Utils.AOD_KEY);
        if (Utils.isAoDAvailable(mContext)) {
            mAoDPreference.setChecked(Utils.isAoDEnabled(mContext));
            mAoDPreference.setOnPreferenceChangeListener(this);
        } else {
            getPreferenceScreen().removePreference(mAoDPreference);
        }

        mAmbientDisplayPreference =
            (SwitchPreference) findPreference(Utils.AMBIENT_DISPLAY_KEY);
        mAmbientDisplayPreference.setChecked(Utils.isDozeEnabled(mContext));
        mAmbientDisplayPreference.setOnPreferenceChangeListener(this);

        mHandwavePreference =
            (SwitchPreference) findPreference(Utils.GESTURE_HAND_WAVE_KEY);
        mHandwavePreference.setChecked(Utils.handwaveGestureEnabled(mContext));
        mHandwavePreference.setOnPreferenceChangeListener(this);

        mPocketPreference =
            (SwitchPreference) findPreference(Utils.GESTURE_POCKET_KEY);
        mPocketPreference.setChecked(Utils.pocketGestureEnabled(mContext));
        mPocketPreference.setOnPreferenceChangeListener(this);

        mBatteryPreference =
            (SwitchPreference) findPreference(Utils.AMBIENT_BATTERY_KEY);
        mBatteryPreference.setChecked(Utils.isAmbientBatteryEnabled(mContext));
        mBatteryPreference.setOnPreferenceChangeListener(this);

        if (mAoDPreference == null) return;
        setPrefs();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final String key = preference.getKey();
        final boolean value = (Boolean) newValue;

        if (Utils.AOD_KEY.equals(key)) {
            mAoDPreference.setChecked(value);
            Utils.enableAoD(value, mContext);
            setPrefs();
            return true;
        } else if (Utils.AMBIENT_DISPLAY_KEY.equals(key)) {
            mAmbientDisplayPreference.setChecked(value);
            Utils.enableDoze(value, mContext);
            return true;
        } else if (Utils.GESTURE_HAND_WAVE_KEY.equals(key)) {
            mHandwavePreference.setChecked(value);
            Utils.enableHandWave(value, mContext);
            return true;
        } else if (Utils.GESTURE_POCKET_KEY.equals(key)) {
            mPocketPreference.setChecked(value);
            Utils.enablePocketMode(value, mContext);
            return true;
        } else if (Utils.AMBIENT_BATTERY_KEY.equals(key)) {
            mBatteryPreference.setChecked(value);
            Utils.enableAmbientBattery(value, mContext);
            return true;
        }
        return false;
    }

    private void setPrefs() {
        final boolean aodEnabled = Utils.isAoDEnabled(mContext);
        mAmbientDisplayPreference.setEnabled(!aodEnabled);
        mHandwavePreference.setEnabled(!aodEnabled);
        mPocketPreference.setEnabled(!aodEnabled);
    }
}
