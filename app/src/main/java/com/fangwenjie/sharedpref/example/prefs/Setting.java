package com.fangwenjie.sharedpref.example.prefs;

import com.fangwenjie.sharedpref.annotations.sharedpreferences.DefaultBoolean;
import com.fangwenjie.sharedpref.annotations.sharedpreferences.DefaultString;
import com.fangwenjie.sharedpref.annotations.sharedpreferences.SharedPref;

/**
 * Created by fangwenjie on 2020/4/14
 */
@SharedPref(name = "SettingPrefs")
public interface Setting {

    @DefaultString(value = "fangwenjie321")
    String settingContent();

    @DefaultString("18 years")
    String age();

    @DefaultBoolean(true)
    boolean activeFlag();
}
