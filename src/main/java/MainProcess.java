import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.knziha.plod.dictionary.mdict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reader.EnglishSrtReadImpl;
import reader.SrtReader;
import reader.WorldBean;
import test.CMN;

import java.io.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class MainProcess {
    private static Logger logger = LoggerFactory.getLogger(EnglishSrtReadImpl.class);

    List<WorldBean> handledWorldList = new ArrayList<>();
    List<WorldBean> notHandledWorldList = new ArrayList<>();
    String handledPath = "";
    String notHandledDirPath = "";
    String exportFileDirPath = "";
    Long timeStart =0L;
    String regularExpress = "[^a-zA-Z ]";


    boolean isShowChinese=true;
    boolean isShowCount=true;





    public static void main(String[] args) {
        logger.info("============= Process Start =============");
        MainProcess mainObj = new MainProcess();
        mainObj.init();
        mainObj.process();
        mainObj.cleanUp();
    }

    private void cleanUp() {
    }


    String propertiesPath ="";
    private void init() {
        timeStart = System.currentTimeMillis();
        handledPath = System.getProperty("user.dir")+"/handledWordDir";
        notHandledDirPath = System.getProperty("user.dir")+"/notHandledWordDir";
        exportFileDirPath = System.getProperty("user.dir")+"/exportNotHandledWordDir";
        propertiesPath= System.getProperty("user.dir")+"/conf/application.properties";

        isShowChinese = Boolean.valueOf(getproperties("isShowChinese"));
        isShowCount = Boolean.valueOf(getproperties("isShowCount"));
        regularExpress = getproperties("regularExpress");

    }

    private String getproperties(String keyName) {
        Properties prop = new Properties();
        InputStream input = null;
        File properFile = new File(propertiesPath);
        String value = null;
        if ( properFile.exists() && properFile.isFile() ){
            try {

                input = new FileInputStream(propertiesPath);

                // load a properties file
                prop.load(input);

                value= prop.getProperty(keyName);
                // get the property value and print it out

            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return value;
    }

    private  void process() {
        handledWorldList =  getHandleWordList();
        notHandledWorldList = getNotHandledWorldList();

        notHandledWorldList.removeAll(handledWorldList);

        if (isShowChinese){
            notHandledWorldList = getChineseMsg(notHandledWorldList);
        }



        String pattern = "yyyy-MM-dd_HH:mm:ss";
        SimpleDateFormat dateFormatter = new SimpleDateFormat(pattern);
        // format the date
        String text  = dateFormatter.format(new Date());
        String exportFileName = exportFileDirPath+"/export_"+text+"_"+System.currentTimeMillis()+".txt";
        writeResult(notHandledWorldList,exportFileName);



        logger.info("Use Time (second): "+ (System.currentTimeMillis()-timeStart)/1000.0);
        logger.info("============= Process Done! =============");
    }


    static String rootPath = "";
    static String klsMdictPath = "";
    /**
     * 获取单词的中文释义
     * @param notHandledWorldList
     * @return
     */
    private List<WorldBean> getChineseMsg(List<WorldBean> notHandledWorldList) {
        List<WorldBean> wordList = new ArrayList<>(notHandledWorldList.size());

        List<String> midictList = new ArrayList<>();
        midictList.add("最终的短语测试牛津英汉汉英词典.mdx");
        midictList.add("柯林斯双解.mdx");

        rootPath = System.getProperty("user.dir")+"/assets/mdicts/";
        klsMdictPath = rootPath+midictList.get(1);
        mdict md = null;
        try {
            md = new mdict(klsMdictPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String key = "kind";
        CMN.show("查询 "+key+" ： "+md.getEntryAt(md.lookUp(key)));
        String chineseMessage = "";
        for (WorldBean item : notHandledWorldList) {
            chineseMessage = "";
            try {
                chineseMessage=md.getRecordAt(md.lookUp(item.getWord()));
            } catch (IOException e) {
                e.printStackTrace();
            };
            item.setChineseMsg(chineseMessage);
            wordList.add(item);
        }

        return wordList;

    }

    /**
     * 写入单词到txt
     * word /t chinese /t count
     * @param notHandledWorldList
     * @param exportFileName
     */
    private void writeResult(List<WorldBean> notHandledWorldList,String exportFileName) {

        File exportFile = new File(exportFileName);
        boolean exorted = false;
        String split = "\t";
        StringBuilder stringBuilder = new StringBuilder();
        for (WorldBean item: notHandledWorldList) {
            try {
                stringBuilder.append(item.getWord());
                if (isShowChinese){
                    stringBuilder.append(split).append(item.getChineseMsg().replaceAll(split,"").trim());
                }
                if (isShowCount){
                    stringBuilder.append(split).append(item.getCount());
                }
                stringBuilder.append("\n");
            }catch (Exception e){
                logger.error("error",e);
            }
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
