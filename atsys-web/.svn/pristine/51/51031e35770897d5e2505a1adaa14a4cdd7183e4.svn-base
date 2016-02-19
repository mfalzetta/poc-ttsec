package com.sixlabs.atsys.service;

import com.sixlabs.atsys.domain.Config;
import com.sixlabs.atsys.domain.Email;
import com.sixlabs.atsys.repository.EmailRep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.*;
import javax.inject.Inject;
import javax.mail.*;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static com.sixlabs.atsys.domain.utils.ObjectUtils.call;
import static com.sixlabs.atsys.domain.utils.ObjectUtils.run;

/**
 * Provê serviço para envio e recebimento de emails. Utiliza as propriedades de configurações definidas em
 * {@link Config} Este serviço também verifica periodicamente emails pendentes de envio e realiza o envio em
 * background.
 */
@Startup
@Singleton
@Lock(LockType.READ)
public class EmailService implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Inject
    private Config config;

    @Inject
    private TemplateService templateService;

    @Inject
    private EmailRep emailRep;

    // Indica se pode enviar emails neste momento.
    private final AtomicBoolean canRun;

    public EmailService() {
        LOG.info("Iniciando serviço de emails...");
        canRun = new AtomicBoolean(true);
    }

    /**
     * Armazena um email para envio em background.
     *
     * @param email O email a ser enviado.
     */
    public void sendLater(Email email) {
        emailRep.save(email);
    }

//    /**
//     * Agenda o envio de emails pendentes a cada 30 segundos.
//     */
//    @Schedule(second = "0,30", minute = "*", hour = "*", persistent = false)
//    private void sendEmails() {
//        run(LOG, canRun, () -> {
//            // Obtém os emails pendentes de envio.
//            final List<Email> emails = emailRep.listAll();
//            if (!emails.isEmpty()) {
//                // Cria uma sessão de email.
//                final Session session = createSession();
//                // Obtém o transporte da sessão de email.
//                final Transport transport = getTransport(session);
//                // Envia os emails.
//                emails.parallelStream().forEach(e -> send(e, session, transport));
//            }
//        });
//    }

//    /**
//     * Envia um email para o endereço especificado. Este método não levanta exceção.
//     *
//     * @param email     O email a ser enviado.
//     * @param session   A sessão de emails.
//     * @param transport A instância de transporte de emails.
//     * @return true, se conseguiu enviar o e-mail; false, se não conseguiu enviar o e-mail.
//     */
//    private boolean send(final Email email, Session session, Transport transport) {
//
//        return tryCatch(LOG, () -> {
//
//            LOG.info("Enviando email:  '{}'...", email);
//            // Incrementa o contador de tentativas de envio.
//            email.incrementTries(LocalDateTime.now());
//            // Cria a mensagem de email.
//            final Message message = new MimeMessage(session);
//            // Configura o destinatário principal.
//            message.setRecipients(Message.RecipientType.TO,
//                    InternetAddress.parse(email.getUser().getEmail()));
//            // Configura o campo assunto da mensagem.
//            message.setSubject(email.getType().getDescription());
//            // Configura o campo from.
//            message.setFrom(InternetAddress.parse(config.getEmailFrom())[0]);
//
//            final Map<String, Object> model = new HashMap<>();
//            model.put("email", email);
//            model.put("dtf", dtf);
//
//            // Obtém o texto do email.
//            final String text = templateService.getText(email.getType().getTemplate(), model);
//            // Configura o conteúdo da mensagem.
//            message.setContent(text, "text/html; charset=utf-8");
//
//            // Envia a mensagem de email.
//            transport.sendMessage(message, message.getAllRecipients());
//            // Atualiza a data de envio do email.
//            email.setSent(LocalDateTime.now());
//
//        }, () -> emailRep.save(email));
//    }

    @Schedule(second = "0,30", minute = "*", hour = "*", persistent = false)
    private void receiveEmails() {

        run(LOG, canRun, () -> {
            // Obtém o store da sessão de email.
            final Store store = getStore(createSession());
            // Obtém o inbox.
            final Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            // Obtém algumas mensagens.
            final Message[] messages = inbox.getMessages(1, 2);

            // Para cada mensagem,
            Stream.of(messages).forEach(msg -> run(LOG, () -> {

                // Assunto do email.
                final String subject = msg.getSubject();
                // Data de recebimento.
//                final LocalDateTime received = LocalDateTime.ofInstant(
//                        m.getSentDate().toInstant(), DateUtils.AMERICA_SAO_PAULO);

                LOG.info("Assunto: {}, Recebida: {}", subject, msg.getReceivedDate());

                final ByteArrayOutputStream content = new ByteArrayOutputStream();
                msg.writeTo(content);

                emailRep.save(new Email(LocalDateTime.now(), msg.getFrom()[0].toString(), msg.getSubject(), content));
            }));
        });
    }

    private static List<Message> receive(Folder  inbox, int qty) {
        final List<Message> messages = new ArrayList<>(qty);
        try {
            inbox.open(Folder.READ_ONLY);
            int idx = 0;
            int i = idx * qty + 1;
            Collections.addAll(messages, inbox.getMessages(i, i + qty - 1));
            inbox.close(true);

        } catch (Exception e) {
            LOG.error("Erro inesperado.", e);
        }
        return messages;
    }

    /**
     * Envia um email de teste para o usuário imediatamente.
     *
     * @param user O destinatário do email de teste.
     */
//    public void sendTestEmailNow(User user) {
//        final Session session = createSession();
//        send(new Email(EmailType.EMAIL_TEST, user), session, getTransport(session));
//    }

    /**
     * Cria uma nova sessão de email. Serão usados os parâmetros de configuração definidos em {@link Config}.
     *
     * @return A nova sessão de email.
     */
    public Session createSession() {
        // Retorna sempre uma nova instância, com o autenticador configurado.
        // OBS: para que o autenticador funcione, é necessário que as propriedades de configurações
        // 'mail.<protocol>.auth' estejam definidas como 'true'.
        return Session.getInstance(config.getEmailProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // Retorna o autenticador que usa o nome de usuário e senha.
                return new PasswordAuthentication(config.getEmailUser(), config.getEmailPassword());
            }
        });
    }

    /**
     * Obtém uma nova instância de {@link Transport} para envio de emails.
     *
     * @param session A sessão de email.
     * @return A instância para envio de emails, já conectada. Nunca retorna null.
     */
    public Transport getTransport(Session session) {
        return call(() -> {
            final Transport transport = session.getTransport();
            transport.connect();
            return transport;
        });
    }

    /**
     * Obtém uma nova instância de {@link Store} para recebimento de emails. Serão usados os parâmetros
     * de configuração definidos em {@link Config}.
     *
     * @param session A sessão de email.
     * @return A instância para recebimento de emails, já conectada. Nunca retorna null.
     */
    public Store getStore(Session session) {
        return call(() -> {
            final Store store = session.getStore();
            store.connect();
            return store;
        });
    }
}
