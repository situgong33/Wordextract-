package reader;

import java.util.List;

public interface SrtReader {

    public List<WorldBean> readSrt(String dirPath);
    public void init();
    public void setRegularExpress(String regularExpree);
}
