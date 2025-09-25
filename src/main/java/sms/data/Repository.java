package sms.data;

import sms.exceptions.RepositoryException;
import sms.exceptions.ValidationException;
import sms.exceptions.NotFoundException;
import java.util.List;

/**
 * Generic Repository interface for CRUD operations
 * Demonstrates Generics usage in the system
 */
public interface Repository<T> {
    
    /**
     * Add an entity to the repository
     * @param item The entity to add
     * @throws RepositoryException if add operation fails
     * @throws ValidationException if item validation fails
     */
    void add(T item) throws RepositoryException, ValidationException;
    
    /**
     * Update an existing entity
     * @param item The entity to update
     * @throws RepositoryException if update operation fails
     * @throws NotFoundException if entity is not found
     */
    void update(T item) throws RepositoryException, NotFoundException;
    
    /**
     * Delete an entity
     * @param item The entity to delete
     * @throws RepositoryException if delete operation fails
     * @throws NotFoundException if entity is not found
     */
    void delete(T item) throws RepositoryException, NotFoundException;
    
    /**
     * Get all entities in the repository
     * @return List of all entities
     * @throws RepositoryException if retrieval operation fails
     */
    List<T> getAll() throws RepositoryException;
    
    /**
     * Find entities based on criteria
     * @param criteria The search criteria
     * @return List of entities matching the criteria
     * @throws RepositoryException if find operation fails
     */
    List<T> find(String criteria) throws RepositoryException;
}