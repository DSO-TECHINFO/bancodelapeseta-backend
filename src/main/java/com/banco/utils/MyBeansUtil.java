package com.banco.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Collection;

@Service
public class MyBeansUtil<T, I> {
    public void copyNonNullProperties(T in, I target) {
        if (in == null || target == null || target.getClass() != in.getClass()) return;

        final BeanWrapper src = new BeanWrapperImpl(in);
        final BeanWrapper trg = new BeanWrapperImpl(target);

        for (final Field property : target.getClass().getDeclaredFields()) {
            Object providedObject = src.getPropertyValue(property.getName());
            if (providedObject != null && !(providedObject instanceof Collection<?>)) {
                trg.setPropertyValue(
                        property.getName(),
                        providedObject);
            }
        }
    }
}