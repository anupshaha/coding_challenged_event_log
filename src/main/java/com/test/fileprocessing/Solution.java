package com.test.fileprocessing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.dboperations.HSQLDBInsertData;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Solution {

    private HSQLDBInsertData insert;
    static Logger logger = Logger.getLogger(Solution.class.getName());

    public Solution() {
    }

    public static String getLogFilePath() {
        String PATH = null;

        try {
            PATH = System.getProperty("user.dir") + PropertyFileUtil.getProperty("PATH");
        } catch (Exception var2) {
            var2.printStackTrace();
        }

        return PATH;
    }

    public static String getTheJsonFilePath() {
        boolean isInputValid = false;
        String userInput = "";
        System.out.print("Do you want to specify the .json file? Select -> (Y/N) ");
        Scanner scanner = new Scanner(System.in);
        String userAnswer = scanner.nextLine();

        while(!isInputValid) {
            String var4 = userAnswer.toLowerCase();
            byte var5 = -1;
            switch(var4.hashCode()) {
            case 110:
                if (var4.equals("n")) {
                    var5 = 1;
                }
                break;
            case 121:
                if (var4.equals("y")) {
                    var5 = 0;
                }
            }

            switch(var5) {
            case 0:
                System.out.print("Please provide the file path: ");
                userInput = scanner.nextLine();
                isInputValid = true;
                break;
            case 1:
                System.out.print("Default file path has been applied\n");
                userInput = getLogFilePath();
                isInputValid = true;
                break;
            default:
                System.out.println("Entered incorrect value.\nPlease try again:");
                userAnswer = scanner.nextLine();
            }
        }

        return userInput;
    }

    public List<ServerLog> parseInputFile(String PATH) {
        List<ServerLog> logs = null;

        try {
            Path path = Paths.get(PATH);
            List<String> list = Files.readAllLines(path);
            logs = list.stream().map((line) -> {
                try {
                    return (ServerLog)(new ObjectMapper()).readValue(line, ServerLog.class);
                } catch (JsonProcessingException var2) {
                    var2.printStackTrace();
                    return null;
                }
            }).collect(Collectors.toList());
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        return logs;
    }

    public long countLineFromFile(String fileName) {
        Path path = Paths.get(fileName);
        long lines = 0L;

        try {
            lines = Files.lines(path).count();
        } catch (IOException var6) {
            var6.printStackTrace();
        }

        return lines;
    }

    public void insertDataInDB(Map<String, List<ServerLog>> logsGroupedByID) {
        this.insert = new HSQLDBInsertData();
        (logsGroupedByID.values().parallelStream().parallel()).forEach((log) -> {
            Map<String, String> dbData = new HashMap<>();
            if (log.size() == 2) {
                long duration = (log.get(1)).getTimestamp() - (log.get(0)).getTimestamp();
                duration = duration < 0L ? -duration : duration;
                dbData.put("id", (log.get(0)).getId());
                dbData.put("type", (log.get(0)).getType());
                dbData.put("host", (log.get(0)).getHost());
                dbData.put("duration", String.valueOf(duration));
                dbData.put("alert", String.valueOf(duration > 4L));

                try {
                    this.insert.insertRecord(dbData);
                } catch (ClassNotFoundException var6) {
                    var6.printStackTrace();
                }
            } else {
                logger.warn("Record is missing or Duplicate records are present for event_id: " + (log.get(0)).getId());
            }
        });
    }

    public void dataProcessing(List<ServerLog> logs) {
        Instant instant = Instant.now();
        long timeStampSeconds0 = instant.toEpochMilli();
        Map<String, List<ServerLog>> logsGroupedByID = (logs.stream().parallel()).collect(Collectors.groupingBy(ServerLog::getId));
        this.insertDataInDB(logsGroupedByID);
        Instant instant1 = Instant.now();
        long timeStampSeconds1 = instant1.toEpochMilli();
        logger.info("Execution completed in " + (timeStampSeconds1 - timeStampSeconds0) + " milliseconds");
    }

    public static void main(String[] args){
        Solution s = new Solution();
        String PATH = getTheJsonFilePath();
        s.dataProcessing(s.parseInputFile(PATH));
    }

    static {
        String log4jConfPath = System.getProperty("user.dir") + PropertyFileUtil.getProperty("log4JProperties");
        PropertyConfigurator.configure(log4jConfPath);
    }
}
