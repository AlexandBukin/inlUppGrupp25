package se.su.inlupp;

import java.util.*;


public class BFSPathFinder<T> implements PathFinder<T> {


    @Override
    public Path<T> findPath(Graph<T> graph, T from, T to) {
        Map<T, Edge<T>> cameFrom = new HashMap<>();
        Queue<T> queue = new LinkedList<>();
        Set<T> visitedNodes = new HashSet<>();
        Map<T, T> previous = new HashMap<>();
        List<Edge<T>> path = new ArrayList<>();

        queue.add(from);
        visitedNodes.add(from);

        while (!queue.isEmpty()) {
            T current = queue.poll(); //tar ut första elementet
            if (current.equals(to)) {
                T reconstruct = to;
                while (!reconstruct.equals(from)) {
                    Edge<T> edge = cameFrom.get(reconstruct);
                    path.add(edge);
                    reconstruct = previous.get(reconstruct);
                }
                Collections.reverse(path);
                return new PathImpl<>(from, path);
            }
            for (Edge<T> edge : graph.getEdgesFrom(current)) {
                T neighbor = edge.getDestination();
                if (!visitedNodes.contains(neighbor)) {
                    visitedNodes.add(neighbor);
                    queue.add(neighbor);
                    cameFrom.put(neighbor, edge);
                    previous.put(neighbor, current);
                }

            }

        }
        return null;

    }


}

