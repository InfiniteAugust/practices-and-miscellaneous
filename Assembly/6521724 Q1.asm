		.data
prompt:	.asciiz "Please enter a number n: "
output: .asciiz "The sum of the numbers from 0 to n is: "
		.text
		.globl main

main:   
		la $a0, prompt
		li $v0, 4
		syscall					#print string for user prompt

		li $v0, 5
		syscall
		move $s0, $v0			#read an int n from user and save it into $s0

		li $t0, 0
		li $t1, 0				#initialization $t0=count $t1=sum

loop:	addi $t0, $t0, 1 		#count = count + 1
		add $t1, $t1, $t0		#sum = sum + count
		bgt $s0, $t0, loop		#loop if count < n

		la $a0, output
		li $v0, 4
		syscall					#print string for output

		move $a0, $t1
		li $v0, 1
		syscall					#print the result

		li $v0, 10
		syscall

