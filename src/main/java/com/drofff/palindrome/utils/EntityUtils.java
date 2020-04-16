package com.drofff.palindrome.utils;

import com.drofff.palindrome.annotation.NonEditable;
import com.drofff.palindrome.document.Entity;
import org.springframework.data.annotation.Id;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import static com.drofff.palindrome.utils.ReflectionUtils.getFieldValueFromObject;
import static com.drofff.palindrome.utils.ReflectionUtils.setFieldValueIntoObject;
import static com.drofff.palindrome.utils.ValidationUtils.validateAllAreNotNull;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class EntityUtils {

    private EntityUtils() {}

    public static <T extends Entity> T copyNonEditableFields(T from, T to) {
        validateAllAreNotNull("Coping operands should both be non null", from, to);
        List<Field> nonEditableFields = getNonEditableFieldsOfClass(from.getClass());
        nonEditableFields.forEach(field -> copyFieldValue(field, from, to));
        return to;
    }

    private static List<Field> getNonEditableFieldsOfClass(Class<?> clazz) {
        Field[] declaredClassFields = clazz.getDeclaredFields();
        return stream(declaredClassFields)
                .filter(EntityUtils::isNonEditableField)
                .collect(toList());
    }

    private static boolean isNonEditableField(Field field) {
        Annotation[] annotations = field.getAnnotations();
        return stream(annotations)
                .anyMatch(EntityUtils::isAnnotationOfNonEditableField);
    }

    private static boolean isAnnotationOfNonEditableField(Annotation annotation) {
        return isNonEditableAnnotation(annotation) || isIdAnnotation(annotation);
    }

    private static boolean isNonEditableAnnotation(Annotation annotation) {
        return annotation instanceof NonEditable;
    }

    private static boolean isIdAnnotation(Annotation annotation) {
        return annotation instanceof Id;
    }

    private static void copyFieldValue(Field field, Object source, Object destination) {
        Object value = getFieldValueFromObject(field, source);
        setFieldValueIntoObject(field, value, destination);
    }

}
