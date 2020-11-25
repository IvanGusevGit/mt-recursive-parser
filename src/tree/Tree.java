package tree;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Tree {
    private String node;
    private List<Tree> children;

    private static int nextFreeId = 0;

    public Tree(String node, Tree ... children) {
        this.node = node;
        this.children = Arrays.asList(children);
    }

    public Tree(String node) {
        this.node = node;
    }

    public Graph collectToGraph() {
        nextFreeId = 1;
        return collectToGraph(this, 0);
    }

    private static Graph collectToGraph(Tree current, int currentId) {
        var graph = new Graph();
        graph.labels.add("A" + currentId + " = " + "\"" + current.node + "\"");
        graph.idValueMap.put(currentId, current.node);
        if (current.children != null) {
            for (var next : current.children) {
                int nextId = nextFreeId++;
                graph.edges.add(new Edge("A" + currentId, "A" + nextId));
                var subGraph = collectToGraph(next, nextId);
                graph.edges.addAll(subGraph.edges);
                graph.labels.addAll(subGraph.labels);
                graph.idValueMap.putAll(subGraph.idValueMap);
            }
        }
        return graph;
    }
}
