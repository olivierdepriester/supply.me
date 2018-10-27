package com.baosong.supplyme.domain.errors;

public class ForbiddenAccessException extends ParameterizedServiceException {

    private static final long serialVersionUID = 1L;

    public ForbiddenAccessException(String entityName, Long id) {
        super( new StringBuilder("Forbidden access on ").append(id).toString(),
        MessageParameterBean.of("forbidden", entityName, id == null ? "new" : id.toString()));
    }

    public ForbiddenAccessException(String entityName) {
        super( new StringBuilder("Forbidden access on new").toString(),
        MessageParameterBean.of("forbidden", entityName, "new"));
    }
}
