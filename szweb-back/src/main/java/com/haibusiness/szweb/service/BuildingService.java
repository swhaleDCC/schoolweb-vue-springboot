package com.haibusiness.szweb.service;

import com.haibusiness.szweb.entity.Building;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BuildingService {
    Building saveBuilding(Building building);


    void removeBuilding(Long id);


    void removeBuildingsInBatch(List<Building> buildings);

    Building updateBuilding(Building building);

    Building getBuildingById(Long id);


    List<Building> listBuildings();


    Page<Building> listBuildingsByTypeAndTitleLike(String type, String title, Pageable pageable);
}
