package com.baosong.supplyme.domain.errors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParameterizedServiceException extends ServiceException {

	/**
	 *
	 */
    private static final long serialVersionUID = -5635854215631699865L;

    private List<MessageParameterBean> messageParameters;

    public ParameterizedServiceException(MessageParameterBean messageParameter) {
        this.messageParameters = Arrays.asList(messageParameter);
    }

    public ParameterizedServiceException(List<MessageParameterBean> messageParameter) {
        this.messageParameters = messageParameter;
    }

    public List<MessageParameterBean> getMessages() {
        return this.messageParameters;
    }
}
