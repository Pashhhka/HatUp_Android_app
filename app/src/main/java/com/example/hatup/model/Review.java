package com.example.hatup.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "reviews",
        foreignKeys = {
                @ForeignKey(entity = Artist.class, parentColumns = "id", childColumns = "artist_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id", onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("artist_id"), @Index("user_id")}
)
public class Review {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "artist_id")
    public long artistId;

    @ColumnInfo(name = "user_id")
    public long userId;

    public float rating;

    @ColumnInfo(name = "comment_text")
    public String commentText;

    public long timestamp;
}