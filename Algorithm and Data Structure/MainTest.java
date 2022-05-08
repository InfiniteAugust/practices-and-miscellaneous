package graph;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import graph.Graph.Vertex;

public class MainTest {
	
	static Graph<Integer> theMap = new Graph<Integer>();
	static Vertex<Integer> vertex0 = theMap.insertVertex(0, "Beijing");
	static Vertex<Integer> vertex1 = theMap.insertVertex(1, "Tianjing");
	static Vertex<Integer> vertex2 = theMap.insertVertex(2, "Chengde");
	static Vertex<Integer> vertex3 = theMap.insertVertex(3, "Shijiazhuang");
	static Vertex<Integer> vertex4 = theMap.insertVertex(4, "Hohhot");
	static Vertex<Integer> vertex5 = theMap.insertVertex(5, "Jinan");
	static Vertex<Integer> vertex6 = theMap.insertVertex(6, "Taiyuan");
	static Vertex<Integer> vertex7 = theMap.insertVertex(7, "Zhengzhou");
	static Vertex<Integer> vertex8 = theMap.insertVertex(8, "Shenyang");
	static Vertex<Integer> vertex9 = theMap.insertVertex(9, "Dalian");
	
	public static void task1() {
		try {
			PrintWriter writer = new PrintWriter("Task1Result.txt", "UTF-8");
			writer.println("[Jingyu LUO], [6521724]");
			/**
			 * construct the graph
			 */
			theMap.insertEdge(vertex0, vertex1, 125);
			theMap.insertEdge(vertex0, vertex2, 226);
			theMap.insertEdge(vertex0, vertex3, 294);
			theMap.insertEdge(vertex0, vertex4, 486);
			theMap.insertEdge(vertex1, vertex2, 271);
			theMap.insertEdge(vertex1, vertex3, 343);
			theMap.insertEdge(vertex1, vertex5, 384);
			theMap.insertEdge(vertex1, vertex8, 640);
			theMap.insertEdge(vertex1, vertex9, 791);
			theMap.insertEdge(vertex2, vertex8, 578);
			theMap.insertEdge(vertex3, vertex4, 714);
			theMap.insertEdge(vertex3, vertex5, 312);
			theMap.insertEdge(vertex3, vertex6, 225);
			theMap.insertEdge(vertex3, vertex7, 420);
			theMap.insertEdge(vertex4, vertex6, 438);
			theMap.insertEdge(vertex5, vertex7, 459);
			theMap.insertEdge(vertex5, vertex9, 478);
			theMap.insertEdge(vertex6, vertex7, 437);
			theMap.insertEdge(vertex8, vertex9, 392);
			
			writer.println("\nSource city: " + vertex4.toString());
			theMap.dijkstra(4);
			theMap.printTask1(writer);
			
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void task2() {
		try {
			PrintWriter writer = new PrintWriter("Task2Result.txt", "UTF-8");
			writer.println("[Jingyu LUO], [6521724]");
			theMap.removeVertex(vertex4);
			for(int i = 0; i < 10; i++) {
				if (i != 4)
					theMap.dijkstra(i);
			}
			theMap.printTask2(writer);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		task1();
		task2();
	}
}
