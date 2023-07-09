package it.uniroma3.twm.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.twm.model.Image;
import it.uniroma3.twm.model.Trip;
import it.uniroma3.twm.service.ImageService;
import it.uniroma3.twm.service.TripService;

@Controller
public class ImageController {
	
	@Autowired
    private ImageService imageService;
	
	@Autowired
	private TripService tripService;
	

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> displayItemImage(@PathVariable("id") Long id) {
        Image img = this.imageService.findById(id);
        byte[] image = img.getBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }	

	@PostMapping("/admin/updateTripImages/{tripId}")
	public String addImageToTrip(@PathVariable("movieId") Long tripId, @RequestParam("tripImage") MultipartFile[] multipartFile, Model model) {
		Trip trip = this.tripService.findById(tripId);
		Set<Image> immagini = new HashSet<>();
		try {
			
			for(MultipartFile file : multipartFile)
				immagini.add(imageService.saveImage(new Image(file.getBytes())));
			
        }
        catch (IOException e){}
		trip.addImages(immagini);
		this.tripService.saveTrip(trip);
		model.addAttribute("trip", trip);
		return "/admin/formUpdateTrip.html";

	}

	@GetMapping("/admin/deleteImage/{tripId}/{imageId}")
	public String removeImage(@PathVariable("tripId") Long tripId, @PathVariable("imageId") Long imageId, Model model){
		Trip trip = this.tripService.findById(tripId);
		Image image = this.imageService.findById(imageId);

		trip.getImages().remove(image);
		this.imageService.deleteImage(image);
		this.tripService.saveTrip(trip);
		model.addAttribute("trip", trip);
		return "/admin/formUpdateTrip.html";
	}

}
