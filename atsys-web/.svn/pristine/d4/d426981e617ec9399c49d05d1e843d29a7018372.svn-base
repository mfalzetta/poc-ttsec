package com.sixlabs.atsys.domain.utils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

/**
 * Ref: http://www.nurkiewicz.com/2014/03/simplifying-readwritelock-with-java-8.html
 */
public class FunctionalLock {

    // The read lock.
    private final Lock readLock;

    // The write lock.
    private final Lock writeLock;

    public FunctionalLock() {
        this(new ReentrantReadWriteLock(false));
    }

    public FunctionalLock(ReadWriteLock lock) {
        readLock = lock.readLock();
        writeLock = lock.writeLock();
    }

    public <T> T readR(Supplier<T> block) {
        readLock.lock();
        try {
            return block.get();
        } finally {
            readLock.unlock();
        }
    }

    public void read(Runnable block) {
        readLock.lock();
        try {
            block.run();
        } finally {
            readLock.unlock();
        }
    }

    public <T> T writeR(Supplier<T> block) {
        writeLock.lock();
        try {
            return block.get();
        } finally {
            writeLock.unlock();
        }
    }

    public void write(Runnable block) {
        writeLock.lock();
        try {
            block.run();
        } finally {
            writeLock.unlock();
        }
    }

}
