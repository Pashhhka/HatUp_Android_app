package com.example.hatup.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.hatup.model.Artist;
import com.example.hatup.model.FavoriteArtist;
import com.example.hatup.model.Review;

import java.util.List;

@Dao
public interface ArtistDao {

    @Query("SELECT * FROM artists")
    LiveData<List<Artist>> getAllArtists();

    @Query("SELECT * FROM artists WHERE id = :artistId")
    LiveData<Artist> getArtistById(long artistId);

    // Поиск артистов по имени (для режима "Список" на вкладке Карта)
    @Query("SELECT * FROM artists WHERE name LIKE '%' || :searchQuery || '%'")
    LiveData<List<Artist>> searchArtists(String searchQuery);

    // ИЗБРАННОЕ
    @Query("SELECT artists.* FROM artists INNER JOIN favorite_artists ON artists.id = favorite_artists.artist_id WHERE favorite_artists.user_id = 1")
    LiveData<List<Artist>> getFavoriteArtists();

    // Проверка, в избранном ли артист (для подсветки сердечка)
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_artists WHERE user_id = 1 AND artist_id = :artistId)")
    LiveData<Boolean> isArtistFavorite(long artistId);

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_artists WHERE user_id = 1 AND artist_id = :artistId)")
    boolean isArtistFavoriteNow(long artistId); // Обычный boolean, не LiveData

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addToFavorites(FavoriteArtist favoriteArtist);

    @Query("DELETE FROM favorite_artists WHERE user_id = 1 AND artist_id = :artistId")
    void removeFromFavorites(long artistId);

    // ОТЗЫВЫ
    @Query("SELECT * FROM reviews WHERE artist_id = :artistId ORDER BY timestamp DESC")
    LiveData<List<Review>> getReviewsForArtist(long artistId);

    @Insert
    void insertReview(Review review);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Artist> artists);

}