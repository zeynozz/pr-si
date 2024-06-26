package at.fhv.backend.model;

import at.fhv.backend.loader.MapLoader;

public class Map {

    private int[][] map;

    public Map() {
    }

    public int[][] getMap() {
        return map;
    }

    public void setMap(int[][] mapArray) {
        this.map = mapArray;
    }

    public void setMapbyPosition(int x, int y, int value) {
        map[y][x] = value;
    }

    public void setInitialMap(String mapName) {
        if (map == null) {
            map = MapLoader.loadMapFromFile(mapName);
        }
    }

    public int getCellValue(int x, int y) {
        return map[y][x];
    }
}
