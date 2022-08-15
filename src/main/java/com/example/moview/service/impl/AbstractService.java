package com.example.moview.service.impl;

import com.example.moview.model.BaseEntity;
import com.example.moview.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public abstract class AbstractService<E extends BaseEntity, R extends JpaRepository<E, ID>, ID>
        implements BaseService<E, ID> {

    protected R repository;

    protected AbstractService(final R repository) {
        this.repository = repository;
    }

    @Override
    public E create(final E entity) {
        return repository.save(entity);
    }

    @Override
    public E update(final E newEntity) {
        return repository.save(newEntity);
    }

    @Override
    public E read(final ID id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public void delete(final ID id) {
        repository.deleteById(id);
    }

    @Override
    public List<E> readAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public Page<E> readAll(final Pageable pageable) {
        return repository.findAll(pageable);
    }
}