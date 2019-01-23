import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;

import java.util.Scanner;

/**
 * @author Edem Kingsley-Amoah
 * The parser class handles parsing of single .vm file, encapsulates access to input code.
 * Reads VM commands, parses them, provides convenient access to their components.
 * Also removes all white space & comments
 */
public class Parser {

	// current is the current command, i.e., the command that the Parser
	// should act on.
	private String current, next;
	private Scanner scanner;
	private boolean hasMoreCommands;

	    

	
	 // opens input file/stream and gets ready to parse it
	public Parser(File src) {
		hasMoreCommands = true;

		try {
			scanner = new Scanner(src);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}

		current = "";
		next = getNextCommand();
	}
	
	// are there more commands in the input?
	public boolean hasMoreCommands() {
		return hasMoreCommands;
	}
	
	
	 // reads next command from input and makes it current command; should be called only if hasMoreCommands() is true
    // initially there is no current command
	public void advance() {
		current = next;
		next = getNextCommand();
	 
    
  }
	
	/**
	 * A private field which has the String next, that gets the next command.
	 **/



	private String getNextCommand() {
		String next;

		// The following regular expression matches any line that is all
		// whitespace or is only a comment.  Continue scanning the input
		// file until we get an actual command or empty the file.

		do {
			if (scanner.hasNextLine())
				next = scanner.nextLine();
			else {
				hasMoreCommands = false;
				return "";
			}
		} while (next.matches("(^\\s*$)|(^\\s*//.*$)"));

		return next;
	}
	
	// returns type for current VM command, C_ARITHMETIC returned for all arithmetic commands
    public CommandType commandType() {
    	String[] line = current.split(" ");
		if (line[0].startsWith("push")) {
			return CommandType.C_PUSH;

		} else if (line[0].startsWith("pop")) {
			return CommandType.C_POP;
		} else if (line[0].startsWith("label")) {
			return CommandType.C_LABEL;
		} else if (line[0].startsWith("goto")) {
			return CommandType.C_GOTO;
		} else if (line[0].startsWith("if")) {
			return CommandType.C_IF;
		} else if (line[0].startsWith("function")) {
			return CommandType.C_FUNCTION;
		} else if (line[0].startsWith("return")) {
			return CommandType.C_RETURN;
		} else if (line[0].startsWith("call")) {
			return CommandType.C_CALL;
		} else if (line[0].startsWith("add") || line[0].startsWith("sub") || line[0].startsWith("neg")
				|| line[0].startsWith("eq") || line[0].startsWith("gt") || line[0].startsWith("lt")
				|| line[0].startsWith("and") || line[0].startsWith("or") || line[0].startsWith("not")) {
			return CommandType.C_ARITHMETIC;
		} else {
			return null;
		}
        
    }

    // returns first argument of current command, in case of C_ARITHMETIC the command itself
    // (add, sub, etc) is returned. should not be called if current command is C_RETURN
    public String arg1() {
 
        String[] line1 = current.split(" ");
		if (commandType() == CommandType.C_ARITHMETIC) {
			return line1[0];
		} else {
			return line1[1];
		}
    }

    // returns 2nd arg of curr command - should be called only if curr command is C_PUSH, C_POP, C_FUNCTION, or C_CALL
    public int arg2() {
		String[] line1 = current.split("\\s+");
		if (commandType() == CommandType.C_POP || commandType() == CommandType.C_PUSH
				|| commandType() == CommandType.C_FUNCTION || commandType() == CommandType.C_CALL) {
			return Integer.parseInt(line1[2]);
		} else {
			return 0;
		}
	}

	
}
