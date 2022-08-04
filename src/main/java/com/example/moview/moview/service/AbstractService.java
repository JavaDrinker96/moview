package com.example.moview.moview.service;

import com.example.moview.moview.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
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
}