package com.haibusiness.szweb.service;

import com.haibusiness.szweb.dao.BuildingDao;
import com.haibusiness.szweb.entity.Building;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class BuildingServiceImpl implements BuildingService {

    private BuildingDao buildingDao;
    @Override
    public Building saveBuilding(Building building) {

        return buildingDao.save(building);
    }

    @Override
    public void removeBuilding(Long id) {

        buildingDao.deleteById(id);
    }

    @Override
    public void removeBuildingsInBatch(List<Building> buildings) {

        buildingDao.deleteInBatch(buildings);
    }

    @Override
    public Building updateBuilding(Building building) {

        return buildingDao.save(building);
    }

    @Override
    public Building getBuildingById(Long id) {

        return buildingDao.getOne(id);
    }

    @Override
    public List<Building> listBuildings() {

        return buildingDao.findAll();
    }
    @Override
    public Page<Building> listBuildingsByTypeAndTitleLike(String type, String title, Pageable pageable) {
        title = "%" + title + "%";
        return buildingDao.findByTypeAndTitleLikeOrderByUpdateTimeDesc(type, title, pageable);
    }


}
