package lx.own.frame.tools.network.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * <p> </p><br/>
 *
 * @author Lx
 * @date 01/07/2017
 */

public class StreamReader {

    public static byte[] readBytes(InputStream input) throws IOException {
        ByteArrayOutputStream reader = null;
        try {
            reader = new ByteArrayOutputStream();
            byte[] arr = new byte[1024];
            int len = -1;
            while ((len = input.read(arr)) != -1) {
                reader.write(arr, 0, len);
            }
            input.close();
            reader.flush();
            byte[] result = reader.toByteArray();
            reader.close();
            return result;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {

                }

            }
        }

    }

    public static String readString(InputStream is, Charset charset) throws IOException {
        return new String(readBytes(is), charset);
    }
}
