package com.example.mvmodelview;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Entity is the room annotation which at the compile time creates all the necessary codes ( programs ) to create the SQLite table for this object.

@Entity(tableName = "note_table")
public class Note
{
    // Room will automatically generate the columns for these fields ( variables ) defined below.
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title, description;

    // Generally column name will be same as our field ( variable ) name but we can change the column name using ColumnInfo() annotation. We used
    // same to name as the variable name to show you how to change the column name but you can name it anything you like.
    @ColumnInfo(name = "priority")
    private int priority;

    public Note(String title, String description, int priority)
    {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Below are the getters to get the fields. Alternatively we can make all the fields ( variables ) public instead of private then we don't need
    // getters but due to encapsulation it's better to have all fields private.

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}