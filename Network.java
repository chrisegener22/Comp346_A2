
import java.util.concurrent.Semaphore;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/** Network class
 *
 * @author Kerly Titus
 */
public class Network extends Thread {
    
    private static int maxNbPackets = 50;                          /* Maximum number of simultaneous transactions handled by the network buffer */
    private static int inputIndexClient, inputIndexServer, outputIndexServer, outputIndexClient; /* Network buffer indices for accessing the input buffer (inputIndexClient, outputIndexServer) and output buffer (inputIndexServer, outputIndexClient) */
    private static String clientIP;                            /* IP number of the client application*/
    private static String serverIP;                            /* IP number of the server application */
    private static int portID;                                 /* Port ID of the client application */
    private static String clientConnectionStatus;              /* Client connection status - connected, disconnected, idle */
    private static String serverConnectionStatus;              /* Server connection status - connected, disconnected, idle */
    private static Transactions inComingPacket[];              /* Incoming network buffer */
    private static Transactions outGoingPacket[];              /* Outgoing network buffer */
    private static String inBufferStatus, outBufferStatus;     /* Current status of the network buffers - normal, full, empty */
    private static String networkStatus;                       /* Network status - active, inactive */
    private static Semaphore inBufferSemaphore = new Semaphore(0); // Initially empty buffer
    private static Semaphore outBufferSemaphore = new Semaphore(0); // Initially empty buffer
    private static Semaphore inBufferEmptySemaphore = new Semaphore(maxNbPackets); // Max slots for empty input
    private static Semaphore outBufferEmptySemaphore = new Semaphore(maxNbPackets); // Max slots for empty output
    private static Semaphore inBufferMutex = new Semaphore(1); // Mutex for input buffer
    private static Semaphore outBufferMutex = new Semaphore(1); // Mutex for output buffer
       
    /** 
     * Constructor of the Network class
     * 
     * @return 
     * @param
     */
     Network()
      { 
    	 int i;  
        
         System.out.println("\n Activating the network ...");
         clientIP = "192.168.2.0";
         serverIP = "216.120.40.10";
         clientConnectionStatus = "idle";
         serverConnectionStatus = "idle";
         portID = 0;
         inComingPacket = new Transactions[maxNbPackets];
         outGoingPacket = new Transactions[maxNbPackets];
         for (i=0; i < maxNbPackets; i++)
         {   inComingPacket[i] = new Transactions();
             outGoingPacket[i] = new Transactions();
         }
         inBufferStatus = "empty";
         outBufferStatus = "empty";
         inputIndexClient = 0;
         inputIndexServer = 0;
         outputIndexServer = 0;
         outputIndexClient = 0;
                
         networkStatus = "active";
      }     
        
     /** 
      * Accessor method of Network class
     * 
     * @return clientIP
     * @param
     */
     public static String getClientIP()
     {
         return clientIP;
     }
         
    /**
     *  Mutator method of Network class
     * 
     * @return 
     * @param cip
     */
     public static void setClientIP(String cip)
     { 
         clientIP = cip;
     }
    
    /**
     *  Accessor method of Network class
     * 
     * @return serverIP
     * @param
     */
     public static String getServerIP()
     {
         return serverIP;
     }
                          
    /**
     *  Mutator method of Network class
     * 
     * @return 
     * @param sip
     */
     public static void setServerIP(String sip)
     { 
         serverIP = sip;
     }
         
    /**
     *  Accessor method of Network class
     * 
     * @return clientConnectionStatus
     * @param
     */
     public static String getClientConnectionStatus()
     {
         return clientConnectionStatus;
     }
                          
    /**
     *  Mutator method of Network class
     * 
     * @return 
     * @param connectStatus
     */
     public static void setClientConnectionStatus(String connectStatus)
     { 
         clientConnectionStatus = connectStatus;
     }
         
