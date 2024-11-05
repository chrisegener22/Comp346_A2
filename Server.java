
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*package comp546pa1w2020;*/

/** Server class
 *
 * @author Kerly Titus
 */

public class Server extends Thread {
	
	/* NEW : Shared member variables are now static for the 2 receiving threads */
	private static int numberOfTransactions;         	/* Number of transactions handled by the server */
	private static int numberOfAccounts;             	/* Number of accounts stored in the server */
	private static int maxNbAccounts;                		/* maximum number of transactions */
	private static Accounts [] account;              		/* Accounts to be accessed or updated */
	/* NEW : member variabes to be used in PA2 with appropriate accessor and mutator methods */
	private String serverThreadId;				 /* Identification of the two server threads - Thread1, Thread2 */
	private static String serverThreadRunningStatus1;	 /* Running status of thread 1 - idle, running, terminated */
	private static String serverThreadRunningStatus2;	 /* Running status of thread 2 - idle, running, terminated */
  
    /** 
     * Constructor method of Client class
     * 
     * @return 
     * @param stid
     */
    public Server(String stid) {
        serverThreadId = stid;

        if (stid.equals("Thread1")) {
            serverThreadRunningStatus1 = "idle";
        } else {
            serverThreadRunningStatus2 = "idle";
        }

        if (!Network.getServerConnectionStatus().equals("connected")) {
            System.out.println("\n Initializing the server ...");
            numberOfTransactions = 0;
            numberOfAccounts = 0;
            maxNbAccounts = 100;
            account = new Accounts[maxNbAccounts];
            initializeAccounts();
            System.out.println("\n Connecting server to network ...");
            if (!Network.connect(Network.getServerIP())) {
                System.out.println("\n Terminating server application, network unavailable");
                System.exit(0);
            }
        }
    }
  
    /** 
     * Accessor method of Server class
     * 
     * @return numberOfTransactions
     * @param
     */
     public int getNumberOfTransactions()
     {
         return numberOfTransactions;
     }
         
    /** 
     * Mutator method of Server class
     * 
     * @return 
     * @param nbOfTrans
     */
     public void setNumberOfTransactions(int nbOfTrans)
     { 
         numberOfTransactions = nbOfTrans;
     }

    /** 
     * Accessor method of Server class
     * 
     * @return numberOfAccounts
     * @param
     */
     public int getNumberOfAccounts()
     {
         return numberOfAccounts;
     }
         
    /** 
     * Mutator method of Server class
     * 
     * @return 
     * @param nbOfAcc
     */
     public void setNumberOfAccounts(int nbOfAcc)
     { 
         numberOfAccounts = nbOfAcc;
     }
         
     /** 
      * Accessor method of Server class
      * 
      * @return maxNbAccounts
      * @param
      */
      public int getMxNbAccounts()
      {
          return maxNbAccounts;
      }
          
     /** 
      * Mutator method of Server class
      * 
      * @return 
      * @param nbOfAcc
      */
      public void setMaxNbAccounts(int nbOfAcc)
      { 
    	  maxNbAccounts = nbOfAcc;
      }
           
      /** 
       * Accessor method of Server class
       * 
       * @return serverThreadId
       * @param
       */
       public String getServerThreadId()
       {
           return serverThreadId;
       }
           
      /** 
       * Mutator method of Server class
       * 
       * @return 
       * @param tId
       */
       public void setServerThreadId(String stid)
       { 
     	  serverThreadId = stid;
       }

       /** 
        * Accessor method of Server class
        * 
        * @return serverThreadRunningStatus1
        * @param
        */
        public String getServerThreadRunningStatus1()
        {
            return serverThreadRunningStatus1;
        }
            
       /** 
        * Mutator method of Server class
        * 
        * @return 
        * @param runningStatus
        */
        public void setServerThreadRunningStatus1(String runningStatus)
        { 
      	  serverThreadRunningStatus1 = runningStatus;
        }
        
        /** 
         * Accessor method of Server class
         * 
         * @return serverThreadRunningStatus2
         * @param
         */
         public String getServerThreadRunningStatus2()
         {
             return serverThreadRunningStatus2;
         }
             
        /** 
         * Mutator method of Server class
         * 
         * @return 
         * @param runningStatus
         */
         public void setServerThreadRunningStatus2(String runningStatus)
         { 
       	  serverThreadRunningStatus2 = runningStatus;
         }
         
    /** 
     * Initialization of the accounts from an input file
     * 
     * @return 
     * @param
     */  
     public void initializeAccounts()
     {
        Scanner inputStream = null; /* accounts input file stream */
        int i = 0;                  /* index of accounts array */
        
        try
        {
         inputStream = new Scanner(new FileInputStream("account.txt"));
        }
        catch(FileNotFoundException e)
        {
            System.out.println("File account.txt was not found");
            System.out.println("or could not be opened.");
            System.exit(0);
        }
        while (inputStream.hasNextLine())
        {
            try
            {   account[i] = new Accounts();
                account[i].setAccountNumber(inputStream.next());    /* Read account number */
                account[i].setAccountType(inputStream.next());      /* Read account type */
                account[i].setFirstName(inputStream.next());        /* Read first name */
                account[i].setLastName(inputStream.next());         /* Read last name */
                account[i].setBalance(inputStream.nextDouble());    /* Read account balance */                
            }
            catch(InputMismatchException e)
            {
                System.out.println("Line " + i + "file account.txt invalid input");
                System.exit(0);
            }
            i++;
        }
        setNumberOfAccounts(i);			/* Record the number of accounts processed */
        
        /* System.out.println("\n DEBUG : Server.initializeAccounts() " + getNumberOfAccounts() + " accounts processed"); */
        
        inputStream.close( );
     }
         
