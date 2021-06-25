package co.edu.uniandes.dse.bookstore.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.bookstore.dto.ReviewDTO;
import co.edu.uniandes.dse.bookstore.entities.ReviewEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.services.ReviewService;

@RestController
@RequestMapping("/books")
public class ReviewController {

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping(value = "/{bookId}/reviews")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ReviewDTO createReview(@PathVariable("bookId") Long bookId, @RequestBody ReviewDTO review)
			throws EntityNotFoundException {
		ReviewEntity reviewEnity = modelMapper.map(review, ReviewEntity.class);
		ReviewEntity newReview = reviewService.createReview(bookId, reviewEnity);
		return modelMapper.map(newReview, ReviewDTO.class);
	}

	@GetMapping(value = "/{bookId}/reviews")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ReviewDTO> getReviews(@PathVariable("bookId") Long bookId) throws EntityNotFoundException {
		List<ReviewEntity> reviews = reviewService.getReviews(bookId);
		return modelMapper.map(reviews, new TypeToken<List<ReviewDTO>>() {}.getType());
	}
	
	@GetMapping(value = "/{bookId}/reviews/{reviewId}")
	@ResponseStatus(code = HttpStatus.OK)
    public ReviewDTO getReview(@PathVariable("bookId") Long bookId, @PathVariable("reviewId") Long reviewId) throws EntityNotFoundException {
        ReviewEntity entity = reviewService.getReview(bookId, reviewId);
        return modelMapper.map(entity, ReviewDTO.class);
    }
	
	@PutMapping(value = "/{bookId}/reviews/{reviewsId}")
	@ResponseStatus(code = HttpStatus.OK)
    public ReviewDTO updateReview(@PathVariable("bookId") Long bookId, @PathVariable("reviewsId") Long reviewId, @RequestBody ReviewDTO review) throws EntityNotFoundException {
		ReviewEntity reviewEntity = modelMapper.map(review, ReviewEntity.class);
        ReviewEntity newEntity = reviewService.updateReview(bookId, reviewId, reviewEntity);
        return modelMapper.map(newEntity, ReviewDTO.class);
    }
	
	@DeleteMapping(value = "/{bookId}/reviews/{reviewId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable("bookId") Long bookId, @PathVariable("reviewId") Long reviewId) throws EntityNotFoundException{
        reviewService.deleteReview(bookId, reviewId);
    }

}
