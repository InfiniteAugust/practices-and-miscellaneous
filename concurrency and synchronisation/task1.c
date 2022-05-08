#include "coursework.h"
#define MAX_PROCESSES 5

int main(int argc, char const *argv[])
{
	//create a queue, set max of the queue to 5 and initialize count to 0
	struct queue *ptr;
	struct queue q;
	ptr = &q;
	ptr = malloc(sizeof(struct queue));

	(*ptr).max = MAX_PROCESSES;
	(*ptr).count = 0;
	
	init(ptr, MAX_PROCESSES);

	printAll(ptr);

	printf("performing FIFO...\n");
	for (int i = 0; i < MAX_PROCESSES; i++)
	{
		struct element temp = generateProcess();
		addFirst(ptr, &temp);
	}

	printAll(ptr);

	for (int i = 0; i < MAX_PROCESSES; ++i)
	{
		removeLast(ptr);
		printAll(ptr);
	}
	

	printf("\nperforming LIFO...\n");
	for (int i = 0; i < MAX_PROCESSES; ++i)
	{
		struct element temp = generateProcess();
		addLast(ptr, &temp);
	}

	printAll(ptr);

	for (int i = 0; i < MAX_PROCESSES; ++i)
	{
		removeLast(ptr);
		printAll(ptr);
	}

	freeAll(ptr);

	return 0;
}