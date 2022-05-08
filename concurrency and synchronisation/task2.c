#include "coursework.h"
#define MAX_PROCESSES 5

void generateSJF(struct queue *my_arr);
void runSJF(struct queue *my_arr);
void average();

long int response[MAX_PROCESSES], turnaround[MAX_PROCESSES];

int main(int argc, char const *argv[])
{
	struct queue *ptr;
	struct queue q;
	ptr = &q;
	ptr = malloc(sizeof(struct queue));

	(*ptr).max = MAX_PROCESSES;
	(*ptr).count = 0;

	init(ptr, MAX_PROCESSES);
	printAll(ptr);

	generateSJF(ptr);

	runSJF(ptr);

	average();

	freeAll(ptr);

	return 0;
}

void generateSJF(struct queue *my_arr){

	printf("Generating processes for SJF...\n");

	struct element temp;
	int j;

	temp = generateProcess();
	printf("SJF: add first\n");
	addFirst(my_arr, &temp);
	printAll(my_arr);

	for (int i = 0; i < MAX_PROCESSES-1; i++){
		j = 0;
		temp = generateProcess();
		//for every generated process, compare it with the others, insert at the correct place 
		while(j <= MAX_PROCESSES-1)
		{
			if (temp.pid_time < my_arr->e[j].pid_time){
				j++;
			}else if(temp.pid_time == my_arr->e[j].pid_time){
					while (temp.created_time.tv_sec < my_arr->e[j].created_time.tv_sec)
						j++;
					break;	
			}else
				break;		
		}
		printf("SJF: add new element\n");
		addHere(my_arr, &temp, j);
		printAll(my_arr);
	}
}


void runSJF(struct queue *my_arr){
	printf("Running the process using SJF...\n");

	struct timeval start;
	struct timeval end;

	for (int i = MAX_PROCESSES-1; i >= 0; i--)
	{
		gettimeofday(&start, NULL);
		runNonPreemptiveJob(my_arr, i);
		gettimeofday(&end, NULL);
		response[i] = getDifferenceInMilliSeconds(my_arr->e[i].created_time, start);
		turnaround[i] = getDifferenceInMilliSeconds(my_arr->e[i].created_time, end);
		printf("C: %lu, S: %lu, E: %lu, R: %lu, T: %lu\n", 
			my_arr->e[i].created_time.tv_sec, start.tv_sec, end.tv_sec, response[i], turnaround[i]);
	}
}


void average(){
	long int resSum = 0;
	long int turSum = 0;

	for (int i = 0; i < MAX_PROCESSES; i++)
	{
		resSum += response[i];
		turSum += turnaround[i];
	}
	printf("Average response time: %.2f milliseconds\nAverage turn around time: %.2f milliseconds\n", 
		(float)resSum/MAX_PROCESSES, (float)turSum/MAX_PROCESSES);
}
