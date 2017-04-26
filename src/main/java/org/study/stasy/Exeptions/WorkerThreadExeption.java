package org.study.stasy.Exeptions;


   public class WorkerThreadExeption extends Exception {
        private Runnable task;

        public WorkerThreadExeption() {
            super();
        }
        public WorkerThreadExeption(String message) {
            super(message);
        }
        public WorkerThreadExeption(String message, Throwable cause) {
            super(message, cause);
        }
        public WorkerThreadExeption(Throwable cause) {
            super(cause);
        }
        public WorkerThreadExeption(String message, Runnable task){
            super(message);
            this.task = task;
        }


        public Runnable getTask() {return task;}
    }
