package sms.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Batch {
    private String yearRange; // e.g., "2024-2028"

    // Default constructor for Jackson
    public Batch() {}

    @JsonCreator
    public Batch(@JsonProperty("yearRange") String yearRange) {
        this.yearRange = yearRange;
    }

    public String getYearRange() {
        return yearRange;
    }

    public void setYearRange(String yearRange) {
        this.yearRange = yearRange;
    }

    public int getStartYear() {
        if (yearRange != null && yearRange.contains("-")) {
            try {
                return Integer.parseInt(yearRange.split("-")[0]);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    public int getEndYear() {
        if (yearRange != null && yearRange.contains("-")) {
            try {
                return Integer.parseInt(yearRange.split("-")[1]);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Batch{yearRange='" + yearRange + "'}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Batch batch = (Batch) obj;
        return yearRange != null ? yearRange.equals(batch.yearRange) : batch.yearRange == null;
    }

    @Override
    public int hashCode() {
        return yearRange != null ? yearRange.hashCode() : 0;
    }
}