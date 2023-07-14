package it.uniroma3.twm.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.twm.model.Image;
import it.uniroma3.twm.model.Reservation;
import it.uniroma3.twm.model.Review;
import it.uniroma3.twm.model.Trip;
import it.uniroma3.twm.repository.ImageRepository;
import it.uniroma3.twm.repository.ReservationRepository;
import it.uniroma3.twm.repository.TripRepository;
import jakarta.transaction.Transactional;

@Service
public class TripService {

	@Autowired
	private TripRepository tripRepository;
	
	@Autowired
	private ImageRepository imageRepository;
	
	@Autowired
	private ReservationRepository reservationRepository;
	
	@Autowired
	private SupportService supportService;
	
	@Autowired
	private UserService userService;

	@Transactional
	public Trip createNewTrip(Trip trip, MultipartFile[] multipartFile) {
		try {
			Set<Image> immagini = new HashSet<>();
			for(MultipartFile file : multipartFile)
				immagini.add(imageRepository.save(new Image(file.getBytes())));
			trip.setImages(immagini);
		}
		catch (IOException e){}
		
		if(trip.getAvailability()==null)
			trip.setAvailability(0);
		if(trip.getPrice()==null)
			trip.setPrice(0.00);

		return 	this.tripRepository.save(trip);
	}

	@Transactional
	public void updateTrip(Trip trip) {
		tripRepository.save(trip);
	}

	public Trip findById(Long id) {
		return this.tripRepository.findById(id).orElse(null);
	}

	public Iterable<Trip> findAllTrip(){
		return this.tripRepository.findAll();
	}
	
	@Transactional
	public Trip saveTrip(Trip trip) {
		return this.tripRepository.save(trip);
	}

	public List<Trip> searchTrips(String category, String origin, String destination, LocalDate departureDate, LocalDate returnDate) {
		
		if (!category.isEmpty() && origin.isEmpty() && destination.isEmpty() && departureDate == null && returnDate == null) {
			return this.tripRepository.findByCategory(category);
		} else
			if (category.isEmpty() && !origin.isEmpty() && destination.isEmpty() && departureDate == null && returnDate == null) {
				return this.tripRepository.findByOrigin(origin);
		} else
			if (!category.isEmpty() && !origin.isEmpty() && destination.isEmpty() && departureDate == null && returnDate == null) {
				return this.tripRepository.findByCategoryAndOrigin(category, origin);
		} else 
			if (!category.isEmpty() && !origin.isEmpty() && !destination.isEmpty() && departureDate == null && returnDate == null) {
				return this.tripRepository.findByCategoryAndOriginAndDestination(category, origin, destination);
		} else 
			if (!category.isEmpty() && !origin.isEmpty() && destination.isEmpty() && departureDate != null && returnDate == null) {
				return this.tripRepository.findByCategoryAndOriginAndDeparturedate(category, origin, departureDate);
		} else 
			if (!category.isEmpty() && !origin.isEmpty() && !destination.isEmpty() && departureDate != null && returnDate == null) {
				return this.tripRepository.findByCategoryAndOriginAndDestinationAndDeparturedate(category, origin, destination, departureDate);
		} else 
			if (!category.isEmpty() && !origin.isEmpty() && !destination.isEmpty() && departureDate != null && returnDate != null) {
				return this.tripRepository.findByCategoryAndOriginAndDestinationAndDeparturedateAndReturndate(category, origin, destination, departureDate, returnDate);
		} else {
			return Collections.emptyList(); // Nessun parametro di ricerca fornito
		}
	}

	public String function(Model model, Trip trip, String username){
		model.addAttribute("trip", trip);
		if(username != null && this.alreadyReviewed(trip.getReviews(),username))
			model.addAttribute("hasNotAlredyCommented", false);
		else
			model.addAttribute("hasNotAlreadyCommented", true);
		model.addAttribute("review", new Review());
		model.addAttribute("reviews", trip.getReviews());
		model.addAttribute("hasReviews", !trip.getReviews().isEmpty());
		
		if(username != null && this.supportService.alreadyPartecipate(trip,username))
            model.addAttribute("hasPartecipated", true);
        else
            model.addAttribute("hasPartecipated", false);

		return "trip.html";
	}

	@Transactional
	public boolean alreadyReviewed(Set<Review> reviews,String username){
		if(reviews != null)
			for(Review rev : reviews)
				if(rev.getUsername().equals(username))
					return true;
		return false;
	}


	public long count() {
		return tripRepository.count();
	}
	
	@Transactional
	public void removeTrip(Long tripId) {
		Trip trip = this.findById(tripId);
		
		for(Reservation res : this.supportService.findAllReservation()) {
			if (res.getTrip().getId()==tripId) {
				res.getUser().getReservation().remove(res);
	        	this.userService.saveUser(res.getUser());
	            this.reservationRepository.delete(res);
			}
		}
		
		this.tripRepository.delete(trip);

	}

}
