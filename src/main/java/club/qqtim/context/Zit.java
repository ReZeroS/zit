package club.qqtim.context;


import club.qqtim.command.Add;
import club.qqtim.command.Branch;
import club.qqtim.command.CatFile;
import club.qqtim.command.Checkout;
import club.qqtim.command.Commit;
import club.qqtim.command.Diff;
import club.qqtim.command.Fetch;
import club.qqtim.command.HashObject;
import club.qqtim.command.Init;
import club.qqtim.command.Lg;
import club.qqtim.command.Log;
import club.qqtim.command.Merge;
import club.qqtim.command.MergeBase;
import club.qqtim.command.Push;
import club.qqtim.command.ReadTree;
import club.qqtim.command.Reset;
import club.qqtim.command.Show;
import club.qqtim.command.Status;
import club.qqtim.command.Tag;
import club.qqtim.command.WriteTree;
import picocli.CommandLine;

/**
 * @author rezeros.github.io
 */
@CommandLine.Command(name = "zit", subcommands = {
        Init.class,
        HashObject.class,
        CatFile.class,
        WriteTree.class,
        ReadTree.class,
        Commit.class,
        Log.class,
        Checkout.class,
        Tag.class,
        Lg.class,
        Branch.class,
        Status.class,
        Reset.class,
        Show.class,
        Diff.class,
        Merge.class,
        MergeBase.class,
        Fetch.class,
        Push.class,
        Add.class
})
public class Zit {
}
