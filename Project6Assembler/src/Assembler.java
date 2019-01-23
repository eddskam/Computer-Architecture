import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.StringWriter;

@SuppressWarnings("unused")
public class Assembler {
	private File assemblyCode;
	private BufferedWriter machineCode;
	private Code code;
	private SymbolTable symbolTable;
	

	
	
	
	
	public Assembler(File source, File target) throws IOException {
		this.assemblyCode = source;
		
		// Create buffered writer.
		FileWriter fw = new FileWriter(target);
		this.machineCode = new BufferedWriter(fw);
		
		// Initialize assembler components.
		this.code = new Code();
		this.symbolTable = new SymbolTable();
		
		
	}
	
	
	
	// Translate assembly file to machine language.
	public void translate() throws IOException {
		this.recordLabelAddress();
		this.parse();
	}
	
	private void recordLabelAddress() throws IOException {
		Parser parser = new Parser(this.assemblyCode);
		while (parser.hasMoreCommands()) {
			parser.advance();
			
			CommandType commandType = parser.commandType();
			
			if (commandType.equals(CommandType.L_COMMAND)) {
				String symbol = parser.symbol();
				int address = this.symbolTable.getProgramAddress();
				this.symbolTable.addEntry(symbol, address);
			} else {
				this.symbolTable.incrementProgramAddress();
			}
		}
		parser.close();
	}
		
	// Parse source file.
	private void parse() throws IOException {
		Parser parser = new Parser(this.assemblyCode);
		while (parser.hasMoreCommands()) {
			parser.advance();
	
			CommandType commandType = parser.commandType();
			String instruction = null;
			
			if (commandType.equals(CommandType.A_COMMAND)) {
				// Format A-Instruction.
				String symbol = parser.symbol();
				Character firstCharacter = symbol.charAt(0);
				boolean isSymbol = (!Character.isDigit(firstCharacter));
				
				String address = null;
				if (isSymbol) {
					boolean symbolExists = this.symbolTable.contains(symbol);
					
					// Record address if symbol does not exist (variable).
					if (!symbolExists) {
						int dataAddress = this.symbolTable.getDataAddress();
						this.symbolTable.addEntry(symbol, dataAddress);
						this.symbolTable.incrementDataAddress();
					}
						
					// Get address  of symbol.
					address = Integer.toString(
							this.symbolTable.getAddress(symbol));
				} else {
					address = symbol;
				}
				
				instruction = this.formatAInstruction(address);
			} else if (commandType.equals(CommandType.C_COMMAND)) {
				// Format C-Instruction
				String comp = parser.comp();
				String dest = parser.dest();
				String jump = parser.jump();
				instruction = this.formatCInstruction(comp, dest, jump);
			}
	
			if (!commandType.equals(CommandType.L_COMMAND)) {
				// Write binary instruction to file.
				this.machineCode.write(instruction);
				this.machineCode.newLine();
			}
		}
		
		// Release resources.
		parser.close();
		this.machineCode.flush();
		this.machineCode.close();
	}

	// Machine-format an A-Instruction.
	private String formatAInstruction(String address) {
		String formattedNumber = this.code.formatNumberAsBinary(address);
		return "0" + formattedNumber;
	}
	
	// Machine-format a C-Instruction.
	private String formatCInstruction( String comp, String dest, String jump) {
		StringWriter instruction = new StringWriter();
		instruction.append("111");
		instruction.append(this.code.comp(comp));
		instruction.append(this.code.dest(dest));
		instruction.append(this.code.jump(jump));
		return instruction.toString();
	}
	
}
	/**

    public static void main(String[] args) {
	// If there's a command line argument, assume it's an ASM file and
	// assemble it.
    	
String name = args[0].substring(0, args[0].indexOf('.'));	//copies name of existing file without the file type
		
		
    	
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
		    fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
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
	    hackFile.write("Hello world\n");

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
**/
