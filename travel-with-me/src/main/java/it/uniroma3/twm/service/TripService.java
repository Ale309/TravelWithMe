package it.uniroma3.twm.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.twm.model.Image;
import it.uniroma3.twm.model.Review;
import it.uniroma3.twm.model.Trip;
import it.uniroma3.twm.repository.ImageRepository;
import it.uniroma3.twm.repository.TripRepository;
import jakarta.transaction.Transactional;

@Service
public class TripService {

	@Autowired
	private TripRepository tripRepository;
	@Autowired
	private ImageRepository imageRepository;

	@Transactional
	public Trip createNewTrip(Trip trip, MultipartFile[] multipartFile) {
		try {
			Set<Image> immagini = new HashSet<>();
			for(MultipartFile file : multipartFile)
				immagini.add(imageRepository.save(new Image(file.getBytes())));
			trip.setImages(immagini);
        }
        catch (IOException e){}

		return 	this.tripRepository.save(trip);
	}
	
    @Transactional
    public void updateTrip(Trip trip) {
        tripRepository.save(trip);
    }
    
    @Transactional
	public Trip findById(Long id) {
		return this.tripRepository.findById(id).orElse(null);
	}

	public Iterable<Trip> findAllTrip(){
		return this.tripRepository.findAll();
	}

	public Trip saveTrip(Trip trip) {
		return this.tripRepository.save(trip);
	}

	public List<Trip> findByCategory(String category) {
		return this.tripRepository.findByCategory(category);
	}
	
	public List<Trip> findByOrigin(String origin) {
		return this.tripRepository.findByOrigin(origin);
	}
	
	public List<Trip> findByCategoryAndOrigin(String category, String origin) {
		return this.tripRepository.findByCategoryAndOrigin(category, origin);
	}
	
	public List<Trip> findByCategoryAndOriginAndDestination(String category, String origin, String destination) {
		return this.tripRepository.findByCategoryAndOriginAndDestination(category, origin, destination);
	}
	
	public List<Trip> findByCategoryAndOriginAndDeparturedate(String category, String origin, LocalDate departuredate) {
		return this.tripRepository.findByCategoryAndOriginAndDeparturedate(category, origin, departuredate);
	}
	
	public List<Trip> findByCategoryAndOriginAndDestinationAndDeparturedate(String category, String origin, String destination, LocalDate departuredate) {
		return this.tripRepository.findByCategoryAndOriginAndDestinationAndDeparturedate(category, origin, destination, departuredate);
	}
	
	public List<Trip> findByCategoryAndOriginAndDestinationAndDeparturedateAndReturndate(String category, String origin, String destination, LocalDate departuredate, LocalDate returndate) {
		return this.tripRepository.findByCategoryAndOriginAndDestinationAndDeparturedateAndReturndate(category, origin, destination, departuredate, returndate);
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

}
