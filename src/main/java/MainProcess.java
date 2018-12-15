import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reader.EnglishSrtReadImpl;
import reader.SrtReader;
import reader.WorldBean;

import java.io.File;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainProcess {
    private static Logger logger = LoggerFactory.getLogger(EnglishSrtReadImpl.class);

    List<WorldBean> handledWorldList = new ArrayList<>();
    List<WorldBean> notHandledWorldList = new ArrayList<>();
    String handledPath = "";
    String notHandledDirPath = "";
    String exportFileDirPath = "";
    Long timeStart =0L;
    String regularExpress = "[^a-zA-Z ]";

    public static void main(String[] args) {
        logger.info("============= Process Start =============");
        MainProcess mainObj = new MainProcess();
        mainObj.init();
        mainObj.process();
    }

    private void init() {
        timeStart = System.currentTimeMillis();
        handledPath = System.getProperty("user.dir")+"/handledWordDir";
        notHandledDirPath = System.getProperty("user.dir")+"/notHandledWordDir";
        exportFileDirPath = System.getProperty("user.dir")+"/exportNotHandledWordDir";
    }

    private  void process() {
        handledWorldList =  getHandleWordList();
        notHandledWorldList = getNotHandledWorldList();

        notHandledWorldList.removeAll(handledWorldList);
        String exportFileName = exportFileDirPath+"/export_"+System.currentTimeMillis()+".txt";
        writeResult(notHandledWorldList,exportFileName);



        logger.info("Use Time (second): "+ (System.currentTimeMillis()-timeStart)/1000.0);
        logger.info("============= Process Done! =============");
    }

    private void writeResult(List<WorldBean> notHandledWorldList,String exportFileName) {

        File exportFile = new File(exportFileName);
        boolean exorted = false;

        StringBuilder stringBuilder = new StringBuilder();
        for (WorldBean item: notHandledWorldList) {
            stringBuilder.append(item.getWord()).append("\n");
        }
        try {
            if (notHandledWorldList.size()>0){
                Files.write(stringBuilder.toString().getBytes(StandardCharsets.UTF_8),exportFile);
                exorted = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("");
        logger.info("");

        if (exorted){
            logger.info("输出结果文件为："+ exportFileName);
        }else {
            logger.info("没有产生数据");
        }
    }

    private List<WorldBean> getHandleWordList() {
        SrtReader srtReader = new EnglishSrtReadImpl();
        srtReader.setRegularExpress(regularExpress);
        srtReader.init();
        return srtReader.readSrt(handledPath);
    }

    private List<WorldBean> getNotHandledWorldList() {
        SrtReader srtReader = new EnglishSrtReadImpl();
        srtReader.setRegularExpress(regularExpress);
        srtReader.init();
        return srtReader.readSrt(notHandledDirPath);
    }


}
