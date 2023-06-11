package ir.sahab;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
public class DirectoryWatcher {

    private static String configPath = "/home/mahdi/IdeaProjects/file_ingester/config.txt";

    public static void main(String[] args) throws IOException, InterruptedException {
        WatchService watchService;
        String watchingFilePath;
        while (true) {
            try {
                File configFile = new File(configPath);
                Scanner configSc = new Scanner(configFile);
                watchingFilePath = configSc.nextLine();
                watchService = FileSystems.getDefault().newWatchService();
                Path path = Path.of(watchingFilePath);
                path.register(
                        watchService, StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE,
                        StandardWatchEventKinds.ENTRY_MODIFY);
                break;
            } catch (Exception e) {
                log.error("PLEASE CHECK YOUR CONFIG DIRECTORY!!");
                e.printStackTrace();
            }
            TimeUnit.SECONDS.sleep(1);
        }

        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                log.info("Event kind:" + event.kind()
                        + ". File affected: " + event.context() + ".");
                String logFile = event.context().toString();
                if (event.kind().equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
                    List<LogModel> logs = new ArrayList<>();
                    try {
                        File file = new File(watchingFilePath + logFile);
                        if (!file.exists())
                            continue;
                        logFile = logFile.substring(0, logFile.indexOf(".log"));
                        Scanner sc = new Scanner(file);     //file to be scanned
                        while (sc.hasNextLine()) {//returns true if and only if scanner has another token
                            String logStr = sc.nextLine();
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