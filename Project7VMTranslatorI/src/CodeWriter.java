import java.io.FileWriter;
import java.io.IOException;
//import java.io.File;

/**Translates VM commands into Hack assembly code
 * 
 * @author Edem Kingsley-Amoah
 *
 */
public class CodeWriter {

	private FileWriter asmFile;
	private String fileName = "";
	private String functionName = "";
	private int arthJumpFlag;
	
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
	public void writeCode(String code) {
		
		 String strACode = null;
	        if (code.equals("add")) {
	            strACode = new StringBuilder().append(getArithFormat1()).append("M=M+D\n").toString();

	        } else if (code.equals("sub")) {
	            strACode = new StringBuilder().append(getArithFormat1()).append("M=M-D\n").toString();
	        } else if (code.equals("neg")) {
	            strACode = new StringBuilder().append("D=0\n").append("@SP\n").append("A=M-1\n").append("M=D-M\n").toString();
	        } else if (code.equals("eq")) {
	            strACode = new StringBuilder().append(getArithFormat1()).append(getArithFormat2("JNE")).toString();
	            arthJumpFlag++;
	        } else if (code.equals("gt")) {
	            strACode = new StringBuilder().append(getArithFormat1()).append(getArithFormat2("JLE")).toString();
	            arthJumpFlag++;
	        } else if (code.equals("lt")) {
	            strACode = new StringBuilder().append(getArithFormat1()).append(getArithFormat2("JGE")).toString();
	            arthJumpFlag++;
	        } else if (code.equals("and")) {
	            strACode = new StringBuilder().append(getArithFormat1()).append("M=M&D\n").toString();
	        } else if (code.equals("or")) {
	            strACode = new StringBuilder().append(getArithFormat1()).append("M=M|D\n").toString();
	        } else if (code.equals("not")) {
	            strACode = new StringBuilder().append("@SP\n").append("A=M-1\n").append("M=!M\n").toString();
	        }
	        try{
				asmFile.write(strACode);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	
	/**
	 * 
	 * @param strCommand the command to be translated which is of type string
	 * @param strSegment the segment of the command, which is a String
	 * @param nIndex the index of the command, which is an integer.
	 */


	    
		
	
		
		// writes the assembly code that is the translation of the given command, where command is either C_PUSH or C_POP, given segment and index as well
	    public void writePushPop(String strCommand, String strSegment, int nIndex) {
	        String code = "";
	        if (strCommand.equals("C_PUSH")) {
	            if (strSegment.equals("static")) {
	                code = new StringBuilder().append("@").append(fileName).append(nIndex).append("\n").append("D=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n").toString();
	            } else if (strSegment.equals("this")) {
	                code = getPushFormat1("THIS", nIndex);
	            } else if (strSegment.equals("local")) {
	                code = getPushFormat1("LCL", nIndex);

	            } else if (strSegment.equals("argument")) {
	                code = getPushFormat1("ARG", nIndex);

	            } else if (strSegment.equals("that")) {
	                code = getPushFormat1("THAT", nIndex);

	            } else if (strSegment.equals("constant")) {
	                code = new StringBuilder().append("@").append(nIndex).append("\n").append("D=A\n").append("@SP\n").append("A=M\n").append("M=D\n").append("@SP\n").append("M=M+1\n").toString();

	            } else if (strSegment.equals("pointer") && nIndex == 0) {
	                code = getPushFormat2("THIS");
	            } else if (strSegment.equals("pointer") && nIndex == 1) {
	                code = getPushFormat2("THAT");
	            } else if (strSegment.equals("temp")) {
	                code = getPushFormat1("R5", nIndex + 5);

	            }

	        } else if (strCommand.equals("C_POP")) {
	            if (strSegment.equals("static")) {
	                code = new StringBuilder().append("@").append(fileName).append(nIndex).append("\nD=A\n@R13\nM=D\n@SP\nAM=M-1\nD=M\n@R13\nA=M\nM=D\n").toString();
	            } else if (strSegment.equals("this")) {
	                code = getPopFormat1("THIS", nIndex);
	            } else if (strSegment.equals("local")) {
	                code = getPopFormat1("LCL", nIndex);

	            } else if (strSegment.equals("argument")) {
	                code = getPopFormat1("ARG", nIndex);

	            } else if (strSegment.equals("that")) {
	                code = getPopFormat1("THAT", nIndex);

	            } else if (strSegment.equals("constant")) {
	                code = new StringBuilder().append("@").append(nIndex).append("\n").append("D=A\n").append("@SP\n").append("A=M\n").append("M=D\n").append("@SP\n").append("M=M+1\n").toString();

	            } else if (strSegment.equals("pointer") && nIndex == 0) {
	                code = getPopFormat2("THIS");
	            } else if (strSegment.equals("pointer") && nIndex == 1) {
	                code = getPopFormat2("THAT");
	            } else if (strSegment.equals("temp")) {
	                code = getPopFormat1("R5", nIndex + 5);

	            }


	        }

		try{
			asmFile.write(code);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	  

	 // get format for arithmetic command - applies to all except for neg and not
	    public String getArithFormat1() {
	        return new StringBuilder().append("@SP\n").append("AM=M-1\n").append("D=M\n").append("A=A-1\n").toString();
	    }
	    
	    /**
	     * 
	     * @param strJump the jump command, which is of type String
	     * @return
	     */

	    // get 2nd part of format for arithmetic commands - only for et, gt, and lt
	    public String getArithFormat2(String strJump) {
	        return new StringBuilder().append("D=M-D\n").append("@FALSE").append(arthJumpFlag).append("\n").append("D;").append(strJump).append("\n@SP\n").append("A=M-1\n").append("M=-1\n").append("@CONTINUE").append(arthJumpFlag).append("\n0;JMP\n").append("(FALSE").append(arthJumpFlag).append(")\n").append("@SP\n").append("A=M-1\n").append("M=0\n").append("(CONTINUE").append(arthJumpFlag).append(")\n").toString();

	    }

	    // get format for pushing onto stack given the segment and index - for this, local, argument, that, and temp
	    public String getPushFormat1(String strSegment, int nIndex) {
	        String strACode;
	        strACode = new StringBuilder().append("@").append(strSegment).append("\nD=M\n@").append(nIndex).append("\n").append("A=D+A\n").append("D=M\n").append("@SP\n").append("A=M\n").append("M=D\n").append("@SP\n").append("M=M+1\n").toString();
	        return strACode;

	    }

	    // get format for pushing onto stack given the segment - for static & pointer
	    public String getPushFormat2(String strSegment) {
	        String strAcode;
	        strAcode = new StringBuilder().append("@").append(strSegment).append("\nD=M\n").append("@SP\n").append("A=M\n").append("M=D\n").append("@SP\n").append("M=M+1\n").toString();
	        return strAcode;
	    }

	    // get format for popping off of stack given the segment and index - for this, local, argument, that, and temp
	    public String getPopFormat1(String strSegment, int nIndex) {
	        String strAcode;
	        strAcode = new StringBuilder().append("@").append(strSegment).append("\nD=M\n@").append(nIndex).append("\n").append("D=D+A\n").append("@R13\n").append("M=D\n").append("@SP\n").append("AM=M-1\n").append("D=M\n").append("@R13\n").append("A=M\n").append("M=D\n").toString();
	        return strAcode;
	    }

	    // get format for popping off of stack given the segment - for static & pointer
	    public String getPopFormat2(String strSegment) {
	        String strAcode;
	        strAcode = new StringBuilder().append("@").append(strSegment).append("\nD=A\n").append("@R13\n").append("M=D\n").append("@SP\n").append("AM=M-1\n").append("D=M\n").append("@R13\n").append("A=M\n").append("M=D\n").toString();
	        return strAcode;

	    }
	

	public void close() {
		try {
			asmFile.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	 // writes assembly code that effects VM initialization - bootstrap code at beginning of output file
    public void writeInit() {
        try {
            asmFile.write(new StringBuilder().append("@256\n").append("D=A\n").append("@SP\n").append("M=D\n").toString());
            writeCall("Sys.init", 0);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    
    /**
     * 
     * @param strLabel the label command of type String
     */

    // writes assembly code that is translation of label command
    // arbitrary string with any sequence of letters, digits, underscore, dot, and colon not beginning with digit
    public void writeLabel(String strLabel) {
        try {
            asmFile.write(new StringBuilder().append("(").append(strLabel).append(")\n").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    
    

    // writes assembly code that is translation of goto command
    public void writeGoto(String strLabel) {
        try {
            asmFile.write(new StringBuilder().append("@").append(strLabel).append("\n0;JMP\n").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // writes assembly code that is translation of if-goto command
    public void writeIf(String strLabel) {
        try {
            asmFile.write(new StringBuilder().append(getArithFormat1()).append("@").append(strLabel).append("\nD;JNE\n").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // writes assembly code that is translation of call command
    public void writeCall(String strFunctionName, int nNumArgs) {
        String strLabel = "RETURN_LABEL" + functionName;
        arthJumpFlag++;
        
        try {
            asmFile.write(new StringBuilder().append("@").append(strLabel).append("\n").append("D=A\n@SP\nA=M\nM=D\n@SP\nM=M+1\n").append(getPushFormat2("LCL")).append(getPushFormat2("ARG")).append(getPushFormat2("THIS")).append(getPushFormat2("THAT")).append("@SP\n").append("D=M\n").append("@5\n").append("D=D-A\n").append("@").append(nNumArgs).append("\n").append("D=D-A\n").append("@ARG\n").append("M=D\n").append("@SP\n").append("D=M\n").append("@LCL\n").append("M=D\n").append("@").append(strFunctionName).append("\n0;JMP\n(").append(strLabel).append(")\n").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // writes assembly code that is translation of return command
    public void writeReturn() {
        try {
            asmFile.write(new StringBuilder().append("@LCL\n").append("D=M\n").append("@FRAME\n").append("M=D\n").append("@5\n").append("A=D-A\n").append("D=M\n").append("@RET\n").append("M=D\n").append(getPopFormat1("ARG", 0)).append("@ARG\n").append("D=M\n").append("@SP\n").append("M=D+1\n").append("@FRAME\n").append("D=M-1\n").append("AM=D\n").append("D=M\n").append("@THAT\n").append("M=D\n").append("@FRAME\n").append("D=M-1\n").append("AM=D\n").append("D=M\n").append("@THIS\n").append("M=D\n").append("@FRAME\n").append("D=M-1\n").append("AM=D\n").append("D=M\n").append("@ARG\n").append("M=D\n").append("@FRAME\n").append("D=M-1\n").append("AM=D\n").append("D=M\n").append("@LCL\n").append("M=D\n").append("@RET\n").append("A=M\n").append("0;JMP\n").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    /**
     * 
     * @param strFunctionName the name of the function command of type String
     * @param nNumLocals the local number(s) which is/are of type int.
     */

    // writes assembly code that is translation of given function command
    public void writeFunction(String strFunctionName, int nNumLocals) {
        try {
            asmFile.write(new StringBuilder().append("(").append(strFunctionName).append(")\n").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int nC = 0; nC < nNumLocals; nC++) {
            writePushPop("C_PUSH", "constant", 0);
        }


    }


}
