package com.example.moview.moview.service;

import com.example.moview.moview.exception.NotFoundException;
import com.example.moview.moview.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
public abstract class AbstractService<E extends BaseEntity, R extends JpaRepository<E, ID>, ID>
        implements BaseService<E, ID> {

    protected R repository;

    protected AbstractService(final R repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public E create(final E entity) {
        return repository.save(entity);
    }

    @Override
    @Transactional
    public E update(final E newEntity) {
        return repository.save(newEntity);
    }

    @Override
    @Transactional
    public E read(final ID id) {
        return repository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Entity with id = %s not found in the database", id.toString())));
    }

    @Override
    @Transactional
    public void delete(final ID id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public List<E> readAll() {
        return repository.findAll();
    }
}
