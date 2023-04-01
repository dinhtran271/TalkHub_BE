package com.talkhub.util.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ThreadpoolWorker {
    public ExecutorService executor;
    public ScheduledThreadPoolExecutor scheduler;
    private static ThreadpoolWorker ins = null;

    private ThreadpoolWorker(){
        int procs = Runtime.getRuntime().availableProcessors();
        executor = Executors.newFixedThreadPool(2*procs);
        scheduler = new ScheduledThreadPoolExecutor(2*procs);
    }

    public static ThreadpoolWorker instance(){
        if(ins == null){
            ins = new ThreadpoolWorker();
        }
        return ins;
    }
}
