package it.uniroma3.twm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.twm.model.Reservation;
import it.uniroma3.twm.model.Trip;
import it.uniroma3.twm.repository.ReservationRepository;
import jakarta.transaction.Transactional;

@Service
public class SupportService {
	
	@Autowired
	private ReservationRepository reservationRepository;
	
	public Iterable<Reservation> findAllReservation() {
		return this.reservationRepository.findAll();
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
