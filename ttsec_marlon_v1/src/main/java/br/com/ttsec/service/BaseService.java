package br.com.ttsec.service;

import br.com.ttsec.service.event.ServiceEvent;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.io.Serializable;
import java.lang.reflect.Method;

public abstract class BaseService<T> implements Serializable {

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void execute(T entity, ServiceEvent event) throws NoSuchMethodException {
        Class clazz = this.getClass();
        Class[] argument = new Class[1];
        argument[0] = entity.getClass();

        Method action = clazz.getMethod(event.getClass().getSimpleName().toLowerCase(), argument);
    }
}
