package fw;

import java.io.Closeable;

public class IOUtils {
    public static void safeClose(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        }catch (Exception e){
            //ignore this exception
        }
    }
}
