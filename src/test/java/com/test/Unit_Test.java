package com.test;

import com.test.fileprocessing.ServerLog;
import com.test.fileprocessing.Solution;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Unit_Test {
    private Solution solution = new Solution();

    public Unit_Test() {
    }

    @Test
    public void parseInputFileTest() {
        List<ServerLog> list = this.solution.parseInputFile(Solution.getLogFilePath());
        long recordCount = this.solution.countLineFromFile(Solution.getLogFilePath());
        long parsedDataCount = (long)list.size();
        Assertions.assertEquals(recordCount, parsedDataCount);
    }
}
