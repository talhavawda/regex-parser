package assignment;

import java.io.IOException;
import java.text.ParseException;

/*
	Then main() method in this class must be used to run the assignment
 */
public class RunAssignment {

	public static void main(String[] args) {

		//for each regex
		String reString = ""; ///...

		//Create regular expression (in the form of a regular expression tree)
		RegExp re;
		try {
			RegExp.setNextStateNum(0);
			re = (new RegExp2AST(reString)).convert();
		} catch (ParseException ex) {
			System.out.println("Error at/near position " + ex.getErrorOffset() + " : " +
					ex.getMessage());
		} catch (IOException ex) {
			System.out.println("IOException encountered");
			ex.printStackTrace();
		}

		//Create an NFA for the regular expression (by using Thompson's Construction)
		Nfa n = re.makeNfa();

		//Create DFA

		//for each test string for this regex: (traverse the DFA and determine if the DFA accepts/rejects this string)


	}
}
