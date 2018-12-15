package reader;

import java.util.Comparator;
import java.util.Objects;

public class WorldBean implements Comparator {
    String word;
    Integer count;
    String chineseMsg;
    String originMessage;

    public WorldBean(String word) {
        this.word = word;
    }

    public WorldBean(String word, Integer count) {
        this.word = word;
        this.count = count;
    }

    public WorldBean(String word, Integer count, String chineseMsg) {
        this.word = word;
        this.count = count;
        this.chineseMsg = chineseMsg;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getChineseMsg() {
        return chineseMsg;
    }

    public void setChineseMsg(String chineseMsg) {
        this.chineseMsg = chineseMsg;
    }

    public String getOriginMessage() {
        return originMessage;
    }

    public void setOriginMessage(String originMessage) {
        this.originMessage = originMessage;
    }

    public void countAddone() {
       this.count++;
    }


    @Override
    public int compare(Object o1, Object o2) {
        o1 = (WorldBean) o1;
        o2 = (WorldBean) o2;
        return ((WorldBean) o1).getCount().compareTo(((WorldBean) o2).getCount());

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorldBean worldBean = (WorldBean) o;
        return Objects.equals(word, worldBean.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word);
    }

    @Override
    public String toString() {
        return "WorldBean{" +
                "word='" + word + '\'' +
                ", count=" + count +
                ", chineseMsg='" + chineseMsg + '\'' +
                ", originMessage='" + originMessage + '\'' +
                '}';
    }
}
