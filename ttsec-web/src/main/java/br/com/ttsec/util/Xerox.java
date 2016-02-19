
package br.com.ttsec.util;

import com.rits.cloning.Cloner;

/**
 * Responsável por fazer clones de objetos.
 *
 * @author averri
 */
public class Xerox {

    private static final Cloner CLONER = new Cloner();

    static {
//        CLONER.dontClone(UnitOfWorkQueryValueHolder.class);
//        CLONER.dontClone(SessionImplementor.class, JDBCTransaction.class, SessionImpl.class);
    }

    private Xerox() {
    }

    public static <E> E copy(E objeto) {
        if (objeto != null) {
            return CLONER.deepClone(objeto);
        } else {
            return null;
        }
    }

}
