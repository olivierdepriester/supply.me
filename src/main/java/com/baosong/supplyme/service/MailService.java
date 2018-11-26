package com.baosong.supplyme.service;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.mail.internet.MimeMessage;

import com.baosong.supplyme.domain.Demand;
import com.baosong.supplyme.domain.User;
import com.baosong.supplyme.service.util.FormatUtil;
import com.google.common.collect.Lists;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import io.github.jhipster.config.JHipsterProperties;

/**
 * Service for sending emails.
 * <p>
 * We use the @Async annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";

    private final JHipsterProperties jHipsterProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    public MailService(JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender,
            MessageSource messageSource, SpringTemplateEngine templateEngine) {

        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml, boolean isUrgent) {
        User temp = new User();
        temp.setEmail(to);
        this.sendEmail(temp, subject, content, isMultipart, isHtml, isUrgent);
    }

    @Async
    private void sendEmail(Iterable<User> to, String subject, String content, boolean isMultipart, boolean isHtml, boolean isUrgent) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}", isMultipart,
                isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            Iterator<User> it = to.iterator();
            while (it.hasNext()) {
                User user = it.next();
                String personal = FormatUtil.formatUser(user);
                message.addTo(user.getEmail(), personal.isEmpty() ? null : personal);
            }
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            message.setPriority(isUrgent ? 1 : 3);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.warn("Email could not be sent to user '{}'", to, e);
            } else {
                log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
            }
        }
    }

    @Async
    private void sendEmail(User to, String subject, String content, boolean isMultipart, boolean isHtml, boolean isUrgent) {
        this.sendEmail(Lists.newArrayList(to), subject, content, isMultipart, isHtml, isUrgent);
    }

    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user, subject, content, false, true, false);

    }

    // @Async
    // public void sendEmailFromTemplate(String to, String templateName, String titleKey) {
    //     Locale locale = Locale.forLanguageTag("en");
    //     Context context = new Context(locale);
    //     context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
    //     String content = templateEngine.process(templateName, context);
    //     String subject = messageSource.getMessage(titleKey, null, locale);
    //     sendEmail(to, subject, content, false, true);
    // }

    @Async
    public void sendActivationEmail(User user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title");
    }

    @Async
    public void sendCreationEmail(User user) {
        log.debug("Sending creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/creationEmail", "email.activation.title");
    }

    @Async
    public void sendPasswordResetMail(User user) {
        log.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title");
    }

    @Async
    public void sendApprovedDemandToPurchaserEmail(Demand demand, Iterable<User> to) {
        log.debug("Sending approved demand email for id {}", demand.getId());
        Context context = getInitializedContextForDemand(demand);
        Locale locale = Locale.forLanguageTag("en");
        context.setLocale(locale);
        String content = templateEngine.process("mail/demandApprovalEmail", context);
        String subject = messageSource.getMessage("email.demand.approval.title",
                new Object[] { demand.getCode() }, locale);
        sendEmail(to, subject, content, false, true, demand.isUrgent());
    }

    /**
     * Send an email on reject to the demand owner from the user who rejected
     *
     * @param demand rejected demand
     * @param user   user who rejected the demand
     */
    @Async
    public void sendRejectedDemandEmail(Demand demand, User user) {
        log.debug("Sending rejected demand email for id {} from '{}'", demand.getId(), user.getLogin());
        Locale locale = Locale.forLanguageTag(demand.getCreationUser().getLangKey());
        Context context = getInitializedContextForDemand(demand);
        context.setLocale(locale);
        context.setVariable("rejecter", FormatUtil.formatUser(user));
        String content = templateEngine.process("mail/demandRejectEmail", context);
        String subject = messageSource.getMessage("email.demand.reject.title",
                new Object[] { demand.getCode() }, locale);
        sendEmail(demand.getCreationUser(), subject, content, false, true, demand.isUrgent());
    }

    /**
     * Generate and send email when requests status is waiting for approval.
     *
     * @param demand
     * @param recipients List of users who will receive the mail.
     */
    @Async
    public void sendWaitingForApprovalDemandEmail(Demand demand, List<User> to) {
        log.debug("Sending waiting_for_appproval demand email for id {}", demand.getId(), to);
        Context context = getInitializedContextForDemand(demand);
        Locale locale = Locale.forLanguageTag("en");
        context.setLocale(locale);
        context.setVariable("author", FormatUtil.formatUser(demand.getCreationUser()));
        String content = templateEngine.process("mail/demandWaitingForApprovalEmail", context);
        String subject = messageSource.getMessage("email.demand.waitingForApproval.title",
                new Object[] { demand.getCode() }, locale);
        sendEmail(to, subject, content, false, true, demand.isUrgent());
    }

    private Context getInitializedContextForDemand(Demand demand) {
        Context context = new Context();
        context.setVariable("demand", demand);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        return context;
    }

}
