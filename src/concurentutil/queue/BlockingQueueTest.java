package concurentutil.queue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 文件搜索
 */
public class BlockingQueueTest {
    public static void main(String[] args) throws InterruptedException {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter base directory (e.g. /Users/will/Desktop/Intellij Projects): ");
        String directory = in.nextLine();
        System.out.print("Enter keyword (e.g. volatile): ");
        String keyword = in.nextLine();

        final int FILE_QUEUE_SIZE = 10;// 阻塞队列大小
        final int SEARCH_THREADS = 100;// 关键字搜索线程个数

        // 基于ArrayBlockingQueue的阻塞队列
        BlockingQueue queue = new ArrayBlockingQueue<>(FILE_QUEUE_SIZE);

        //只启动一个线程来搜索目录
        FileEnumerationTask enumerator = new FileEnumerationTask(queue, new File(directory));
        new Thread(enumerator).start();

        //启动100个线程用来在文件中搜索指定的关键字
        for (int i = 1; i <= SEARCH_THREADS; i++)
            new Thread(new SearchTask(queue, keyword)).start();

        Thread.sleep(2000);
        System.out.println("从队列取走一个元素，队列剩余" + queue.size() + "个元素");
    }
}

class FileEnumerationTask implements Runnable {
    //把元文件对象，放在阻塞队列最后，用来标示文件已被遍历完
    static File DUMMY = new File("");

    private BlockingQueue queue;
    private File startingDirectory;

    FileEnumerationTask(BlockingQueue queue, File startingDirectory) {
        this.queue = queue;
        this.startingDirectory = startingDirectory;
    }

    public void run() {
        try {
            enumerate(startingDirectory);
            queue.put(DUMMY);//执行到这里说明指定的目录下文件已被遍历完
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 将指定目录下的所有文件以File对象的形式放入阻塞队列中
    private void enumerate(File directory) throws InterruptedException {
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory())
                enumerate(file);
            else
                //将元素放入队尾，如果队列满，则阻塞
                queue.put(file);
        }
    }
}

class SearchTask implements Runnable {
    private BlockingQueue queue;
    private String keyword;

    SearchTask(BlockingQueue queue, String keyword) {
        this.queue = queue;
        this.keyword = keyword;
    }

    public void run() {
        try {
            boolean done = false;
            while (!done) {
                //取出队首元素，如果队列为空，则阻塞
                File file = (File) queue.take();
                if (file == FileEnumerationTask.DUMMY) {
                    //取出来后重新放入，好让其他线程读到它时也很快的结束
                    /*
                    我个人觉得，如果不放进去的话，会造成这样的情况：
                    起了多个线程，但是由于queue为空了，所以肯定会有线程一直阻塞着。
                    这些阻塞表现在进入while之后，一直阻塞在queue.take()，无法跳出while。

                    所以 LZ就做了一步queue.put(file)，这样可以让这些阻塞的线程跳出while。

                    但是这样的做法最后的结果，就是queue永远有一个DUMMY在。。
                    */
                    queue.put(file);
                    done = true;
                } else
                    search(file);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void search(File file) throws IOException {
        Scanner in = new Scanner(new FileInputStream(file));
        int lineNumber = 0;
        while (in.hasNextLine()) {
            lineNumber++;
            String line = in.nextLine();
            if (line.contains(keyword))
                System.out.printf("%s, %s:%d:%s%n", Thread.currentThread().getId(), file.getPath(), lineNumber, line);
        }
        in.close();
    }
}
