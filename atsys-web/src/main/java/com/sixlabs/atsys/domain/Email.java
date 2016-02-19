package com.sixlabs.atsys.domain;

import com.sixlabs.atsys.domain.utils.ObjectUtils;
import org.apache.commons.io.IOUtils;

import javax.persistence.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.time.LocalDateTime;

@Entity
@Table(name = "EMAIL")
public class Email extends EntityBaseUUID {

    @Column(name = "RECEIVED")
    private LocalDateTime received;

    @Column(name = "FROM_ADDRESS")
    private String fromAddress;

    @Column(name = "SUBJECT")
    private String subject;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "TEXT")
    private String text;

    @Column(name = "LENGHT")
    private int lenght;

    @Column(name = "HASH")
    private String hash;

    protected Email() {
        super();
    }

    public Email(LocalDateTime received, String fromAddress, String subject, ByteArrayOutputStream content) {
        if (received == null) {
            throw new IllegalArgumentException("'received' cannot be null.");
        }
        if (fromAddress == null) {
            throw new IllegalArgumentException("'fromAddress' cannot be null.");
        }
        if (subject == null) {
            throw new IllegalArgumentException("'subject' cannot be null.");
        }
        if (text == null) {
            throw new IllegalArgumentException("'text' cannot be null.");
        }
        this.received = received;
        this.fromAddress = fromAddress;
        this.subject = subject;
        this.text = content.toString();
        this.lenght = content.toByteArray().length;

        try {
            // Obtém o hash sha1 do texto.
            this.hash = ObjectUtils.toSHA1(this.text);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public LocalDateTime getReceived() {
        return received;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public int getLenght() {
        return lenght;
    }

    public String getHash() {
        return hash;
    }

    @Override
    public String toString() {
        return "Email{" +
                "received=" + received +
                ", fromAddress='" + fromAddress + '\'' +
                ", subject='" + subject + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }

    public static void main(String[] args) throws IOException {

        PipedOutputStream pipeout = new PipedOutputStream();
        PipedInputStream pipein = new PipedInputStream(pipeout);

        pipeout.write("Alexandre".getBytes());
        pipeout.flush();
        pipeout.close();

        final String text = IOUtils.toString(pipein);
        System.out.println(text);
    }


}
