import random as rand
import time as tim
from datetime import datetime
components_name = ['comp_one']
INFO = ['FATAL','INFO']
ThreadName = ['thread_01', 'thread_02', 'thread_03']
msg = ['what a beautiful morning!']

for i in range(0, 1):
    f = open("/home/mahdi/IdeaProjects/file_ingester/Logs/" + 
             str(rand.choices(components_name, k=1)[0]) + "-" +
             str(datetime.today().strftime('%Y_%m_%d-%H_%M_%S')) + str(i) +
             ".log", "w")
    log_count = rand.randint(1,100)
    for j in range(0, 1):
        if j != 0:
            f.write("\n")
        f.write(str(datetime.today().strftime('%Y-%m-%d %H:%M:%S')) + " " + str(j) + " " +
                str(rand.choices(ThreadName, k=1)[0]) + " " +
                str(rand.choices(INFO, k=1)[0]) + " pakage.name.ClassName - " +
                str(msg[0]) + str(rand.randint(0,10000)))
        #tim.sleep(rand.randint(1,4))
    f.close()
    tim.sleep(1)
