package db;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import models.Point;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;


import java.util.List;
import java.util.Map;

@Named("pointsList")
@ApplicationScoped
public class PointDataModel extends LazyDataModel<Point> {
    @Inject
    private PointRepository pointRepository;

    @Override
    public int count(Map<String, FilterMeta> map) {
        System.out.println(pointRepository.getPointsCount());
        return pointRepository.getPointsCount();
    }

    @Override
    public List<Point> load(int first, int pageSize, Map<String, SortMeta> map, Map<String, FilterMeta> map1) {
        return pointRepository.getPoints();
    }
}
