/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tsp;

/**
 *
 * @author jays
 */
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A solution that solves TSP problem
 * Using standard input and output
 * Multi Threaded
 * @author peji
 */
public class TSP {
    /**
     * Count of vertices
     */
    static int verCount;
    /**
     * Distances matrix
     */
    static int[][] distances;
    /**
     * Matrix to save subproblems data
     */
    static int[][] dpData;
    
    /**
     * A runnable solution for TSP
     * Used for multi threading
     */
    private static class TSPThread implements Runnable {
        /**
         * start and end vertices for problem
         */
        private static int start, end;
        /**
         * set of vertices to go
         */
        private static List<Integer> verSet;
        /**
         * Specific index for thread
         * Used for saving result in sharedData array
         */
        final int sharedIndex;
        /**
         * sharedData array used to save threads results
         * Each thread has it's own index ( sharedIndex )
         */
        final int[] sharedData;
        /**
         * Constructor to setup thread data
         * @param start is the vertex to start from
         * @param verSet is the set of vertices that should be passed
         * @param end is the vertex should be returned to after traveling all vertices in verSet
         * @param index is index of thread
         * @param sharedData is shared array between threads, used to save results
         */
        TSPThread( int start, List<Integer> verSet, int end, int index, int[] sharedData ) {
            this.start = start;
            this.end = end;
            this.verSet = verSet;
            this.sharedData = sharedData;
            this.sharedIndex = index;
        }
        
        /**
         * The method that calculates minimum moves for TSP problem.
         * Works with O( n^2 * 2^n )
         * @param start is the vertex to start from
         * @param verSet is the set of vertices that should be passed
         * @param end is the vertex should be returned to after traveling all vertices in verSet
         * @return the minimum moves
         */
        private static int g( int start, List<Integer> verSet ) {

            int i, min = Integer.MAX_VALUE, buff, elem, dploc = 0;
            List list;

            if( verSet.isEmpty() ) {                                                // Checks if there is no vertex to travel to, retruns the distance from "start" to "end"
                return distances[start][end];
            }
            
            for( i = 0; i < verSet.size(); i++ ) {                                  // Generates the index of result in dpData
                dploc += Math.pow( 2, start<verSet.get(i)?verSet.get(i)-1:verSet.get(i) ) - 1;
            }
            
            if( dpData[start][dploc] != 0 ) {                                       // Checks if there is data in dploc, and returns it if there is
                return dpData[start][dploc];
            }
            
            for( i = 0; i < verSet.size(); i++ ) {
                elem = verSet.get(i);                                               // Gets the ith element from verSet and sets elem to it
                list = new ArrayList< Integer >();                                  // Makes a new list for next subproblem
                list.addAll( verSet );                                              // Adds All verSet elements to new list
                list.remove( list.indexOf( elem ) );                                // Removes elem from new list
                buff = distances[start][elem] + g( elem, list );                    // Calculates current subproblem recursively
                if( buff < min ) {                                                  // Checks if calculated subproblem is less than others set min to it
                    min = buff;
                }
            }

            dpData[start][dploc] = min;                                             // Saves the result in dpData
            
            return min;
        }
        
        @Override
        public void run() {
            sharedData[ sharedIndex ] += g( start, verSet );                        // Solves problem in thread and stores result in sharedData[ sharedIndex ]
        }
        
    }
    
    public static int runThreads( int start, List<Integer> vertices, int end ) {
        
        ExecutorService executor = Executors.newCachedThreadPool();

        int min = Integer.MAX_VALUE, elem;
        int[] sharedData = new int[ vertices.size() ];
        List list;

        if( vertices.isEmpty() ) {                                                  // Checks if there is no vertex to travel to, retruns the distance from "start" to "end"
            return distances[start][end];
        }
        
        /*
         * For each vertex in 'vertices' list, one thread will be run
         * Each thread solves it's subproblem and stores data in sharedData array
         */
        
        for( int i = 0; i < vertices.size(); i++ ) {
            elem = vertices.get(i);                                                 // Gets the ith element from verSet and sets elem to it
            list = new ArrayList< Integer >();                                      // Makes a new list for next subproblem
            list.addAll( vertices );                                                // Adds All verSet elements to new list
            list.remove( list.indexOf( elem ) );                                    // Removes elem from new list
            sharedData[i] = distances[start][elem];                                 // Adding distance between start and elem to sharedData
            executor.execute( new TSPThread( elem, list, end, i, sharedData ) );    // Adds and executes thread in threadpool
            
            try {
                Thread.sleep(20);                                                   // Waits 20 ms
            } catch (InterruptedException ex) {
                System.out.println( ex.getMessage() );
            }
        }
        
        executor.shutdown();                                                        // Tells threadpool shutdown after all threads terminated
        
        try {
            while( ! executor.isTerminated() )                                      // While threadpool is not terminated wait
                Thread.sleep(200);
        } catch (InterruptedException ex) {
            System.out.println( ex.getMessage() );
        }
        
        for( int i = 0; i < sharedData.length; i++ ) {
            min = sharedData[i] < min ? sharedData[i] : min;                        // Looks in sharedData for least result
        }

        return min;                                                                 // Returns minimum cost
    }
    
    public static void main(String[] args) {
        
        Scanner input = new Scanner( System.in );
        
        int i, j, start;
        
        List< Integer > vertices = new ArrayList< Integer >();
        
        System.out.println("Please Enter vertice count : ");
        
        verCount = input.nextInt();                                             // Gets vertices count from stdio
        distances = new int[verCount][verCount];                                // Builds distances matrix
        dpData = new int[verCount][ (int)( Math.pow( 2, verCount - 1 ) - 1 ) ]; // Builds dpData
        
        for( i = 0; i < verCount; i++ ) {                                       // Generates vertices array
            vertices.add( i );
        }

        System.out.println("Please Enter distances matrix : ");
        
        for( i = 0; i < verCount; i++ ) {                                       // Gets distances from stdio
            for( j = 0; j < verCount; j++ ) {
                distances[i][j] = input.nextInt();
            }
        }
        
        System.out.println("Please Enter Start Vertex : ");
        start = input.nextInt();                                                // Gets start vertex from stdio
        vertices.remove( start );                                               // Removes start vertex from vertices list
        System.out.println( "Min move : " + runThreads( start, vertices, start ) );// Calculates and prints the result
        
    }
}