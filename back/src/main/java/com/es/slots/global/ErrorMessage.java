package com.es.slots.global;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class ErrorMessage {
    private String message;
    private int statusCode;
    private Date timestamp;
}
