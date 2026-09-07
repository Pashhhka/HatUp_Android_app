package com.example.hatup.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
        User.class, Artist.class, Event.class, EventTrack.class,
        DonationGoal.class, DonationHistory.class, BankCard.class,
        Review.class, FavoriteArtist.class, FavoriteGenre.class
}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract ArtistDao artistDao();
    public abstract EventDao eventDao();
    public abstract DonationDao donationDao();

    private static volatile AppDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "hatup_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                UserDao userDao = INSTANCE.userDao();
                ArtistDao artistDao = INSTANCE.artistDao();
                EventDao eventDao = INSTANCE.eventDao();

                User currentUser = new User();
                currentUser.nickname = "Слушатель HatUp";
                currentUser.city = "Москва";
                currentUser.visitedEventsCount = 0;
                currentUser.totalDonatedSum = 0.0;
                userDao.insertUser(currentUser);

                Artist artist1 = new Artist();
                artist1.name = "Группа КИНО (Каверы)";
                artist1.locationName = "Арбат";
                artist1.rating = 4.8f;
                artist1.description = "Легенды русского рока на улицах столицы.";

                Artist artist2 = new Artist();
                artist2.name = "Джаз-банда 'Саксофон'";
                artist2.locationName = "Парк Горького";
                artist2.rating = 5.0f;
                artist2.description = "Вечерний джаз, импровизации и хорошее настроение.";

                Artist artist3 = new Artist();
                artist3.name = "Скрипач-виртуоз";
                artist3.locationName = "Невский проспект";
                artist3.rating = 4.9f;
                artist3.description = "Классика и современные хиты на скрипке.";

                artistDao.insertAll(java.util.Arrays.asList(artist1, artist2, artist3));

                FavoriteArtist fav1 = new FavoriteArtist();
                fav1.userId = 1;
                fav1.artistId = 1;

                FavoriteArtist fav2 = new FavoriteArtist();
                fav2.userId = 1;
                fav2.artistId = 2;

                artistDao.addToFavorites(fav1);
                artistDao.addToFavorites(fav2);

                long currentTime = System.currentTimeMillis();
                long oneDay = 86400000L;

                Event event1 = new Event();
                event1.artistId = 1;
                event1.title = "Вечер памяти В. Цоя";
                event1.address = "ул. Старый Арбат, д. 10";
                event1.timestamp = currentTime + oneDay;
                event1.attendeesCount = 150;

                Event event2 = new Event();
                event2.artistId = 2;
                event2.title = "Джаз на закате";
                event2.address = "Парк Горького, у фонтана";
                event2.timestamp = currentTime + (oneDay * 3);
                event2.attendeesCount = 45;

                Event event3 = new Event();
                event3.artistId = 3;
                event3.title = "Уличная классика";
                event3.address = "ВДНХ, Главный вход";
                event3.timestamp = currentTime + (oneDay * 2);
                event3.attendeesCount = 80;

                Event event4 = new Event();
                event4.artistId = 1;
                event4.title = "Рок-акустика";
                event4.address = "Площадь Революции";
                event4.timestamp = currentTime + (oneDay * 7);
                event4.attendeesCount = 200;

                eventDao.insertAll(java.util.Arrays.asList(event1, event2, event3, event4));


                DonationDao donationDao = INSTANCE.donationDao();

                DonationGoal g1 = new DonationGoal();
                g1.artistId = 1;
                g1.title = "Музыкальные инструменты";
                g1.currentAmount = 7350;
                g1.targetAmount = 10000;

                DonationGoal g2 = new DonationGoal();
                g2.artistId = 1;
                g2.title = "Студийная запись";
                g2.currentAmount = 3300;
                g2.targetAmount = 7000;

                donationDao.insertAllGoals(java.util.Arrays.asList(g1, g2));
            });
        }
    };
}