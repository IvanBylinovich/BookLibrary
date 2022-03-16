package com.solbeg.BookLibrary.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.solbeg.BookLibrary.utils.LibraryConstants.HEADER_ERROR;
import static com.solbeg.BookLibrary.utils.LibraryConstants.HEADER_ERROR_MESSAGE;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class JWTResponseService {

    public static Map<String, String> responseErrorConfiguration(HttpServletResponse response, Exception exception) {
        response.setHeader(HEADER_ERROR, exception.getMessage());
        response.setStatus(FORBIDDEN.value());
        Map<String, String> error = new HashMap<>();
        error.put(HEADER_ERROR_MESSAGE, exception.getMessage());
        return error;
    }

    public static void configurationJSONResponse(HttpServletResponse response, Map<String, String> values) throws IOException {
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), values);
    }

}
