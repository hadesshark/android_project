package helloandroid.textreader4;

import java.io.Serializable;

/**
 * Created by hadesshark on 2017/9/3.
 */

public class Content implements Serializable {
    private String content;

    public Content(String content) {
        this.content = content;
    }

    public String getContent() {
//        System.out.println(content);
        return content;
    }
}
