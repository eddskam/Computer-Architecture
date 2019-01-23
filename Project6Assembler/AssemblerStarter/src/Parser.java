import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

	// current holds the current command, i.e., the command that the
	// parser is acting on.
	private String current, next;
	private Scanner scanner;
	private boolean hasMoreCommands;

	public Parser(String asmFile) {
		hasMoreCommands = true;

		try {
			scanner = new Scanner(new File(asmFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}

		current = "";
		next = getNextCommand();
	}

	public boolean hasMoreCommands() {
		return hasMoreCommands;
	}

	public void advance() {
		current = next;
		next = getNextCommand();
	}

	private String getNextCommand() {
		String next;

		// After preprocessing, the line might be empty.  Keep going
		// until we get a non-empty line or reach the end of the ASM
		// file

		do {
			if (scanner.hasNextLine())
				next = preprocess(scanner.nextLine());
			else {
				hasMoreCommands = false;
				return "";
			}
		} while (next.length() == 0);

		return next;		
	}

	// Strip whitespace and comments from a string.

	private String preprocess(String str) {		
		String s = str.replaceAll("\\s",  "");
		int index = s.indexOf("//");
		if (index != -1)
			s = s.substring(0, index);

		return s;
	}
}
