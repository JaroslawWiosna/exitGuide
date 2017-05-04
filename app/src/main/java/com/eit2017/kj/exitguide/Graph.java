package com.eit2017.kj.exitguide;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;


public class Graph
{
    // No. of vertices
    private int V;

    // Array of lists for Adjacency List Representation
    private LinkedList<Integer> adj[];
    // Array of lists for shortest path
    public ArrayList<Integer> shortestPath;

    Graph(Map<Integer, Room> floorPlan)
    {
        V = floorPlan.size();
        adj = new LinkedList[floorPlan.size()];
        for (int i=0; i<floorPlan.size(); ++i)
            adj[i] = new LinkedList();


        for(int i=0; i<floorPlan.size(); i++)
        {
            for(int j=0; j<4; j++) //4 - numbers of walls
            {
                // Add edges into the graph only when there is a transition between rooms
                if(floorPlan.get(i).doors[j])
                {
                    this.addEdge(floorPlan.get(i).id, floorPlan.get(i).neighbours[j]);
                }
            }
        }

    }

    //Function to add an edge into the graph
    void addEdge(int v, int w)
    {
        adj[v].add(w); // Add w to v's list.
    }

    // A function used by DFS
    void DFSUtil(int v, boolean visited[], ArrayList<ArrayList<Integer>> paths, ArrayList<Integer> currentPath)
    {
        // Mark the current node as visited and add it to current path
        visited[v] = true;
        currentPath.add(v);

        // Recur for all the vertices adjacent to this vertex
        Iterator<Integer> i = adj[v].listIterator();

        while (i.hasNext())
        {
            int n = i.next();
            if (!visited[n]) {
                ArrayList<Integer> pathFromCrossroad = currentPath;
                if (adj[v].size() > 1) {
                    pathFromCrossroad = (ArrayList<Integer>) currentPath.clone();
                    paths.add(pathFromCrossroad);
                }
                DFSUtil(n, visited, paths,pathFromCrossroad);
            }
        }
    }

    // The function to do DFS traversal. It uses recursive DFSUtil()
    void DFS(int v)
    {
        // Arrays of lists for keeping track of visited paths
        ArrayList<ArrayList<Integer>>  paths = new ArrayList<>();
        ArrayList<Integer> firstPath = new ArrayList<>();
        paths.add(firstPath);

        // Mark all the vertices as not visited(set as
        // false by default in java)
        boolean visited[] = new boolean[V];

        // Call the recursive helper function to print DFS traversal
        DFSUtil(v, visited, paths, firstPath);

        // Find an exit path (the abstract exit room has identifier number
        // equal to number of rooms minus one: V-1)
        for (ArrayList<Integer> path:
            paths) {
            if(path.contains(V-1)) {
                shortestPath = path;
                return;
            }
        }
    }

    public static void main(String args[])
    {
        RoomsFactory.generateSampleMap();

        // Create graph based on loaded map
        Graph g = new Graph(FloorPlan.getInstance().getRoomMap());

        // Find an exit path started in given room
        int startingRoom = 5; //check the starting point
        g.DFS(startingRoom);

        // Print an exit path
        System.out.print(g.shortestPath.toString());
    }
}
