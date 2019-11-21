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
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import timeutil.TimeStamp;

/**
 * @author Nico Kuijpers
 * Modified for FUN3 by Gertjan Schouten
 */
public class KochManager {
    private boolean interupt;
//    private KochFractal koch;
    private ArrayList<Edge> edges;
    private FUN3KochFractalFX application;
    private TimeStamp tsCalc;
    private TimeStamp tsDraw;
    private int done = 0;
    private Task<ArrayList<Edge>> genLeft;
    private Task<ArrayList<Edge>> genRight;
    private Task<ArrayList<Edge>> genBottom;

    private Thread t1;
    private Thread t2;
    private Thread t3;
    private Thread tr;
    private ArrayList<Thread> threads;
    private boolean completed;
    private int numberEdges = 0;

    //Bindings
//    private DoubleProperty progressLeft = new SimpleDoubleProperty(this, "progressLeft", 4);
//    private DoubleProperty progressRight = new SimpleDoubleProperty(this, "progressRight",4);
//    private DoubleProperty progressBottom = new SimpleDoubleProperty(this, "progressBottom",4);

//    private IntegerProperty edgesLeftText;
//    private IntegerProperty edgesRightText;
//    private IntegerProperty edgesBottomText;
//    private int edgesLeft;
//    private int edgesRight;
//    private int edgesBottom;


    public int getNumberEdges() {
        return numberEdges;
    }

    public void setNumberEdges(int numberEdges) {
        this.numberEdges = numberEdges;
    }

    public KochManager(FUN3KochFractalFX application) {
        this.edges = new ArrayList<Edge>();
//        this.koch = new KochFractal(this);
        this.application = application;
        this.tsCalc = new TimeStamp();
        this.tsDraw = new TimeStamp();
        this.interupt = false;
        this.threads = new ArrayList<Thread>();
        completed = false;
//        edgesLeftText = new SimpleIntegerProperty(this, "edgesLeftText", 0);
//        edgesRightText = new SimpleIntegerProperty(this, "edgesRightText", 0);
//        edgesBottomText = new SimpleIntegerProperty(this,  "edgesBottomText", 0);
    }

    public void changeLevel(int nxt) {
        if (this.threads.size() > 0) {
            this.threads.clear();
        }
        this.completed = false;
        this.interupt = false;
        this.done = 0;
        edges.clear();
//        koch.setLevel(nxt);
        tsCalc.init();
        tsCalc.setBegin("Begin calculating");

        // Thread pool
        ExecutorService pool = Executors.newFixedThreadPool(3);
        //Threads
        genLeft = new GenLeft(this, nxt);
//        this.t1 = new Thread(genLeft);
//        threads.add(t1);
//        t1.start();
//        Future<ArrayList<Edge>> futLeft = (Future<ArrayList<Edge>>) pool.submit(genLeft);
        genBottom = new GenBottom(this, nxt);
//        this.t2 = new Thread(genBottom);
//        threads.add(t2);
//        t2.start();
//        Future<ArrayList<Edge>> futBottom = (Future<ArrayList<Edge>>) pool.submit(genBottom);
        genRight = new GenRight(this, nxt);
//        this.t3 = new Thread(genRight);
//        threads.add(t3);
//        t3.start();
//        Future<ArrayList<Edge>> futRight = (Future<ArrayList<Edge>>) pool.submit(genRight);


//        ArrayList<Edge> edgeLeft = null;
//        try {
//            edgeLeft = futLeft.get();
//            edges.addAll(edgeLeft);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        ArrayList<Edge> edgeBottom = null;
//        try {
//            edgeBottom = futBottom.get();
//            edges.addAll(edgeBottom);
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        ArrayList<Edge> edgeRight = null;
//        try {
//            edgeRight = futRight.get();
//            edges.addAll(edgeRight);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }


        setUpFrontEnd();


        pool.submit(genLeft);
        pool.submit(genBottom);
        pool.submit(genRight);
        pool.shutdown();

//        Runnable runner = new Runner(this, this.koch);
//        this.tr = new Thread(runner);
//        tr.start();



//        while (this.done < 3) {
//            System.out.println("idk");
//        }
//        if (done == 3) {
//            System.out.println("idk");

//        }


//        while (!interupt) {
//            if (done == 3) {
//                this.interupt = true;
//                t1.interrupt();
//                t2.interrupt();
//                t3.interrupt();
//            }
//        }

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

    public Edge addEdge(Edge e) {
//        return edges.add(e);
        return e;
    }

    public synchronized void done(ArrayList<Edge> edges) {
        done++;
        this.edges.addAll(edges);
        checkIfDone();
    }

    public boolean isInterupt() {
        return interupt;
    }

    public void setInterupt(boolean interupt) {
        this.interupt = interupt;
    }

//    public void updateLeft(double progress){
//        edgesLeft++;
//        setEdgesLeftText(edgesLeft);
////        application.setProgressLeft(progress);
//    }

//    public void updateRight(double progress){
//        edgesRight++;
//        setEdgesRightText(edgesRight);
////        application.setProgressRight(progress);
//    }
//
//    public void updateBottom(double progress){
//        edgesBottom++;
//        setEdgesBottomText(edgesBottom);
//        double numberEdges = getNumberEdges();
//        double bottom = progress/(numberEdges/3);
////        application.setProgressBottom(bottom);
//    }
//
//    public IntegerProperty edgesLeftTextProperty() {
//        return edgesLeftText;
//    }
//
//    public IntegerProperty edgesRightTextProperty() {
//        return edgesRightText;
//    }
//
//    public IntegerProperty edgesBottomTextProperty() {
//        return edgesBottomText;
//    }
//
//    public void setEdgesLeftText(int edgesLeftText) {
//        this.edgesLeftText.set(edgesLeftText);
//    }
//
//    public void setEdgesRightText(int edgesRightText) {
//        this.edgesRightText.set(edgesRightText);
//    }
//
//    public void setEdgesBottomText(int edgesBottomText) {
//        this.edgesBottomText.set(edgesBottomText);
//    }

    public synchronized void checkIfDone() {
        if(done == 3) {
            tsCalc.setEnd("End calculating");
            application.setTextNrEdges("" + this.getNumberEdges());
            application.setTextCalc(tsCalc.toString());
            application.requestDrawEdges();
        }
    }

    //    public void everythingDone() {
//        this.completed = true;
//        tsCalc.setEnd("End calculating");
//        application.setTextNrEdges("" + koch.getNrOfEdges());
//        application.setTextCalc(tsCalc.toString());
//        application.requestDrawEdges();
//    }
//
//    public void checkDone() {
//        if (!this.completed) {
//            for (Thread t : threads) {
//                if (!t.getState().equals(Thread.State.TERMINATED)) {
//                    this.interupt = false;
//                    break;
//                }
//            }
//            everythingDone();
//        }
//    }
}
