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
    public static final String APPROVAL_LVL1 = "ROLE_APPROVAL_LVL1";

    /**
     * Approval lvl 2 : can approve any demand
     */
    public static final String APPROVAL_LVL2 = "ROLE_APPROVAL_LVL2";

    /**
     * Material manager : can create and edit every material
     */
    public static final String MATERIAL_MANAGER = "ROLE_MATERIAL_MANAGER";

    /**
     * Material manager : can create and edit every material
     */
    public static final String DELIVERY_MANAGER = "ROLE_DELIVERY_MANAGER";

    private AuthoritiesConstants() {
    }
}
