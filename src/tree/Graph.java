package tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {

    List<String> labels = new ArrayList<>();
    List<Edge> edges = new ArrayList<>();
    Map<Integer, String> idValueMap = new HashMap<>();

    public List<String> toDot() {
        List<String> result = new ArrayList<>();
        result.add("digraph Q {");
        result.add("    node [shape=record];");
        for (int i = 0; i < labels.size(); i++) {
            result.add("    A" + i + "  [label = \"" + idValueMap.get(i) + "\"];");
        }
        for (var edge : edges) {
            result.add("    " + edge.from + " -> " + edge.to);
        }
        result.add("}");
        return result;
    }

}
