/**
 * Copyright (C) 2010-2016 eBusiness Information, Excilys Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.fangwenjie.sharedpref.api.sharedpreferences;

import android.content.SharedPreferences;

import java.util.Set;

public final class StringSetPrefField extends AbstractPrefField<Set<String>> {

	StringSetPrefField(SharedPreferences sharedPreferences, String key, Set<String> defaultValue) {
		super(sharedPreferences, key, defaultValue);
	}

	@Override
	public Set<String> getOr(Set<String> defaultValue) {
		return SharedPreferencesCompat.getStringSet(sharedPreferences, key, defaultValue);
	}

	@Override
	protected void putInternal(Set<String> value) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		SharedPreferencesCompat.putStringSet(editor, key, value);
		apply(editor);
	}

}
