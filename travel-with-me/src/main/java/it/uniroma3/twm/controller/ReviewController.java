package it.uniroma3.twm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.twm.controller.validator.ReviewValidator;
import it.uniroma3.twm.model.Trip;
import it.uniroma3.twm.model.Review;
import it.uniroma3.twm.service.TripService;
import it.uniroma3.twm.service.ReviewService;
import jakarta.validation.Valid;

@Controller
public class ReviewController {

	@Autowired
	private TripService tripService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private ReviewValidator reviewValidator;
	@Autowired
	private GlobalController globalController;


	@PostMapping("/user/uploadReview/{tripId}")
	public String newReview(Model model, @Valid @ModelAttribute("review") Review review, BindingResult bindingResult, @PathVariable("tripId") Long id) {
		review.setUsername(this.globalController.getUser().getUsername());
		this.reviewValidator.validate(review,bindingResult);
		if(!bindingResult.hasErrors()) {
			Trip trip = this.tripService.findById(id);
			if(this.globalController.getUser() != null && !trip.getReviews().contains(review)){
				this.reviewService.saveReview(review);
				trip.getReviews().add(review);
			}
			this.tripService.saveTrip(trip);
			
			return this.tripService.function(model, trip, this.globalController.getUser().getUsername());
		}else
		System.out.println(review.getUsername());
		System.out.println(review.getTitle());
		System.out.println(review.getDescription());
		System.out.println(review.getRating());
		System.out.println(review.getId());
		System.out.println(bindingResult.getAllErrors());


			return "error.html";
	}

	@GetMapping("/admin/deleteReview/{tripId}/{reviewId}")
	public String removeReview(Model model, @PathVariable("tripId") Long tripId, @PathVariable("reviewId") Long reviewId){
		Trip trip = this.tripService.findById(tripId);
		Review review = this.reviewService.findById(reviewId);

		trip.getReviews().remove(review);
		this.reviewService.deleteReview(review);
		this.tripService.saveTrip(trip);
		return this.tripService.function(model, trip, this.globalController.getUser().getUsername());
	}
}
