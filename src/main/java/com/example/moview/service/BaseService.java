package com.example.moview.service;

import com.example.moview.model.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BaseService<E extends BaseEntity, ID> {

    E create(E entity);

    E update(E newEntity);

    E readById(ID id);

    void delete(ID id);

    List<E> readAll();

    Page<E> readAll(Pageable pageable);

}