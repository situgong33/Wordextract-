package reader;

import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import java.util.stream.Collectors;

public class EnglishSrtReadImpl implements reader.SrtReader {

    Logger logger = LoggerFactory.getLogger(EnglishSrtReadImpl.class);

    List<File> srtFileInputList = new ArrayList<>();

    List<WorldBean> wordSortList = new ArrayList<>();
    public List<WorldBean> readSrt(String dirPath) {
        logger.info("base dir: "+ dirPath);
        File  inputFile = new File(dirPath);
       for(File file : Files.fileTreeTraverser().preOrderTraversal(inputFile)){
           if(file.isFile()) {
               srtFileInputList.add(file);
               logger.info(file.getAbsolutePath());
           }

        }

        String englishMatch = "[^a-zA-Z ]";
        List<String> proccessDate= new ArrayList<>();
        List<String> allLine;

        for (File file: srtFileInputList) {
            try {
                allLine = Files.readLines(file, StandardCharsets.UTF_8);

                proccessDate = allLine.stream()
                        .filter(string -> !string.trim().isEmpty())
                        .map(string -> string.replaceAll(englishMatch, "").toLowerCase().trim())
                        .filter(string -> !string.trim().isEmpty())
                        .collect(Collectors.toList());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        Set<WorldBean> wordSet = new HashSet<>();
        List<WorldBean> wordList = new ArrayList<>();
        for (String item : proccessDate){
            List<String> words = Arrays.asList(item.split(" "));
            for (String word: words) {
                wordList.add(new WorldBean(word,1));
                wordSet.add(new WorldBean(word,1));
            }

        }

        for (WorldBean item : wordSet) {
            for (WorldBean itemL: wordList) {
                if (item.equals(itemL)){
                    item.countAddone();
                }

            }
            if (item.getCount()>1){
                item.setCount(item.getCount()-1);
            }

        }

        wordSortList = new ArrayList<>(wordSet);
        wordSortList.sort(new Comparator<WorldBean>() {
            @Override
            public int compare(WorldBean o1, WorldBean o2) {
                return o2.getCount().compareTo(o1.getCount());
            }
        });


//        for (WorldBean item : wordSortList) {
//            logger.info(item.toString());
//
//        }

        logger.info("总单词："+ wordList.size());
        logger.info("不重复单词："+ wordSet.size());
        logger.info("不重复排序单词："+ wordSortList.size());

        return  wordSortList;
    }


    public void init() {

    }
}
