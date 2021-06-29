/*
MIT License

Copyright (c) 2021 Universidad de los Andes - ISIS2603

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
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
