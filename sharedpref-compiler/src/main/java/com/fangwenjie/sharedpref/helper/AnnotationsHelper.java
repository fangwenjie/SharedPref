package com.fangwenjie.sharedpref.helper;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.util.ElementFilter;

/**
 * Created by fangwenjie on 2020/4/21
 */
public class AnnotationsHelper {

    @SuppressWarnings("unchecked")
    public static  <T> T extractAnnotationParameter(Element element, String annotationName, String methodName) {
        Annotation annotation;
        try {
            annotation = element.getAnnotation((Class<? extends Annotation>) Class.forName(annotationName));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not load annotation class " + annotationName, e);
        }
        Method method;
        try {
            method = annotation.getClass().getMethod(methodName);
            return (T) method.invoke(annotation);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof MirroredTypeException) {
                MirroredTypeException cause = (MirroredTypeException) e.getCause();
                return (T) cause.getTypeMirror();
            } else {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<ExecutableElement> getValidMethods(Element element) {
        List<? extends Element> members = element.getEnclosedElements();
        List<ExecutableElement> methods = ElementFilter.methodsIn(members);
        List<ExecutableElement> validMethods = new ArrayList<>();
        for (ExecutableElement method : methods) {
            validMethods.add(method);
        }
        return validMethods;
    }
}
