#include "osc_queue.h"
#include <stdlib.h>

int init(struct queue *my_arr, int arr_size){

	printf("initialising...\n");

	my_arr->e = malloc(arr_size * sizeof(struct element));

	if (my_arr == NULL)
	{
		printf("fail to initialise\n");
		return 1;
	}

	for (int i = 0; i < arr_size; i++)
	{	
		my_arr->e[i].pid = 0;
		my_arr->e[i].pid_time = 0;
		my_arr->e[i].created_time.tv_sec = 0;
		my_arr->e[i].created_time.tv_usec = 0;
		my_arr->e[i].pid_priority = 0;
	}
	
	printf("Init: successfully malloc queue of size %d\n", arr_size);
	return 0;
}


int addFirst(struct queue *my_arr, struct element *new_e){

	if (my_arr->max == my_arr->count){
		printf("fail to add element, the array is full\n");
		return 1;
	}

	for (int i = my_arr->count-1; i >= 0 ; i--){
		my_arr->e[i+1] = my_arr->e[i];
	}
	my_arr->e[0] = *new_e;
	printf("Add: pid %d into the queue with time %d\n", new_e->pid, new_e->pid_time);
	my_arr->count++;

	return 0;
}

int addHere(struct queue *my_arr, struct element *new_e, int index)
{
	if (my_arr->max == my_arr->count)
	{
		printf("fail to add element, the array is full\n");
		return 1;
	}
	//if it's empty
	if (my_arr->count == 0)
	{
		my_arr->e[0] = *new_e;
		printf("Add: pid %d into the queue with time %d\n", new_e->pid, new_e->pid_time);
		my_arr->count++;
		return 0;
	}

	if (index >= my_arr->max)
	{
		printf("invalid index: index out of bound\n");
		return 1;
	}
	//sequentially move elements rightwards
	for (int i = my_arr->count-1; i >= index ; i--)
	{
		my_arr->e[i+1] = my_arr->e[i]; 
	}
	//add element at given index
	my_arr->e[index] = *new_e;
	printf("Add: pid %d into the queue with time %d\n", new_e->pid, new_e->pid_time);
	my_arr->count++;

	return 0;

}

int addLast(struct queue *my_arr, struct element *new_e)
{
	if (my_arr->max == my_arr->count)
	{
		printf("fail to add element, the array is full\n");
		return 1;
	}

	my_arr->e[my_arr->count] = *new_e;
	printf("Add: pid %d into the queue with time %d\n", new_e->pid, new_e->pid_time);
	my_arr->count++;

	return 0;
}

void freeAll(struct queue *my_arr){
	free(my_arr);
}

int getCount(struct queue *my_arr){
	return my_arr->count;
}

void printAll(struct queue *my_arr){

	printf("There are %d processes in total\n", my_arr->count);

	for (int i = 0; i < my_arr->max; i++)
	{
		printf("#[%d]: pid: %d running time: %d, created time: %ld sec %d usec, %d priority\n", 
			i, my_arr->e[i].pid, my_arr->e[i].pid_time, my_arr->e[i].created_time.tv_sec, 
			my_arr->e[i].created_time.tv_usec, my_arr->e[i].pid_priority);
	}
}

void removeLast(struct queue *my_arr){

	printf("Remove: pid: %d from the queue index %d\n", my_arr->e[my_arr->count-1].pid, my_arr->count-1);

	my_arr->e[my_arr->count-1].pid = 0;
	my_arr->e[my_arr->count-1].pid_time = 0;
	my_arr->e[my_arr->count-1].created_time.tv_sec = 0;
	my_arr->e[my_arr->count-1].created_time.tv_usec = 0;
	my_arr->e[my_arr->count-1].pid_priority = 0;
	my_arr->count--;	
}