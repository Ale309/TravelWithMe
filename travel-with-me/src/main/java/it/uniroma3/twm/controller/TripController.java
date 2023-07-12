package it.uniroma3.twm.controller;

import java.time.LocalDate;

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
			
			model.addAttribute("user");

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
		model.addAttribute("numtrips",tripService.count());
		model.addAttribute("trips", this.tripService.findAllTrip());
		return "trips.html";
	}

	@GetMapping(value="/admin/formUpdateTrip/{id}")
	public String formUpdateTrip(@PathVariable("id") Long id, Model model) {
		Trip trip = tripService.findById(id);
		if(trip != null) {
			model.addAttribute("trip", tripService.findById(id));
			
		}else {
			return "error.html";
		}
		return "admin/formUpdateTrip.html";

	}
	
	@PostMapping(value="/admin/changeDescription/{trip.id}")
	public String changeDescription(@PathVariable("id") Long id, @RequestParam("newDescription") String newDescription, Model model) {
		if(newDescription!=null) {
			Trip trip = tripService.findById(id);
			trip.setDescription(newDescription);
			this.tripService.saveTrip(trip);
			model.addAttribute("trip",trip);
		}
		return "admin/formUpdateTrip.html";
	}
	
	@PostMapping(value="/admin/changePrice/{trip.id}")
	public String changePrice(@PathVariable("id") Long id, @RequestParam("newPrice") Double newPrice, Model model) {
		if(newPrice!=null) {
			Trip trip = tripService.findById(id);
			trip.setPrice(newPrice);
			this.tripService.saveTrip(trip);
			model.addAttribute("trip",trip);
		}
		return "admin/formUpdateTrip.html";
	}
	
	@PostMapping(value="/admin/changeAvailablity/{trip.id}")
	public String changeAvailability(@PathVariable("id") Long id, @RequestParam("newAvailability") Integer newAvailability, Model model) {
		if(newAvailability!=null) {
			Trip trip = tripService.findById(id);
			trip.setAvailability(newAvailability);
			this.tripService.saveTrip(trip);
			model.addAttribute("trip",trip);
		}
		return "admin/formUpdateTrip.html";
	}

	@GetMapping("/searchTrip")
	public String formSearchTrip() {
		return "formSearchTrip";
	}

	@PostMapping("/searchTripsByCategory")
	public String searchTripsByCategory(Model model, @RequestParam String category) {
		model.addAttribute("trips", this.tripService.findByCategory(category));
		return "trips.html";
	}
	
	@PostMapping("/searchTripsByOrigin")
	public String searchTripsByOrigin(Model model, @RequestParam String origin) {
		model.addAttribute("trips", this.tripService.findByOrigin(origin));
		return "trips.html";
	}
	
	@PostMapping("/searchTripsByCategoryAndOrigin")
	public String searchTripsByCategoryAndOrigin(Model model, @RequestParam String category, @RequestParam String origin) {
		model.addAttribute("trips", this.tripService.findByCategoryAndOrigin(category, origin));
		return "trips.html";
	}
	
	@PostMapping("/searchTripsByCategoryAndOriginAndDestination")
	public String searchTripsByCategoryAndOriginAndDestination(Model model, @RequestParam String category, @RequestParam String origin, @RequestParam String destination) {
		model.addAttribute("trips", this.tripService.findByCategoryAndOriginAndDestination(category, origin, destination));
		return "trips.html";
	}
	
	@PostMapping("/searchTripsByCategoryAndOriginAndDateofdeparture")
	public String searchTripsByCategoryAndOriginAndDateofdeparture(Model model, @RequestParam String category, @RequestParam String origin, @RequestParam LocalDate dateofdeparture) {
		model.addAttribute("trips", this.tripService.findByCategoryAndOriginAndDateofdeparture(category, origin, dateofdeparture));
		return "trips.html";
	}
	
	@PostMapping("/searchTripsByCategoryAndOriginAndDestinationAndDateofdeparture")
	public String searchTripsByCategoryAndOriginAndDestinationAndDateofdeparture(Model model, @RequestParam String category, @RequestParam String origin, @RequestParam String destination, @RequestParam LocalDate dateofdeparture) {
		model.addAttribute("trips", this.tripService.findByCategoryAndOriginAndDestinationAndDateofdeparture(category, origin, destination, dateofdeparture));
		return "trips.html";
	}


}
