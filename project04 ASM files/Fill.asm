// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, the
// program clears the screen, i.e. writes "white" in every pixel.

@SCREEN
D=A	
@curr_screen_pos
M=D

@8191
D=A
@SCREEN
D=D+A
@max_screen_pos
M=D	
	
(START)
	@KBD
	D=M // if no key pressed, d == 0
	@WHITE
	D; JEQ
	
	@BLACK
	0; JMP

(BLACK)
	@curr_screen_pos
	A=M // D is now the number that we want
	M=-1 // M[curr_screen_pos] = -1

	@INCR
	0; JMP

(WHITE)
	@curr_screen_pos
	A=M
	M=0
	@INCR

(INCR)
	@curr_screen_pos
	D=M+1
	M=D

	@max_screen_pos
	D=D-M
	@START
	D;JNE
	
	@SCREEN
	D=A
	@curr_screen_pos
	M=D
	@START
	0;JMP 
