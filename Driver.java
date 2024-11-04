

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
      // Initialize and start the network
      Network network = new Network();
      network.start();

      // Start the sending client
      Client sendingClient = new Client("sending");
      sendingClient.start();

      // Start the receiving client
      Client receivingClient = new Client("receiving");
      receivingClient.start();

      // Initialize and start the server
      Server server1 = new Server("Serv 1");
      server1.start();

      Server server2 = new Server("Serv 2");
      server2.start();
  }
    
}
