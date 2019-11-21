/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
import java.util.concurrent.*;

import fun3kochfractalfx.FUN3KochFractalFX;
import generators.GenBottom;
import generators.GenLeft;
import generators.GenRight;
import javafx.concurrent.Task;
import timeutil.TimeStamp;

/**
 * @author Nico Kuijpers
 * Modified for FUN3 by Gertjan Schouten
 */
public class KochManager {
    private ArrayList<Edge> edges;
    private FUN3KochFractalFX application;
    private TimeStamp tsCalc;
    private TimeStamp tsDraw;
    private int done = 0;
    private Task<ArrayList<Edge>> genLeft;
    private Task<ArrayList<Edge>> genRight;
    private Task<ArrayList<Edge>> genBottom;

    private int numberEdges = 0;

    public int getNumberEdges() {
        return numberEdges;
    }

    public void setNumberEdges(int numberEdges) {
        this.numberEdges = numberEdges;
    }

    public KochManager(FUN3KochFractalFX application) {
        this.edges = new ArrayList<Edge>();
        this.application = application;
        this.tsCalc = new TimeStamp();
        this.tsDraw = new TimeStamp();
    }

    public void changeLevel(int nxt) {
        this.done = 0;
        edges.clear();
        tsCalc.init();
        tsCalc.setBegin("Begin calculating");

        // Thread pool
        ExecutorService pool = Executors.newFixedThreadPool(3);
        //Tasks
        genLeft = new GenLeft(this, nxt);
        genBottom = new GenBottom(this, nxt);
        genRight = new GenRight(this, nxt);

        setUpFrontEnd();

        pool.submit(genLeft);
        pool.submit(genBottom);
        pool.submit(genRight);
        pool.shutdown();

    }

    public void drawEdges() {
        tsDraw.init();
        tsDraw.setBegin("Begin drawing");
        application.clearKochPanel();
        for (Edge e : edges) {
            application.drawEdge(e);
        }
        tsDraw.setEnd("End drawing");
        application.setTextDraw(tsDraw.toString());
    }

    public void setUpFrontEnd() {
        application.getPbLeft().progressProperty().bind(genLeft.progressProperty());
        application.getPbRight().progressProperty().bind(genRight.progressProperty());
        application.getPbBottom().progressProperty().bind(genBottom.progressProperty());

        application.getLabelProgressLeftEdgesText().textProperty().bind(genLeft.messageProperty());
        application.getLabelProgressRightEdgesText().textProperty().bind(genRight.messageProperty());
        application.getLabelProgressBottomEdgesText().textProperty().bind(genBottom.messageProperty());
    }

    public synchronized void done(ArrayList<Edge> edges) {
        done++;
        this.edges.addAll(edges);
        checkIfDone();
    }

    public synchronized void checkIfDone() {
        if(done == 3) {
            tsCalc.setEnd("End calculating");
            application.setTextNrEdges("" + this.getNumberEdges());
            application.setTextCalc(tsCalc.toString());
            application.requestDrawEdges();
        }
    }
}
