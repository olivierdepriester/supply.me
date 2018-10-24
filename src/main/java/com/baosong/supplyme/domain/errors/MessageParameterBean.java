package com.baosong.supplyme.domain.errors;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class MessageParameterBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String message;

    private List<String> parameters;

    MessageParameterBean(String message, List<String> parameters) {
        this.message = message;
        this.parameters = parameters;
    }

    MessageParameterBean(String message, String parameter) {
        this(message, Arrays.asList(parameter));
    }

    /**
     * Create a parameterized message.
     *
     * @param message parameterized message code
     * @param params list of parameter values
     * @return
     */
    public static MessageParameterBean of(String message, String... params) {
        return new MessageParameterBean(message, Arrays.asList(params));
    }

    public String getMessage() {
        return this.message;
    }

    public List<String> getParameters() {
        return this.parameters;
    }

    public String toString() {
        return new StringBuilder(this.message).append(":").append(this.parameters.toString()).toString();
    }
}
