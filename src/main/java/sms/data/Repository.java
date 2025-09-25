package sms.data;

import sms.exceptions.RepositoryException;
import java.util.List;
import java.util.Optional;

/**
 * Generic Repository interface for CRUD operations
 * Demonstrates Generics usage in the system
 */
public interface Repository<T> {
    
    /**
     * Save an entity to the repository
     * @param entity The entity to save
     * @return The saved entity
     * @throws RepositoryException if save operation fails
     */
    T save(T entity) throws RepositoryException;
    
    /**
     * Find an entity by its ID
     * @param id The ID to search for
     * @return Optional containing the entity if found, empty otherwise
     * @throws RepositoryException if find operation fails
     */
    Optional<T> findById(String id) throws RepositoryException;
    
    /**
     * Find all entities in the repository
     * @return List of all entities
     * @throws RepositoryException if find operation fails
     */
    List<T> findAll() throws RepositoryException;
    
    /**
     * Update an existing entity
     * @param entity The entity to update
     * @return The updated entity
     * @throws RepositoryException if update operation fails
     */
    T update(T entity) throws RepositoryException;
    
    /**
     * Delete an entity by its ID
     * @param id The ID of the entity to delete
     * @return true if deleted successfully, false otherwise
     * @throws RepositoryException if delete operation fails
     */
    boolean deleteById(String id) throws RepositoryException;
    
    /**
     * Load all entities from persistent storage
     * @throws RepositoryException if load operation fails
     */
    void loadAll() throws RepositoryException;
    
    /**
     * Save all entities to persistent storage
     * @throws RepositoryException if save operation fails
     */
    void saveAll() throws RepositoryException;
    
    /**
     * Get the total count of entities
     * @return The count of entities
     */
    long count();
}