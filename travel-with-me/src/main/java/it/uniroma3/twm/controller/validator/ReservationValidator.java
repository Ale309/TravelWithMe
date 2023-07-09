package it.uniroma3.twm.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.twm.model.Reservation;
import it.uniroma3.twm.repository.ReservationRepository;

@Component
public class ReservationValidator implements Validator {
	
	@Autowired
	private ReservationRepository reservationRepository;

	@Override
	public void validate(Object o, Errors errors) {
		Reservation reservation = (Reservation)o;
		if (reservation.getTrip()!=null && reservation.getUser()!=null 
				&& reservationRepository.existsByDateofreservationAndTripAndUser(reservation.getDateofreservation(), reservation.getTrip(), reservation.getUser())) {
			errors.reject("reservation.duplicate");
		}
	}
	@Override
	public boolean supports(Class<?> aClass) {
		return Reservation.class.equals(aClass);
	}
}