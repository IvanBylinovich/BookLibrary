package com.solbeg.BookLibrary.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

import static com.solbeg.BookLibrary.utils.LibraryConstants.DATE_TIME_EXCEPTION_PATTERN;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomErrorResponseForRuntimeException {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_EXCEPTION_PATTERN)
    private ZonedDateTime timestamp;
    private int status;
    private String error;
}
