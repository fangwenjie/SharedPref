package com.fangwenjie.sharedpref.example.prefs;

import com.fangwenjie.sharedpref.annotations.sharedpreferences.DefaultString;
import com.fangwenjie.sharedpref.annotations.sharedpreferences.SharedPref;

/**
 * Created by fangwenjie on 2020/4/3
 */
@SharedPref(name = "UserPrefs")
public interface User {

    @DefaultString("123")
    String userName();

    @DefaultString("test")
    String settingName();

    @DefaultString("content")
    String settingContent();
}
