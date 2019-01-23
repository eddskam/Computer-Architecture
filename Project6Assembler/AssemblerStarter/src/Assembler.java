import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Assembler {

    public static void main(String[] args) {
	// If there's a command line argument, assume it's an ASM file and
	// assemble it.
	if (args.length != 0) {
	    new Assembler().assemble(args[0]);
	    return;
	}

	// Otherwise, pop-up a JFileChooser to allow the user to use a GUI
	// to select the ASM file.
	JFileChooser fc = new JFileChooser();

	//Schedule a job for the event dispatch thread:
	//creating and showing the assembler's JFileChooser.
	// Wait for the job to finish before continuing.
	try {
	    SwingUtilities.invokeAndWait(new Runnable() {
		public void run() {
		    //Turn off metal's use of bold fonts
		    UIManager.put("swing.boldMetal", Boolean.FALSE);
		    // Only allow the selection of files with an extension of .asm.
		    fc.setFileFilter(new FileNameExtensionFilter("ASM files", "asm"));
		    // Uncomment the following to allow the selection of files and
		    // directories.  The default behavior is to allow only the selection
		    // of files.
		    //fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		    if (fc.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
			System.out.println("No file selected; terminating.");
			System.exit(1);
		    }
		}
	    });
	} catch (Exception e) {
	    e.printStackTrace();
	    return;
	}

	new Assembler().assemble(fc.getSelectedFile().getPath());
    }

    private void assemble(String asmFile) {
	// ASM file name without the .asm extension.
	String basename = new String(asmFile.substring(0, asmFile.indexOf('.')));
	Parser parser = new Parser(asmFile);

	// Uncomment each of the following after you implement each
	// class.
	//Code code = new Code();
	//SymbolTable symbolTable = new SymbolTable();

	// Output file
	FileWriter hackFile;
	// Memory location of next assembly command.
	int nextCommandLoc = 0;
	// Memory location of next variable
	int nextVariableLoc = 16;


	// Your code for the first pass goes here.


	// Prepare for the second pass.
	parser = new Parser(asmFile);

	try {
	    hackFile = new FileWriter(basename + ".hack");


	    // Your code for the second pass goes here.


	    // Uncomment the following to confirm that the Hack file
	    // is being written.  
	    //hackFile.write("Hello world\n");

	    hackFile.close();
	} catch (IOException e) {
	    e.printStackTrace();
	    System.exit(1);
	}

    }

    // You might find this useful.
    // Convert the decimal value val to a 15 bit binary string.  Assume that
    // val's value lies between 0 and 32,767, inclusive.  In other words, assume
    // that val is non-negative and will fit into a 15 bit number.
    private String toImmediate(int val) {
	String field = "";

	for (int i = 0; i < 15; i++) {
	    field = (val % 2) + field;
	    val /= 2;
	}
	return field;
    }
}
