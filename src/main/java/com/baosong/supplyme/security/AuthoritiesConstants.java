package com.baosong.supplyme.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

	/**
	 * Administrators
	 */
    public static final String ADMIN = "ROLE_ADMIN";

    /**
     * Default user
     */
    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    /**
     * Purchaser : manage purchase orders
     */
    public static final String PURCHASER = "ROLE_PURCHASER";

    /**
     * Approval lvl 1 : can approve demand if the estimated amount is low
     */
    public static final String VALIDATION_LVL1 = "ROLE_VALIDATION_LVL1";

    /**
     * Approval lvl 2 : can approve any demand
     */
    public static final String VALIDATION_LVL2 = "ROLE_VALIDATION_LVL2";

        /**
     * Approval lvl 3 : can approve any demand
     */
    public static final String VALIDATION_LVL3 = "ROLE_VALIDATION_LVL3";

        /**
     * Approval lvl 4 : can approve any demand
     */
    public static final String VALIDATION_LVL4 = "ROLE_VALIDATION_LVL4";

        /**
     * Approval lvl 5 : can approve any demand
     */
    public static final String VALIDATION_LVL5 = "ROLE_VALIDATION_LVL5";

    /**
     * Material manager : can create and edit every material
     */
    public static final String MATERIAL_MANAGER = "ROLE_MATERIAL_MANAGER";

    /**
     * Delivery note manager : can create and edit delivery notes
     */
    public static final String SUPPLIER_MANAGER = "ROLE_SUPPLIER_MANAGER";

    /**
     * Project manager : can create and edit projects
     */
    public static final String PROJECT_MANAGER = "ROLE_PROJECT_MANAGER";

    /**
     * Dlivery note manager : can create and edit delivery notes
     */
    public static final String DELIVERY_MANAGER = "ROLE_DELIVERY_MANAGER";

    private AuthoritiesConstants() {
    }
}
