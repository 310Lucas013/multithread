import calculate.KochManager;
import fun3kochfractalfx.FUN3KochFractalFX;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class KochManagerTest {

    private KochManager kochManager;
    private FUN3KochFractalFX application;

    @BeforeEach
    public void setup() {
        this.application = new FUN3KochFractalFX();
        kochManager = new KochManager(this.application);
    }

//    @Test
//    public void increaseLevel() {
//        this.kochManager.changeLevel(2);
//        Assert.assertEquals(12, this.kochManager.getNumberEdges());
//    }

    @Test
    public void numberOfEdges() {
        Assert.assertEquals(0, this.kochManager.getNumberEdges());
    }
}
