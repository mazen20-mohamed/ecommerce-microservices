package com.mazen.OrderService.model;

public enum OrderStatus {
    CURRENT,
    PACKING,
    SHIPPED,
    DELIVERED,
    CANCELED,
    FINISHED;

    public boolean isCurrent() {
        return this == CURRENT || this == PACKING || this == SHIPPED || this == DELIVERED;
    }
}