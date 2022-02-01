package com.solbeg.BookLibrary.config;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;

import java.util.Collections;

class SwaggerSettings {
    public static ApiInfo apiInfo() {
        return new ApiInfo(
                "Book library",
                "Book library API",
                "v 0.5",
                "solbeg.com",
                new Contact("Ivan Bylinovich", "https://solbeg.com", "ivan.bylinovich@solbeg.com"),
                "License of API", "API license URL", Collections.emptyList());
    }
}
