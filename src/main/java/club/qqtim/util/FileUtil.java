package club.qqtim.util;

import club.qqtim.common.ConstantVal;
import club.qqtim.context.ZitContext;
import club.qqtim.util.handler.PosixHandler;
import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;
import com.google.common.io.CharSource;
import com.google.common.io.Files;
import com.google.common.primitives.Chars;
import jnr.posix.POSIX;
import jnr.posix.POSIXFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author rezeros.github.io
 */
@Slf4j
public final class FileUtil {

    private FileUtil() {
    }


    public static void setRootPathContext(String target) {
        System.setProperty("user.dir", target);
        POSIX posix = POSIXFactory.getPOSIX(new PosixHandler(), true);
        posix.chdir(target);
    }



    public static void mkdir(String dirName) {
        boolean mkdir = new File(dirName).mkdir();
        if (mkdir) {
            log.info("Init {} directory in .zit repository", Paths.get(Objects.requireNonNull(FileUtil.getCurrentDir()), dirName));
        } else {
            log.info("Create directory failed, please check your access right.");
        }
    }

    public static String getCurrentDir() {
        File file = new File(".");
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            log.error("can not get the current dir, please check your access right");
        }
        return null;
    }

    public static void deleteDir(String path){
        deleteDir(new File(path), null);
    }

    public static void deleteDir(String path, Predicate<String> ignorePredicate){
        deleteDir(new File(path), ignorePredicate);
    }

    public static void deleteDir(File file, Predicate<String> ignorePredicate){
        if (ignorePredicate != null) {
            final boolean ignorePath = ignorePredicate.test(file.getPath());
            if (ignorePath) {
                return;
            }
        }
        if (file.isDirectory()) {
            Arrays.stream(Objects.requireNonNull(file.listFiles()))
                    .forEach(currentFile -> deleteDir(currentFile, ignorePredicate));
        }
        final boolean delete = file.delete();
        if (!delete) {
            log.error("delete file failed, please check your access right.");
        }
    }

    public static void createFile(String fileContents, String fileName) {
        createFile(fileContents.getBytes(StandardCharsets.UTF_8), fileName);
    }

    public static void createFile(byte[] fileContents, String fileName) {
        File hashObject = new File(fileName);
        try {
            // first create the parent directory
            Files.createParentDirs(hashObject);
            // then create the file
            Files.write(fileContents, hashObject);
        } catch (IOException e) {
            log.error(e.toString());
        }
    }

    public static void createParentDirs(String path) {
        try {
            Files.createParentDirs(new File(path));
        } catch (IOException e) {
            log.error(e.toString());
        }
    }


    public static String getFileAsString(String path, String type) {
        try {
            return getFileByteSource(path, type).asCharSource(Charsets.UTF_8).read();
        } catch (IOException e) {
            log.error(e.toString());
        }
        return null;
    }

    public static ByteSource getFileByteSource(String path, String type) throws IOException {
        char nullChar = 0;
        ByteSource byteSource = Files.asByteSource(new File(path));
        if (ConstantVal.NONE.equals(type)) {
            return byteSource;
        }
        // todo: refactor the below code to extract a method like obj.partition in python
        byte[] fileWithHeader = byteSource.read();
        byte[] header = new byte[type.getBytes(Charsets.UTF_8).length];
        byte[] nullBytes = new byte[Chars.toByteArray(nullChar).length];
        byte[] fileContent = new byte[fileWithHeader.length - header.length - nullBytes.length];
        ByteBuffer fileWithHeaderBuffer = ByteBuffer.wrap(fileWithHeader);
        fileWithHeaderBuffer.get(header, 0, header.length);
        log.debug("current object type is {}:", new String(header, Charsets.UTF_8));
        fileWithHeaderBuffer.get(nullBytes, 0, nullBytes.length);
        fileWithHeaderBuffer.get(fileContent, 0, fileContent.length);
        return ByteSource.wrap(fileContent);
    }



    public static void emptyCurrentDir() {
        File file = new File(ConstantVal.BASE_PATH);
        final String[] paths = file.list();
        if (paths != null) {
            Arrays.stream(paths).forEach(path -> {
                if (ZitContext.isNotIgnored(path)) {
                    FileUtil.deleteDir(path);
                }
            });
        }
    }


    public static boolean isFile(String path) {
        return new File(path).isFile();
    }



    public static void copy(String from, String to) {
        copy(Paths.get(from), Paths.get(to));
    }

    public static void copy(Path from, Path to) {
        try {
            final File fromFile = from.toFile();
            final File toFile = to.toFile();
            Files.createParentDirs(toFile);
            Files.copy(fromFile, toFile);
        } catch (IOException e) {
            log.error(e.toString());
        }
    }


    public static String readFileFirstLine(File file) {
        String value;
        try {
            final CharSource charSource = Files.asCharSource(file, Charsets.UTF_8);
            value = Objects.requireNonNull(charSource.readFirstLine()).trim();
        } catch (IOException e) {
            log.error(e.toString());
            return null;
        }
        return value;
    }


    /**
     * convert file content to byte[]
     */
    public static byte[] getFileAsBytes(File file){
        try {
            return Files.toByteArray(file);
        } catch (IOException e) {
            log.error(e.toString());
        }
        return null;
    }


    public static Stream<Path> walk(Path start, int maxDepth, FileVisitOption... options) throws IOException {
        return java.nio.file.Files.walk(start, maxDepth, options);
    }


    public static String convertUnixPath(String path) {
        if (Objects.isNull(path)) {
            return null;
        }
        return path.replace(File.separatorChar, '/');
    }
}
