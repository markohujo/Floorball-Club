package cz.cvut.fit.tjv.hujomark.project.business;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Abstract superclass for all entities supporting CRUD operations
 * @param <E> Type of entity
 * @param <K> Type of primary key
 */
public abstract class AbstractCrudService<E, K> {
    private final JpaRepository<E, K> jpaRepository;

    protected AbstractCrudService(JpaRepository<E, K> jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    /**
     * Creates and stores a new entity
     * @param entity - Entity to be stored
     * @throws IllegalArgumentException if entity already exists
     */
    public void create(E entity) {
        if (jpaRepository.exists(Example.of(entity)))
            throw new IllegalArgumentException("Entity already exists.");
        jpaRepository.save(entity);
    }

    public List<E> readAll() {
        return jpaRepository.findAll();
    }

    public Optional<E> readById(K id) {
        return jpaRepository.findById(id);
    }

    public void deleteById(K id) {
        jpaRepository.deleteById(id);
    }
}
