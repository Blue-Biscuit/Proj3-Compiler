.data
.comm	a,4,4

.comm	b,4,4

.comm	c,4,4

.text
	.align 4
.globl  main
main:
main_bb2:
main_bb3:
	movl	$2, %EAX
	movl	%EAX, %ESI
	movl	$2, %EAX
	movl	%EAX, %EDI
	movl	%ESI, %EAX
	addl	%EDI, %EAX
main_bb1:
	ret
