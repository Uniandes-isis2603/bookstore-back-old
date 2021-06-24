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

package co.edu.uniandes.dse.bookstore.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.bookstore.entities.BookEntity;
import co.edu.uniandes.dse.bookstore.entities.ReviewEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.repositories.BookRepository;
import co.edu.uniandes.dse.bookstore.repositories.ReviewRepository;
import lombok.Data;

@Data
@Service
public class ReviewService {

	@Autowired
	ReviewRepository reviewRepository;
	
	@Autowired
	BookRepository bookRepository;

	public ReviewEntity createReview(Long bookId, ReviewEntity reviewEntity) throws EntityNotFoundException {
		BookEntity book = bookRepository.findById(bookId).get();
		reviewEntity.setBook(book);
		return this.reviewRepository.save(reviewEntity);
	}
	
	public List<ReviewEntity> getReviews(Long bookId) {
        BookEntity bookEntity = bookRepository.findById(bookId).get();
        return bookEntity.getReviews();
    }
	
	public ReviewEntity getReview(Long bookId, Long reviewId) {
        return reviewRepository.findByBookIdAndId(bookId, reviewId);
    }
	
	public ReviewEntity updateReview(Long bookId, ReviewEntity reviewEntity) {
        BookEntity bookEntity = bookRepository.findById(bookId).get();
        reviewEntity.setBook(bookEntity);
        return this.reviewRepository.save(reviewEntity);
    }
	
	public void deleteReview(Long bookId, Long reviewId) throws EntityNotFoundException {
        ReviewEntity old = getReview(bookId, reviewId);
        if (old == null) {
            throw new EntityNotFoundException("The review with id = " + reviewId + " is not associated to book with id = " + bookId);
        }
        reviewRepository.deleteById(old.getId());
    }
}
