package com.baosong.supplyme.domain.errors;

import java.util.Arrays;
import java.util.List;

public class ParameterizedServiceException extends ServiceException {

	/**
	 *
	 */
    private static final long serialVersionUID = -5635854215631699865L;

    private List<MessageParameterBean> messageParameters;

    public ParameterizedServiceException(String message, MessageParameterBean messageParameter) {
        super(message, "");
        this.messageParameters = Arrays.asList(messageParameter);
    }

    public ParameterizedServiceException(String message, List<MessageParameterBean> messageParameter) {
        super(message, "");
        this.messageParameters = messageParameter;
    }

    public List<MessageParameterBean> getMessages() {
        return this.messageParameters;
    }
}
