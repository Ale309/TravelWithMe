package it.uniroma3.twm.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Trip {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String category;
    
    @NotBlank
    private String origin;
    
    @NotBlank
    private String destination;
    
    private String description;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateofdeparture;
    
    @NotNull
    private Integer availability;
    
    @NotNull
    private Double price;
    
    private String transport;

	private Set<Image> images;
    
    private Set<Review> reviews;

    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getDateofdeparture() {
		return dateofdeparture;
	}

	public void setDateofdeparture(LocalDate dateofdeparture) {
		this.dateofdeparture = dateofdeparture;
	}

	public Integer getAvailability() {
		return availability;
	}

	public void setAvailability(Integer availability) {
		this.availability = availability;
	}
	
	public Double getPrice() {
		return price;
	}
	
	public void setPrice(Double price) {
		this.price = price;
	}
	
	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public Set<Image> getImages() {
		return images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}

	public Set<Review> getReviews() {
		return reviews;
	}

	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}

	@Override
	public int hashCode() {
		return Objects.hash(category, dateofdeparture, destination, origin);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Trip other = (Trip) obj;
		return Objects.equals(category, other.category) && Objects.equals(dateofdeparture, other.dateofdeparture)
				&& Objects.equals(destination, other.destination) && Objects.equals(origin, other.origin);
	}
    
    
}