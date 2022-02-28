package com.solbeg.BookLibrary.model;

import java.util.Arrays;

public enum OrderStatus {
    DRAFT, PAID, RECEIVED;

    public static boolean validationStatusName(String status) {
        return !Arrays.stream(OrderStatus.values()).map(OrderStatus::name).toList().contains(status);
    }
}
