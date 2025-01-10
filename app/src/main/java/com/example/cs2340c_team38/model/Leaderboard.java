package com.example.cs2340c_team38.model;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Leaderboard {
    private static volatile Leaderboard leaderboard;
    private final List<ScoreEntry> scores;
    private volatile ScoreEntry recent;

    private Leaderboard() {
        this.scores = new CopyOnWriteArrayList<>();
    }

    public static Leaderboard getInstance() {
        if (leaderboard == null) {
            synchronized (Leaderboard.class) {
                if (leaderboard == null) {
                    leaderboard = new Leaderboard();
                }
            }
        }
        return leaderboard;
    }

    public void addScore(String playerName, int score) {
        ScoreEntry newEntry = new ScoreEntry(playerName, score);
        scores.add(newEntry);
        recent = newEntry;

        // Sort in descending order by score
        Collections.sort(scores, (a, b) -> b.getScore() - a.getScore());
    }

    public List<ScoreEntry> getTopScores(int limit) {
        return scores.subList(0, Math.min(limit, scores.size()));
    }

    public ScoreEntry getMostRecent() {
        return recent;
    }

    public void clearAll() {
        scores.clear();
    }

    public static class ScoreEntry {
        private String playerName;
        private int score;
        private Date dateTime;

        // Constructor
        public ScoreEntry(String playerName, int score) {
            this.playerName = playerName;
            this.score = score;
            this.dateTime = new Date();
            addToDatabase();
        }

        // Getters and setters
        public String getPlayerName() {
            return playerName;
        }

        public void setPlayerName(String playerName) {
            this.playerName = playerName;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public Date getDateTime() {
            return dateTime;
        }

        public void setDateTime(Date dateTime) {
            this.dateTime = dateTime;
        }

        // Add the score entry to Firebase Realtime Database
        private void addToDatabase() {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference leaderboardRef = database.getReference("leaderboard");

            leaderboardRef.push().setValue(this)
                    .addOnSuccessListener(aVoid -> System.out.println("Score added successfully!"))
                    .addOnFailureListener(e -> System.err.println("Failed to add score: " + e.getMessage()));
        }
    }
}
