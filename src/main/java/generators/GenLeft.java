package generators;

import calculate.Edge;
import calculate.KochFractal;
import calculate.KochManager;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class GenLeft extends Task<ArrayList<Edge>> implements Callable<ArrayList<Edge>>, Generator {
    private ArrayList<Edge> edges;
    private int nxt;
    private KochManager kochManager;
    private KochFractal koch;

    public GenLeft(KochManager kochManager, int nxt) {
        this.kochManager = kochManager;
        this.koch = new KochFractal();
        this.edges = new ArrayList<Edge>();
        this.nxt = nxt;
    }

    @Override
    public ArrayList<Edge> call() {
        koch.addObserver(this);
        this.kochManager.setNumberEdges(this.koch.setLevel(nxt));
        koch.generateLeftEdge();
        this.kochManager.done(edges);
        return edges;
    }

    @Override
    public void update(Edge e) {
        edges.add(e);
        updateProgress(edges.size(), this.kochManager.getNumberEdges()/3);
        updateMessage(String.valueOf(edges.size()));
    }
}
