package com.example.hatup.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
        tableName = "favorite_artists",
        primaryKeys = {"user_id", "artist_id"},
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Artist.class, parentColumns = "id", childColumns = "artist_id", onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("artist_id")}
)
public class FavoriteArtist {
    @ColumnInfo(name = "user_id")
    public long userId;

    @ColumnInfo(name = "artist_id")
    public long artistId;
}