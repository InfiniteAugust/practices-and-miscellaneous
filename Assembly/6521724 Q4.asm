		.data
prompt:		.asciiz "Input string: \n"
yes_msg:	.asciiz "It is a palindrome.\n"
no_msg:		.asciiz "It is not a palindrome.\n"
buffer: 	.space 100		#buffer for user input
buffer2:	.space 100		#buffer for string without punctuation
		.text
		.globl main

main:		la $a0, prompt
		li $v0, 4
		syscall
		la $a0, buffer 
		li $a1, 100
		li $v0, 8
		syscall			#read string

		la $s1, buffer			
		la $s2, buffer	
		la $s3, buffer2		#allocate space to store the new string	

#To delete whitespaces and punctuations 
loop:		lb $t0, ($s1) 		  #load one byte of the string
		beq $t0, ',', delete	  #branch to delete if it’s one of these signs
		beq $t0, '.', delete	 
		beq $t0, ':', delete 
		beq $t0, '?', delete 
		beq $t0, ';', delete 
		beq $t0, '!', delete
		beq $t0, ' ', delete
		j next		          #if no punctuation or space, goto next

delete:		addi $s1, $s1, 1          #test next char
		j loop 

next:		sb $t0, ($s3)		  #save the letter
		addi $s1, $s1, 1     	  #test next char

		bne $t0, $zero, loop      #not terminated, loop
		j movestr 		  #otherwise next procedure


movestr:	la $s1, buffer2
		la $s2, buffer2		#move the new string into s1 and s2


length: 	lb $t1, ($s2)		#load a byte from $s2 into $t1
		beqz $t1, endLEN	#if byte = 0, end
		addi $s2, $s2, 1 	#next byte
		j length

endLEN: 	sub $s2, $s2, 2			

check:    	bge $s1, $s2, palin	#if $s1 > $s2, it is  palindrome
		lb $t1, ($s1)		#load byte from $s1 to $t1
		lb $t2, ($s2)		#load byte from $s2 to $t2
		bne $t1, $t2, Npalin	#if not equal, it is not a palindrome

		addi $s1, $s1, 1 	#$s1++
		sub $s2, $s2, 1 	#$s2--
		j check					

palin:  	la $a0, yes_msg			
		li $v0, 4
		syscall			#print is palindrome msg
		j exit

Npalin: 	la $a0, no_msg
		li $v0, 4
		syscall			#print is not palindrome msg
		j exit

exit: 		li $v0, 10
		syscall
