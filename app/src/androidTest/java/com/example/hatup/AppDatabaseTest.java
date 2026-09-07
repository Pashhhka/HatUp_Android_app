package com.example.hatup;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.hatup.data.AppDatabase;
import com.example.hatup.data.ArtistDao;
import com.example.hatup.data.UserDao;
import com.example.hatup.model.Artist;
import com.example.hatup.model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class AppDatabaseTest {

    // Это правило заставляет Room выполнять все фоновые задачи синхронно (важно для тестов)
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AppDatabase db;
    private UserDao userDao;
    private ArtistDao artistDao;

    // Выполняется ПЕРЕД каждым тестом
    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        // Используем in-memory базу, чтобы не мусорить в реальной БД на телефоне
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries() // Разрешаем работу в главном потоке ТОЛЬКО для тестов
                .build();
        userDao = db.userDao();
        artistDao = db.artistDao();
    }

    // Выполняется ПОСЛЕ каждого теста
    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertAndGetUserTest() throws InterruptedException {
        // 1. Создаем тестового пользователя
        User user = new User();
        user.nickname = "TestUser";
        user.city = "Питер";

        // 2. Добавляем в базу
        userDao.insertUser(user);

        // 3. Достаем из базы
        User loadedUser = getOrAwaitValue(userDao.getCurrentUser());

        // 4. Проверяем, что данные совпали
        assertNotNull(loadedUser);
        assertEquals("TestUser", loadedUser.nickname);
        assertEquals("Питер", loadedUser.city);
    }

    @Test
    public void insertAndGetArtistTest() throws InterruptedException {
        // 1. Создаем тестового артиста
        Artist artist = new Artist();
        artist.name = "Король и Шут";
        artist.locationName = "Парк Победы";
        artist.rating = 5.0f;

        // 2. Добавляем
        artistDao.insertAll(java.util.Arrays.asList(artist));

        // 3. Ищем в базе по части имени (проверяем метод searchArtists)
        java.util.List<Artist> searchResults = getOrAwaitValue(artistDao.searchArtists("Шут"));

        // 4. Проверяем
        assertNotNull(searchResults);
        assertEquals(1, searchResults.size()); // Должен найтись ровно 1 артист
        assertEquals("Король и Шут", searchResults.get(0).name);
    }

    // Вспомогательный метод, чтобы заставить LiveData выдать значение "здесь и сейчас"
    private <T> T getOrAwaitValue(final LiveData<T> liveData) throws InterruptedException {
        final Object[] data = new Object[1];
        final CountDownLatch latch = new CountDownLatch(1);
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(T o) {
                data[0] = o;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
        latch.await(2, TimeUnit.SECONDS);
        return (T) data[0];
    }
}