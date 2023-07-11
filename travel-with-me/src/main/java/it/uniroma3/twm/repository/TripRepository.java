package it.uniroma3.twm.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.twm.model.Trip;

public interface TripRepository extends CrudRepository<Trip, Long> {

	public List<Trip> findByCategory(String category);
	
	public List<Trip> findByOrigin(String origin);
	
	public List<Trip> findByCategoryAndOrigin(String category, String origin);
	
	public List<Trip> findByCategoryAndOriginAndDestination(String category, String origin, String destination);
	
	public List<Trip> findByCategoryAndOriginAndDateofdeparture(String category, String origin, LocalDate dateofdeparture);
	
	public List<Trip> findByCategoryAndOriginAndDestinationAndDateofdeparture(String category, String origin, String destination, LocalDate dateofdeparture);


	public boolean existsByCategoryAndDateofdepartureAndOriginAndDestination(String category, LocalDate dateOfDeparture, String origin, String destination);
	
}