package com.example.hatup.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "donation_goals")
public class DonationGoal {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "artist_id")
    public long artistId;

    public String title;

    @ColumnInfo(name = "current_amount")
    public double currentAmount;

    @ColumnInfo(name = "target_amount")
    public double targetAmount;
}