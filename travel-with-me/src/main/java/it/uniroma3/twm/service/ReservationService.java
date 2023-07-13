package it.uniroma3.twm.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.twm.controller.GlobalController;
import it.uniroma3.twm.model.Credentials;
import it.uniroma3.twm.model.Reservation;
import it.uniroma3.twm.model.Trip;
import it.uniroma3.twm.model.User;
import it.uniroma3.twm.repository.ReservationRepository;
import jakarta.transaction.Transactional;

@Service
public class ReservationService {
	
	@Autowired
	private GlobalController globalController;
	
	@Autowired
	private TripService tripService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private ReservationRepository reservationRepository;

	@Transactional
	public Reservation createNewReservation(Long id) {
		Reservation reservation = new Reservation();

		String username = this.globalController.getUser().getUsername();
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

	public Iterable<Reservation> findAllReservation() {
		return this.reservationRepository.findAll();
	}

	@Transactional
	public void deleteReservation(Reservation reservation) {
		this.reservationRepository.delete(reservation);
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
