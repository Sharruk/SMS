package sms.search;

import java.util.List;

/**
 * Generic interface for searchable entities
 * @param <T> The type of objects that can be searched
 */
public interface Searchable<T> {
    /**
     * Search for entities based on provided criteria
     * @param criteria The search criteria (e.g., name, ID, email)
     * @return List of entities matching the criteria
     */
    List<T> search(String criteria);
}