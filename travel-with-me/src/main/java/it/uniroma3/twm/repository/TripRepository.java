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
	
	public List<Trip> findByCategoryAndOriginAndDeparturedate(String category, String origin, LocalDate departureDate);
	
	public List<Trip> findByCategoryAndOriginAndDestinationAndDeparturedate(String category, String origin, String destination, LocalDate departuredate);

	public List<Trip> findByCategoryAndOriginAndDestinationAndDeparturedateAndReturndate(String category, String origin, String destination, LocalDate departuredate, LocalDate returndate);


	public boolean existsByCategoryAndDeparturedateAndReturndateAndOriginAndDestination(String category, LocalDate departureDate, LocalDate returnDate, String origin, String destination);
	
}