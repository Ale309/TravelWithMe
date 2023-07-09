package it.uniroma3.twm.model;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Reservation {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	private LocalDate dateOfReservation;
	
	@ManyToOne
	private Trip trip;
	
	@ManyToOne
	private User user;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDateofreservation() {
		return dateOfReservation;
	}

	public void setDateofreservation(LocalDate dateOfReservation) {
		this.dateOfReservation = dateOfReservation;
	}

	public Trip getTrip() {
		return trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dateOfReservation, id, trip, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reservation other = (Reservation) obj;
		return Objects.equals(dateOfReservation, other.dateOfReservation) && Objects.equals(id, other.id)
				&& Objects.equals(trip, other.trip) && Objects.equals(user, other.user);
	}
	
	
	
}
