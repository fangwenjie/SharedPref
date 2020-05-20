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
package com.fangwenjie.sharedpref;

public class Option {

	private final String name;
	private final String defaultValue;

	public Option(String name, String defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}

	public String getName() {
		return name;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public String toString() {
		return "Option{" +
				"name='" + name + '\'' +
				", defaultValue='" + defaultValue + '\'' +
				'}';
	}
}