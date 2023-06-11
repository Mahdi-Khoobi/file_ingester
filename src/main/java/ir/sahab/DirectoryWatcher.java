package ir.sahab;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DirectoryWatcher {

    public static void main(String[] args) throws IOException, InterruptedException {
        WatchService watchService
                = FileSystems.getDefault().newWatchService();

        Path path = Path.of("/home/mahdi/IdeaProjects/file_ingester/Logs");

        path.register(
                watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                System.out.println("Event kind:" + event.kind()
                        + ". File affected: " + event.context() + ".");
                String logFile = event.context().toString();
                if (event.kind().equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
                    List<LogModel> logs = new ArrayList<>();
                    try {
                        File file = new File("/home/mahdi/IdeaProjects/file_ingester/Logs/" + logFile);
                        if (!file.exists())
                            continue;
                        logFile = logFile.substring(0, logFile.indexOf(".log"));
                        Scanner sc = new Scanner(file);     //file to be scanned
                        while (sc.hasNextLine()) {//returns true if and only if scanner has another token
                            String logStr = sc.nextLine();
                            System.out.println(logStr);
                            LogModel temp = LogModel.synthesisLog(logFile, logStr);
                            logs.add(temp);
                        }
                        file.delete();
                        for (LogModel i : logs)
                            KafkaProducer.produceLog(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
            key.reset();
        }
    }
}