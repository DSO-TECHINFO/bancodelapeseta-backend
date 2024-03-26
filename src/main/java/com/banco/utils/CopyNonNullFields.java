package com.banco.utils;

import com.banco.exceptions.CustomException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class CopyNonNullFields {
    public void copyNonNullProperties(Object src, Object target, boolean nonNullPropertiesFlag) throws CustomException {

        BeanUtils.copyProperties(src, target, getNullPropertyNames(src, nonNullPropertiesFlag));

    }

    private static String[] getNullPropertyNames(Object source, boolean nonNullPropertiesFlag) throws CustomException {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null)
                emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        String [] array = emptyNames.toArray(result);
        if(result.length >0 && nonNullPropertiesFlag)
            throw new CustomException("BADATA-001", "Fields: " + Arrays.toString(array) + " cannot be null", 400);
        return array;
    }
}
