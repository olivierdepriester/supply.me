package com.baosong.supplyme.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {

    /**
     * Define which authority can validate for a demand validation authority level
     * Key : Authority level Value : List of allowed authority
     *
     * Example :
     */
    private final static Map<String, Set<String>> VALIDATION_AUTHORITIES;

    private final static List<String> SORTED_AUTHORITIES;

    static {

        VALIDATION_AUTHORITIES = new HashMap<>();
        VALIDATION_AUTHORITIES.put(AuthoritiesConstants.VALIDATION_LVL1,
                Sets.newHashSet(AuthoritiesConstants.VALIDATION_LVL1, AuthoritiesConstants.VALIDATION_LVL2,
                        AuthoritiesConstants.VALIDATION_LVL3, AuthoritiesConstants.VALIDATION_LVL4,
                        AuthoritiesConstants.VALIDATION_LVL5));
        VALIDATION_AUTHORITIES.put(AuthoritiesConstants.VALIDATION_LVL2,
                Sets.newHashSet(AuthoritiesConstants.VALIDATION_LVL2, AuthoritiesConstants.VALIDATION_LVL3,
                        AuthoritiesConstants.VALIDATION_LVL4, AuthoritiesConstants.VALIDATION_LVL5));
        VALIDATION_AUTHORITIES.put(AuthoritiesConstants.VALIDATION_LVL3,
                Sets.newHashSet(AuthoritiesConstants.VALIDATION_LVL3, AuthoritiesConstants.VALIDATION_LVL4,
                        AuthoritiesConstants.VALIDATION_LVL5));
        VALIDATION_AUTHORITIES.put(AuthoritiesConstants.VALIDATION_LVL4,
                Sets.newHashSet(AuthoritiesConstants.VALIDATION_LVL4, AuthoritiesConstants.VALIDATION_LVL5));
        VALIDATION_AUTHORITIES.put(AuthoritiesConstants.VALIDATION_LVL5,
                Sets.newHashSet(AuthoritiesConstants.VALIDATION_LVL5));

        SORTED_AUTHORITIES = new ArrayList<>();
        SORTED_AUTHORITIES.add(AuthoritiesConstants.VALIDATION_LVL1);
        SORTED_AUTHORITIES.add(AuthoritiesConstants.VALIDATION_LVL2);
        SORTED_AUTHORITIES.add(AuthoritiesConstants.VALIDATION_LVL3);
        SORTED_AUTHORITIES.add(AuthoritiesConstants.VALIDATION_LVL4);
        SORTED_AUTHORITIES.add(AuthoritiesConstants.VALIDATION_LVL5);
    }

    private SecurityUtils() {
    }

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user
     */
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                return springSecurityUser.getUsername();
            } else if (authentication.getPrincipal() instanceof String) {
                return (String) authentication.getPrincipal();
            }
            return null;
        });
    }

    /**
     * Get the JWT of the current user.
     *
     * @return the JWT of the current user
     */
    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .filter(authentication -> authentication.getCredentials() instanceof String)
                .map(authentication -> (String) authentication.getCredentials());
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> authentication.getAuthorities().stream().noneMatch(
                        grantedAuthority -> grantedAuthority.getAuthority().equals(AuthoritiesConstants.ANONYMOUS)))
                .orElse(false);
    }

    /**
     * If the current user has a specific authority (security role).
     * <p>
     * The name of this method comes from the isUserInRole() method in the Servlet
     * API
     *
     * @param authority the authority to check
     * @return true if the current user has the authority, false otherwise
     * @see AuthoritiesConstants
     */
    public static boolean isCurrentUserInRole(String authority) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional
                .ofNullable(securityContext.getAuthentication()).map(authentication -> authentication.getAuthorities()
                        .stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority)))
                .orElse(false);
    }

    /**
     * If the current user has a specific authority (security role).
     * <p>
     * The name of this method comes from the isUserInRole() method in the Servlet
     * API
     *
     * @param authority the authority to check
     * @return true if the current user has the authority, false otherwise
     * @see AuthoritiesConstants
     */
    public static boolean isCurrentUserInRole(Collection<String> authorities) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> authentication.getAuthorities().stream()
                        .anyMatch(grantedAuthority -> authorities.contains(grantedAuthority.getAuthority())))
                .orElse(false);
    }

    public static String getCurrentUserHighestAuthority() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return getHighestAuthority(securityContext.getAuthentication().getAuthorities().stream()
                .filter(ga -> SORTED_AUTHORITIES.contains(ga.getAuthority())).map(ga -> ga.getAuthority())
                .collect(Collectors.toList()));
    }

    /**
     * Compare and get the highest {@code Authority} from a collection.
     *
     * @param authorities The authority collection.
     * @return The highest authority or null if {@code authorities} is null.
     */
    public static String getHighestAuthority(Collection<String> authorities) {
        if (authorities == null || authorities.isEmpty()) {
            return null;
        }
        return authorities.stream().reduce(null, (prev, next) -> getHigherAuthority(prev, next));
    }

    /**
     * Compare and get the higher {@code Authority} among 2.
     *
     * @param x the first {@code Authority} to compare
     * @param y the second {@code Authority} to compare
     * @return The higher authority
     */
    public static String getHigherAuthority(String x, String y) {
        return compare(x, y) > 0 ? x : y;
    }

    /**
     * Compares two {@code Authority} values.
     *
     * @param x the first {@code Authority} to compare
     * @param y the second {@code Authority} to compare
     * @return the value {@code 0} if {@code x == y}; a value less than {@code 0} if
     *         {@code x < y}; and a value greater than {@code 0} if {@code x > y}
     */
    public static int compare(String x, String y) {
        if (StringUtils.isEmpty(x)) {
            return -1;
        } else if (StringUtils.isEmpty(y)) {
            return 1;
        } else {
            return Integer.compare(SORTED_AUTHORITIES.indexOf(x), SORTED_AUTHORITIES.indexOf(y));
        }
    }

    /**
     * Get the authority level immediatly greater than the one given as a parameter
     *
     * @param authorityLevel current authority level.
     * @return the next authority level. if the parameter is the latest return {@code null}.
     *  if the paramater is not in the list of authority levels
     */
    public static String getNextAuthorityLevel(String authorityLevel) {
        int index = SORTED_AUTHORITIES.indexOf(authorityLevel);
        if (index < SORTED_AUTHORITIES.size() - 1) {
            return SORTED_AUTHORITIES.get(index + 1);
        } else {
            return null;
        }
    }
}
