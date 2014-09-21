/************************************************************************************
 *
 *  Copyright (C) 2013 HTC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 ************************************************************************************/
package com.htc.sample.bleexample.profiles;

import android.content.Context;

import com.htc.android.bluetooth.le.gatt.BleClientProfile;
import com.htc.sample.bleexample.Prefs;
import com.htc.sample.bleexample.constants.BleServices;
import com.htc.sample.bleexample.constants.BleUtils;
import com.htc.sample.bleexample.constants.TiBleConstants;

/**
 * Creates the right profile for a service.
 *
 */
public class ProfileClientFactory {
	
	public static CharSequence[] getServiceIds() {
		return new CharSequence[] {
				BleServices.IMMEDIATE_ALERT,
				BleServices.HEART_RATE,
				TiBleConstants.IRTEMPERATURE_SERV_UUID,
				TiBleConstants.ACCELEROMETER_SERV_UUID,
				TiBleConstants.SIMPLE_KEYS_SERV_UUID,
		};
	}
	
	public static CharSequence[] getServiceLabels() {
		final CharSequence[] services = getServiceIds();
		final CharSequence[] lables = new CharSequence[services.length];
		for(int i = 0; i < services.length; i++) {
			lables[i] = BleUtils.getLabelForUuid((String) services[i]);
		}
		return lables;
	}
	
	public static final BleClientProfile getClient(final Context aContext) {
		final Prefs prefs = new Prefs(aContext);
		final String service = prefs.getService();
		final byte encryption = prefs.getEncryptionLevel();
		
		if (service.equals(BleServices.IMMEDIATE_ALERT)) {
			return new IasProfileClient(aContext, encryption);
		} else if (service.equals(BleServices.HEART_RATE)) {
			return new HrmProfileClient(aContext, encryption);
		} else if (service.equals(TiBleConstants.IRTEMPERATURE_SERV_UUID)) {
			return new TempProfileClient(aContext, encryption);
		} else if (service.equals(TiBleConstants.ACCELEROMETER_SERV_UUID)) {
			return new AccelProfileClient(aContext, encryption);
		} else if (service.equals(TiBleConstants.SIMPLE_KEYS_SERV_UUID)) {
			return new SimpleKeysProfileClient(aContext, encryption);
		}
		
		throw new RuntimeException("No profile defined for that service.");
	}

}
