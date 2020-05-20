package com.fangwenjie.sharedpref.internal;

import com.fangwenjie.sharedpref.Log;
import com.fangwenjie.sharedpref.Option;
import com.fangwenjie.sharedpref.annotations.sharedpreferences.DefaultBoolean;
import com.fangwenjie.sharedpref.annotations.sharedpreferences.DefaultFloat;
import com.fangwenjie.sharedpref.annotations.sharedpreferences.DefaultInt;
import com.fangwenjie.sharedpref.annotations.sharedpreferences.DefaultLong;
import com.fangwenjie.sharedpref.annotations.sharedpreferences.DefaultString;
import com.fangwenjie.sharedpref.annotations.sharedpreferences.DefaultStringSet;
import com.fangwenjie.sharedpref.annotations.sharedpreferences.SharedPref;
import com.fangwenjie.sharedpref.api.sharedpreferences.AbstractPrefField;
import com.fangwenjie.sharedpref.api.sharedpreferences.BooleanPrefField;
import com.fangwenjie.sharedpref.api.sharedpreferences.FloatPrefField;
import com.fangwenjie.sharedpref.api.sharedpreferences.IntPrefField;
import com.fangwenjie.sharedpref.api.sharedpreferences.LongPrefField;
import com.fangwenjie.sharedpref.api.sharedpreferences.StringPrefField;
import com.fangwenjie.sharedpref.api.sharedpreferences.StringSetPrefField;
import com.fangwenjie.sharedpref.helper.AnnotationsHelper;
import com.fangwenjie.sharedpref.helper.CanonicalNameConstants;
import com.fangwenjie.sharedpref.internal.exception.VersionMismatchException;
import com.fangwenjie.sharedpref.internal.handler.SharedPrefHandler;
import com.fangwenjie.sharedpref.internal.holder.InterfaceHolder;
import com.fangwenjie.sharedpref.internal.holder.MethodHolder;
import com.fangwenjie.sharedpref.internal.process.TimeStats;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created by fangwenjie on 2020/4/2
 */
public class SharedPrefAnnotationsProcessor extends AbstractProcessor {
    private TimeStats timeStats = new TimeStats();

    private Filer filer;// 文件管理工具类
    private Types typeUtils; //类型处理的工具
    private Elements elementUtils;//元素超处理的工具
    private SharedPrefHandler sharedPrefHandler;

    private Map<String, String> options = null;

    public static final class DefaultPrefInfo<T> {
        final Class<? extends Annotation> annotationClass;
        final Class<? extends AbstractPrefField<?>> prefFieldClass;
        final T defaultValue;
        final String fieldHelperMethodName;

        DefaultPrefInfo(Class<? extends Annotation> annotationClass, Class<? extends AbstractPrefField<?>> prefFieldClass, T defaultValue, String fieldHelperMethodName) {
            this.annotationClass = annotationClass;
            this.prefFieldClass = prefFieldClass;
            this.defaultValue = defaultValue;
            this.fieldHelperMethodName = fieldHelperMethodName;
        }

        @Override
        public String toString() {
            return "DefaultPrefInfo{" +
                    "annotationClass=" + annotationClass +
                    ", prefFieldClass=" + prefFieldClass +
                    ", defaultValue=" + defaultValue +
                    ", fieldHelperMethodName='" + fieldHelperMethodName + '\'' +
                    '}';
        }
    }

    private static final Map<String, DefaultPrefInfo<?>> DEFAULT_PREF_INFO = new HashMap<String, DefaultPrefInfo<?>>() {
        {
            put("boolean", new DefaultPrefInfo<>(DefaultBoolean.class, BooleanPrefField.class, false, "booleanField"));
            put("float", new DefaultPrefInfo<>(DefaultFloat.class, FloatPrefField.class, 0f, "floatField"));
            put("int", new DefaultPrefInfo<>(DefaultInt.class, IntPrefField.class, 0, "intField"));
            put("long", new DefaultPrefInfo<>(DefaultLong.class, LongPrefField.class, 0L, "longField"));
            put(CanonicalNameConstants.STRING, new DefaultPrefInfo<>(DefaultString.class, StringPrefField.class, "", "stringField"));
            put(CanonicalNameConstants.STRING_SET, new DefaultPrefInfo<Set<String>>(DefaultStringSet.class, StringSetPrefField.class, null, "stringSetField"));
        }
    };

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        options = processingEnv.getOptions();
        sharedPrefHandler = new SharedPrefHandler(filer);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        timeStats.clear();
        timeStats.start("Whole Processing");

