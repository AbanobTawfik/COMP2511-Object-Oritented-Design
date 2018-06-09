package Lab5;

import java.util.ArrayList;

public class GenericGraph<E> implements Graph<E> {
    private ArrayList<E> vertices = new ArrayList<E>();
    private int[][] edges = new int[0][];

    @Override
    public int[][] getEdges() {
        return edges;
    }

    @Override
    public int size() {
        return vertices.size();
    }

    @Override
    public ArrayList<E> getVertices() {
        return vertices;
    }

    @Override
    public E indexNode(int index) {
        return vertices.get(index);
    }

    @Override
    public int nodeIndex(E node) {
        return vertices.indexOf(node);
    }

    @Override
    public boolean removeNode(E node) {
        int index = nodeIndex(node);
        int[][] Override = new int[vertices.size() - 1][vertices.size() - 1];
        int k = 0;
        int x = 0;
        for (int i = 0; i < vertices.size(); i++) {
            if (i == index)
                continue;
            for (int j = 0; j < vertices.size(); j++) {
                if (j == index)
                    continue;
                Override[x][k] = edges[i][j];
                k++;
            }
            x++;
            k = 0;
        }
        vertices.remove(node);
        return true;
    }

    @Override
    public ArrayList<E> getNeighbours(E node) {
        int index = nodeIndex(node);
        ArrayList<E> neighbours = new ArrayList<E>();
        for (int i = 0; i < vertices.size(); i++) {
            if (edges[i][index] == 1)
                neighbours.add(vertices.get(i));
        }
        return neighbours;
    }

    @Override
    public boolean isConnected(E node1, E node2) {
        int index1 = nodeIndex(node1);
        int index2 = nodeIndex(node2);
        return ((edges[index1][index2] == 1) && (edges[index2][index1] == 1));
    }

    @Override
    public boolean addEdge(E node1, E node2) {
        if (!(vertices.contains(node1) || (vertices.contains(node2))))
            return false;
        int index1 = nodeIndex(node1);
        int index2 = nodeIndex(node2);
        edges[index1][index2] = 1;
        return false;
    }

    @Override
    public boolean addNode(E node) {
        if (vertices.contains(node))
            return false;
        int[][] Override = new int[vertices.size()+1][vertices.size()+1];
        if(vertices.size() == 0){
            Override[0][0] = 0;

        }else {
            for (int i = 0; i < vertices.size(); i++)
                for (int j = 0; j < vertices.size(); j++)
                    Override[i][j] = edges[i][j];
            for (int i = 0; i < vertices.size(); i++)
                edges[i][vertices.size() - 1] = -1;
            for (int i = 0; i < vertices.size(); i++)
                edges[vertices.size() - 1][i] = -1;
        }
        edges = Override;
        vertices.add(node);
        return true;
    }

    @Override
    public void print(){
        for(int i = 0; i < vertices.size(); i++){
            System.out.print("<" + vertices.get(i) + "> -> [");
            for(int j = 0; j < vertices.size(); j++){
                if(edges[i][j] == 1)
                    System.out.print(vertices.get(j) + ", ");
            }
            System.out.println("]");
        }
    }
}
