package com.google.android.gms.maps.model;

public interface TileProvider {
    public static final Tile NO_TILE;

    static {
        NO_TILE = new Tile();
    }
}