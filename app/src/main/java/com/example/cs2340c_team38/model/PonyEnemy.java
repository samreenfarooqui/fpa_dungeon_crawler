package com.example.cs2340c_team38.model;

public class PonyEnemy implements Enemy {
    private int x;
    private int y;

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void onCollisionWithPlayer(Player player) {
        // Implement what happens when an AlienEnemy collides with the player
    }

    @Override
    public void setPosition(int x, int y, TileType[][] tileMap) {

    }

    @Override
    public void addObserver(Observer o) {

    }

    @Override
    public void removeObserver(Observer o) {

    }

    @Override
    public void notifyObservers() {

    }
}
