package graph;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import java.util.PriorityQueue;
import java.util.Set;

/**
 * @author Jingyu LUO
 * @param <V>
 */
public class Graph<V> {
	
	private HashMap<Vertex<V>, ArrayList<Edge<V>>> adjacencyListGraph;
	private int numVertices;
	private int numEdges;
	private HashMap<Vertex<V>, Integer> output1 = new HashMap<Vertex<V>, Integer>();
	private HashMap<Vertex<V>, Integer> output2 = new HashMap<Vertex<V>, Integer>();

	/**
	 * constructor
	 */
	public Graph(){
		adjacencyListGraph = new HashMap<Vertex<V>, ArrayList<Edge<V>>>();
		numVertices = 0;
		numEdges = 0;
	}
	
	/**
	 * Returns an iteration of all the vertices of the graph
	 * @return
	 */
	public Set<Vertex<V>> vertices(){
		Set<Vertex<V>> a =  adjacencyListGraph.keySet();
		return a;
	}
	
	/**
	 * Returns an iteration of all the edges of the graph
	 * @return
	 */
	public Iterator<Entry<Vertex<V>, ArrayList<Edge<V>>>> edges(){
		Iterator<Entry<Vertex<V>, ArrayList<Edge<V>>>> b = adjacencyListGraph.entrySet().iterator();
		return b;
	}
	
	/**
	 * return number of vertices 
	 * @return
	 */
	public int numVertices() {
		return numVertices;
	}
	
	/**
	 * return number of all edges
	 * @return
	 */
	public int numEdges() {
		return numEdges;
	}
	
	/**
	 * insert a vertex and return the inserted element 
	 * @param vertex
	 * @return
	 */
	public Vertex<V> insertVertex(V element, String name){
		if (element == null) 
			throw new NullPointerException("Element cannot be null!");
		Vertex<V> newVertex = new Vertex<V>(element, name);
		adjacencyListGraph.put(newVertex, new ArrayList<>());
		numVertices++;
		return newVertex;		
	}
	
	/**
	 * insert an edge from source to destination and return the inserted edge
	 * @param v1
	 * @param v2
	 * @param weight
	 * @return
	 */
	public Edge<V> insertEdge(Vertex<V> v1, Vertex<V> v2, int weight){
		if (v1 == null || v2 == null)
			throw new NullPointerException("Start and end cannot be null!");
		//for adjacency list, one edge will be recored twice with different key
		Edge<V> newEdge = new Edge<V>(v1, v2, weight);
		Edge<V> newEdge2 = new Edge<V>(v2, v1, weight);
		
		ArrayList<Edge<V>> list = adjacencyListGraph.get(v1);
		list.add(newEdge);
		adjacencyListGraph.put(v1, list);
		
		list = adjacencyListGraph.get(v2);
		list.add(newEdge2);
		adjacencyListGraph.put(v2, list);
		numEdges++;
		return newEdge;
	}
	
	/**
	 * Removes vertex v and all its incident edges from the graph.
	 * @param target
	 */
	public void removeVertex(Vertex<V> target) {
		ArrayList<Edge<V>> edges = adjacencyListGraph.get(target);
		
		for(Edge<V> edge : edges) 
		{
			//for every vertices associated to this vertex, get their edges(array list)
			ArrayList<Edge<V>> edgesOfV2 = adjacencyListGraph.get(edge.getV2());
			
			for(int i = 0; i < edgesOfV2.size(); i++) 
			{
				Edge<V> egdeOfV2 = edgesOfV2.get(i);
				if(egdeOfV2.getV2().getCity() == target.getCity())
					edgesOfV2.remove(egdeOfV2);
			}
		}
		adjacencyListGraph.remove(target);
	}
	
	/**
	 * Removes edge the from v1 to v2 in both adjacency list of v1 and v2
	 * @param v1, v2
	 */
	public void removeEdge(Vertex<V> v1, Vertex<V> v2) {
		ArrayList<Edge<V>> edges = new ArrayList<Edge<V>>();
		
		edges = adjacencyListGraph.get(v1);
		for(int i = 0; i < edges.size(); i++) {
			Edge<V> edge = edges.get(i);
			Vertex<V> v = edge.getV2();
			if(v.getCity() == v2.getCity())
				edges.remove(edge);
		}
		adjacencyListGraph.replace(v1, edges);
		
		edges = adjacencyListGraph.get(v2);
		for(int i = 0; i < edges.size(); i++) {
			Edge<V> edge = edges.get(i);
			Vertex<V> v = edge.getV1();
			if(v.getCity() == v1.getCity())
				edges.remove(edge);
		}
		adjacencyListGraph.replace(v2, edges);
	}
	
	public void printGraph() {
		System.out.println("Graph: ");
		for (Vertex<V> vertex : adjacencyListGraph.keySet()) {
			System.out.println("\n[" + vertex.toString() + "]");
			ArrayList<Edge<V>> edges = adjacencyListGraph.get(vertex);
			for (Edge<V> edge : edges) {
				System.out.println("to "+edge.getV2().toString()+", "+edge.getWeight());
			}
		}
	}
	
