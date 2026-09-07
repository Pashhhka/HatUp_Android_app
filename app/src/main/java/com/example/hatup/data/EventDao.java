package com.example.hatup.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.hatup.model.Event;
import com.example.hatup.model.EventTrack;

import java.util.List;

@Dao
public interface EventDao {

    // Лента (ближайшие не прошедшие события)
    @Query("SELECT * FROM events WHERE is_past = 0 ORDER BY timestamp ASC")
    LiveData<List<Event>> getUpcomingEvents();

    // Карта (события, которые идут ПРЯМО СЕЙЧАС - желтая нота)
    @Query("SELECT * FROM events WHERE is_active_now = 1")
    LiveData<List<Event>> getActiveEventsForMap();

    // Детали события
    @Query("SELECT * FROM events WHERE id = :eventId")
    LiveData<Event> getEventById(long eventId);

    // Треклист для карточки события
    @Query("SELECT * FROM event_tracks WHERE event_id = :eventId ORDER BY order_index ASC")
    LiveData<List<EventTrack>> getTracksForEvent(long eventId);

    // АНОНСЫ (Гениальный запрос: будущие ивенты только от любимых артистов)
    @Query("SELECT events.* FROM events " +
            "INNER JOIN favorite_artists ON events.artist_id = favorite_artists.artist_id " +
            "WHERE favorite_artists.user_id = 1 " +
            "AND events.is_past = 0 " +
            "ORDER BY events.timestamp ASC")
    LiveData<List<Event>> getAnnouncements();

    // Кнопка "Я приду"
    @Query("UPDATE events SET attendees_count = attendees_count + 1 WHERE id = :eventId")
    void incrementAttendees(long eventId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Event> events);

    // 1. Ближайшие (сортировка по времени, берем 10 штук)
    @Query("SELECT * FROM events ORDER BY timestamp ASC LIMIT 10")
    LiveData<List<Event>> getNearestEvents();

    // 2. Рекомендованные (ивенты только от любимых артистов)
    @Query("SELECT events.* FROM events " +
            "INNER JOIN favorite_artists ON events.artist_id = favorite_artists.artist_id " +
            "WHERE favorite_artists.user_id = 1 " +
            "ORDER BY events.timestamp ASC")
    LiveData<List<Event>> getRecommendedEvents();

    // 3. Популярные (сортировка по количеству "Я приду" и по времени)
    @Query("SELECT * FROM events ORDER BY attendees_count DESC, timestamp ASC LIMIT 10")
    LiveData<List<Event>> getPopularEvents();
}