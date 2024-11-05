

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kerly Titus
 */
public class Driver {

    /** 
     * main class
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
    	Network objNetwork = new Network( );            /* Activate the network */
      objNetwork.start();

      Server objserver1 = new Server("serv 1");
      objserver1.start();
      Server objserver2 = new Server("serv 2");
      objserver2.start();

      Client objClient1 = new Client("sending");          /* Start the sending client thread */
      objClient1.start();
      Client objClient2 = new Client("receiving");        /* Start the receiving client thread */
      objClient2.start();
        
      try {
        objserver1.join();
        objserver2.join();
      } catch (InterruptedException e) {
        System.out.println("Error waiting for server threads to terminate.");
      }
      
      System.out.println("Both server threads have terminated. Disconnecting network.");
      Network.disconnect(Network.getServerIP()); // Access disconnect method in a static way

    }
    
 }
