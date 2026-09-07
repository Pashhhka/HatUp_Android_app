package com.example.hatup.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "artists")
public class Artist {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;

    @ColumnInfo(name = "avatar_path")
    public String avatarPath;

    @ColumnInfo(name = "header_path")
    public String headerPath;

    @ColumnInfo(name = "location_name")
    public String locationName;

    public float rating;
    public String description;

    @ColumnInfo(name = "vk_link")
    public String vkLink;

    @ColumnInfo(name = "tg_link")
    public String tgLink;
}