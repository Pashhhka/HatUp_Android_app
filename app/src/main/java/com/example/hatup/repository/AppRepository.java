package com.example.hatup.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.hatup.data.AppDatabase;
import com.example.hatup.data.ArtistDao;
import com.example.hatup.data.DonationDao;
import com.example.hatup.data.EventDao;
import com.example.hatup.data.UserDao;
import com.example.hatup.model.Artist;
import com.example.hatup.model.BankCard;
import com.example.hatup.model.DonationGoal;
import com.example.hatup.model.DonationHistory;
import com.example.hatup.model.Event;
import com.example.hatup.model.EventTrack;
import com.example.hatup.model.FavoriteArtist;
import com.example.hatup.model.FavoriteGenre;
import com.example.hatup.model.Review;
import com.example.hatup.model.User;

import java.util.List;

public class AppRepository {

    private UserDao userDao;
    private ArtistDao artistDao;
    private EventDao eventDao;
    private DonationDao donationDao;


    public AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        userDao = db.userDao();
        artistDao = db.artistDao();
        eventDao = db.eventDao();
        donationDao = db.donationDao();
        donationDao = db.donationDao();
    }

    public LiveData<User> getCurrentUser() {
        return userDao.getCurrentUser();
    }

    public void incrementVisitedEvents() {
        AppDatabase.databaseWriteExecutor.execute(() -> userDao.incrementVisitedEvents());
    }

    public LiveData<List<String>> getFavoriteGenres() {
        return userDao.getFavoriteGenres();
    }

    public void addFavoriteGenre(String genreName) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            FavoriteGenre genre = new FavoriteGenre();
            genre.userId = 1;
            genre.genreName = genreName;
            userDao.addFavoriteGenre(genre);
        });
    }

    public LiveData<List<Artist>> getAllArtists() {
        return artistDao.getAllArtists();
    }

    public LiveData<Artist> getArtistById(long artistId) {
        return artistDao.getArtistById(artistId);
    }

    public LiveData<List<Artist>> searchArtists(String query) {
        return artistDao.searchArtists(query);
    }

    public LiveData<List<Artist>> getFavoriteArtists() {
        return artistDao.getFavoriteArtists();
    }

    public LiveData<Boolean> isArtistFavorite(long artistId) {
        return artistDao.isArtistFavorite(artistId);
    }

    public void toggleFavoriteArtist(long artistId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            // Проверяем, существует ли запись прямо перед действием
            boolean exists = artistDao.isArtistFavoriteNow(artistId);
            if (exists) {
                artistDao.removeFromFavorites(artistId);
            } else {
                FavoriteArtist fav = new FavoriteArtist();
                fav.userId = 1;
                fav.artistId = artistId;
                artistDao.addToFavorites(fav);
            }
        });
    }

    public LiveData<List<Review>> getReviewsForArtist(long artistId) {
        return artistDao.getReviewsForArtist(artistId);
    }

    public void insertReview(Review review) {
        AppDatabase.databaseWriteExecutor.execute(() -> artistDao.insertReview(review));
    }

    public LiveData<List<Event>> getUpcomingEvents() {
        return eventDao.getUpcomingEvents();
    }

    public LiveData<List<Event>> getActiveEventsForMap() {
        return eventDao.getActiveEventsForMap();
    }

    public LiveData<Event> getEventById(long eventId) {
        return eventDao.getEventById(eventId);
    }

    public LiveData<List<EventTrack>> getTracksForEvent(long eventId) {
        return eventDao.getTracksForEvent(eventId);
    }

    public LiveData<List<Event>> getAnnouncements() {
        return eventDao.getAnnouncements();
    }

    public void attendEvent(long eventId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.incrementAttendees(eventId);
            userDao.incrementVisitedEvents();
        });
    }

    public LiveData<List<DonationGoal>> getGoalsByArtist(long artistId) {
        return donationDao.getGoalsByArtist(artistId);
    }





    public LiveData<User> getUserById(long userId) {
        return userDao.getCurrentUser();
    }

    public LiveData<List<DonationHistory>> getUserDonationHistory() {
        return donationDao.getUserDonationHistory();
    }

    public LiveData<List<BankCard>> getUserCards() {
        return donationDao.getUserCards();
    }

    public void addBankCard(BankCard card) {
        AppDatabase.databaseWriteExecutor.execute(() -> donationDao.addBankCard(card));
    }

    public void makeDonation(DonationHistory donation) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            donationDao.insertDonation(donation);
            userDao.addDonationAmount(donation.amount);
            if (donation.goalId != null && donation.goalId > 0) {
                donationDao.updateGoalProgress(donation.goalId, donation.amount);
            }
        });
    }

    public LiveData<Integer> getSupportedArtistsCount() {
        return donationDao.getSupportedArtistsCount();
    }

    public LiveData<List<Event>> getNearestEvents() {
        return eventDao.getNearestEvents();
    }

    public LiveData<List<Event>> getRecommendedEvents() {
        return eventDao.getRecommendedEvents();
    }

    public LiveData<List<Event>> getPopularEvents() {
        return eventDao.getPopularEvents();
    }


}