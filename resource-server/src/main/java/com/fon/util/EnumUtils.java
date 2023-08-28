package com.fon.util;

import com.fon.entity.enumeration.RoomsNumber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EnumUtils {

    public static <E extends Enum<E>> List<String> getLabels(Class<E> enumClass) {
        List<String> labels = new ArrayList<>();

        for (E enumValue : enumClass.getEnumConstants()) {
            try {
                Field labelField = enumClass.getDeclaredField("label");
                labelField.setAccessible(true);
                String label = (String) labelField.get(enumValue);
                labels.add(label);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return labels;
    }

    public static <E extends Enum<E>> E fromLabel(Class<E> enumClass, String label) {
        if (RoomsNumber.class.equals(enumClass)) {
            if (Float.parseFloat(label) > 5.0f) {
                label = "5+";
            } else {
                if (Float.parseFloat(label) < 0.5f) {
                    label = "0.5";
                }
            }
        }
        for (E enumValue : enumClass.getEnumConstants()) {
            try {
                Field labelField = enumClass.getDeclaredField("label");
                labelField.setAccessible(true);
                String value = (String) labelField.get(enumValue);
                if (value.equalsIgnoreCase(label)) {
                    return enumValue;
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        throw new IllegalArgumentException("Invalid label: " + label);
    }
}
