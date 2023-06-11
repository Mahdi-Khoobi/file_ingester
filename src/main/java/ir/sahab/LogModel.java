package ir.sahab;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LogModel {
    private String componentName;
    private String fileDate;
    private String fileTime;
    private String logDate;
    private String logTime;
    private String threadName;
    private String type;
    private String className;
    private String massage;


    public static LogModel synthesisLog(String fileName, String logStr) {
        String componentName = fileName.substring(0, fileName.indexOf("-"));
        String fileDate = fileName.substring(fileName.indexOf("-") + 1, fileName.lastIndexOf("-"));
        String fileTime = fileName.substring(fileName.lastIndexOf("-") + 1);

        String[] details = logStr.split(" ", 8);

        String logDate = details[0];
        String logTime = details[1];
        String threadName = details[3];
        String type = details[4];
        String className = details[5];
        String massage = details[7];

        return new LogModel(componentName, fileDate, fileTime,
                logDate, logTime, threadName, type, className, massage);
    }

}
