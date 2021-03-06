package controller;

import javafx.application.Application;
import model.Node;
import model.scheduler.AbstractScheduler;
import org.graphstream.graph.Edge;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.List;
import java.util.Set;

/**
 * This is the class that Main class calls when client requests visualisation. This class creates
 * an GUIMain object ad stores appropriate fields that will be used by the GUI .
 */
public class GUIEntry {
    private static List<Node> _nodes;
    private static String _filename;
    private static int _numProcessor;
    private static int _parallelised;
    private static SingleGraph _graph = new SingleGraph("graph");
    private static Controller _controller;
    private static GUITimer _timer;
    private static AbstractScheduler _scheduler;

    public GUIEntry(List<Node> nodes, AbstractScheduler scheduler, String filename, int numProcessor, int parallelised) {
        _nodes = nodes;
        _filename = filename;
        _numProcessor = numProcessor;
        _parallelised = parallelised;
        _scheduler = scheduler;
        createGraph();

        //creates a new timer instance
        _timer = new GUITimer();

        //displays the JavaFX windwo
        Thread GUI = new Thread() {
            public void run()  {
                Application.launch(GUIMain.class);
            }
        };
        GUI.start();
    }

    /**
     * Getter methods used by the GUI.
     * @return
     */
    public static SingleGraph getGraph() {
        return _graph;
    }

    public static String getFilename() {
        return _filename;
    }

    public static int getNumProcessor() {
        return _numProcessor;
    }

    public static int getParallelised() {
        return _parallelised;
    }

    public static List<Node> getNodes() {
        return _nodes;
    }

    public static GUITimer getTimer() {
        return _timer;
    }

    public static AbstractScheduler getScheduler() {
        return _scheduler;
    }

    public static int getNumNode() {
        return _nodes.size();
    }


    /**
     * Method reads in raw input nodes and transform it into a SingleGraph instance used later for graph visualisation.
     * Nodes and edges are stored based on their ID. Edges are directed.
     *
     * Info such as node weight, processor number, start time is also stored which will be used in visualisation
     */
    private void createGraph() {
        //add nodes to SingleGraph
        for (Node n : _nodes) {
            org.graphstream.graph.Node node = _graph.addNode(n.getId());
            node.addAttribute("weight", String.valueOf(n.getWeight()));
            node.addAttribute("processor", "null");
            node.addAttribute("startTime", "null");


        }
        //add edges
        for (Node parent : _nodes) {
            Set<Node> children = parent.getChildren().keySet();
            for (Node c : children) {
                String id = parent.getId() + c.getId();
                Edge edge = _graph.addEdge(id, parent.getId(), c.getId(), true);
                edge.addAttribute("weight", String.valueOf(parent.getPathWeightToChild(c)));
            }
        }
    }

    /**
     * Getter and setter for controller
     * @return
     */
    public static Controller getController() {
        return _controller;
    }

    public static void setController(Controller controller) {
        _controller = controller;
    }

}

