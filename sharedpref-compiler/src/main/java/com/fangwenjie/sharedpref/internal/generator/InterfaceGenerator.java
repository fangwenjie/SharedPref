package com.fangwenjie.sharedpref.internal.generator;

import android.content.Context;
import android.content.SharedPreferences;

import com.fangwenjie.sharedpref.api.sharedpreferences.EditorHelper;
import com.fangwenjie.sharedpref.api.sharedpreferences.SharedPreferencesHelper;
import com.fangwenjie.sharedpref.internal.CommentHelper;
import com.fangwenjie.sharedpref.internal.holder.InterfaceHolder;
import com.fangwenjie.sharedpref.internal.holder.MethodHolder;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import javax.lang.model.element.Modifier;

/**
 * Created by fangwenjie on 2020/4/20
 */
public class InterfaceGenerator {

    public static JavaFile generatorClassFile(InterfaceHolder holder) {
        String packageName = holder.packageName;
        String className = holder.interfaceName;
        JavaFile javaFile = null;
        try {
            MethodSpec editMethod = MethodSpec.methodBuilder("edit")
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("return new $L.$L(getSharedPreferences())", className + "SharedPref", className + "SharedPrefEditor")
                    .returns(TypeVariableName.get(className + "SharedPref." + className + "SharedPrefEditor"))
                    .build();

            MethodSpec localClassName = MethodSpec.methodBuilder("getLocalClassName")
                    .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                    .addParameter(Context.class, "context")
                    .addStatement("$T packageName = context.getPackageName()", String.class)
                    .addStatement("$T className = context.getClass().getName()", String.class)
                    .addStatement("int packageLen = packageName.length()")
                    .addCode("if (((!className.startsWith(packageName))||(className.length()<= packageLen))||(className.charAt(packageLen)!= '.')) {\n" +
                            "    return className;\n" +
                            "}")
                    .addStatement("return className.substring((packageLen + 1))")
                    .returns(String.class)
                    .build();

            //Editor的静态内部类
            TypeSpec editorType = generatorEditorType(holder);

            //文件结构层
            TypeSpec.Builder mainTypeSpecBuilder = TypeSpec.classBuilder(className + "SharedPref")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .superclass(SharedPreferencesHelper.class)
                    .addMethod(MethodSpec.constructorBuilder()
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(Context.class, "context")
                            .addStatement("super(context.getSharedPreferences(($N(context)+$S), 0))", localClassName, "_" + className + "SharedPref")
                            .build())
                    .addMethod(editMethod)
                    .addMethod(localClassName);


            //生成业务方法
            for (MethodHolder methodHolder : holder.methodHolderList) {
                if (!methodHolder.invalid()) {
                    MethodSpec methodSpec = MethodGenerator.generator(methodHolder);
                    mainTypeSpecBuilder.addMethod(methodSpec);
                }
            }

            mainTypeSpecBuilder.addType(editorType);

            javaFile = JavaFile.builder(packageName, mainTypeSpecBuilder.build())
                    .addFileComment(CommentHelper.fileComment())
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return javaFile;
    }

    private static TypeSpec generatorEditorType(InterfaceHolder holder) {
        String className = holder.interfaceName;

        TypeSpec.Builder editorTypeBuilder = TypeSpec.classBuilder(className + "SharedPrefEditor")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .superclass(ParameterizedTypeName.get(ClassName.get(EditorHelper.class), TypeVariableName.get("" + className + "SharedPref." + className + "SharedPrefEditor")))
                .addMethod(MethodSpec.constructorBuilder()
                        .addParameter(SharedPreferences.class, "sharedPreferences")
                        .addStatement("super(sharedPreferences)")
                        .build());

        //生成业务方法
        for (MethodHolder methodHolder : holder.methodHolderList) {
            if (!methodHolder.invalid()) {
                MethodSpec methodSpec = MethodGenerator.generatorEditor(methodHolder,className);
                editorTypeBuilder.addMethod(methodSpec);
            }
        }

        return editorTypeBuilder.build();
    }

}
