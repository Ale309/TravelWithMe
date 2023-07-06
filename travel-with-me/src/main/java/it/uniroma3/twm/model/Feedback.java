package it.uniroma3.twm.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Feedback extends Review{
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	private String suggestions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
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
