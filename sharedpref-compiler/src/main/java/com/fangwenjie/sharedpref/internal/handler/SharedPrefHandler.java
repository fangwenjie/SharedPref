package com.fangwenjie.sharedpref.internal.handler;

import androidx.annotation.Nullable;

import com.fangwenjie.sharedpref.Log;
import com.fangwenjie.sharedpref.internal.generator.InterfaceGenerator;
import com.fangwenjie.sharedpref.internal.holder.InterfaceHolder;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;

public class SharedPrefHandler {

    public SharedPrefHandler(Filer filer) {
        this.filer = filer;
    }

    private Filer filer;// 文件管理工具类

    private final HashSet<InterfaceHolder> interfaceHolderList = new HashSet<>();

    /**
     * 通过，包名和类型确定一个SharePref 文件的区域定义
     *
     * @param packageName
     * @param interfaceName
     * @return
     */
    public @Nullable
    InterfaceHolder getInterface(String packageName, String interfaceName) {
        for (InterfaceHolder holder : interfaceHolderList) {
            if (holder.packageName.equals(packageName)
                    && holder.interfaceName.equals(interfaceName)) {
                return holder;
            }
        }
        return null;
    }

    public void addInterfaceHolder(InterfaceHolder holder) {
        interfaceHolderList.add(holder);
    }

    /**
     * 生成文件代码
     */
    public void generatorCode() {
        toLog();

        for (InterfaceHolder holder : interfaceHolderList) {
            if (holder.invalid()) {
                continue;
            }

            String packageName = holder.packageName;
            String className = holder.interfaceName;
            holder.setDefaultValue();
            JavaFile javaFile = InterfaceGenerator.generatorClassFile(holder);
            generatorFile(packageName, className, javaFile);
        }
    }

    /**
     * 创建文件
     *
     * @param packageName
     * @param className
     * @param javaFile
     */
    private void generatorFile(String packageName, String className, JavaFile javaFile) {
        try {
            JavaFileObject jfo = filer.createSourceFile(packageName + "." + className + "SharedPref");
            Writer writer = jfo.openWriter();
            writer.write(javaFile.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void toLog() {
        for (InterfaceHolder holder : interfaceHolderList) {
            Log.d("SharedPrefHandler", holder + "\n");
        }
    }
}