package it.uniroma3.twm.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.twm.controller.validator.ReservationValidator;
import it.uniroma3.twm.model.Credentials;
import it.uniroma3.twm.model.Reservation;
import it.uniroma3.twm.model.User;
import it.uniroma3.twm.service.CredentialsService;
import it.uniroma3.twm.service.ReservationService;
import it.uniroma3.twm.service.TripService;
import it.uniroma3.twm.service.UserService;

@Controller
public class ReservationController {
	
	@Autowired
	private ReservationService reservationService;
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TripService tripService;
	
	@Autowired
	private ReservationValidator reservationValidator;
	
	@Autowired
	private GlobalController globalController;
	
	@PostMapping(value="/newReservation/{tripId}")
	public String newReservation(@PathVariable("tripId") Long id, BindingResult bindingResult, Model model) {
		
		Reservation reservation = new Reservation();
		
		String username = this.globalController.getUser().getUsername();
		User user = new User();
		for(Credentials cred : this.credentialsService.findAllCredentials()) {
			if(cred.getUsername().equals(username))
				user = cred.getUser();
				reservation.setUser(user);
		}
		reservation.setTrip(this.tripService.findById(id));
		reservation.setDateofreservation(LocalDate.now());
		
		user.getReservation().add(reservation);
		this.userService.saveUser(user);
		
		reservation.getTrip().setAvailability(reservation.getTrip().getAvailability()-1);
		this.tripService.saveTrip(reservation.getTrip());
		
		this.reservationValidator.validate(reservation, bindingResult);
		
		if (!bindingResult.hasErrors()) {
			model.addAttribute("reservation", this.reservationService.createNewReservation(reservation));
			return "trips.html";
		}else{
			return "error.html";
		}
	}
    

	@GetMapping("/reservation/{id}")
	public String getReservation(@PathVariable("id") Long id, Model model) {
		Reservation reservation = this.reservationService.findById(id);
		if(reservation != null) {
			model.addAttribute("reservation", reservation);
			return "reservation.html";
		}else {
			return "error.html";
		}
	}
	
	@GetMapping("/myReservations")
	public String getMyReservations(Model model) {
		
		String username = this.globalController.getUser().getUsername();
		User user = new User();
		for(Credentials cred : this.credentialsService.findAllCredentials()) {
			if(cred.getUsername().equals(username))
				user = cred.getUser();
		}
		
		model.addAttribute("reservations", user.getReservation());
		return "myReservations.html";
	}
	
	@GetMapping("/reservations")
	public String getReservations(Model model) {
		model.addAttribute("reservations", this.reservationService.findAllReservation());
		return "admin/Reservations.html";
	}
	
	@GetMapping("/deleteReservation/{reservationId}")
	public String removeReservation(Model model, @PathVariable("reservationId") Long reservationId) {
		Reservation reservation = this.reservationService.findById(reservationId);
		this.reservationService.deleteReservation(reservation);
		return "reservations.html";
	}
}
