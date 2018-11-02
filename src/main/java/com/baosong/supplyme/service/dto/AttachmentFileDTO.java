package com.baosong.supplyme.service.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.baosong.supplyme.domain.AttachmentFile;

/**
 * A DTO representing an attachment file.
 */
public class AttachmentFileDTO {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    private String name;

    @Size(max = 255)
    private String type;

    @NotNull
    @Min(value = 1)
    private Long size;

    @Max(value = 50)
    private String temporaryToken;

    private byte[] content;

    public AttachmentFileDTO() {
        // Empty constructor needed for Jackson.
    }

    public AttachmentFileDTO(AttachmentFile af) {
        this.id = af.getId();
        this.type = af.getType();
        this.size = af.getSize();
        this.name = af.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the size
     */
    public Long getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(Long size) {
        this.size = size;
    }

    /**
     * @return the temporaryToken
     */
    public String getTemporaryToken() {
        return temporaryToken;
    }

    /**
     * @param temporaryToken the temporaryToken to set
     */
    public void setTemporaryToken(String temporaryToken) {
        this.temporaryToken = temporaryToken;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the content
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "AttachedFileDTO{" +
            "id='" + id + '\'' +
            ",name='" + name + '\'' +
            ", size=" + size.toString() +
            ", type='" + type + '\'' +
            ", temporaryToken ='" + temporaryToken + '\'' +
            "}";
    }
}
