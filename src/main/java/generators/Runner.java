package generators;

import calculate.KochFractal;
import calculate.KochManager;

public class Runner implements Runnable {
    private KochFractal kochFractal;
    private KochManager kochManager;

    public Runner(KochManager kochManager, KochFractal kochFractal) {
        this.kochManager = kochManager;
        this.kochFractal = kochFractal;
    }

    @Override
    public void run() {
//        while(! this.kochManager.isInterupt()){
//            try {
//                wait();
//            } catch (InterruptedException e)  {
//                Thread.currentThread().interrupt();
//                e.printStackTrace();
//            }
//        }
        //this.kochManager.checkDone();
    }
}
