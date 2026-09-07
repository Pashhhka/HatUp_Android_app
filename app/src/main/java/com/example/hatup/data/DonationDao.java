package com.example.hatup.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.hatup.model.BankCard;
import com.example.hatup.model.DonationGoal;
import com.example.hatup.model.DonationHistory;

import java.util.List;

@Dao
public interface DonationDao {

    // Получить все цели конкретного артиста
    @Query("SELECT * FROM donation_goals WHERE artist_id = :artistId")
    LiveData<List<DonationGoal>> getGoalsByArtist(long artistId);

    // Получить историю донатов (для вкладки "Поддержка")
    @Query("SELECT * FROM donations_history WHERE user_id = 1 ORDER BY timestamp DESC")
    LiveData<List<DonationHistory>> getUserDonationHistory();

    // Сделать донат (записать в историю)
    @Insert
    void insertDonation(DonationHistory donation);

    // Обновить прогресс цели (если это не "основной донат")
    @Query("UPDATE donation_goals SET current_amount = current_amount + :amount WHERE id = :goalId")
    void updateGoalProgress(long goalId, double amount);

    // Обновить общую сумму донатов пользователя
    @Query("UPDATE users SET total_donated_sum = total_donated_sum + :amount WHERE id = 1")
    void updateUserTotalDonated(double amount);

    // БАНКОВСКИЕ КАРТЫ
    @Query("SELECT * FROM bank_cards WHERE user_id = 1")
    LiveData<List<BankCard>> getUserCards();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addBankCard(BankCard card);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllGoals(List<DonationGoal> goals);


    @Query("SELECT COUNT(DISTINCT artist_id) FROM donations_history WHERE user_id = 1")
    LiveData<Integer> getSupportedArtistsCount();
}