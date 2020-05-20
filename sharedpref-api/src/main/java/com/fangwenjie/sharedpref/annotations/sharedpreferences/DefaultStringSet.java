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
package com.fangwenjie.sharedpref.annotations.sharedpreferences;

import com.fangwenjie.sharedpref.annotations.ResId;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Use on methods in {@link SharedPref} annotated class to specified the default
 * value of this preference.
 * </p>
 * <p>
 * The annotation value must be a <code>String[]</code>. The generated method
 * will return a {@link java.util.Set Set&lt;String&gt;} containing the values
 * of the given array.
 * </p>
 * <p>
 * The key of the preference will be the method name by default. This can be
 * overridden by specifying a string resource with the {@link #keyRes()}
 * parameter.
 * </p>
 * <p>
 * <b>Implementation note:</b>
 * </p>
 * <p>
 * Since {@code SharedPreferences.getStringSet} is only available from API 11,
 * the generated method serializes the {@code Set<String>} into a {@code String}
 * , and persists it with
 * {@link android.content.SharedPreferences.Editor#putString(String, String)
 * SharedPreferences.Editor#putString(String, String)} using API 10 and below.
 * From API 11 and up, the generated method simply uses the native
 * {@link android.content.SharedPreferences SharedPreferences}
 * {@code Set<String>} methods.
 * </p>
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface DefaultStringSet {

	/**
	 * The default value of the preference.
	 *
	 * @return the default value
	 */
	String[] value();

	/**
	 * The R.string.* field which refers to the key of the preference.
	 * 
	 * @return the resource name of the preference key
	 */
	int keyRes() default ResId.DEFAULT_VALUE;
}