import checkers.AreaChecker;
import models.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AreaCheckerTest {
    private AreaChecker areaChecker;

    @BeforeEach
    void setUp() {
        areaChecker = new AreaChecker();
    }

    @Test
    void checkHitInRect() {
        Point point = new Point(0.5, 0.5, 1);
        areaChecker.checkHit(point);
        assertTrue(point.getResult());
    }



    @Test
    void checkHitInSector() {
        Point point = new Point(0.1, -0.1, 0.5);
        areaChecker.checkHit(point);
        assertTrue(point.getResult());
    }

    @Test
    void checkHitInTriangle() {
        Point point = new Point(-0.1, -0.1, 0.5);
        areaChecker.checkHit(point);
        assertTrue(point.getResult());
    }

    @Test
    void checkMiss() {
        Point point = new Point(1, 1, 1);
        areaChecker.checkHit(point);
        assertFalse(point.getResult());
    }
}
