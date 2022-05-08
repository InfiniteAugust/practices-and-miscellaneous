#include <semaphore.h>
#include <pthread.h>
#include <fcntl.h>
#include "coursework.h"

#define MAX_BUFFER_SIZE 10
#define MAX_NUMBER_OF_JOBS 50
#define SEM_SYNCH "synchPandC"
#define SEM_CON "makeConsumerSleep"

struct timeval start;
struct timeval end;
long int response[MAX_NUMBER_OF_JOBS], turnaround[MAX_NUMBER_OF_JOBS];
int index = 0; //index of response and turnaround arrays

struct queue myQueue;
struct queue *myBuffer = &myQueue;

sem_t *synch;
sem_t *sleepConsumer;

void *producer();
void *consumer();
void average();

int main(int argc, char const *argv[])
{
	sem_unlink(SEM_SYNCH);
	sem_unlink(SEM_CON);

	synch = sem_open(SEM_SYNCH, O_CREAT, NULL, 1);
	sleepConsumer = sem_open(SEM_CON, O_CREAT, NULL, 1);

	myBuffer = malloc(sizeof(struct queue));
	(*myBuffer).max = MAX_BUFFER_SIZE;
	(*myBuffer).count = 0;
	init(myBuffer, MAX_BUFFER_SIZE);

	pthread_t tid1, tid2;

	pthread_create(&tid1, NULL, producer, NULL);
	pthread_create(&tid2, NULL, consumer, NULL);

	pthread_join(tid1, NULL);
	pthread_join(tid2, NULL);

	sem_close(synch);
	sem_close(sleepConsumer);

	average();
	freeAll(myBuffer);
	
	return 0;
}

void *producer(){
	struct element temp;
	int numOfgeneratedProcess = 0;
	void *p1;

	while(1){
		if (getCount(myBuffer) < MAX_BUFFER_SIZE){
			//critical section
			sem_wait(synch);

			//generate a new element
			temp = generateProcess();
			numOfgeneratedProcess++;
			printf("Generate element %d/50, pid: %d\n", numOfgeneratedProcess, temp.pid);
			addFirst(myBuffer, &temp);
			printf("\n");
			if (getCount(myBuffer) == 1)
				sem_post(sleepConsumer);

			sem_post(synch);
		}
		if (numOfgeneratedProcess >= MAX_NUMBER_OF_JOBS)
			return p1;
	}	
}

void *consumer(){
	int tempCount;
	struct element tempEle;
	int numOfRemovedProcess = 0;
	void *p2;
	while(1){
		//removes one element at a time from the buffer and runs it.
		if (getCount(myBuffer) > 0){
			//critical section
			sem_wait(synch);
			
			//remove last
			tempEle = myBuffer->e[getCount(myBuffer)-1];
            removeLast(myBuffer);
            
            //run the job
			gettimeofday(&start, NULL);
			runNonPreemptiveJobv2(&tempEle);
			gettimeofday(&end, NULL);
			response[index] = getDifferenceInMilliSeconds(myBuffer->e[myBuffer->count-1].created_time, start);
			turnaround[index] = getDifferenceInMilliSeconds(myBuffer->e[myBuffer->count-1].created_time, end);
			printf("created_time: %lu, start time: %lu, end time: %lu, response time: %lu, turnaround time: %lu\n",
				myBuffer->e[myBuffer->count-1].created_time.tv_sec, start.tv_sec, end.tv_sec, response[index], turnaround[index]);
			index++;
			
			numOfRemovedProcess++;
			printf("\n");
			tempCount = getCount(myBuffer);

			sem_post(synch);
			if (tempCount == 0)
				sem_wait(sleepConsumer);	
		}
		if (numOfRemovedProcess >= MAX_NUMBER_OF_JOBS)
			return p2;
	}
}

void average(){
	long int resSum = 0;
	long int turSum = 0;

	for (int i = 0; i < MAX_NUMBER_OF_JOBS; i++)
	{
		resSum += response[i];
		turSum += turnaround[i];
	}
	printf("Average response time: %.2f milliseconds\nAverage turn around time: %.2f milliseconds\n", 
		(float)resSum/MAX_NUMBER_OF_JOBS, (float)turSum/MAX_NUMBER_OF_JOBS);
}
