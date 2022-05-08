			.data
prompt1:	.asciiz "Input string: "
prompt2:	.asciiz "Input character: "
output: 	.asciiz "\nOutput: "
buffer:   	.space 100
			.text
			.globl main

main:
		la $a0, prompt1
		li $v0, 4
		syscall				 #prompt for string
		la $a0, buffer 
		li $a1, 100
		li $v0, 8
		syscall				 #read string
		la $s0, buffer		 	#save string in $s0

		la $a0, prompt2		
		li $v0, 4
		syscall				 #prompt for character 
		li $v0, 12
		syscall				 #read a character (into $v0)
		move $s1, $v0	 	 	#load char into $s1
		
		li $s2, 0			 #initialize count = 0

loop:	lb $t0, ($s0)			 	#load one byte of string into $t0
		addi $s0, $s0, 1 
		beq $t0, $s1, tag1		 #goto tag1 if matches 
		j tag2				 #otherwise goto tag2

tag1:   addi $s2, $s2, 1 	 		#count++
		bne $t0, $zero, loop		 #loop if character of str != nul
		j end				 #end if = nul

tag2:	bne $t0, $zero, loop 			#loop if character of str != nul
		j end				 #end if = nul

end:	la $a0, output
		li $v0, 4			 #output = 
		syscall
		move $a0, $s2
		li $v0, 1 			 #print result
		syscall

		li $v0, 10
		syscall				 #end

