package com.baosong.supplyme.web.rest.errors;

import java.net.URI;
import java.util.List;

import com.baosong.supplyme.domain.errors.MessageParameterBean;
import com.baosong.supplyme.domain.errors.ParameterizedServiceException;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class BadRequestServiceException extends AbstractThrowableProblem {

    private static final long serialVersionUID = -3776722563672737835L;

    private List<MessageParameterBean> messages;

    public BadRequestServiceException(ParameterizedServiceException e, String entityName) {
        this(ErrorConstants.DEFAULT_TYPE, e, entityName);
    }

    public BadRequestServiceException(URI type, ParameterizedServiceException e, String entityName) {
        super(type, e.getMessage(), Status.BAD_REQUEST, null, null, null, null);
        this.messages = e.getMessages();
    }

    public Iterable<MessageParameterBean> getMessages() {
        return this.messages;
    }
}
