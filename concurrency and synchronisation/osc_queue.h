#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <sys/time.h>
#include <unistd.h>

struct element			// represents a job or a process
{
	int pid;		// unique identifier
	int pid_time;		// to store the length of time the job will be running
	int pid_priority;	// to store priority level of the job

	struct timeval created_time;	// time when the element was created
};

struct queue			// represents a queue of elements
{
	struct element *e;	// to store elements
	int max;		// the maximum length of the queue
	int count;		// the current number of elements in the queue 
};

int addFirst(struct queue *my_arr, struct element *new_e);

int addHere(struct queue *my_arr, struct element *new_e, int index);

int addLast(struct queue *my_arr, struct element *new_e);


void freeAll(struct queue *my_arr);
// free dynamic memory allocation
int getCount(struct queue *my_arr);

int init(struct queue *my_arr, int arr_size);
// initialise all of the variables
void printAll(struct queue *my_arr);


void removeLast(struct queue *my_arr);
