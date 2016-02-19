package com.sixlabs.atsys.web.rest;

import com.sixlabs.atsys.domain.Captcha;
import com.google.code.kaptcha.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.awt.image.BufferedImage;

/**
 * Responsável por produzir imagens de captcha. Quando um cliente invoca a URL '/captcha/captcha.jpg'
 * o CaptchaController gera o texto e a imagem do captcha. O texto do captcha é armazenado
 * na sessão HTTP, associado à chave 'captcha', e a imagem é enviada para o cliente imediatamente.
 *
 * @author averri
 */
@Path("/captcha")
public class CaptchaController {

    private final static Logger LOG = LoggerFactory.getLogger(CaptchaController.class);

    @Inject
    @Captcha
    private Producer captcha;

    @Context
    private HttpServletRequest request;

    @GET
    @Path("/image.jpg")
    @Produces("image/jpeg")
    public Response generateCaptcha() {

        // Cria o texto do captcha.
        final String text = captcha.createText();
        // Cria a imagem do captcha.
        final BufferedImage challenge = captcha.createImage(text);
        // Armazena o texto do captcha na sessão HTTP.
        request.getSession(true).setAttribute("captcha", text);
        // Cria um stream de saída, onde serão escritos os bytes da imagem.
        final StreamingOutput imageStream = output -> ImageIO.write(challenge, "jpg", output);

        return Response.ok(imageStream)
                .header("Cache-Control", "no-store")
                .header("Pragma", "no-cache")
                .header("Expires", 0)
                .build();
    }

}
