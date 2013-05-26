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
import java.util.List;

/**
 * A solution that solves TSP problem
 * Using standard input and output
 * 
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
     * The method that calculates minimum moves for TSP problem.
     * Works with O( n^2 * 2^n )
     * @param start is the vertex to start from
     * @param verSet is the set of vertices that should be passed
     * @param end is the vertex should be returned to after traveling all vertices in @param verSet
     * @return the minimum moves
     */
    private static int g( int start, List<Integer> verSet, int end ) {
        
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
            buff = distances[start][elem] + g( elem, list, end );               // Calculates current subproblem recursively
            if( buff < min ) {                                                  // Checks if calculated subproblem is less than others set min to it
                min = buff;
            }
        }
        
        dpData[start][dploc] = min;                                             // Saves the result in dpData
        
        return min;
    }
    
    public static void main(String[] args) {
        
        Scanner input = new Scanner( System.in );
        
        int i, j, start, end;
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
        
        System.out.println("Please Enter Start and End vertices : ");
        
        start = input.nextInt();                                                // Gets start vertex from stdio
        
        vertices.remove( start );                                               // Removes start vertex from vertices list
        
        System.out.println( "Min move : " + g( start, vertices, start ) );      // Calculates and prints the result
        
    }
}