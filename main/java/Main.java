import java.util.Random;

import service.DummyThrottle;
import service.Throttle;
import data.Static;

public class Main {
    public static Boolean stop = false;

    public static void main(String[] args) throws InterruptedException {
        // create throttle
        Throttle throttle = new DummyThrottle();

        // simulate a 600 rps loading by creating task in 50ms in average per thread
        for (int i = 0; i < 30; i++) {
            Thread thread = new Thread(() -> {
                while (!Main.stop) {
                    // test throttle service
                    if (throttle.processRequest()) {
                        Static.incrementSuccess();
                    } else {
                        Static.incrementFail();
                    }

                    // random sleep to create request loading
                    Random random = new Random();
                    int randomStop = random.nextInt(100);
                    try {
                        Thread.sleep(randomStop);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }

        // experiment static recording
        Thread recordingThread = new Thread(() -> {
            System.out.println("Success# Fail# Total#");
            System.out.println("-------- ----- ------");


            while (!stop) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int success = Static.recordAndClearSuccess();
                int fail = Static.recordAndClearFail();
                System.out.println(String.format("% 8d % 5d % 6d", success, fail, success + fail));
            }
        });
        recordingThread.start();

        Thread.sleep(1000 * 10);
        Main.stop = true;
        recordingThread.join();
    }
}
