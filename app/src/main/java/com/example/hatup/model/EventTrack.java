package com.example.hatup.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "event_tracks",
        foreignKeys = @ForeignKey(
                entity = Event.class,
                parentColumns = "id",
                childColumns = "event_id",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("event_id")}
)
public class EventTrack {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "event_id")
    public long eventId;

    @ColumnInfo(name = "track_name")
    public String trackName;

    @ColumnInfo(name = "order_index")
    public int orderIndex;
}