package sms.sort;

import java.util.List;

/**
 * Generic interface for sortable entities
 * @param <T> The type of objects that can be sorted
 */
public interface Sortable<T> {
    /**
     * Sort entities based on provided criteria
     * @param criteria The sort criteria (e.g., "name", "id", "date")
     * @return List of entities sorted by the criteria
     */
    List<T> sort(String criteria);
}