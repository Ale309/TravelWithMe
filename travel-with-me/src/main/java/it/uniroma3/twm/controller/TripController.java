package it.uniroma3.twm.controller;

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
import it.uniroma3.twm.model.Trip;
import it.uniroma3.twm.model.Review;
import it.uniroma3.twm.service.ImageService;
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
		model.addAttribute("movie", new Trip());
		return "admin/formNewTrip.html";
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

	@GetMapping(value="/admin/indexTrip")
	public String indexTrip() {
		return "admin/indexTrip.html";
	}

	@GetMapping(value="/admin/manageTrips")
	public String manageTrips(Model model) {
		model.addAttribute("trips", this.tripService.findAllTrip());
		return "admin/manageTrip.html";
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

			if(this.globalController.getUser() != null && this.globalController.getUser().getUsername() != null && this.movieService.alreadyReviewed(movie.getReviews(),this.globalController.getUser().getUsername()))
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

	@GetMapping("/formSearchTrip")
	public String formSearchTrip() {
		return "formSearchTrip.html";
	}

	@PostMapping("/searchTrips")
	public String searchTrips(Model model, @RequestParam String category) {
		model.addAttribute("trips", this.tripService.findByCategory(category));
		return "foundTrips.html";
	}
	


}
