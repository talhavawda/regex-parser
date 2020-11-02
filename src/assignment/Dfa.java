package assignment;

import java.util.*;

public class Dfa {
	
	/*
	 *    Constants to define DFA characteristics of the DFA.
	 *      
	 *    SIGMA_LOWER and SIGMA_UPPER gives the Unicode codes for the first and last
	 *    characters in the input alphabet. As given, they represent the alphabet of 
	 *    the standard printable characters space (code 32) through ~ (code 126). 
	 *    But if e.g. you only wanted the characters A through E you could set
	 *    SIGMA_LOWER to 64 (code A) and SIGMA_UPPER to 68 (code E).
	 *    
	 *    MAX_STATES gives the maximum number of DFA states
	 *    
	 *    TRAP and START gives the row in the transition table for the trap and 
	 *    start states respectively
	 *    
	 *    ACCEPT and NON_ACCEPT are codes for accept (final) and non-accept states
	 *    respectively.
	 *    
	 *    For these constants to be useful, you should use them in your code rather 
	 *    than hardcode them.     
	 */	
	
    public static final int SIGMA_LOWER = 32;
    public static final int SIGMA_UPPER = 127;
    public static final int MAX_STATES  = 256;
    public static final int ACCEPT      = -1;
    public static final int START       = 1;
    public static final int TRAP        = 0;
    
    /*
     *  The structure of the transition table "transTable" is as follows
     *  
     *  transTable[i,0] gives for each state i, i = 0,1,... whether the state is 
     *       accepting or not. So it will have the value ACCEPT or NON_ACCEPT
     *  transTable[i,j] gives for each j = 1,2,... the next state of the DFA for each 
     *       symbol in the alphabet. The next state for the character with code 
     *       SIGMA_LOWER is stored at index j=1,the next state for the character with 
     *       code SIGMA_LOWER + 1  is stored at index j = 2, etc. 
     *  
     *  The trap state is 0 (TRAP = 0) so transTable[0,j] = 0 for j = 1,2,...
     *  
     *  The start state is 1 (START = 1)
     *  
     *  The List "states" holds as a Set of NfaStates the states from the nfa that make up 
     *  each dfa state.  Position i in the list holds the nfa states for dfa state i. 
     *  NB This is, of course, not necessary for using the dfa, but is merely for 
     *  information purposes
     *  
     */
    
    protected int[][]                        transTable;
    protected ArrayList<HashSet<NfaState>>   states;	
    protected int                            size;
    
    public Dfa() {
    }
     
    public Dfa(int[][]                      transTable,
               ArrayList<HashSet<NfaState>> states,
               int                          size) {
               	
       this.transTable = transTable;
       this.states     = states;
       this.size       = size;        	
   }
    
   int[][] getTransTable() {
	   
	   return transTable;
   }
   
   int getSize()  {
	   
	   return size;
   }
   
   ArrayList<HashSet<NfaState>> getStates() {
   
      return states;
   }
}