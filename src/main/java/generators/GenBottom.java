package generators;

import calculate.Edge;
import calculate.KochFractal;
import calculate.KochManager;
import javafx.concurrent.Task;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class GenBottom extends Task<ArrayList<Edge>> implements Callable<ArrayList<Edge>>, Generator {
    private ArrayList<Edge> edges;
    private int nxt;
    private KochManager kochManager;
    private KochFractal koch;

    public GenBottom(KochManager kochManager, int nxt /*, KochFractal koch*/) {
        this.kochManager = kochManager;
        this.koch = new KochFractal();
        this.edges = new ArrayList<Edge>();
        this.nxt = nxt;
    }
//
//    @Override
//    public void run() {
//        this.kochManager.setNumberEdges(this.koch.setLevel(nxt));
//        edges = koch.generateBottomEdge();
//        this.kochManager.done(edges);
//        //this.kochManager.checkDone();
//    }

    @Override
    public ArrayList<Edge> call() throws Exception {
        koch.addObserver(this);
        this.kochManager.setNumberEdges(this.koch.setLevel(nxt));
        koch.generateBottomEdge();
        //edges = koch.generateBottomEdge();
        //this.kochManager.done(edges);
        //this.kochManager.checkDone();
        this.kochManager.done(edges);
        return edges;
    }

    @Override
    public void update(Edge e) {
        edges.add(e);
        updateProgress(edges.size(), this.kochManager.getNumberEdges()/3);
        updateMessage(String.valueOf(edges.size()));
        //this.kochManager.updateBottom(this.edges.size());
    }
}
