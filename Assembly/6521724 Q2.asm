			.data
prompt1:	.asciiz "Please enter number x: "
prompt2:	.asciiz "Please enter number y: "
outstr:		.asciiz "The result is: "
error_msg:  	.asciiz "Error! Arithmatic overflow."
			.text
			.globl main

main:
		la $a0, prompt1
		li $v0, 4
		syscall				#print string for prompt1
		li $v0, 5
		syscall
		move $s0, $v0			#read number x, save in $s0

		la $a0, prompt2
		li $v0, 4
		syscall				#print string for prompt2
		li $v0, 5
		syscall
		move $s1, $v0			#read number y, save in $s1



#calculation
		add $s0, $s0, $s1		#$s0 = x + y
		bgt $s0, 2147483646, exit
		blt $s0, -2147483645, exit
		
		mul $s2, $s0, $s0		#$s2 = (x + y)^2
		bgt $s2, 2147483646, exit
		blt $s2, -2147483645, exit
		
		mul $s2, $s2, $s0		#$s0 = (x + y)^3
		bgt $s2, 2147483646, exit
		blt $s2, -2147483645, exit

		add $s1, $s1, $s1		#$s1 = 2y
		bgt $s1, 2147483646, exit
		blt $s1, -2147483645, exit

		mul $s3, $s1, $s1		#$s3 = (2y)^2
		bgt $s3, 2147483646, exit
		blt $s3, -2147483645, exit
		
		mul $s3, $s3, $s1		#$s3 = (2y)^3
		bgt $s3, 2147483646, exit
		blt $s3, -2147483645, exit
		
		add $s3, $s3, $s2		#$s3 = result
		bgt $s3, 2147483646, exit
		blt $s3, -2147483645, exit

		j end

exit: 		la $a0, error_msg
		li $v0, 4
		syscall				#print string for error massage 

		li $v0, 10
		syscall	

end: 		la $a0, outstr
		li $v0, 4
		syscall				#print output message

		move $a0, $s3
		li $v0, 1
		syscall				#print result 

		li $v0, 10
		syscall				


