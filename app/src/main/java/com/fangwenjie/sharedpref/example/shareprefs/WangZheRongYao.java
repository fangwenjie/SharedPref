package com.fangwenjie.sharedpref.example.shareprefs;

import com.fangwenjie.sharedpref.annotations.sharedpreferences.DefaultString;
import com.fangwenjie.sharedpref.annotations.sharedpreferences.SharedPref;

/**
 * Created by fangwenjie on 2020/4/21
 */
@SharedPref
public interface WangZheRongYao {
    @DefaultString("鲁班7号")
    String luBan();
}
