package club.qqtim.arg;


import picocli.CommandLine;

/**
 * @author lijie78
 */
@CommandLine.Command(name = "zit", subcommands = {
        Init.class,
        HashObject.class,
        CatFile.class,
        WriteTree.class
})
public class Zit {
}