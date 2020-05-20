package com.fangwenjie.sharedpref.internal.generator;

import com.fangwenjie.sharedpref.annotations.sharedpreferences.DefaultBoolean;
import com.fangwenjie.sharedpref.annotations.sharedpreferences.DefaultFloat;
import com.fangwenjie.sharedpref.annotations.sharedpreferences.DefaultInt;
import com.fangwenjie.sharedpref.annotations.sharedpreferences.DefaultLong;
import com.fangwenjie.sharedpref.annotations.sharedpreferences.DefaultString;
import com.fangwenjie.sharedpref.api.sharedpreferences.BooleanPrefEditorField;
import com.fangwenjie.sharedpref.api.sharedpreferences.BooleanPrefField;
import com.fangwenjie.sharedpref.api.sharedpreferences.FloatPrefEditorField;
import com.fangwenjie.sharedpref.api.sharedpreferences.FloatPrefField;
import com.fangwenjie.sharedpref.api.sharedpreferences.IntPrefEditorField;
import com.fangwenjie.sharedpref.api.sharedpreferences.IntPrefField;
import com.fangwenjie.sharedpref.api.sharedpreferences.LongPrefEditorField;
import com.fangwenjie.sharedpref.api.sharedpreferences.LongPrefField;
import com.fangwenjie.sharedpref.api.sharedpreferences.StringPrefEditorField;
import com.fangwenjie.sharedpref.api.sharedpreferences.StringPrefField;
import com.fangwenjie.sharedpref.internal.holder.MethodHolder;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeVariableName;

import javax.lang.model.element.Modifier;

/**
 * Created by fangwenjie on 2020/4/20
 */
public class MethodGenerator {

    /**
     * 业务方法
     *
     * @param holder
     * @return
     */
    public static MethodSpec generator(MethodHolder holder) {
        String methodName = holder.methodName;
        String annotationName = holder.annotation.getSimpleName().toString();
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC);

        if (annotationName.equals(DefaultString.class.getSimpleName())) {
            String defaultValue = holder.getOption() != null ? holder.getOption().getDefaultValue() : "";
            methodBuilder.addStatement("return stringField($S,$S)", methodName, defaultValue)
                    .returns(StringPrefField.class);
        } else if (annotationName.equals(DefaultInt.class.getSimpleName())) {
            String defaultValue = holder.getOption() != null ? holder.getOption().getDefaultValue() : "0";
            methodBuilder.addStatement("return intField($S,$L)", methodName, defaultValue)
                    .returns(IntPrefField.class);
        } else if (annotationName.equals(DefaultBoolean.class.getSimpleName())) {
            String defaultValue = holder.getOption() != null ? holder.getOption().getDefaultValue() : "false";
            methodBuilder.addStatement("return booleanField($S,$L)", methodName, defaultValue)
                    .returns(BooleanPrefField.class);
        } else if (annotationName.equals(DefaultFloat.class.getSimpleName())) {
            String defaultValue = holder.getOption() != null ? holder.getOption().getDefaultValue() : "0F";
            methodBuilder.addStatement("return floatField($S,$L)", methodName, defaultValue)
                    .returns(FloatPrefField.class);
        } else if (annotationName.equals(DefaultLong.class.getSimpleName())) {
            String defaultValue = holder.getOption() != null ? holder.getOption().getDefaultValue() : "0L";
            methodBuilder.addStatement("return longField($S,$L)", methodName, defaultValue)
                    .returns(LongPrefField.class);
        }

        return methodBuilder.build();
    }

    public static MethodSpec generatorEditor(MethodHolder holder, String className) {
        String methodName = holder.methodName;
        String annotationName = holder.annotation.getSimpleName().toString();
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC);

        if (annotationName.equals(DefaultString.class.getSimpleName())) {
            methodBuilder.addStatement("return stringField($S)", methodName)
                    .returns(ParameterizedTypeName.get(ClassName.get(StringPrefEditorField.class), TypeVariableName.get("" + className + "SharedPref." + className + "SharedPrefEditor")));
        } else if (annotationName.equals(DefaultInt.class.getSimpleName())) {
            methodBuilder.addStatement("return intField($S)", methodName)
                    .returns(ParameterizedTypeName.get(ClassName.get(IntPrefEditorField.class), TypeVariableName.get("" + className + "SharedPref." + className + "SharedPrefEditor")));
        } else if (annotationName.equals(DefaultBoolean.class.getSimpleName())) {
            methodBuilder.addStatement("return booleanField($S)", methodName)
                    .returns(ParameterizedTypeName.get(ClassName.get(BooleanPrefEditorField.class), TypeVariableName.get("" + className + "SharedPref." + className + "SharedPrefEditor")));
        } else if (annotationName.equals(DefaultFloat.class.getSimpleName())) {
            methodBuilder.addStatement("return floatField($S)", methodName)
                    .returns(ParameterizedTypeName.get(ClassName.get(FloatPrefEditorField.class), TypeVariableName.get("" + className + "SharedPref." + className + "SharedPrefEditor")));
        } else if (annotationName.equals(DefaultLong.class.getSimpleName())) {
            methodBuilder.addStatement("return longField($S)", methodName)
                    .returns(ParameterizedTypeName.get(ClassName.get(LongPrefEditorField.class), TypeVariableName.get("" + className + "SharedPref." + className + "SharedPrefEditor")));
        }
        return methodBuilder.build();
    }
}