        try {
            checkApiAndProcessorVersions();
            processThrowing(annotations, roundEnvironment);
        } catch (VersionMismatchException e) {
            e.printStackTrace();
        }

        timeStats.stop("Whole Processing");
        timeStats.logStats();
        return true;
    }

    private void checkApiAndProcessorVersions() throws VersionMismatchException {
        //读取Api version
        //读取Compiler version
        //if(api_v not match compiler_v){
        //  throw new VersionMismatchException();
        // }
    }

    private void processThrowing(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        if (nothingToDo(annotations, roundEnvironment)) {
            return;
        }

        for (TypeElement annotation : annotations) {
            handleAnnotationElement(annotation, roundEnvironment);
        }
        sharedPrefHandler.generatorCode();
        Log.d("options", options.toString());
    }

    private boolean nothingToDo(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return roundEnv.processingOver() || annotations.isEmpty();
    }

    private void fetchMethodDefaultValue(Element element, Map<String, Option> optionMap) {
        List<ExecutableElement> executableElementList = AnnotationsHelper.getValidMethods(element);

        for (ExecutableElement method : executableElementList) {
            DefaultPrefInfo<?> prefInfo = DEFAULT_PREF_INFO.get(method.getReturnType().toString());
            Log.d("method:", method.toString());
            Log.d("method return:", method.getReturnType().toString());

            Annotation annotation = method.getAnnotation(prefInfo.annotationClass);
            if (annotation != null) {
                Object value = AnnotationsHelper.extractAnnotationParameter(method, prefInfo.annotationClass.getName(), "value");
                Log.d("value:", value.toString());
                Option option = new Option(method.getSimpleName().toString(), String.valueOf(value));
                optionMap.put(option.getName(), option);
            }
        }
    }

    private void handleAnnotationElement(TypeElement annotation, RoundEnvironment roundEnvironment) {
        Log.d("Start", "start");
        Log.d("annotation:", annotation.toString());


        Set<? extends Element> elementsAnnotation = roundEnvironment.getElementsAnnotatedWith(annotation);
        for (Element element : elementsAnnotation) {

            Log.d("element:", element.toString());

            PackageElement packageElement = elementUtils.getPackageOf(element);
            Log.d("packageElement:", packageElement.toString());

            String packageName = packageElement.toString();
            String interfaceName = null;

            MethodHolder methodHolder = null;

            ElementKind elementKind = element.getKind();
            Map<String, Option> optionMap = new HashMap<>();
            Log.d("annotationKind:", elementKind.toString());
            if (elementKind == ElementKind.INTERFACE) {
                interfaceName = element.getSimpleName().toString();
                fetchMethodDefaultValue(element, optionMap);
            } else if (elementKind == ElementKind.METHOD) {
                methodHolder = new MethodHolder(annotation, element.getSimpleName().toString());
                Element parentElement = element.getEnclosingElement();
                Log.d("ParentElement:", parentElement.toString());
                fetchMethodDefaultValue(element, optionMap);

                if (parentElement.getKind() == ElementKind.INTERFACE) {
                    interfaceName = parentElement.getSimpleName().toString();
                    packageName = elementUtils.getPackageOf(parentElement).toString();
                }
            }

            //构建文件级别的结构
            InterfaceHolder interfaceHolder = sharedPrefHandler.getInterface(packageName, interfaceName);
            if (interfaceHolder == null) {
                interfaceHolder = new InterfaceHolder(packageName, interfaceName);
                sharedPrefHandler.addInterfaceHolder(interfaceHolder);
            }

            //方法级别的结构
            if (methodHolder != null) {
                Log.d("methodHolder", methodHolder.toString());
                interfaceHolder.addMethodHolder(methodHolder);
            }

            interfaceHolder.addOptionMap(optionMap);
        }

        Log.d("End", "end\n");
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> supportTypes = new LinkedHashSet<>();
        supportTypes.add(DefaultBoolean.class.getCanonicalName());
        supportTypes.add(DefaultFloat.class.getCanonicalName());
        supportTypes.add(DefaultInt.class.getCanonicalName());
        supportTypes.add(DefaultLong.class.getCanonicalName());
        supportTypes.add(DefaultString.class.getCanonicalName());
        supportTypes.add(SharedPref.class.getCanonicalName());
        return supportTypes; //将要支持的注解放入其中
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();// 表示支持最新的Java版本
    }
}
