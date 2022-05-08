#include <semaphore.h>
#include <pthread.h>
#include <fcntl.h>
#include "coursework.h"

#define MAX_BUFFER_SIZE 10
#define MAX_NUMBER_OF_JOBS 50
#define SEM_OCP "occupiedbuffer"
#define SEM_EPT "emptybuffer"
#define SEM_SYNCH "synchPandC"
#define SEM_CON "makeConsumerSleep"
#define SEM_PRO "makeProducerSleep"

struct timeval start;
struct timeval end;
long int response[MAX_NUMBER_OF_JOBS], turnaround[MAX_NUMBER_OF_JOBS];
int numOfgeneratedJobs = 0;
int numOfRemovedJobs = 0;

struct queue *myBuffer[PRIORITY];
struct queue q[PRIORITY];

sem_t *synch;
sem_t *occupied;
sem_t *empty;
sem_t *sleepConsumer;
sem_t *sleepProducer;

void *producer();
void *consumer();
void average();

int main(int argc, char const *argv[])
{
	sem_unlink(SEM_SYNCH);
	sem_unlink(SEM_OCP);
	sem_unlink(SEM_EPT);
	sem_unlink(SEM_CON);
	sem_unlink(SEM_PRO);

	synch         = sem_open(SEM_SYNCH, O_CREAT, NULL, 1);
	occupied      = sem_open(SEM_OCP, O_CREAT, NULL, 0);
	empty         = sem_open(SEM_EPT, O_CREAT, NULL, MAX_BUFFER_SIZE);
	sleepConsumer = sem_open(SEM_CON, O_CREAT, NULL, 1);
	sleepProducer = sem_open(SEM_PRO, O_CREAT, NULL, 1);

	for (int i = 0; i < PRIORITY; i++)
	{
		myBuffer[i] = &q[i];
		myBuffer[i] = malloc(sizeof(struct queue));
		(*myBuffer[i]).max = MAX_BUFFER_SIZE;
		(*myBuffer[i]).count = 0;
		init(myBuffer[i], MAX_BUFFER_SIZE);
	}
	pthread_t tidP, tidC1, tidC2, tidC3;
	
	pthread_create(&tidP, NULL, producer, NULL);
	pthread_create(&tidC1, NULL, consumer, NULL);
	pthread_create(&tidC2, NULL, consumer, NULL);
	pthread_create(&tidC3, NULL, consumer, NULL);
	
	pthread_join(tidP, NULL);
	pthread_join(tidC1, NULL);
	pthread_join(tidC2, NULL);
	pthread_join(tidC3, NULL);
	
	sem_close(synch);
	sem_close(occupied);
	sem_close(empty);
	sem_close(sleepConsumer);
	sem_close(sleepProducer);

	freeAll(myBuffer[0]);
	freeAll(myBuffer[1]);
	freeAll(myBuffer[2]);

	return 0;
}

void *producer(){
	struct element temp;
	void *p1;
	while(1){
		sem_wait(empty);
		sem_wait(synch);
	
		if (getCount(myBuffer[0]) < MAX_BUFFER_SIZE && getCount(myBuffer[1]) < MAX_BUFFER_SIZE && getCount(myBuffer[2]) < MAX_BUFFER_SIZE){
	
			temp = generateProcess();
			numOfgeneratedJobs++;
			printf("\nGenerate element %d/50, pid: %d, priority: %d\n", numOfgeneratedJobs, temp.pid, temp.pid_priority);
			
			//add generated job into buffer
			sem_wait(sleepProducer);
			switch(temp.pid_priority){
				case 0:
					addFirst(myBuffer[0], &temp);
					break;
				case 1:
					addFirst(myBuffer[1], &temp);
					break;
				case 2:
					addFirst(myBuffer[2], &temp);
					break;				
			}
			sem_post(sleepProducer);
			
			if (getCount(myBuffer[0]) == 1 && getCount(myBuffer[1]) == 1 && getCount(myBuffer[2]) == 1)
				sem_post(sleepConsumer);
		}
		sem_post(synch);
		sem_post(occupied);
		if (numOfgeneratedJobs == MAX_NUMBER_OF_JOBS){
		    printf("\nProducer finished generating %d jobs\n", numOfgeneratedJobs);
			return p1;
		}
	}
}

void *consumer(){
	int priorityOfElem;
	struct element tempEle;
	void *p2;
	while(1){
	    
		sem_wait(occupied);
		sem_wait(synch);
		
		if (getCount(myBuffer[0]) > 0){
			tempEle = myBuffer[0]->e[getCount(myBuffer[0])-1];
			priorityOfElem = 0;
			removeLast(myBuffer[0]);
			
		}else if(getCount(myBuffer[1]) > 0){
		    tempEle = myBuffer[1]->e[getCount(myBuffer[1])-1];
		    priorityOfElem = 1;
			removeLast(myBuffer[1]);
			
		}else if(getCount(myBuffer[2]) > 0){
		    tempEle = myBuffer[2]->e[getCount(myBuffer[2])-1];
		    priorityOfElem = 2;
			removeLast(myBuffer[2]);
	
	    }else 
	        sem_wait(sleepConsumer);
	        
	    sem_post(synch);
		sem_post(empty);
		
		//buffer is possibly modified
		sem_wait(sleepProducer);
		runPreemptiveJobv2(&tempEle);
		if (tempEle.pid_time > 0){
			addFirst(myBuffer[priorityOfElem], &tempEle);
		}else
		    numOfRemovedJobs++;
		sem_post(sleepProducer);

		if (numOfRemovedJobs == MAX_NUMBER_OF_JOBS){
			printf("Consumer finished consuming %d jobs\n", numOfRemovedJobs); 
		    return p2;
		}
	}		
}
