package com.fangwenjie.sharedpref.internal.holder;

import javax.lang.model.element.PackageElement;

/**
 * Created by fangwenjie on 2020/4/15
 */
public class PackageHolder {
    private final PackageElement packageElement;

    public PackageHolder(PackageElement packageElement) {
        this.packageElement = packageElement;
    }

    @Override
    public String toString() {
        return "PackageHolder{" +
                "packageElement=" + packageElement +
                '}';
    }
}
