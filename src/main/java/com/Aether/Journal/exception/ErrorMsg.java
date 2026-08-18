package com.Aether.Journal.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ErrorMsg {
    private String msg;
    private HttpStatus httpStatus;
    private LocalDateTime dateTime;

}
