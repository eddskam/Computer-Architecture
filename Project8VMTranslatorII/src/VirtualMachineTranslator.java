import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

/**This class translates virtual machine code (.vm) to assembly code (.asm).
 * 
 * @author Edem Kingsley-Amoah
 *
 */
public class VirtualMachineTranslator {

    public static void main(String[] args) {
	if (args.length != 0) {  // Assume file/folder name passed on command line.
	    new VirtualMachineTranslator().translate(args[0]);
	    return;
	}

	// Pop-up a JFileChooser.
	final JFileChooser fc = new JFileChooser();

	try {
	    SwingUtilities.invokeAndWait(new Runnable() {
		public void run() {
		    UIManager.put("swing.boldMetal", Boolean.FALSE);
		    fc.setFileFilter(new FileNameExtensionFilter("VM files", "vm"));
		    fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		    if (fc.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
			System.out.println("No file selected; terminating.");
			System.exit(1);
		    }
		}
	    });
	} catch (Exception e) {
	    e.printStackTrace();
	    System.exit(1);
	}

	new VirtualMachineTranslator().translate(fc.getSelectedFile().getPath());
    }
    
    /**
     * A private field that has the parameter
     * @param path the directory of the file to be selected of type String
     */

    private void translate(String path) {
	String basename;
	File folder = new File(path);
	ArrayList<File> files;

	if (folder.isFile()) {   // Folder is actually a file; translating a single
	    // VM file.
	    files = new ArrayList<File>();
	    files.add(folder);
	    basename = folder.getPath(); 
	    basename = basename.substring(0, basename.indexOf('.'));
	}
	else {   // folder is a folder; translating a folder of VM files.
	    files = new ArrayList<File>(Arrays.asList(folder.listFiles()));
	    basename = folder.getPath() + "/" + folder.getName();
	}
	
	 // construct CodeWriter to generate 
	//code into corresponding output file, only 1 codewriter

	CodeWriter codeWriter = new CodeWriter(basename);
	codeWriter.writeInit();

	// Iterate through all the VM files, or file, that we're translating.

	for (File file : files) {

	    if (!file.getName().endsWith(".vm"))   // Skip non-VM files.
		continue;

	    basename = file.getName(); 
	    basename = basename.substring(0, basename.indexOf('.'));
	    Parser parser = new Parser(file);
	    codeWriter.setFileName(basename);

	    while (parser.hasMoreCommands()) {
		parser.advance();

		// Add your code to perform translation from VM code
		// to ASM code here.
		
		if(parser.commandType() == CommandType.C_ARITHMETIC) {
			codeWriter.writeArith(parser.arg1());
		}
			else if(parser.commandType() == CommandType.C_POP || parser.commandType() == CommandType.C_PUSH) {
				codeWriter.writePushPop(parser.commandType(), parser.arg1(), parser.arg2());
			}
			else if(parser.commandType() == CommandType.C_LABEL) {
			codeWriter.writeLabel(parser.arg1());
		}
			else if(parser.commandType() == CommandType.C_GOTO) {
				codeWriter.writeGoto(parser.arg1());
	    }
			else if(parser.commandType() == CommandType.C_IF) {
				codeWriter.writeIf(parser.arg1());
			}
			else if(parser.commandType() == CommandType.C_RETURN) {
				codeWriter.writeReturn();
			}
			else if(parser.commandType() == CommandType.C_FUNCTION) {
				codeWriter.writeFunction(parser.arg1(),parser.arg2());
			}
			else if(parser.commandType() == CommandType.C_CALL) {
				codeWriter.writeCall(parser.arg1(),parser.arg2());
			}
	    }
	}
	
	//save file
	codeWriter.close();
	
	 System.out.println("File created : " + basename + ".asm");
    }
}