	/**
	 * find the vertex according to the number as the starting point  
	 * @param NOVertex
	 */
	void dijkstra(int NOVertex) {
		Vertex<V> start = null;
		Iterator<Vertex<V>> iterator = adjacencyListGraph.keySet().iterator();
		while(iterator.hasNext()) {
			start = iterator.next();
			if((Integer)start.getVertex() == NOVertex) 
				break;
		}
		
		//initialization
		int sum = 0;
		start.setDist(0);
		//construct a priority queue of vertices which are compared by the distance
		PriorityQueue<Vertex<V>> pq = new PriorityQueue<Vertex<V>>(new Comparator<Vertex<V>>() {
			@Override
			public int compare(Vertex<V> arg0, Vertex<V> arg1) {
				return arg0.getDist() - arg1.getDist();
			}
		});
		pq.offer(start);
		
		while(!pq.isEmpty()) {
			//start searching from u
			Vertex<V> u = pq.poll();
			//put the node into its path after it is removed from the queue
			u.addPath(u);
			//get all edges leaving this node, add the visited node into cloud 
			ArrayList<Edge<V>> edges = adjacencyListGraph.get(u);
			u.setVisited(true);
			//search all vertices connected to u
			for (Edge<V> edge : edges) {
				if(!edge.getV2().isVisited()) {
					if(edge.getV2().getDist() > u.getDist() + edge.getWeight()) {
						//edge relaxation, update the distance
						int v = u.getDist() + edge.getWeight();
						edge.getV2().setDist(v);
						//update path
						ArrayList<Vertex<V>> w = u.getPath();
						edge.getV2().setPath(w);
					}
				}
			}
			
			//reorder priority queue
			pq.clear();
			for(Vertex<V> vertex : adjacencyListGraph.keySet()) {
				if(!vertex.isVisited())
					pq.add(vertex);
			}
			
			output1.put(u, u.getDist());
			sum += u.getDist();
		}
		//reset 
		for(Vertex<V> v : adjacencyListGraph.keySet()) {
			v.setVisited(false);
			v.setDist(Integer.MAX_VALUE);
		}
		output2.put(start, (Integer)sum);
	}
	
	void printTask1(PrintWriter writer) {
		for(Vertex<V> v : output1.keySet()) {
			writer.println("\nTo "+v+": "+output1.get(v)+"km  "+v.getPath());
		}
	}
	
	void printTask2(PrintWriter writer) {
		int min = 10000;
		Vertex<V> center = new Vertex<V>();
		for(Vertex<V> v : output2.keySet()) {
			if(v.getCity() != "Hohhot") {
				writer.println("\n["+v+"]   ["+output2.get(v)+"km] ");
				if(output2.get(v) < min) {
					min = output2.get(v);
					center = v;
				}
			}
		}
		writer.println("\nThe chosen logistic center is ["+center+"]. Summation of distances is: "+min+"km.");
	}
	
	/**
	 * inner class Vertex
	 * @author Jingyu LUO
	 * @param <V>
	 */
	public static class Vertex<V> {
		private boolean isVisited = false;
		//serial number of the vertex
		private V element;
		//city name
		private String city;
		//distance to this vertex
		private int dist = Integer.MAX_VALUE;
		//shortest path to this vertex
		private ArrayList<Vertex<V>> path = new ArrayList<Vertex<V>>();
		
		/**
		 * constructor 
		 * @param element
		 */
		public Vertex(V element, String name) {
			this.element = element;
			this.city = name;
		}
		public Vertex() {
			
		}
		public V getVertex () {
			return element;
		}
		public void setVertex(V ele) {
			this.element = ele;
		}
		public boolean isVisited() {
			return isVisited;
		}
		public void setVisited(boolean isVisited) {
			this.isVisited = isVisited;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public int getDist() {
			return dist;
		}
		public void setDist(int dist) {
			this.dist = dist;
		}
		public void addPath(Vertex<V> v) {
			path.add(v);
		}
		public ArrayList<Vertex<V>> getPath(){
			return path;
		}
		public void setPath(ArrayList<Vertex<V>> v) {
			path = v;
		}
		@Override
		public String toString() {
			return city;
		}
	}
	
	/**
	 * inner class Edge
	 * @author Jingyu LUO
	 * @param <V>
	 */
	public static class Edge<V> {
		private int weight;
		//numerical order of v1 is smaller than that of v2
		private Vertex<V> v1, v2; 
		
		/**
		 * constructor 
		 * @param s
		 * @param e
		 * @param element
		 */
		public Edge(Vertex<V> s, Vertex<V> e, int w) {
			this.v1 = s;
			this.v2 = e;
			this.weight = w;
		}
		public int getWeight() {
			return weight;
		}
		public void setWeight(int w) {
			this.weight = w;
		}
		public Vertex<V> getV1(){
			return v1;
		}
		public Vertex<V> getV2(){
			return v2;
		}
		public void setV1(Vertex<V> end) {
			this.v1 = end;
		}
		public void setV2(Vertex<V> start) {
			this.v2 = start;
		}
		@Override
		public String toString() {
			return "v1: "+v1.toString()+", v2: "+v2.toString()+", weight: "+weight;
		}
	}
}
