package helloandroid.txtreader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import info.monitorenter.cpdetector.io.JChardetFacade;


/**
 * Created by hadesshark on 2017/8/31.
 */

public class CharsetDetector {
    public CharsetDetector() {
    }

    public static Charset detect(InputStream in) {
        JChardetFacade detector = JChardetFacade.getInstance();
        Charset charset = null;

        try {
            in.mark(100);
            charset = detector.detectCodepage(in, 100);
            in.reset();
        } catch (IllegalArgumentException var4) {
            var4.printStackTrace();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        return charset;
    }
}
