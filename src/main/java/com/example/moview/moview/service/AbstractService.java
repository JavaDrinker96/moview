package com.example.moview.moview.service;

import com.example.moview.moview.exception.NotFoundException;
import com.example.moview.moview.exception.NullParameterException;
import com.example.moview.moview.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Component
public abstract class AbstractService<E extends BaseEntity, R extends JpaRepository<E, ID>, ID>
        implements BaseService<E, ID> {

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
    public E read(final ID id) {
        checkForNull(id);
        return repository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("%s with id = %s not found in the database",
                        clazz.getName(), id.toString()))
        );
    }

    @Override
    @Transactional
    public void delete(final ID id) {
        checkForNull(id);
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public List<E> readAll() {
        return repository.findAll();
    }

    private void checkForNull(final Object object) {
        if (Objects.isNull(object)) {
            throw new NullParameterException(
                    String.format("The parameter cannot be null in the service method of %s.", clazz.getName()));
        }
    }
}
