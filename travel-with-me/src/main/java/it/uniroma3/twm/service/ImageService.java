package it.uniroma3.twm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.twm.model.Image;
import it.uniroma3.twm.repository.ImageRepository;

@Service
public class ImageService {
	
	@Autowired
	private ImageRepository imageRepository;

	public Image findById(Long id) {
		return this.imageRepository.findById(id).get();
	}
	
	public Image saveImage(Image image) {
		return this.imageRepository.save(image);
	}
	

	public void deleteImage(Image image) {
		this.imageRepository.delete(image);
		
	}

}
