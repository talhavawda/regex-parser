package assignment;

import java.io.*;
import java.text.ParseException;
import java.util.Scanner;


/*
	Then main() method in this class must be used to run the assignment
 */
public class RunAssignment {

	/**
		Converting each Regular Expression to a DFA and using the DFA to determine whether each test string for this
		Regular Expression belongs to the language defined by the Regular Expression
	 */

	public static void main(String[] args) throws IOException {

		Scanner file = new Scanner(new File("TestData.txt"));	//A file to read the test data from the text file


		while(file.hasNextLine()) {	//traversing through TestData file

			//The string representing the Regular Expression that is read in from the text file
			String reString = file.nextLine();		//reading new regular expression from file
			
			System.out.println("=============== Converting regular expression  " + reString + "  to RegExp expression tree ===============");

			RegExp re = null; //initialise
			Nfa n = null; //initialise

			try {
				RegExp.setNextStateNum(0);
				re = (new RegExp2AST(reString)).convert(); //Create regular expression (in the form of a regular expression tree)
				System.out.println("Successfully Converted!");

				//Create an NFA for the regular expression (by using Thompson's Construction)
				System.out.println("\n=============== Converting regular expression  " + reString + "  to NFA ===============");
				n = re.makeNfa();

				System.out.println("Successfully Converted!");

			} catch (ParseException ex) {
				System.out.println("Error at/near position  " + ex.getErrorOffset() + "  : " +
					ex.getMessage());
			} catch (IOException ex) {
				System.out.println("IOException encountered");
				ex.printStackTrace();
			}
			
			//Create DFA from the NFA using Subset Construction
			System.out.println("\n=============== Converting NFA to DFA ===============");
			Dfa d = Dfa.makeDfa(n);
			System.out.println("Successfully Converted!");

			//Create DfaAcceptor object based on this DFA
			System.out.println("\n=============== Creating DFA Acceptor for this DFA ===============");
			DfaAcceptor da = new DfaAcceptor(d);
			System.out.println("Successfully Created!");

			//for each test string for this regular expression: traverse the DFA and determine if the DFA accepts/rejects this string


			System.out.println("\n\n========== Loading test data for regex:   " + reString + "  ==========");

			String test = ""; //store test string

			if (file.hasNextLine()) {
				test = file.nextLine();
			}

			do{
				if(test.equals("")){ //empty test string
					System.out.println("\nTesting an EMPTY STRING");
				}
				else{ //non-empty test string
					System.out.println("\nTesting the string: " + test);
				}
			
				if(da.accept(test)){ //determining if must ACCEPT or REJECT this test string
					System.out.println("String ACCEPTED!");
				}
				else{
					System.out.println("String REJECTED!");
				}

				if (file.hasNextLine()) {
					test = file.nextLine();
				}
			
			}while(!test.equals("//"));

			System.out.println("==============================================================================\n\n");
		
		}
		
		System.out.println("RunAssignment Completed Successfully! \nThank You!");
		
	}
}
