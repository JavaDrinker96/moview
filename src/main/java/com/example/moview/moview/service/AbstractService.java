package com.example.moview.moview.service;

import com.example.moview.moview.model.BaseEntity;
import com.example.moview.moview.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@Component
public abstract class AbstractService<E extends BaseEntity, R extends BaseRepository<E>> implements BaseService<E> {

    protected R repository;
    protected Class<E> clazz;

    public AbstractService(final R repository, final Class<E> clazz) {
        this.repository = repository;
        this.clazz = clazz;
    }

    @Override
    @Transactional
    public E create(@NotNull final E entity) {
        return repository.save(entity);
    }

    @Override
    @Transactional
    public E update(@NotNull final E newEntity) {
        return repository.save(newEntity);
    }

    @Override
    @Transactional
    public E read(@NotNull final Long id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Unable to find %s with id %d", clazz.getName(), id))
        );
    }

    @Override
    @Transactional
    public void delete(@NotNull final Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public List<E> readAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public Page<E> readAll(@NotNull final Pageable pageable) {
        return repository.findAll(pageable);
    }
}
