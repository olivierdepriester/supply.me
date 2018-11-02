package com.baosong.supplyme.service;

import java.util.List;

import com.baosong.supplyme.domain.errors.ServiceException;
import com.baosong.supplyme.service.dto.AttachmentFileDTO;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for managing Demand.
 */
public interface AttachmentFileService {

    /**
     * Remove a draft attachment file
     *
     * @param temporaryToken
     * @return
     * @throws ServiceException
     */
    boolean removeDraftAttachmentFile(String temporaryToken) throws ServiceException;

    /**
     * Save the draft attached files after their upload.
     *
     * @param attachmentFiles
     * @return
     */
    List<AttachmentFileDTO> saveDraftAttachmentFiles(List<MultipartFile> attachmentFiles);

    /**
     * Save the attachment files of a demand.
     *
     * @param id Demand identifier
     * @param attachmentFiles Attachment files list
     * @return The updated attachment list
     */
    List<AttachmentFileDTO> saveAttachmentFiles(Long id, List<AttachmentFileDTO> attachmentFiles) throws ServiceException;

}
