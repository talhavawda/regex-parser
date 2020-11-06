package assignment;

import java.util.HashSet;

/**
 * A Class to represent a single state in the DFA (which is constructed from the NFA by Subset Construction)
 */
public class DfaState {

	private static int nextStateNum = 0;  // Current unique state number

	private int stateNumber; //the identifier of this state. Will be used to re
	private HashSet<NfaState> nfaStateSet; //the set of states in the NFA that this DFA state represents
	private boolean isStartState;
	private boolean isAcceptState; //to represent whether this state is an Accept (Final) State in the DFA or not
	private boolean isTrapState;

	//Constructor
	public DfaState(HashSet<NfaState> nfaStateSet) {
		this.stateNumber = nextStateNum++; //post-crement -> set this.stateNumber to the current value of nextStateNum, and then increment nextStateNum by 1
		this.nfaStateSet = nfaStateSet;

		isStartState = stateNumber == Dfa.START; //if(stateNumber == Dfa.START) {isStartState = true;} else {isStartState = false;}
		isTrapState = stateNumber == Dfa.TRAP;

		setIsAcceptState();


	}

	/**
	 * Determine whether this DFA State is an Accept State of the DFA or not
	 * The method is private as it is called by the constructor
	 */
	private void setIsAcceptState() {
		/*
			This DFA State is an Accept/Final State if the set of NFA States that it represents consists of an (at least one) Accept State in the NFA
		 */

		isAcceptState = false;

		for (NfaState state : nfaStateSet) {
			if (state.getSymbol() == NfaState.ACCEPT) {
				isAcceptState = true;
				break; //since at least one state in the NFA is an Accept State, this DFA State will be an Accept State. So we can terminate the traversal.
			}
		}
	}

	public int getStateNumber() {
		return stateNumber;
	}

	public HashSet<NfaState> getNfaStateSet() {
		return nfaStateSet;
	}


	public boolean isStartState() {
		return isStartState;
	}

	public boolean isAcceptState() {
		return  isAcceptState;
	}

	public boolean isTrapState() {
		return isTrapState;
	}
}
