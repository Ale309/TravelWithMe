package it.uniroma3.twm.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.twm.controller.validator.TripValidator;
import it.uniroma3.twm.model.Review;
import it.uniroma3.twm.model.Trip;
import it.uniroma3.twm.service.ReservationService;
import it.uniroma3.twm.service.TripService;
import jakarta.validation.Valid;

@Controller
public class TripController {

	@Autowired
	private TripService tripService;
	
	@Autowired
	private ReservationService reservationService;
	
	@Autowired 
	private TripValidator tripValidator;
	
	@Autowired
	private GlobalController globalController;

	@GetMapping(value="/admin/formNewTrip")
	public String formNewTrip(Model model) {
		model.addAttribute("trip", new Trip());
		return "admin/formNewTrip.html";
	}

	@PostMapping("/admin/trip")
	public String newTrip(@Valid @ModelAttribute("trip") Trip trip, BindingResult bindingResult,@RequestParam("tripImage") MultipartFile[] multipartFile, Model model) {
		trip.setDays(ChronoUnit.DAYS.between(trip.getDeparturedate(), trip.getReturndate()) + 1);
		this.tripValidator.validate(trip, bindingResult);
		if (!bindingResult.hasErrors()) {
			model.addAttribute("trip", this.tripService.createNewTrip(trip, multipartFile));
			return "trip.html";
		} else {
			return "error.html";
		}
	}

	@GetMapping("/trip/{id}")
	public String getTrip(@PathVariable("id") Long id, Model model) {
		Trip trip = this.tripService.findById(id);
		if(trip != null) {
			model.addAttribute("trip", trip);
			model.addAttribute("review",new Review());
			model.addAttribute("reviews",trip.getReviews());
			model.addAttribute("hasReviews",!trip.getReviews().isEmpty());

			if(this.globalController.getUser() != null && this.globalController.getUser().getUsername() != null && this.tripService.alreadyReviewed(trip.getReviews(),this.globalController.getUser().getUsername()))
	            model.addAttribute("hasNotAlreadyCommented", false);
	        else
	            model.addAttribute("hasNotAlreadyCommented", true);
			
			if(this.globalController.getUser() != null && this.globalController.getUser().getUsername() != null && this.reservationService.alreadyPartecipate(trip,this.globalController.getUser().getUsername()))
	            model.addAttribute("hasPartecipated", true);
	        else
	            model.addAttribute("hasPartecipated", false);
			
			return "trip.html";

		}else {
			return "error.html";
		}
	}

	@GetMapping("/trips")
	public String getTrips(Model model) {
		model.addAttribute("numtrips",this.tripService.count());
		model.addAttribute("trips", this.tripService.findAllTrip());
		return "trips.html";
	}

	@GetMapping(value="/admin/formUpdateTrip/{id}")
	public String formUpdateTrip(@PathVariable("id") Long id, Model model) {
		Trip trip = this.tripService.findById(id);
		if(trip != null) {
			model.addAttribute("trip", this.tripService.findById(id));
			
		}else {
			return "error.html";
		}
		return "admin/formUpdateTrip.html";

	}
	
	@PostMapping(value="/admin/changeDescription/{id}")
	public String changeDescription(@PathVariable("id") Long id, @RequestParam("newDescription") String newDescription, Model model) {
		if(newDescription!=null) {
			Trip trip = this.tripService.findById(id);
			trip.setDescription(newDescription);
			this.tripService.saveTrip(trip);
			model.addAttribute("trip",trip);
		}
		return "admin/formUpdateTrip.html";
	}
	
	@PostMapping(value="/admin/changePrice/{id}")
	public String changePrice(@PathVariable("id") Long id, @RequestParam("newPrice") Double newPrice, Model model) {
		if(newPrice!=null) {
			Trip trip = this.tripService.findById(id);
			trip.setPrice(newPrice);
			this.tripService.saveTrip(trip);
			model.addAttribute("trip",trip);
		}
		return "admin/formUpdateTrip.html";
	}
	
	@PostMapping(value="/admin/changeAvailability/{id}")
	public String changeAvailability(@PathVariable("id") Long id, @RequestParam("newAvailability") Integer newAvailability, Model model) {
		if(newAvailability!=null) {
			Trip trip = this.tripService.findById(id);
			trip.setAvailability(newAvailability);
			this.tripService.saveTrip(trip);
			model.addAttribute("trip",trip);
		}
		return "admin/formUpdateTrip.html";
	}

	@GetMapping("/searchTrip")
	public String formSearchTrip(Model model) {
		model.addAttribute("newTrip", new Trip());
		return "formSearchTrip";
	}
	
	@PostMapping("/findTrip")
	public String searchTrip(Model model,
			@RequestParam(name="category", required = false) String category,
			@RequestParam(name="origin", required = false) String origin,
			@RequestParam(name="destination", required = false) String destination,
			@RequestParam(name="departuredate", required = false) LocalDate departuredate,
			@RequestParam(name="returndate", required = false) LocalDate returndate) {
		
		List<Trip> trips = this.tripService.searchTrips(category, origin, destination, departuredate, returndate);
	    model.addAttribute("trips", trips);

		return "foundTrips.html";
	}

}
