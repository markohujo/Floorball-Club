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

    /**
     * @throws IllegalArgumentException if primary key of the given entity is null
     */
    protected abstract boolean exists(E entity);

    /**
     * @throws IllegalArgumentException if the given entity already exists or is null
     */
    public void create(E entity) {
        if (exists(entity))
            throw new IllegalArgumentException("Entity already exists.");
        repository.save(entity);
    }

    public List<E> readAll() {
        return repository.findAll();
    }

    /**
     * @throws IllegalArgumentException if id is null
     */
    public Optional<E> readById(K id) {
        return repository.findById(id);
    }

    /**
     * @throws IllegalArgumentException if the given entity is null or does not exist
     */
    public void update(E entity) {
        if (exists(entity))
            repository.save(entity);
        else throw new IllegalArgumentException("Entity does not exist.");
    }

    public void deleteById(K id) {
        repository.deleteById(id);
    }
}
