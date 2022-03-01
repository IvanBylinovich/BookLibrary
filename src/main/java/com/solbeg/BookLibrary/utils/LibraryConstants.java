package com.solbeg.BookLibrary.utils;

public class LibraryConstants {

    public static final String COMMA_DELIMITER = ", ";
    public static final String WHITESPACE_DELIMITER = " ";
    public static final String PROJECT_BASE_PACKAGE_PATH = "com.solbeg.BookLibrary";
    public static final String DATE_TIME_EXCEPTION_PATTERN = "yyyy-MM-dd hh:mm:ss";

    public static final String AUTHOR_FIRST_NAME_SWAGGER_EXAMPLE = "Stephen";
    public static final String AUTHOR_LAST_NAME_SWAGGER_EXAMPLE = "King";
    public static final String ID_SWAGGER_EXAMPLE = "b234a9fa-0985-4c2e-b106-50cd0eb24ae8";
    public static final String TITLE_SWAGGER_EXAMPLE = "The Dark Half";
    public static final String URL_SWAGGER_EXAMPLE = "https://en.wikipedia.org/wiki/The_Dark_Half#/media/File:Darkhalf.jpg";
    public static final String PRICE_SWAGGER_EXAMPLE = "9.99";
    public static final String DATE_TIME_SWAGGER_EXAMPLE = "2022-02-01T13:15:10.4609297+03:00";
    public static final String TAGS_SWAGGER_EXAMPLE = "FANTASTIC, NOVEL";
    public static final String TAG_SWAGGER_EXAMPLE = "NOVEL";
    public static final String QUANTITY_SWAGGER_EXAMPLE = "3";
    public static final String TOTAL_AMOUNT_SWAGGER_EXAMPLE = "29.97";
    public static final String USERNAME_SWAGGER_EXAMPLE = "user@user.by";
    public static final String PASSWORD_SWAGGER_EXAMPLE = "qJP7h3TRkL*&7";
    public static final String USER_FIRST_NAME_SWAGGER_EXAMPLE = "Ivan";
    public static final String USER_LAST_FIRST_NAME_SWAGGER_EXAMPLE = "Bylinovich";
    public static final String HEADER_USERNAME = "username";
    public static final String HEADER_PASSWORD = "password";
    public static final String HEADER_ERROR = "error";
    public static final String JWT_SECRET = "secret";
    public static String JWT_PREFIX = "Bearer ";

    public static final String[] ENDPOINTS_AUTHORIZATION_WHITELIST = {
            "/",
            "/login/**",
            "/users/refreshToken",
            "/users/registration",
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
    };
    public static final String[] ENDPOINTS_USER_AUTHORITY_READ = {
            "/tags/allTags",
            "/tags/{id}",
            "/orders/{id:[^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$]}",
            "/authors/allAuthors",
            "/authors/{id:[^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$]}",
            "/books/allBooks",
            "/books/{id:[^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$]}",
            "/users/{id:[^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$]}",
    };
    public static final String[] ENDPOINTS_USER_AUTHORITY_WRITE = {
            "/orders", "/orders/",
    };
    public static final String[] ENDPOINTS_USER_AUTHORITY_EDITE = {
            "/orders/{id:[^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$]}", "/users/{id}"
    };
    public static final String[] ENDPOINTS_USER_AUTHORITY_DELETE = {
            "/orders/{id:[^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$]}",
            "/users/{id:[^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$]}"
    };
    public static final String[] ENDPOINTS_ADMIN_AUTHORITY_DELETE = {
            "/authors/{id}",
            "/books/{id}",
            "/tags/{id}"
    };
    public static final String[] ENDPOINTS_ADMIN_AUTHORITY_PATCH = {
            "/orders/{id}"
    };
    public static final String[] ENDPOINTS_ADMIN_AUTHORITY_EDITE = {
            "/authors/{id}", "/books/{id}", "/tags/{id}"
    };
    public static final String[] ENDPOINTS_ADMIN_AUTHORITY_WRITE = {
            "/authors", "/authors/", "/books", "/books/", "/tags", "/tags/", "/users/admin"
    };
    public static final String[] ENDPOINTS_ADMIN_AUTHORITY_READ = {
            "/orders/allOrders", "/orders/allOrders/", "/users/allUsers", "/users/allUsers/"
    };

}
