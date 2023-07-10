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
import it.uniroma3.twm.service.TripService;
import jakarta.validation.Valid;

@Controller
public class TripController {

	@Autowired
	private TripService tripService;
	
	@Autowired 
	private TripValidator tripValidator;
	
	@Autowired
	private GlobalController globalController;

	@GetMapping(value="/admin/formNewTrip")
	public String formNewTrip(Model model) {
		model.addAttribute("trip", new Trip());
		return "admin/formNewTrip.html";
	}

	@PostMapping("/trip")
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

			if(this.globalController.getUser() != null && this.globalController.getUser().getUsername() != null && this.tripService.alreadyReviewed(trip.getReviews(),this.globalController.getUser().getUsername()))
	            model.addAttribute("hasNotAlredyCommented", false);
	        else
	            model.addAttribute("hasNotAlreadyCommented", true);
			return "trip.html";

		}else {
			return "error.html";
		}
	}

	@GetMapping("/trip")
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

	@GetMapping("/formSearchTrip")
	public String formSearchTrip() {
		return "formSearchTrip.html";
	}

	@PostMapping("/searchTripsByCategory")
	public String searchTripsByCategory(Model model, @RequestParam String category) {
		model.addAttribute("trips", this.tripService.findByCategory(category));
		return "foundTrips.html";
	}
	
	@PostMapping("/searchTripsByOrigin")
	public String searchTripsByOrigin(Model model, @RequestParam String origin) {
		model.addAttribute("trips", this.tripService.findByOrigin(origin));
		return "foundTrips.html";
	}
	
	@PostMapping("/searchTripsByCategoryAndOrigin")
	public String searchTripsByCategoryAndOrigin(Model model, @RequestParam String category, @RequestParam String origin) {
		model.addAttribute("trips", this.tripService.findByCategoryAndOrigin(category, origin));
		return "foundTrips.html";
	}
	
	@PostMapping("/searchTripsByCategoryAndOriginAndDestination")
	public String searchTripsByCategoryAndOriginAndDestination(Model model, @RequestParam String category, @RequestParam String origin, @RequestParam String destination) {
		model.addAttribute("trips", this.tripService.findByCategoryAndOriginAndDestination(category, origin, destination));
		return "foundTrips.html";
	}
	
	@PostMapping("/searchTripsByCategoryAndOriginAndDateofdeparture")
	public String searchTripsByCategoryAndOriginAndDateofdeparture(Model model, @RequestParam String category, @RequestParam String origin, @RequestParam LocalDate dateofdeparture) {
		model.addAttribute("trips", this.tripService.findByCategoryAndOriginAndDateofdeparture(category, origin, dateofdeparture));
		return "foundTrips.html";
	}
	
	@PostMapping("/searchTripsByCategoryAndOriginAndDestinationAndDateofdeparture")
	public String searchTripsByCategoryAndOriginAndDestinationAndDateofdeparture(Model model, @RequestParam String category, @RequestParam String origin, @RequestParam String destination, @RequestParam LocalDate dateofdeparture) {
		model.addAttribute("trips", this.tripService.findByCategoryAndOriginAndDestinationAndDateofdeparture(category, origin, destination, dateofdeparture));
		return "foundTrips.html";
	}


}