    /** 
     * Find and return the index position of an account 
     * 
     * @return account index position or -1
     * @param accNumber
     */
     public int findAccount(String accNumber)
     {
         int i = 0;
         
         /* Find account */
         while ( !(account[i].getAccountNumber().equals(accNumber)))
             i++;
         if (i == getNumberOfAccounts())
             return -1;
         else
             return i;
     }
     
    /** 
     * Processing of the transactions
     * 
     * @return 
     * @param trans
     */
    public boolean processTransactions(Transactions trans) {
        int accIndex;
        double newBalance;
    
        while (!Network.getClientConnectionStatus().equals("disconnected")) {
            if (Network.getInBufferStatus().equals("empty")) {
                Thread.yield();
                continue;
            }
            
            Network.transferIn(trans); // Transfer from input buffer
            accIndex = findAccount(trans.getAccountNumber());
    
            // Process transaction types
            if (trans.getOperationType().equals("DEPOSIT")) {
                newBalance = deposit(accIndex, trans.getTransactionAmount());
            } else if (trans.getOperationType().equals("WITHDRAW")) {
                newBalance = withdraw(accIndex, trans.getTransactionAmount());
            } else if (trans.getOperationType().equals("QUERY")) {
                newBalance = query(accIndex);
            } else {
                continue;
            }
    
            trans.setTransactionBalance(newBalance);
            trans.setTransactionStatus("done");
    
            if (Network.getOutBufferStatus().equals("full")) {
                Thread.yield();
                continue;
            }
            Network.transferOut(trans); // Transfer to output buffer
            setNumberOfTransactions(getNumberOfTransactions() + 1);
        }
        return true;
    }
    
         
    /** 
     * Processing of a deposit operation in an account
     * 
     * @return balance
     * @param i, amount
     */
   
     public synchronized double deposit(int i, double amount)
     {  double curBalance;      /* Current account balance */
       
     		curBalance = account[i].getBalance( );          /* Get current account balance */
        
     		/* NEW : A server thread is blocked before updating the 10th , 20th, ... 70th account balance in order to simulate an inconsistency situation */
     		if (((i + 1) % 10 ) == 0)
     		{
     			try {
     					Thread.sleep(100);
     				}
     				catch (InterruptedException e) {
        	
     				} 
     		} 
        
     		System.out.println("\n DEBUG : Server.deposit - " + "i " + i + " Current balance " + curBalance + " Amount " + amount + " " + getServerThreadId());
        
     		account[i].setBalance(curBalance + amount);     /* Deposit amount in the account */
     		return account[i].getBalance ();                /* Return updated account balance */
     }
         
    /**
     *  Processing of a withdrawal operation in an account
     * 
     * @return balance
     * @param i, amount
     */
 
     public synchronized double withdraw(int i, double amount) {
        double curBalance = account[i].getBalance();
    
        if (curBalance < amount) {
            System.out.println("\n DEBUG : Server.withdraw - Insufficient funds for account " + account[i].getAccountNumber());
            return curBalance; // Return current balance without modification
        }
    
        account[i].setBalance(curBalance - amount);
        return account[i].getBalance();
    }
    

    /**
     *  Processing of a query operation in an account
     * 
     * @return balance
     * @param i
     */
 
     public synchronized double query(int i)
     {  double curBalance;      /* Current account balance */
        
     	curBalance = account[i].getBalance( );          /* Get current account balance */
        
        System.out.println("\n DEBUG : Server.query - " + "i " + i + " Current balance " + curBalance + " " + getServerThreadId()); 
        
        return curBalance;                              /* Return current account balance */
     }
         
     /**
      *  Create a String representation based on the Server Object
     * 
     * @return String representation
     */
     public String toString() 
     {	
    	 return ("\n server IP " + Network.getServerIP() + "connection status " + Network.getServerConnectionStatus() + "Number of accounts " + getNumberOfAccounts());
     }
     
    /**
     * Code for the run method
     * 
     * @return 
     * @param
     */
      
     public void run() {
        Transactions trans = new Transactions();
        long serverStartTime = System.currentTimeMillis();
    
        // Set the running status based on thread ID
        if (serverThreadId.equals("Thread1")) {
            serverThreadRunningStatus1 = "running";
        } else {
            serverThreadRunningStatus2 = "running";
        }
    
        // Process all transactions
        processTransactions(trans);
    
        long serverEndTime = System.currentTimeMillis();
    
        // Set the terminated status and print termination message
        if (serverThreadId.equals("Thread1")) {
            serverThreadRunningStatus1 = "terminated";
        } else {
            serverThreadRunningStatus2 = "terminated";
        }
    
        Network.disconnect(Network.getServerIP());

        System.out.println("\nTerminating " + serverThreadId + " - Running time: " + (serverEndTime - serverStartTime) + " ms");
    }
    
}


