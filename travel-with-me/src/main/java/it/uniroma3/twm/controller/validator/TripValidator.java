package it.uniroma3.twm.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.twm.model.Trip;
import it.uniroma3.twm.repository.TripRepository;

@Component
public class TripValidator implements Validator {
	@Autowired
	private TripRepository tripRepository;

	@Override
	public void validate(Object o, Errors errors) {
		Trip trip = (Trip)o;
		if (trip.getCategory()!= null && trip.getDeparturedate()!= null && trip.getReturndate()!= null && trip.getDestination()!= null && trip.getOrigin() != null
				&& tripRepository.existsByCategoryAndDeparturedateAndReturndateAndOriginAndDestination(trip.getCategory(), trip.getDeparturedate(), trip.getReturndate(), trip.getDestination(), trip.getOrigin())) {
			errors.reject("trip.duplicate");
		}
	}
	@Override
	public boolean supports(Class<?> aClass) {
		return Trip.class.equals(aClass);
	}
}