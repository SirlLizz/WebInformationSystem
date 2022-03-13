package com.example.webinformationsystem.dao;

import com.example.webinformationsystem.model.Customer;

import java.util.List;

public interface Repository<T> {
    List<T> get();
    int add(T obj);

    int remove(String id);
    int change(String id, T obj);
}
