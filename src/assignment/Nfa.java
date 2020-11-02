package assignment;

import java.util.*;

/*
 * Class representing a NFA
 */
public class Nfa {
	
	protected NfaState start;           // NFA start state
	protected NfaState accept;          // NFA final (accept) state
	protected int      numStates = 0;   // Number of states in NFA

	/*
	 * Construct a new empty Nfa
	 */
	public Nfa() {	
	
	   this(null,null,0);	
	}
	
	/*
	 * Construct a new NFA with specified start and final states and number of states
	 */
	public Nfa(NfaState start,
	           NfaState accept,
	           int      numStates) {
	             	
	     this.start  = start;
	     this.accept = accept; 
	     this.numStates = numStates;        	
	}
	
	public NfaState getStart() {
		
		return start;
	}
	
	/*
	 * Getters and setters for instance variables
	 */
	public NfaState getAccept() {
		
		return accept;
	}
	
	public void setStart(NfaState start) {
		
		this.start = start;
	}
	
	public void setAccept(NfaState accept) {
		
		this.accept = accept;
	}
	
	public int getNumStates() {
		
		return numStates;
	}
	
	public void setNumStates(int num) {
		
		numStates = num;
	}
	
	/*
	 * Return a string representation of this Nfa in a form of a list of the 
	 * nodes comprising the Nfa and the transitions from each node.
	 */
	public String toString() {
		
		Stack<NfaState>   stack = new Stack<NfaState>();
		HashSet<NfaState> c     = new HashSet<NfaState>();
		String result = "Number of states : " + numStates + "\n";
		result += "Start state : " + start.getStateNo() + "\n";
		result += "Final state : " + accept.getStateNo() + "\n";
		result += "States :\n";
		stack.push(start);
		c.add(start);
		while (!stack.empty()) {
		   NfaState t = stack.pop();
		   result += t + "\n";
		   if (t.next1 != null && ! c.contains(t.next1)) {
		     c.add(t.next1);
		     stack.push(t.next1);
		   }
		   if (t.next2 != null && ! c.contains(t.next2)) {
		     stack.push(t.next2); 
		     c.add(t.next2);
		   } 
		}		
		return result;
	}	 	             	     	     
}