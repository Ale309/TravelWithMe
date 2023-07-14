package it.uniroma3.twm.service;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.twm.model.Credentials;
import it.uniroma3.twm.model.Reservation;
import it.uniroma3.twm.model.Trip;
import it.uniroma3.twm.model.User;
import it.uniroma3.twm.repository.ReservationRepository;
import jakarta.transaction.Transactional;

@Service
public class ReservationService {
	
	@Autowired
	private TripService tripService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private ReservationRepository reservationRepository;

	@Transactional
	public Reservation createNewReservation(Long id, String username) {
		Reservation reservation = new Reservation();
		User user = new User();
		for(Credentials cred : this.credentialsService.findAllCredentials()) {
			if(cred.getUsername().equals(username)) {
				user = cred.getUser();
				reservation.setUser(user);
			}
		}
		reservation.setTrip(this.tripService.findById(id));
		reservation.setDateofreservation(LocalDate.now());

		user.getReservation().add(reservation);
		this.userService.saveUser(user);

		reservation.getTrip().setAvailability(reservation.getTrip().getAvailability()-1);
		this.tripService.saveTrip(reservation.getTrip());
		return 	this.reservationRepository.save(reservation);
	}

	@Transactional
	public Reservation findById(Long id) {
		return this.reservationRepository.findById(id).orElse(null);
	}

	@Transactional
	public Set<Reservation> deleteReservation(Long reservationId) {
		Reservation reservation = this.findById(reservationId);
		
		User user = reservation.getUser();
		
		user.getReservation().remove(reservation);
		this.userService.saveUser(user);
		
		reservation.getTrip().setAvailability(reservation.getTrip().getAvailability()+1);
		this.tripService.saveTrip(reservation.getTrip());
		
		this.reservationRepository.delete(reservation);
		
		return user.getReservation();
	}

	@Transactional
	public boolean alreadyPartecipate(Trip trip, String username) {
		Iterable<Reservation> reservations = this.reservationRepository.findAll();
		if(reservations!= null)
			for(Reservation res : reservations) {
				if(trip.getId()==res.getTrip().getId() && res.getUser().getUsername().equals(username))
					return true;
			}
		return false;
	}

}