    /**
     *  Accessor method of Network class
     * 
     * @return serverConnectionStatus
     * @param
     */
     public static String getServerConnectionStatus()
     {
         return serverConnectionStatus;
     }
                          
    /**
     *  Mutator method of Network class
     * 
     * @return 
     * @param connectStatus
     */
     public static void setServerConnectionStatus(String connectStatus)
     { 
         serverConnectionStatus = connectStatus;
     } 
         
    /**
     *  Accessor method of Network class
     * 
     * @return portID
     * @param
     */
     public static int getPortID()
     {
         return portID;
     }
     
    /**
     *  Mutator method of Network class
     * 
     * @return 
     * @param pid
     */
     public static void setPortID(int pid)
     { 
         portID = pid;
     }

    /**
     *  Accessor method of Netowrk class
     * 
     * @return inBufferStatus
     * @param
     */
     public static String getInBufferStatus()
     {
         return inBufferStatus;
     }
         
    /**
     *  Mutator method of Network class
     * 
     * @return 
     * @param inBufStatus
     */
     public static void setInBufferStatus(String inBufStatus)
     { 
         inBufferStatus = inBufStatus;
     }
         
    /**
     *  Accessor method of Netowrk class
     * 
     * @return outBufferStatus
     * @param
     */
     public static String getOutBufferStatus()
     {
         return outBufferStatus;
     }
         
    /**
     *  Mutator method of Network class
     * 
     * @return 
     * @param outBufStatus
     */
     public static void setOutBufferStatus(String outBufStatus)
     { 
         outBufferStatus = outBufStatus;
     }

    /**
     *  Accessor method of Netowrk class
     * 
     * @return networkStatus
     * @param
     */
     public static String getNetworkStatus()
     {
         return networkStatus;
     }
         
    /**
     *  Mutator method of Network class
     * 
     * @return 
     * @param netStatus
     */
     public static void setNetworkStatus(String netStatus)
     { 
         networkStatus = netStatus;
     }
         
    /**
     *  Accessor method of Netowrk class
     * 
     * @return inputIndexClient
     * @param
     */
     public static int getinputIndexClient()
     {
         return inputIndexClient;
     }
         
    /**
     *  Mutator method of Network class
     * 
     * @return 
     * @param i1
     */
     public static void setinputIndexClient(int i1)
     { 
         inputIndexClient = i1;
     }
         
     /**
      *  Accessor method of Netowrk class
     * 
     * @return inputIndexServer
     * @param
     */
     public static int getinputIndexServer()
     {
         return inputIndexServer;
     }
         
    /**
     *  Mutator method of Network class
     * 
     * @return 
     * @param i2
     */
     public static void setinputIndexServer(int i2)
     { 
         inputIndexServer = i2;
     }     
         
    /**
     *  Accessor method of Netowrk class
     * 
     * @return outputIndexServer
     * @param
     */
     public static int getoutputIndexServer()
     {
         return outputIndexServer;
     }
         
    /**
     *  Mutator method of Network class
     * 
     * @return 
     * @param o1
     */
     public static void setoutputIndexServer(int o1)
     { 
         outputIndexServer = o1;
     }
         
     /**
      *  Accessor method of Netowrk class
     * 
     * @return outputIndexClient
     * @param
     */
     public static int getoutputIndexClient()
     {
         return outputIndexClient;
     }
         
    /**
     *  Mutator method of Network class
     * 
     * @return 
     * @param o2
     */
     public static void setoutputIndexClient(int o2)
     { 
         outputIndexClient = o2;
     }

	 /**
	 *  Accessor method of Netowrk class
	 * 
	 * @return maxNbPackets
	 * @param
	 */
	 public static int getMaxNbPackets()
	 {
	     return maxNbPackets;
	 }
	 
    /**
     *  Mutator method of Network class
     * 
     * @return 
     * @param maxPackets
     */
     public static void setMaxNbPackets(int maxPackets)
     { 
         maxNbPackets = maxPackets;
     }
         
