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
import java.util.Arrays;

public class TSP {
    /**
     * Count of vertices
     */
    static int verCount;
    /**
     * Distances matrix
     */
    static int[][] distances;
    
    static int[][] dpData;
    static boolean dpUsed;
    
    private static int g( int start, List<Integer> verSet, int end ) {
        
        int i, min = Integer.MAX_VALUE, buff, elem, dploc = 0;
        List list;
        
        if( verSet.isEmpty() ){
            return distances[start][end];
        }
        
        for( i = 0; i < verSet.size(); i++ ){
            dploc += Math.pow( 2, start<verSet.get(i)?verSet.get(i)-1:verSet.get(i) ) - 1;
        }
        
        if( dpData[start][dploc] != 0 ){
            dpUsed = true;
            return dpData[start][dploc];
        }
        
        for( i = 0; i < verSet.size(); i++ ){
            elem = verSet.get(i);
            list = new ArrayList< Integer >();
            list.addAll( verSet );
            list.remove( list.indexOf( elem ) );
            buff = distances[start][elem] + g( elem, list, end );
            if( buff < min ){
                min = buff;
            }
        }
        
        dpData[start][dploc] = min;
        
        return min;
    }
    
    public static void main(String[] args) {
        
        Scanner input = new Scanner( System.in );
        
        dpUsed = false;
        
        int i, j, start, end;
        ArrayList< Integer > path = new ArrayList< Integer >();
        
        System.out.println("Please Enter vertice count : ");
        
        verCount = input.nextInt();
        distances = new int[verCount][verCount];
        dpData = new int[verCount][ (int)( Math.pow( 2, verCount - 1 ) - 1 ) ];
        
        for( i = 0; i < verCount; i++ ){
            path.add( i );
        }
        
        System.out.println("Please Enter distance matrix : ");
        
        for( i = 0; i < verCount; i++ ){
            
            for( j = 0; j < verCount; j++ ){
                distances[i][j] = input.nextInt();
            }
            
        }
        
        System.out.println("Please Enter Start and End vertices : ");
        
        start = input.nextInt();
        end = input.nextInt();
        
        path.remove( start );
        
        System.out.println( "Min move : " + g( start, path, end ) );
        System.out.println( "Dynamic Data" + (dpUsed?" Used":" NOT Used") + "." );
        
        System.out.println("Dynamic Data Array :");
        for( i = 0; i < verCount; i++ )
            System.out.println( Arrays.toString(dpData[i]) );
    }
}