package com.gougou.ib.company.thread;

public class ThreadSleep {

    public static void sleep(Integer millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
