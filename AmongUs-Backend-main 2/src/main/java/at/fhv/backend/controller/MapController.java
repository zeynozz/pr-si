package at.fhv.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import at.fhv.backend.services.MapService;

@Controller
public class MapController {
    private final MapService mapService;

    @Autowired
    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    @MessageMapping("/mapupdate")
    @SendTo("/topic/mapupdate")
    public int[][] updateMap(int[][] map) {
        mapService.setMap(map);
        return mapService.getMap();
    }

    @MessageMapping("/mapupdatebyposition")
    @SendTo("/topic/mapupdatebyposition")
    public int[][] updateMapByPosition(int x, int y, int value) {
        mapService.setMapbyPosition(x, y, value);
        return mapService.getMap();
    }
}
