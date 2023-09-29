public class Main {
    public static void main(String[] args) {
        Runnable logger1 = new Logger("The Quick Brown Fox Jumped Over The Lazy Dog");
        Runnable logger2 = new Logger("Far Over The Misty Mountains Cold");

        new Thread(logger1).start();
        new Thread(logger2).start();
    }

     static class Logger implements Runnable {
        private final String sentence;

        Logger(String sentence) {
            this.sentence = sentence;
        }

        @Override
        public void run() {
            for (String word : sentence.split(" ")) {
                System.out.println(word);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            };
        }
    }
}