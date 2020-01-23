package ty.henry.cinemaapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ty.henry.cinemaapp.dto.RatingForm;
import ty.henry.cinemaapp.model.Movie;
import ty.henry.cinemaapp.model.Rating;
import ty.henry.cinemaapp.model.RatingId;
import ty.henry.cinemaapp.model.User;
import ty.henry.cinemaapp.persistence.RatingRepository;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserService userService;

    public Rating findRatingByUserAndMovie(User user, Movie movie) {
        return ratingRepository.findByUserAndMovie(user, movie);
    }

    @Transactional
    public boolean rateMovie(User user, Movie movie, RatingForm ratingForm) {
        RatingId ratingId = new RatingId(movie.getId(), user.getId());
        boolean firstRate = !ratingRepository.findById(ratingId).isPresent();
        Rating rating = new Rating();
        rating.setId(ratingId);
        rating.setValue(ratingForm.getRatingValue());
        rating.setMovie(movie);
        rating.setUser(user);
        ratingRepository.save(rating);
        if(firstRate) {
            userService.addPointToUser(user);
        }
        return firstRate;
    }

    public int getRatingCountForMovie(Movie movie) {
        return ratingRepository.getRatingCountForMovie(movie);
    }

    public float getAvgRatingForMovie(Movie movie) {
        return ratingRepository.getAvgRatingForMovie(movie);
    }
}
