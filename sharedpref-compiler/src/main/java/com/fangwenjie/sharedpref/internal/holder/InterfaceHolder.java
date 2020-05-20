package com.fangwenjie.sharedpref.internal.holder;

import com.fangwenjie.sharedpref.Option;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fangwenjie on 2020/4/15
 */
public class InterfaceHolder {
    public final String packageName;
    public final String interfaceName;

    public final List<MethodHolder> methodHolderList = new ArrayList<>();

    public final Map<String, Option> optionMap = new HashMap<>();

    public InterfaceHolder(String packageName, String interfaceName) {
        this.packageName = packageName;
        this.interfaceName = interfaceName;
    }

    public void addMethodHolder(MethodHolder holder) {
        methodHolderList.add(holder);
    }

    public void addOptionMap(Map<String,Option> optionMap){
        this.optionMap.putAll(optionMap);
    }

    public void setDefaultValue(){
        for (MethodHolder methodHolder:methodHolderList){
            String methodName = methodHolder.methodName;
            Option option = optionMap.get(methodName);
            if (option != null){
                methodHolder.setOption(option);
            }
        }
    }

    /**
     * 无效Holder
     *
     * @return
     */
    public boolean invalid() {
        return interfaceName == null || packageName == null;
    }

    @Override
    public String toString() {
        return "InterfaceHolder{" +
                "packageName='" + packageName + '\'' +
                ", interfaceName='" + interfaceName + '\'' +
                ", methodHolderList=" + methodHolderList +
                '}';
    }
}
