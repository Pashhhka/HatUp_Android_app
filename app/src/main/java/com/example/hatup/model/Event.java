package com.example.hatup.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "events",
        foreignKeys = @ForeignKey(
                entity = Artist.class,
                parentColumns = "id",
                childColumns = "artist_id",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("artist_id")}
)
public class Event {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "artist_id")
    public long artistId;

    public String title;

    public long timestamp;

    public double latitude;
    public double longitude;

    public String address;

    @ColumnInfo(name = "attendees_count")
    public int attendeesCount = 0;

    @ColumnInfo(name = "is_past")
    public boolean isPast = false;

    @ColumnInfo(name = "is_active_now")
    public boolean isActiveNow = false;
}