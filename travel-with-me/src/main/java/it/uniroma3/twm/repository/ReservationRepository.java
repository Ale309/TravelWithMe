package it.uniroma3.twm.repository;

import java.time.LocalDate;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.twm.model.Reservation;
import it.uniroma3.twm.model.Trip;
import it.uniroma3.twm.model.User;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {

	public boolean existsByDateofreservationAndTripAndUser(LocalDate dateOfReservation, Trip trip, User user);


}