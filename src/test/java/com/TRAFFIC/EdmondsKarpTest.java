package com.TRAFFIC;

import com.traffic.algorithms.EdmondsKarp;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EdmondsKarpTest {

   
    @Test
    void testSimpleMaxFlow() {
       
        int V = 4;
        int[][] capacity = new int[V][V];
        capacity[0][1] = 10;
        capacity[0][2] = 5;
        capacity[1][3] = 10;
        capacity[2][3] = 10;

        int source = 0;
        int sink = 3;
        int expectedFlow = 15; 
        
        int actualFlow = EdmondsKarp.maxFlow(capacity, source, sink);
        assertEquals(expectedFlow, actualFlow, "Max flow for the simple graph should be 15.");
    }
    
    
    @Test
    void testComplexMaxFlow() {
        
        int V = 4;
        int[][] capacity = new int[V][V];
        
        capacity[0][1] = 10; 
        capacity[0][2] = 10; 
        capacity[1][2] = 2; 
        capacity[1][3] = 8;  
        capacity[2][3] = 10; 

        int source = 0;
        int sink = 3;
        int expectedFlow = 18; 
        
        int actualFlow = EdmondsKarp.maxFlow(capacity, source, sink);
        assertEquals(expectedFlow, actualFlow, "Max flow for the complex graph should be 18.");
    }
}


