package cz.cvut.fit.tjv.hujomark.project.business;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Abstract superclass for all entities supporting CRUD operations
 * @param <E> Type of entity
 * @param <K> Type of primary key
 * @param <R> JpaRepository interface of given entity E inheriting from JpaRepository
 */
public abstract class AbstractCrudService<E, K, R extends JpaRepository<E, K>> {
    protected final R repository;

    protected AbstractCrudService(R repository) {
        this.repository = repository;
    }

    protected abstract boolean exists(E entity);

    /**
     * Creates and stores a new entity
     * @param entity - Entity to be stored
     * @throws IllegalArgumentException if entity already exists
     */
    public void create(E entity) {
        if (exists(entity))
            throw new IllegalArgumentException("Entity already exists.");
        repository.save(entity);
    }

    public List<E> readAll() {
        return repository.findAll();
    }

    public Optional<E> readById(K id) {
        return repository.findById(id);
    }

    public void deleteById(K id) {
        repository.deleteById(id);
    }
}
