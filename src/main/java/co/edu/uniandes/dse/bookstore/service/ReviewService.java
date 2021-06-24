package co.edu.uniandes.dse.bookstore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.bookstore.entities.BookEntity;
import co.edu.uniandes.dse.bookstore.entities.ReviewEntity;
import co.edu.uniandes.dse.bookstore.exceptions.BusinessLogicException;
import co.edu.uniandes.dse.bookstore.repository.BookRepository;
import co.edu.uniandes.dse.bookstore.repository.ReviewRepository;
import lombok.Data;

@Data
@Service
public class ReviewService {

	@Autowired
	ReviewRepository reviewRepository;
	
	@Autowired
	BookRepository bookRepository;

	public ReviewEntity createReview(Long bookId, ReviewEntity reviewEntity) throws BusinessLogicException {
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
	
	public void deleteReview(Long bookId, Long reviewId) throws BusinessLogicException {
        ReviewEntity old = getReview(bookId, reviewId);
        if (old == null) {
            throw new BusinessLogicException("The review with id = " + reviewId + " is not associated to book with id = " + bookId);
        }
        reviewRepository.deleteById(old.getId());
    }
}
