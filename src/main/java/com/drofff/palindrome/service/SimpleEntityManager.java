package com.drofff.palindrome.service;

import java.util.List;

public interface SimpleEntityManager<T> {

    void create(T t);

    void update(T t);

    void delete(T t);

    List<T> getAll();

    T getById(String id);

}
