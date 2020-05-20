package com.drofff.palindrome.service;

public interface SimpleEntityManager<T> {

    void create(T t);

    void update(T t);

    void delete(T t);

}
