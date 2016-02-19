package com.sixlabs.atsys.domain.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class StreamUtils {

    private static final Logger LOG = LoggerFactory.getLogger(StreamUtils.class);

    /**
     * Copia um dados de um stream para outro.
     *
     * @param is O stream de entrada.
     * @param os O stream de saída.
     */
    public static void copy(final InputStream is, final OutputStream os) throws IOException {
        final BufferedInputStream bis = new BufferedInputStream(is);
        final BufferedOutputStream bos = new BufferedOutputStream(os);
        final byte[] buf = new byte[4096];
        try {
            while (bis.read(buf) != -1) {
                bos.write(buf);
                bos.flush();
            }

        } finally {
            close(bos);
            close(bis);
        }
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                LOG.warn("Erro ao fechar recurso.", e);
            }
        }
    }


}
