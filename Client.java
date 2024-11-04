
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/** Client class
 *
 * @author Kerly Titus
 */

public class Client extends Thread { 
    
    private static int numberOfTransactions;   		/* Number of transactions to process */
    private static int maxNbTransactions;      		/* Maximum number of transactions */
    private static Transactions [] transaction; 	        /* Transactions to be processed */
    private String clientOperation;    			/* sending or receiving */
       
	/** Constructor method of Client class
 	 * 
     * @return 
     * @param
     */
     Client(String operation)
     { 
       if (operation.equals("sending"))
       { 
           System.out.println("\n Initializing client sending application ...");
           numberOfTransactions = 0;
           maxNbTransactions = 100;
           transaction = new Transactions[maxNbTransactions];  
           clientOperation = operation; 
           System.out.println("\n Initializing the transactions ... ");
           readTransactions();
           System.out.println("\n Connecting client to network ...");
           String cip = Network.getClientIP();
           if (!(Network.connect(cip)))
           {   System.out.println("\n Terminating client application, network unavailable");
               System.exit(0);
           }
       	}
       else
    	   if (operation.equals("receiving"))
           { 
    		   System.out.println("\n Initializing client receiving application ...");
    		   clientOperation = operation; 
           }
     }
           
    /** 
     * Accessor method of Client class
     * 
     * @return numberOfTransactions
     * @param
     */
     public int getNumberOfTransactions()
     {
         return numberOfTransactions;
     }
            
    /** 
     * Mutator method of Client class
     * 
     * @return 
     * @param nbOfTrans
     */
     public void setNumberOfTransactions(int nbOfTrans)
     { 
         numberOfTransactions = nbOfTrans;
     }
         
    /** 
     * Accessor method of Client class
     * 
     * @return clientOperation
     * @param
     */
     public String getClientOperation()
     {
         return clientOperation;
     }
         
    /** 
     * Mutator method of Client class
	 * 
	 * @return 
	 * @param operation
	 */
	 public void setClientOperation(String operation)
	 { 
	     clientOperation = operation;
	 }
         
    /** 
     * Reading of the transactions from an input file
     * 
     * @return 
     * @param
     */
    public void readTransactions() {
        Scanner inputStream = null; // Transactions input file stream
        int i = 0; // Index of transactions array
    
        try {
            inputStream = new Scanner(new FileInputStream("transaction2.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("File transaction.txt was not found or could not be opened.");
            System.exit(0);
        }
    
        while (inputStream.hasNextLine()) {
            try {
                transaction[i] = new Transactions(); // Initialize the transaction object
                transaction[i].setAccountNumber(inputStream.next()); // Read account number
                transaction[i].setOperationType(inputStream.next()); // Read transaction type
                transaction[i].setTransactionAmount(inputStream.nextDouble()); // Read transaction amount
                transaction[i].setTransactionStatus("pending"); // Set current transaction status
                i++;
            } catch (InputMismatchException e) {
                System.out.println("Line " + i + " in file transactions.txt contains invalid input.");
                System.exit(0);
            }
        }
    
        setNumberOfTransactions(i); // Record the number of transactions processed
        inputStream.close();
    }
    
     
    /** 
     * Sending the transactions to the server 
     * 
     * @return 
     * @param
     */
    public void sendTransactions() {
        long sendClientStartTime = System.currentTimeMillis(); // Start time
    
        for (int i = 0; i < getNumberOfTransactions(); i++) {
            // Ensure each transaction exists before sending
            if (transaction[i] != null) {
                while (Network.isFull()) {
                    Thread.yield(); // Yield if input buffer is full
                }
                System.out.println("Client: Sending transaction " + transaction[i].getAccountNumber() 
                                   + " of type " + transaction[i].getOperationType() 
                                   + " with amount " + transaction[i].getTransactionAmount());
                Network.send(transaction[i]); // Send transaction to the network
                transaction[i].setTransactionStatus("sent");
            } else {
                System.out.println("Error: transaction[" + i + "] is null.");
            }
        }
    
        long sendClientEndTime = System.currentTimeMillis(); // End time
        System.out.println("Client send thread running time: " + (sendClientEndTime - sendClientStartTime) + " ms");
    }
    
    
    
         
 	/** 
  	 * Receiving the completed transactions from the server
     * 
     * @return 
     * @param transact
     */
    public void receiveTransactions(Transactions transact) {
        long receiveClientStartTime = System.currentTimeMillis(); // Record the start time
    
        for (int i = 0; i < getNumberOfTransactions(); i++) {
            // Check if the output buffer is empty before receiving
            while (Network.isEmpty()) {
                Thread.yield(); // Yield if the network's output buffer is empty
            }
    
            // Receive the completed transaction from the network's output buffer
            Network.receive(transact);
    
            // Display the received transaction details
            System.out.println("Client: Received transaction " + transact.getAccountNumber() 
                               + " with final balance " + transact.getTransactionBalance() 
                               + " and status " + transact.getTransactionStatus());
        }
    
        long receiveClientEndTime = System.currentTimeMillis(); // Record the end time
        System.out.println("Client receive thread running time: " + (receiveClientEndTime - receiveClientStartTime) + " ms");
    }
    
    
     
    /** 
     * Create a String representation based on the Client Object
     * 
     * @return String representation
     * @param 
     */
     public String toString() 
     {
    	 return ("\n client IP " + Network.getClientIP() + " Connection status" + Network.getClientConnectionStatus() + "Number of transactions " + getNumberOfTransactions());
     }
    
       
    /** Code for the run method
     * 
     * @return 
     * @param
     */
    @Override
    public void run() {
    long startTime = System.currentTimeMillis(); // Start time for running time measurement

    if (clientOperation.equals("sending")) {
        sendTransactions();
    } else if (clientOperation.equals("receiving")) {
        Transactions transact = new Transactions();
        receiveTransactions(transact);
    }

    long endTime = System.currentTimeMillis(); // End time for running time measurement
    System.out.println("Client " + clientOperation + " thread running time: " + (endTime - startTime) + " ms");
    }

                
}
