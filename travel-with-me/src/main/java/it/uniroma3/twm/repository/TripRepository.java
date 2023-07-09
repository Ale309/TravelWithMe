package it.uniroma3.twm.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.twm.model.Trip;

public interface TripRepository extends CrudRepository<Trip, Long> {

	public List<Trip> findByCategory(String category);
	
	public List<Trip> findByOrigin(String origin);

	public boolean existsByCategoryAndDateofdepartureAndOriginAndDestination(String category, LocalDate dateOfDeparture, String origin, String destination);
	
}