package com.fangwenjie.sharedpref.internal.holder;

import com.fangwenjie.sharedpref.Option;

import javax.lang.model.element.TypeElement;

/**
 * Created by fangwenjie on 2020/4/15
 */
public class MethodHolder extends AnnotationHolder {

    public final String methodName;

    private Option option;


    public MethodHolder(TypeElement annotation, String methodName) {
        super(annotation);
        this.methodName = methodName;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public Option getOption() {
        return option;
    }

    public boolean invalid() {
        return methodName == null || methodName.length() == 0;
    }

    @Override
    public String toString() {
        return "MethodHolder{" +
                "methodName='" + methodName + '\'' +
                '}';
    }
}
