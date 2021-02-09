package club.qqtim.command;


import club.qqtim.common.ConstantVal;
import club.qqtim.context.ZitContext;
import club.qqtim.data.ZitObject;
import club.qqtim.util.FileUtil;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * @author rezeros.github.io
 * If hash-object was for storing an individual file, then write-tree is for storing a whole directory.
 */
@Data
@Slf4j
@CommandLine.Command(name = "write-tree")
public class WriteTree implements Callable<String> {

    @Override
    public String call() {
        return writeTree();
    }

    /**
     * @return tree Id
     */
    private String writeTree() {

        // init the indexAsTree to visual tree
        final String indexContent = FileUtil.getFileAsString(ConstantVal.INDEX, ConstantVal.NONE);
        Map<String, String> indexItems = new Gson().fromJson(indexContent, Map.class);
        Map<String, Object> indexAsTree = new HashMap<>();
        for (Map.Entry<String, String> pathObjectId : indexItems.entrySet()) {
            String path = pathObjectId.getKey();
            String objectId = pathObjectId.getValue();
            List<String> pathAndFile = Arrays.asList(path.split("/"));
            List<String> dirPaths = pathAndFile.subList(0, pathAndFile.size() - 1);
            String fileName = pathAndFile.get(pathAndFile.size() - 1);

            Map<String, Object> current = indexAsTree;
            for (String dirPath : dirPaths) {
                current = (Map<String, Object>) current.computeIfAbsent(dirPath, e -> new HashMap<>());
            }
            current.put(fileName, objectId);
        }

        return writeTreeRecursive(indexAsTree);
    }

    private String writeTreeRecursive(Map<String, Object> treeDict) {
        List<ZitObject> zitObjects = new ArrayList<>();
        for (Map.Entry<String, Object> keyVal : treeDict.entrySet()) {
            String type, objectId;
            String name = keyVal.getKey();
            Object value = keyVal.getValue();
            if (value instanceof Map) {
                type = ConstantVal.TREE;
                objectId = writeTreeRecursive((Map<String, Object>) value);
            } else {
                type = ConstantVal.BLOB;
                objectId = (String) value;
            }
            zitObjects.add(new ZitObject(type, objectId, name));
        }
        String fileContent = zitObjects.stream().sorted()
                .map(e -> String.format("%s %s %s\n", e.getType(), e.getObjectId(), e.getName()))
                .collect(Collectors.joining());
        return HashObject.hashObject(fileContent.getBytes(Charsets.UTF_8), ConstantVal.TREE);
    }


}
