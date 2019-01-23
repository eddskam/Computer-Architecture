import java.io.FileWriter;
import java.io.IOException;


/**Translates VM commands into Hack assembly code
 * 
 * @author Edem Kingsley-Amoah
 *
 */
public class CodeWriter {

	private FileWriter asmFile;
	private String fileName = "";
	private String functionName = "";
	private int labelCount = 0;
	
	/**
	 *  
	 * @param basename the prefix of the file name which has ".asm" as its
	 * extension
	 */

	public CodeWriter(String basename) {
		try {  // Open the ASM file for writing.
			asmFile = new FileWriter(basename + ".asm");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * // informs code writer that translation of new VM file is started
	 * @param fileName which is the name of the file of type String
	 */

	public void setFileName(String fileName) {
		this.fileName = fileName;
		functionName = "";
	}

	// You might find this useful.
	// Write a sequence of ASM code to the ASM file.
	// The parameter code is expected to contain all necessary newline
	// characters.
	
	/**
	 * 
	 * @param code the translated assembly code of type String
	 */
	
	private void writeCode(String code) {
		try{
			asmFile.write(code);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	int trueOrFalse = 0;
	
	/**
	 * 
	 * @param strCommand the arithmetic command that translates the vm code to hack code
	 */
	
	public void writeArith(String strCommand) {
		if (strCommand.equals("add")) {
			String add = "@SP\n" + "AM=M-1\n" + "D=M\n" + "@SP\n" + "AM=M-1\n" + "M=M+D\n" + "@SP\n" + "M=M+1\n";
			writeCode(add);

		} else if (strCommand.equals("sub")) {
			String sub = "@SP\n" + "AM=M-1\n" + "D=M\n" + "@SP\n" + "AM=M-1\n" + "M=M-D\n" + "@SP\n" + "M=M+1\n";
			writeCode(sub);

		} else if (strCommand.equals("neg")) {
			String neg = "@SP\n" + "AM=M-1\n" + "M=-M\n" + "@SP\n" + "M=M+1\n";
			writeCode(neg);

		} else if (strCommand.equals("eq")) {
			String eq = "@SP\n" + "AM=M-1\n" + "D=M\n" + "A=A-1\n" + "D=M-D\n" + "@FALSE" + trueOrFalse + "\n" + "D;"
					+ "JNE\n" + "@SP\n" + "A=M-1\n" + "M=-1\n" + "@GOTO" + trueOrFalse + "\n" + "0;JMP\n" + "(FALSE"
					+ trueOrFalse + ")\n" + "@SP\n" + "A=M-1\n" + "M=0\n" + "(GOTO" + trueOrFalse + ")\n";
			writeCode(eq);
			trueOrFalse++;

		} else if (strCommand.equals("gt")) {
			String gt = "@SP\n" + "AM=M-1\n" + "D=M\n" + "A=A-1\n" + "D=M-D\n" + "@FALSE" + trueOrFalse + "\n" + "D;"
					+ "JLE\n" + "@SP\n" + "A=M-1\n" + "M=-1\n" + "@GOTO" + trueOrFalse + "\n" + "0;JMP\n" + "(FALSE"
					+ trueOrFalse + ")\n" + "@SP\n" + "A=M-1\n" + "M=0\n" + "(GOTO" + trueOrFalse + ")\n";
			writeCode(gt);
			trueOrFalse++;

		} else if (strCommand.equals("lt")) {

			String lt = "@SP\n" + "AM=M-1\n" + "D=M\n" + "A=A-1\n" + "D=M-D\n" + "@FALSE" + trueOrFalse + "\n" + "D;"
					+ "JGE\n" + "@SP\n" + "A=M-1\n" + "M=-1\n" + "@GOTO" + trueOrFalse + "\n" + "0;JMP\n" + "(FALSE"
					+ trueOrFalse + ")\n" + "@SP\n" + "A=M-1\n" + "M=0\n" + "(GOTO" + trueOrFalse + ")\n";
			writeCode(lt);
			trueOrFalse++;

		} else if (strCommand.equals("and")) {
			String and = "@SP\n" + "AM=M-1\n" + "D=M\n" + "@SP\n" + "AM=M-1\n" + "M=M&D\n" + "@SP\n" + "M=M+1\n";
			writeCode(and);

		} else if (strCommand.equals("or")) {
			String or = "@SP\n" + "AM=M-1\n" + "D=M\n" + "@SP\n" + "AM=M-1\n" + "M=M|D\n" + "@SP\n" + "M=M+1\n";
			;
			writeCode(or);

		} else if (strCommand.equals("not")) {
			String not = "@SP\n" + "A=M-1\n" + "M=!M\n";
			writeCode(not);
		}}
	        
	
	/**
	 * 
	 * @param strCommand the command to be translated which is of type string
	 * @param strSegment the segment of the command, which is a String
	 * @param nIndex the index of the command, which is an integer.
	 */


	    
		
	
		
		// writes the assembly code that is the translation of the given command, where command is either C_PUSH or C_POP, given segment and index as well
	    public void writePushPop(CommandType strCommand, String strSegment, int nIndex) {

	        if (strCommand == CommandType.C_PUSH) {
				if (strSegment.equals("constant")) {
					String constant = "@" + nIndex + "\n" + "D=A\n" + "@SP\n" + "A=M\n" + "M=D\n" + "@SP\n" + "M=M+1\n";
					writeCode(constant);
				} else if (strSegment.equals("argument")) {
					String argument = "@" + "ARG" + "\n" + "D=M\n" + "@" + nIndex + "\n" + "A=A+D\n" + "D=M\n" + "@SP\n"
							+ "A=M\n" + "M=D\n" + "@SP\n" + "M=M+1\n";
					writeCode(argument);
				} else if (strSegment.equals("local")) {
					String local = "@" + "LCL" + "\n" + "D=M\n" + "@" + nIndex + "\n" + "A=A+D\n" + "D=M\n" + "@SP\n"
							+ "A=M\n" + "M=D\n" + "@SP\n" + "M=M+1\n";
					writeCode(local);
				} else if (strSegment.equals("this")) {
					String THIS = "@" + "THIS" + "\n" + "D=M\n" + "@" + nIndex + "\n" + "A=A+D\n" + "D=M\n" + "@SP\n"
							+ "A=M\n" + "M=D\n" + "@SP\n" + "M=M+1\n";
					writeCode(THIS);
				} else if (strSegment.equals("that")) {
					String THAT = "@" + "THAT" + "\n" + "D=M\n" + "@" + nIndex + "\n" + "A=A+D\n" + "D=M\n" + "@SP\n"
							+ "A=M\n" + "M=D\n" + "@SP\n" + "M=M+1\n";
					writeCode(THAT);
				} else if (strSegment.equals("temp")) {
					String temp = "@" + "R5" + "\n" + "D=M\n" + "@" + (nIndex + 5) + "\n" + "A=A+D\n" + "D=M\n" + "@SP\n"
							+ "A=M\n" + "M=D\n" + "@SP\n" + "M=M+1\n";
					writeCode(temp);
				} else if (strSegment.equals("pointer")) {
					String pointer = "@" + "R5" + "\n" + "D=M\n" + "@" + (nIndex + 3) + "\n" + "A=A+D\n" + "D=M\n" + "@SP\n"
							+ "A=M\n" + "M=D\n" + "@SP\n" + "M=M+1\n";
					writeCode(pointer);
				} else if (strSegment.equals("static")) {
					String static1 = "@" + fileName + "." + nIndex + "\n" + "D=M\n" +  "@SP\n" + "A=M\n" + "M=D\n" + "@SP\n" + "M=M+1\n";
					writeCode(static1);
				}
			}

			else if (strCommand == CommandType.C_POP) {
				if (strSegment.equals("argument")) {
					String argument = "@" + "ARG" + "\n" + "D=M\n" + "@" + nIndex + "\n" + "D=D+A\n" + "@" + "R13" + "\n"
							+ "M=D\n" + "@SP\n" + "AM=M-1\n" + "D=M\n" + "@R13\n" + "A=M\n" + "M=D\n";
					writeCode(argument);
				} else if (strSegment.equals("local")) {
					String local = "@" + "LCL" + "\n" + "D=M\n" + "@" + nIndex + "\n" + "D=D+A\n" + "@" + "R13" + "\n"
							+ "M=D\n" + "@SP\n" + "AM=M-1\n" + "D=M\n" + "@R13\n" + "A=M\n" + "M=D\n";
					writeCode(local);
				} else if (strSegment.equals("this")) {
					String THIS = "@" + "THIS" + "\n" + "D=M\n" + "@" + nIndex + "\n" + "D=D+A\n" + "@" + "R13" + "\n"
							+ "M=D\n" + "@SP\n" + "AM=M-1\n" + "D=M\n" + "@R13\n" + "A=M\n" + "M=D\n";
					writeCode(THIS);
				} else if (strSegment.equals("that")) {
					String THAT = "@" + "THAT" + "\n" + "D=M\n" + "@" + nIndex + "\n" + "D=D+A\n" + "@" + "R13" + "\n"

							
							+ "M=D\n" + "@SP\n" + "AM=M-1\n" + "D=M\n" + "@R13\n" + "A=M\n" + "M=D\n";
					writeCode(THAT);
				} else if (strSegment.equals("temp")) {
					String temp = "@" + "R5" + "\n" + "D=M\n" + "@" + (nIndex + 5) + "\n" + "D=D+A\n" + "@" + "R13" + "\n"
							+ "M=D\n" + "@SP\n" + "AM=M-1\n" + "D=M\n" + "@R13\n" + "A=M\n" + "M=D\n";
					writeCode(temp);
				} else if (strSegment.equals("pointer")) {
					String pointer = "@" + "R5" + "\n" + "D=M\n" + "@" + (nIndex + 3) + "\n" + "D=D+A\n" + "@" + "R13" + "\n"
							+ "M=D\n" + "@SP\n" + "AM=M-1\n" + "D=M\n" + "@R13\n" + "A=M\n" + "M=D\n";
					writeCode(pointer);
				} else if (strSegment.equals("static")) {
					String static1 = "@" + fileName + "." + nIndex + "\n" + "D=A\n" + "@" + "R13" + "\n"
							+ "M=D\n" + "@SP\n" + "AM=M-1\n" + "D=M\n" + "@R13\n" + "A=M\n" + "M=D\n";
					writeCode(static1);
				}
			}
		}
	    
	    /**
	     * 
	     * @param strLabel the label command of type String
	     */

	    // writes assembly code that is translation of label command
	    // arbitrary string with any sequence of letters, digits, underscore, dot, and colon not beginning with digit
	    public void writeLabel(String strLabel) {
	    	String label1 = "(" + fileName + "$" + strLabel + ")\n";
			writeCode(label1);


	    }
	    
	    

	    // writes assembly code that is translation of goto command
	    public void writeGoto(String strLabel) {
	    	String label1 = "@" + fileName + "$" + strLabel + "\n" + "0;JMP\n";
			writeCode(label1);
	        }

	    

	    // writes assembly code that is translation of if-goto command
	    public void writeIf(String strLabel) {
	    	String label1 = "@SP\n" + "AM = M-1" + "\n" + "D=M\n" + "A=A-1\n" + "@" + fileName + "$" + strLabel + "\n" + "D;JNE\n";
			writeCode(label1);

	    }

	    // writes assembly code that is translation of call command
	    public void writeCall(String functionName, int nNumArgs) {
	    	writeCode("// start call\n");
			String pushRA = "@" + (fileName + "$" + labelCount) + "\n" + "D=A\n" + "@SP" + "\n" + "A=M\n" + "M=D\n" + "@SP" + "\n"
					+ "M=M+1\n";
			writeCode(pushRA);
			String local = "@" + "LCL" + "\n" + "D=M\n" + "@" + "SP"+ "\n" + "A=M\n" + "M=D\n" + "@SP\n" + "M=M+1\n";
			writeCode(local);
			String argument = "@" + "ARG" + "\n" + "D=M\n" + "@" + "SP" + "\n" + "A=M\n" + "M=D\n" + "@SP\n" + "M=M+1\n";
			writeCode(argument);
			String THIS = "@" + "THIS" + "\n" + "D=M\n" + "@" + "SP" + "\n" + "A=M\n" + "M=D\n" + "@SP\n" + "M=M+1\n";
			writeCode(THIS);
			String THAT = "@" + "THAT" + "\n" + "D=M\n" + "@" + "SP" + "\n" + "A=M\n" + "M=D\n" + "@SP\n" + "M=M+1\n";
			writeCode(THAT);
			String Call = "@SP" + "\n" + "D=M\n" + "@5" + "\n" + "D=D-A\n" + "@" + nNumArgs + "\n" + "D=D-A\n" + "@ARG"
					+ "\n" + "M=D\n" + "@SP" + "\n" + "D=M\n" + "@LCL" + "\n" + "M=D\n" + "@" + functionName + "\n"
					+ "0;JMP" + "\n" + "(" + (fileName + "$" + labelCount) + ")" + "\n";
			labelCount++;
			writeCode(Call);
	writeCode("// endCall\n");


	    }    

		
	
	 // writes assembly code that effects VM initialization - bootstrap code at beginning of output file
    public void writeInit() {
    	writeCode("@256" + "\n" + "D=A\n" + "@SP" + "\n"+ "M=D\n");
		writeCall("Sys.init", 0);


    }
    
   

    // writes assembly code that is translation of return command
    public void writeReturn() {
    	writeCode("//Start return\n");
		String returnString = "@LCL\n" + "D=M\n" + "@R13\n" + "M=D\n" + "@5\n" + "A=D-A\n" + "D=M\n" + "@R14\n" + "M=D\n" +  "@" + "ARG" + "\n" +
                 "D=M\n@" + 0 + "\nD=D+A\n" + "@R15\n" + "M=D\n" + "@SP\n" + "AM=M-1\n" + "D=M\n" + "@R15\n" + "A=M\n" + "M=D\n" +
                 "@ARG\n" + "D=M\n" + "@SP\n" + "M=D+1\n" + "@R13\n" + "D=M-1\n" + "AM=D\n" + "D=M\n" + "@" + "THAT" + "\n" + "M=D\n" +
		         "@R13\n" + "D=M-1\n" + "AM=D\n" + "D=M\n" + "@" + "THIS" + "\n" + "M=D\n" + "@R13\n" + "D=M-1\n" + "AM=D\n" + "D=M\n" +
                 "@" + "ARG" + "\n" + "M=D\n" + "@R13\n" + "D=M-1\n" + "AM=D\n" + "D=M\n" + "@" + "LCL" + "\n" + "M=D\n" +
                 "@R14\n" + "A=M\n" + "0;JMP\n";
				
		writeCode(returnString);
		writeCode("// endReturn\n");

    }
    
    /**
     * 
     * @param strFunctionName the name of the function command of type String
     * @param nNumLocals the local number(s) which is/are of type int.
     */

    // writes assembly code that is translation of given function command
    public void writeFunction(String functionName, int nNumLocals) {
		writeCode("// start function\n");
		String funcname = ("(" + functionName + ")\n");
		writeCode(funcname);
		for (int i = 0; i < nNumLocals; i++) {

			writePushPop(CommandType.C_PUSH, "constant", 0);

		}
		writeCode("//end function\n");
    }
		
		public void close() {
			try {
				asmFile.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}


}
