package com.sixlabs.atsys.domain.utils;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonObject;
import javax.websocket.Session;

import static com.sixlabs.atsys.domain.utils.ObjectUtils.run;

public class WSUtils {

    private static final Logger LOG = LoggerFactory.getLogger(WSUtils.class);

    /**
     * Envia a mensagem em formato texto para o cliente da sessão websocket. Este método levanta exceção.
     *
     * @param ws   A sessão websocket.
     * @param text A mensagem em formato texto.
     */
    public static void sendMsg(Session ws, String text) {
        run(() -> ws.getBasicRemote().sendText(text));
    }

    /**
     * Envia a mensagem em formato Json para o cliente da sessão websocket. Este método levanta exceção.
     *
     * @param ws   A sessão websocket.
     * @param json A mensagem em formato json.
     */
    public static void sendMsg(Session ws, JsonObject json) {
        sendMsg(ws, json.toString());
    }

    /**
     * Envia a mensagem em formato texto para o cliente da sessão websocket. Qualquer problema ocorrido
     * será registrado no log, este método não levanta exceção.
     *
     * @param log  O log a ser registrado, em caso de erros.
     * @param ws   A sessão websocket.
     * @param text A mensagem em formato texto.
     */
    public static void sendMsg(Logger log, Session ws, String text) {
        run(log, () -> ws.getBasicRemote().sendText(text));
    }

    /**
     * Envia a mensagem em formato Json para o cliente da sessão websocket. Qualquer problema ocorrido
     * será registrado no log, este método não levanta exceção.
     *
     * @param log  O log a ser registrado, em caso de erros.
     * @param ws   A sessão websocket.
     * @param json A mensagem em formato Json.
     */
    public static void sendMsg(Logger log, Session ws, JsonObject json) {
        run(log, () -> ws.getBasicRemote().sendText(json.toString()));
    }

    /**
     * Encerra uma sessão websocket, sem levantar exceção.
     *
     * @param ws A sessão websocket. Aceita nulo, neste caso o método faz nada.
     */
    public static void close(@Nullable Session ws) {
        if (ws != null && ws.isOpen()) run(LOG, ws::close);
    }

}
