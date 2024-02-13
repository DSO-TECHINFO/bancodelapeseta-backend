package com.banco.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GlobalException {
    private String reason;
    private Integer status;
    private String code;
    private Map<String, Object> details;
}
