package com.example.hatup.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.hatup.model.FavoriteGenre;
import com.example.hatup.model.User;

import java.util.List;

@Dao
public interface UserDao {

    // Получить текущего пользователя (с id = 1)
    // LiveData позволяет UI автоматически обновляться при изменении данных в БД
    @Query("SELECT * FROM users WHERE id = 1 LIMIT 1")
    LiveData<User> getCurrentUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    // Обновить статистику посещенных мероприятий
    @Query("UPDATE users SET visited_events_count = visited_events_count + 1 WHERE id = 1")
    void incrementVisitedEvents();

    // ЖАНРЫ
    @Query("SELECT genre_name FROM favorite_genres WHERE user_id = 1")
    LiveData<List<String>> getFavoriteGenres();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addFavoriteGenre(FavoriteGenre genre);

    @Query("DELETE FROM favorite_genres WHERE user_id = 1 AND genre_name = :genreName")
    void removeFavoriteGenre(String genreName);

    @Query("UPDATE users SET total_donated_sum = total_donated_sum + :amount WHERE id = 1")
    void addDonationAmount(double amount);
}