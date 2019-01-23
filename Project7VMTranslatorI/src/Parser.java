import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
//import java.util.Hashtable;
import java.util.List;
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
	
	 private String mArg0 = null;
	    private String mArg1 = null;
	    private String mArg2 = null;
	    private String mCmdType = null;
	    private static List<String> arithCmds = Arrays.asList("add", "sub", "neg", "eq", "gt", "lt", "and", "or", "not");

	
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
	 
     String[] cmds = current.split(" ");
     mArg0 = cmds[0];
     if (cmds.length > 1) {
         mArg1 = cmds[1];
     }
     if (cmds.length > 2) {
         mArg2 = cmds[2];
     }

     if (mArg0.equals("push")) {
         mCmdType = "C_PUSH";
     } else if (mArg0.equals("pop")) {
         mCmdType = "C_POP";
     } else if (arithCmds.contains(mArg0)) {
         mCmdType = "C_ARITHMETIC";
     }
     else if (mArg0.equals("label")) {
         mCmdType = "C_LABEL";
     }
     else if (mArg0.equals("goto")) {
         mCmdType = "C_GOTO";
     }
     else if (mArg0.equals("if-goto")) {
         mCmdType = "C_IF";
     }
     else if (mArg0.equals("function")) {
         mCmdType = "C_FUNCTION";
     }
     else if (mArg0.equals("return")) {
         mCmdType = "C_RETURN";
     }
     else if (mArg0.equals("call")) {
         mCmdType = "C_CALL";
     }
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
    public String commandType() {
        return mCmdType;
    }

    // returns first argument of current command, in case of C_ARITHMETIC the command itself
    // (add, sub, etc) is returned. should not be called if current command is C_RETURN
    public String arg1() {
        String strArg1 = null;
        if (mCmdType.equals("C_ARITHMETIC")) {
            strArg1 = mArg0;
        } else if (!(mCmdType.equals("C_RETURN"))) {
            strArg1 = mArg1;
        }
        return strArg1;
    }

    // returns 2nd arg of curr command - should be called only if curr command is C_PUSH, C_POP, C_FUNCTION, or C_CALL
    public int arg2() {
        int nArg2 = 0;
        if (mCmdType.equals("C_PUSH") || mCmdType.equals("C_POP") || mCmdType.equals("C_FUNCTION") || mCmdType.equals("C_CALL")) {
            nArg2 = Integer.parseInt(mArg2);
        }
        return nArg2;

    }
	
	
}
