package Lab7;

public class Main {
    private static ThreadSafeBoundedQueue<Integer> queue= new ThreadSafeBoundedQueue<Integer>(100);

    public static void main(String args[]){
        Main mb = new Main();
        mb.runTest();
    }

    public void runTest(){

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 200; i++){
                    queue.addItem(i);
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 33; i < 200; i++){
                    queue.addItem(i);
                }
            }
        });
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 66; i < 200; i++){
                    queue.addItem(i);
                }
            }
        });
        t1.start();
        t2.start();
        t3.start();
    }
}
