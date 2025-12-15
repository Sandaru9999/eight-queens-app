package com.traffic;

import com.traffic.algorithms.EdmondsKarp;
import com.traffic.db.DatabaseManager;
import com.traffic.algorithms.Dinics; 

public class GameService {
    private TrafficNetwork network;
    private final DatabaseManager dbManager; 
    private final int source = TrafficNetwork.nameToIndex('A'); 
    private final int sink = TrafficNetwork.nameToIndex('T'); 

    public GameService() {
        this.network = new TrafficNetwork();
        this.dbManager = new DatabaseManager(); 
    }
    
   
    public void startNewRound() {
        this.network = new TrafficNetwork();
    }

    public TrafficNetwork getCurrentNetwork() {
        return network;
    }
    
   
    public GameResult findAndRecordMaxFlow() {
        
        int[][] capacityEK = network.getCapacity(); 
        long startEK = System.nanoTime();
        int maxFlowEK = EdmondsKarp.maxFlow(capacityEK, source, sink);
        long endEK = System.nanoTime();
       
        double timeEK = (double)(endEK - startEK) / 1_000_000.0; 

       
        int[][] capacityDinic = network.getCapacity(); 
        
        long startDinic = System.nanoTime();
        
      
        int maxFlowDinic = Dinics.maxFlow(capacityDinic, source, sink); 
        
        long endDinic = System.nanoTime();
        
      
        double timeDinic = (double)(endDinic - startDinic) / 1_000_000.0;
        
        
        int correctFlow = maxFlowEK; 
        
        return new GameResult(correctFlow, timeEK, timeDinic);
    }
    
   
    public void saveRecord(String playerName, GameResult result) {
       
        dbManager.saveGameRecord(playerName, result.getCorrectFlow(), 
                                 result.getTimeEK(), result.getTimeDinic());
    }
    
    public static class GameResult {
        private final int correctFlow;
        private final double timeEK;      
        private final double timeDinic;   
        
       
        public GameResult(int correctFlow, double timeEK, double timeDinic) {
            this.correctFlow = correctFlow;
            this.timeEK = timeEK;
            this.timeDinic = timeDinic;
        }
        
        public int getCorrectFlow() { return correctFlow; }
        public double getTimeEK() { return timeEK; }     
        public double getTimeDinic() { return timeDinic; } 
    }
}
