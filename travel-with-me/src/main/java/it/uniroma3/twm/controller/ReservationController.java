package it.uniroma3.twm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.twm.model.Credentials;
import it.uniroma3.twm.model.Reservation;
import it.uniroma3.twm.model.User;
import it.uniroma3.twm.service.CredentialsService;
import it.uniroma3.twm.service.ReservationService;
import it.uniroma3.twm.service.TripService;

@Controller
public class ReservationController {
	
	@Autowired
	private ReservationService reservationService;
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private TripService tripService;
	
	@Autowired
	private GlobalController globalController;
	
	@GetMapping("/newReservation/{id}")
	public String newReservation(@PathVariable("id") Long id, Model model) {
		
		model.addAttribute("reservation", this.reservationService.createNewReservation(id));
		model.addAttribute("numtrips",this.tripService.count());
		model.addAttribute("trips", this.tripService.findAllTrip());
		return "trips.html";
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
	
	@GetMapping("/admin/reservations")
	public String getReservations(Model model) {
		model.addAttribute("reservations", this.reservationService.findAllReservation());
		return "/admin/Reservations.html";
	}
	
	@GetMapping("/deleteReservation/{reservationId}")
	public String removeReservation(Model model, @PathVariable("reservationId") Long reservationId) {
		Reservation reservation = this.reservationService.findById(reservationId);
		this.reservationService.deleteReservation(reservation);
		return "/admin/reservations.html";
	}
}
