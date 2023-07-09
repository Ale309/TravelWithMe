package it.uniroma3.twm.model;

import java.util.Objects;

import jakarta.persistence.Entity;

@Entity
public class Feedback extends Review{

	private String suggestions;
	
	public String getSuggestions() {
		return suggestions;
	}

	public void setSuggestions(String suggestions) {
		this.suggestions = suggestions;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Feedback)) return false;
        if (!super.equals(o)) return false;
        Feedback feedback = (Feedback) o;
        return Objects.equals(suggestions, feedback.suggestions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), suggestions);
    }
	
}
