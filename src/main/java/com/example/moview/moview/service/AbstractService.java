package com.example.moview.moview.service;

import com.example.moview.moview.exception.NullParameterException;
import com.example.moview.moview.model.BaseEntity;
import com.example.moview.moview.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

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
    public E create(final E entity) {
        checkForNull(entity);
        return repository.save(entity);
    }

    @Override
    @Transactional
    public E update(final E newEntity) {
        checkForNull(newEntity);
        return repository.save(newEntity);
    }

    @Override
    @Transactional
    public E read(final Long id) {
        checkForNull(id);
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Unable to find %s with id %d", clazz.getName(), id))
        );
    }

    @Override
    @Transactional
    public void delete(final Long id) {
        checkForNull(id);
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public List<E> readAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public Page<E> readAll(final Pageable pageable) {
        return repository.findAll(pageable);
    }

    private void checkForNull(final Object object) {
        if (Objects.isNull(object)) {
            throw new NullParameterException(
                    String.format("The parameter cannot be null in the service method of %s.", clazz.getName()));
        }
    }
}
