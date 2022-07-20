package com.example.moview.moview.service;

import com.example.moview.moview.model.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BaseService<E extends BaseEntity> {

    E create(E entity);

    E update(E newEntity);

    E read(Long id);

    void delete(Long id);

    List<E> readAll();

    Page<E> readAll(Pageable pageable);
}
