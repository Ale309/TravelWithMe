package it.uniroma3.twm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.twm.model.Reservation;
import it.uniroma3.twm.repository.ReservationRepository;
import jakarta.transaction.Transactional;

@Service
public class ReservationService {
	
	@Autowired
	private ReservationRepository reservationRepository;
	
	@Transactional
	public Reservation createNewReservation(Reservation reservation) {
		return 	this.reservationRepository.save(reservation);
	}
	
    @Transactional
	public Reservation findById(Long id) {
		return this.reservationRepository.findById(id).orElse(null);
	}

	public Iterable<Reservation> findAllReservation() {
		return this.reservationRepository.findAll();
	}

	public void deleteReservation(Reservation reservation) {
		this.reservationRepository.delete(reservation);
	}

}