#include "coursework.h"
#define MAX_PROCESSES 5

void generatePQ(struct queue *my_arr[]);
void runPQ(struct queue *my_arr[]);
void average();

long int response[MAX_PROCESSES], turnaround[MAX_PROCESSES];

int main(int argc, char const *argv[])
{
	struct queue *ptr[PRIORITY];
	struct queue q[PRIORITY];

	for (int i = 0; i < PRIORITY; i++)
	{
		ptr[i] = &q[i];
		ptr[i] = malloc(sizeof(struct queue));
		(*ptr[i]).max = MAX_PROCESSES;
		(*ptr[i]).count = 0;

		init(ptr[i], MAX_PROCESSES);
		printf("Q #%d\n", i);
		printAll(ptr[i]);
	}

	generatePQ(ptr);

	runPQ(ptr);

	average();

	freeAll(ptr[0]);
	freeAll(ptr[1]);
	freeAll(ptr[2]);

	return 0;
}

void generatePQ(struct queue *my_arr[]){
	struct element temp;

	printf("\nGenerating processes for PQ...\n");

	for (int i = 0; i < MAX_PROCESSES; ++i)
	{
		temp = generateProcess();
		printf("New process has priority %d\nPQ: %d, ", temp.pid_priority, temp.pid_priority);
		addFirst(my_arr[temp.pid_priority], &temp);

		printf("Q #0\n");
		printAll(my_arr[0]);
		printf("Q #1\n");
		printAll(my_arr[1]);
		printf("Q #2\n");
		printAll(my_arr[2]);
	}
}

void runPQ(struct queue *my_arr[]){
	printf("Running the process using PQ...\n");

	struct timeval start;
	struct timeval end;
	//a and b are indice of response[] and turnaround[]
	int a = 0;
	int b = 0;

	//for each queue, run the jobs inside it
	for (int i = 0; i < PRIORITY; i++)
	{
		int countOfProcesses = my_arr[i]->count;
		//for each job inside the queue, run for the first round
		for (int j = 0; j < countOfProcesses; j++)
		{
			printf("\n\n");
			gettimeofday(&start, NULL);
			response[a++] = getDifferenceInMilliSeconds(my_arr[i]->e[j].created_time, start);
			printf("Q: %d, P: %d, C: %lu, S: %lu, R: %lu.\n", 
				i, my_arr[i]->e[j].pid, my_arr[i]->e[j].created_time.tv_sec, start.tv_sec, response[--a]);
			runPreemptiveJob(my_arr[i], j);

				//if the job has pid_time of only 1 or 2, it will finish in the first round
				if (my_arr[i]->e[j].pid_time == 0){
					gettimeofday(&end, NULL);
					turnaround[b++] = getDifferenceInMilliSeconds(my_arr[i]->e[j].created_time, end);
					printf("\nQ: %d, P: %d, C: %lu, E: %lu, T: %lu.\n\n", 
						i, my_arr[i]->e[j].pid, my_arr[i]->e[j].created_time.tv_sec, end.tv_sec, turnaround[b-1]);
					my_arr[i]->e[j] = my_arr[i]->e[j+1]; //move the element behind the finished job forwards
					my_arr[i]->count--;  //decrease count for this job is already done, remove it from the queue
				}
		}
		//for each job of the current queue, iteratively run them in turn 
		int j = my_arr[i]->count;
		while(j > 0){
			for (int k = 0; k < j; k++)
			{
				runPreemptiveJob(my_arr[i], k);
				if (my_arr[i]->e[k].pid_time <= 0){
					gettimeofday(&end, NULL);
					turnaround[b++] = getDifferenceInMilliSeconds(my_arr[i]->e[k].created_time, end);
					printf("\nQ: %d, P: %d, C: %lu, E: %lu, T: %lu.\n\n", 
						i, my_arr[i]->e[k].pid, my_arr[i]->e[k].created_time.tv_sec, end.tv_sec, turnaround[b-1]);
					my_arr[i]->e[k] = my_arr[i]->e[k+1];  //move the element behind the finished job forwards
					j--;  //decrease counter j for this job is already done, remove it from the queue
				}
			}
		}
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