    /**
     *  Transmitting the transactions from the client to the server through the network 
     *  
     * @return
     * @param inPacket transaction transferred from the client
     * 
     */
    public static boolean send(Transactions inPacket) throws InterruptedException {
        inBufferEmptySemaphore.acquire(); // Wait for an empty slot in the input buffer
        inBufferMutex.acquire(); // Lock access to the input buffer
    
        // Insert the packet into the buffer
        inComingPacket[inputIndexClient].setAccountNumber(inPacket.getAccountNumber());
        inComingPacket[inputIndexClient].setOperationType(inPacket.getOperationType());
        inComingPacket[inputIndexClient].setTransactionAmount(inPacket.getTransactionAmount());
        inComingPacket[inputIndexClient].setTransactionBalance(inPacket.getTransactionBalance());
        inComingPacket[inputIndexClient].setTransactionError(inPacket.getTransactionError());
        inComingPacket[inputIndexClient].setTransactionStatus("transferred");
    
        // Update the input index
        inputIndexClient = (inputIndexClient + 1) % maxNbPackets;
        inBufferStatus = (inputIndexClient == outputIndexServer) ? "full" : "normal";
    
        inBufferMutex.release(); // Unlock access to the input buffer
        inBufferSemaphore.release(); // Signal that a packet is available in the input buffer
        return true;
    }
    
      
         
      /** Transmitting the transactions from the server to the client through the network 
     * @return
     * @param outPacket updated transaction received by the client
     * 
     */
    public static boolean receive(Transactions outPacket) throws InterruptedException {
        outBufferSemaphore.acquire(); // Wait for an available packet in the output buffer
        outBufferMutex.acquire(); // Lock access to the output buffer
    
        // Retrieve the packet from the buffer
        outPacket.setAccountNumber(outGoingPacket[outputIndexClient].getAccountNumber());
        outPacket.setOperationType(outGoingPacket[outputIndexClient].getOperationType());
        outPacket.setTransactionAmount(outGoingPacket[outputIndexClient].getTransactionAmount());
        outPacket.setTransactionBalance(outGoingPacket[outputIndexClient].getTransactionBalance());
        outPacket.setTransactionError(outGoingPacket[outputIndexClient].getTransactionError());
        outPacket.setTransactionStatus("done");
    
        // Update the output index
        outputIndexClient = (outputIndexClient + 1) % maxNbPackets;
        outBufferStatus = (outputIndexClient == inputIndexServer) ? "empty" : "normal";
    
        outBufferMutex.release(); // Unlock access to the output buffer
        outBufferEmptySemaphore.release(); // Signal an empty slot in the output buffer
        return true;
    }
    
      
         
    
    /**
     *  Transferring the completed transactions from the server to the network buffer
     *  
     * @return
     * @param outPacket updated transaction transferred by the server to the network output buffer
     * 
     */
    public static boolean transferOut(Transactions outPacket) throws InterruptedException {
        outBufferEmptySemaphore.acquire(); // Wait for an empty slot in the output buffer
        outBufferMutex.acquire(); // Lock access to the output buffer
    
        // Place packet data in the output buffer
        outGoingPacket[inputIndexServer].setAccountNumber(outPacket.getAccountNumber());
        outGoingPacket[inputIndexServer].setOperationType(outPacket.getOperationType());
        outGoingPacket[inputIndexServer].setTransactionAmount(outPacket.getTransactionAmount());
        outGoingPacket[inputIndexServer].setTransactionBalance(outPacket.getTransactionBalance());
        outGoingPacket[inputIndexServer].setTransactionError(outPacket.getTransactionError());
        outGoingPacket[inputIndexServer].setTransactionStatus("transferred");
    
        // Update the input index for the server
        inputIndexServer = (inputIndexServer + 1) % maxNbPackets;
        outBufferStatus = (inputIndexServer == outputIndexClient) ? "full" : "normal";
    
        outBufferMutex.release(); // Unlock access to the output buffer
        outBufferSemaphore.release(); // Signal that a packet is available in the output buffer
        return true;
    }
    
    
         
