package com.example.hatup.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String nickname;
    public String city;

    @ColumnInfo(name = "avatar_path")
    public String avatarPath;

    @ColumnInfo(name = "visited_events_count")
    public int visitedEventsCount = 0;

    @ColumnInfo(name = "total_donated_sum")
    public double totalDonatedSum = 0.0;
}