package com.example.hatup.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "donations_history",
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Artist.class, parentColumns = "id", childColumns = "artist_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = DonationGoal.class, parentColumns = "id", childColumns = "goal_id", onDelete = ForeignKey.SET_NULL)
        },
        indices = {@Index("user_id"), @Index("artist_id"), @Index("goal_id")}
)
public class DonationHistory {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "user_id")
    public long userId;

    @ColumnInfo(name = "artist_id")
    public long artistId;

    @ColumnInfo(name = "goal_id")
    public Long goalId;

    public double amount;
    public String comment;
    public long timestamp;
}