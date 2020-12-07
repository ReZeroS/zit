package club.qqtim.command;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

import java.util.concurrent.Callable;

/**
 * @author lijie78
 */
@Data
@Slf4j
@CommandLine.Command(name = "cat-file")
public class CatFile implements Callable<String> {

    @CommandLine.Parameters(index = "0")
    private String id;

    @CommandLine.Parameters(index = "1", defaultValue = "blob")
    private String type;

    @Override
    public String call() {
        String id = club.qqtim.data.Data.getId(this.id);
        final String fileContent = new club.qqtim.data.Data().getObjectAsString(id, type);
        log.info(fileContent);
        return fileContent;
    }
}