/*===========================================================
    Java version of the test and set method for mutual exclusion

    Hans Vandierendonck, 22 October 2013
===========================================================*/

import java.util.concurrent.atomic.AtomicInteger;
import java.util.Random;

class Time {
    public static void delay(int msec) {
        // Pause thread for specified number of milliseconds
        try {
            Thread.sleep(msec);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class TestAndSetMutex {
    private AtomicInteger current;
    private AtomicInteger dispenser;

    public TestAndSetMutex() {
        current = new AtomicInteger(0);
        dispenser = new AtomicInteger(0);
    }

    public void pre_protocol() {
        int ticket = dispenser.getAndIncrement();
        while (current.get() != ticket) ;
    }

    public void post_protocol() {
        current.getAndIncrement();
    }
}

class TestAndSet {
    public static void main(String[] args) {
        TestAndSetMutex tas_mutex = new TestAndSetMutex();
        Process thread1 = new Process(tas_mutex, 1);
        Process thread2 = new Process(tas_mutex, 2);

        thread1.start();
        thread2.start();
    }
}

class Process extends Thread {
    private Random rnd = new Random();
    private TestAndSetMutex mux;
    private int id;

    Process(TestAndSetMutex mux_, int id_) {
        mux = mux_;
        id = id_;
    }

    public void run() {
        while (true) {
            nonCriticalSection();
            preProtocol();
            criticalSection();
            postProtocol();
        }
    }

    public void nonCriticalSection() {
       System.out.println(id + " nc: Entering nonCritical section");
        Time.delay(rnd.nextInt(20));

       System.out.println(id + " nc: Leaving nonCritical section");
    }

    public void preProtocol() {
       System.out.println(id + " prep: Entering preProtocol section");
        mux.pre_protocol();
	    Time.delay(rnd.nextInt(20));
       System.out.println(id + " prep: Leaving preProtocol section");
    }

    public void criticalSection() {
       System.out.println(id + " cs: Entering critical section");
       System.out.println(id + " cs: In critical section");
        Time.delay(rnd.nextInt(20));
       System.out.println(id + " cs: Leaving critical section");
    }

    public void postProtocol() {
        Time.delay(rnd.nextInt(20));
       System.out.println(id + " postp: Entering postProtocol section");
        mux.post_protocol();
       System.out.println(id + " postp: Leaving postProtocol section");
    }
}