    /**
     *  Transferring the transactions from the network buffer to the server
     * @return
     * @param inPacket transaction transferred from the input buffer to the server 
     * 
     */
    public static boolean transferIn(Transactions inPacket) throws InterruptedException {
        inBufferSemaphore.acquire(); // Wait for an available packet in the input buffer
        inBufferMutex.acquire(); // Lock access to the input buffer
    
        // Transfer packet data to `inPacket`
        inPacket.setAccountNumber(inComingPacket[outputIndexServer].getAccountNumber());
        inPacket.setOperationType(inComingPacket[outputIndexServer].getOperationType());
        inPacket.setTransactionAmount(inComingPacket[outputIndexServer].getTransactionAmount());
        inPacket.setTransactionBalance(inComingPacket[outputIndexServer].getTransactionBalance());
        inPacket.setTransactionError(inComingPacket[outputIndexServer].getTransactionError());
        inPacket.setTransactionStatus("received");
    
        // Update the output index for the server
        outputIndexServer = (outputIndexServer + 1) % maxNbPackets;
        inBufferStatus = (outputIndexServer == inputIndexClient) ? "empty" : "normal";
    
        inBufferMutex.release(); // Unlock access to the input buffer
        inBufferEmptySemaphore.release(); // Signal an empty slot in the input buffer
        return true;
    }
    
     
         
     /**
      *  Handling of connection requests through the network 
      *  
      * @return valid connection
      * @param IP
      * 
      */
     public static boolean connect(String IP)
     {
         if (getNetworkStatus().equals("active"))
         {
             if (getClientIP().equals(IP))
             {
                setClientConnectionStatus("connected");
                setPortID(0);
             }
             else
             if (getServerIP().equals(IP))
             {
                setServerConnectionStatus("connected");
             }
             return true;
         }
         else
             return false;
     }
     
     /**
      *  Handling of disconnection requests through the network 
      * @return valid disconnection
      * @param IP
      * 
      */
     public static boolean disconnect(String IP)
     {
          if (getNetworkStatus( ).equals("active"))
         {
             if (getClientIP().equals(IP))
             {
                setClientConnectionStatus("disconnected");
             }
             else
             if (getServerIP().equals(IP))
             {
                setServerConnectionStatus("disconnected");
             }
             return true;
         }
         else
             return false;
     }
         
     /**
      *  Create a String representation based on the Network Object
      * 
      * @return String representation
      */
	    public String toString() 
	    {
	        return ("\n Network status " + getNetworkStatus() + "Input buffer " + getInBufferStatus() + "Output buffer " + getOutBufferStatus());
	    }
       
    /**
     *  Code for the run method
     * 
     * @return 
     * @param
     */
    public void run() {
        System.out.println("\n DEBUG: Network.run() - starting network thread");
    
        while (networkStatus.equals("active")) {
            // Check if both client and server are disconnected, then deactivate the network
            if (getClientConnectionStatus().equals("disconnected") && getServerConnectionStatus().equals("disconnected")) {
                setNetworkStatus("inactive");
                System.out.println("Terminating network thread - both client and server are disconnected.");
                break;
            }
    
            // Control processing based on buffer statuses and connection statuses
            if (inBufferStatus.equals("full") && clientConnectionStatus.equals("connected")) {
                Thread.yield();  // Allow client to process full buffer
            }
    
            if (outBufferStatus.equals("empty") && serverConnectionStatus.equals("connected")) {
                Thread.yield();  // Allow server to populate empty buffer
            }
    
            Thread.yield();  // General yield to allow other threads time to process
        }
        
        System.out.println("Network thread terminated cleanly.");
    }
    
    
    
}
