package com.example.moview.moview.service;

import com.example.moview.moview.model.BaseEntity;

import java.util.List;

public interface BaseService<E extends BaseEntity, ID> {

    E create(E entity);

    E update(E newEntity);

    E read(ID id);

    void delete(ID id);

    List<E> readAll();
}
