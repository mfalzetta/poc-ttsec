package com.sixlabs.atsys.service;

import org.jetbrains.annotations.NotNull;

/**
 * Denota uma sessão que foi destruída. Contém o usuário que estava associado à sessão.
 */
public class SessionDestroyed {

    // O username associado à sessão destruída.
    private final String  username;

    public SessionDestroyed(String username) {
        if(username == null) {
            throw new IllegalArgumentException("'username' não pode ser nulo.");
        }
        this.username = username;
    }

    @NotNull
    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionDestroyed that = (SessionDestroyed) o;
        return username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String toString() {
        return "SessionDestroyed{" +
                "username='" + username + '\'' +
                '}';
    }
}
