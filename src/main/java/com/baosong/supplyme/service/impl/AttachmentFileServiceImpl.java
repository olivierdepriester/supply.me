package com.baosong.supplyme.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.baosong.supplyme.config.ApplicationProperties;
import com.baosong.supplyme.domain.AttachmentFile;
import com.baosong.supplyme.domain.Demand;
import com.baosong.supplyme.domain.errors.ServiceException;
import com.baosong.supplyme.repository.AttachmentFileRepository;
import com.baosong.supplyme.service.AttachmentFileService;
import com.baosong.supplyme.service.DemandService;
import com.baosong.supplyme.service.dto.AttachmentFileDTO;
import com.baosong.supplyme.service.mapper.AttachmentFileMapper;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing Demand.
 */
@Service
@Transactional
public class AttachmentFileServiceImpl implements AttachmentFileService {

    private final Logger log = LoggerFactory.getLogger(AttachmentFileServiceImpl.class);

    private final AttachmentFileRepository attachmentFileRepository;

    private final ApplicationProperties applicationProperties;

    private final AttachmentFileMapper attachmentFileMapper;

    @Autowired
    private DemandService demandService;

    public AttachmentFileServiceImpl(AttachmentFileRepository attachmentFileRepository,
            ApplicationProperties applicationProperties, AttachmentFileMapper attachmentFileMapper) {
        this.applicationProperties = applicationProperties;
        this.attachmentFileRepository = attachmentFileRepository;
        this.attachmentFileMapper = attachmentFileMapper;
    }

    /**
     * Save uploaded attachment files in a temporary folder
     *
     * @param files : List of {@see MultipartFile} to upload
     *
     * @return DTO list with the token generated representing each file.
     *
     */
    @Override
    public List<AttachmentFileDTO> saveDraftAttachmentFiles(List<MultipartFile> files) {
        final String temporaryPath = applicationProperties.getStorage().getAttachments().getTemporaryPath();
        List<AttachmentFileDTO> attachmentFiles = new ArrayList<>(files.size());
        AttachmentFileDTO attachmentFile = null;
        try {
            for (MultipartFile file : files) {
                attachmentFile = new AttachmentFileDTO();
                attachmentFile.setName(file.getOriginalFilename());
                attachmentFile.setSize(file.getSize());
                attachmentFile.setType(file.getContentType());
                attachmentFile.setTemporaryToken(UUID.randomUUID().toString());
                attachmentFiles.add(attachmentFile);
                File fileToWrite = new File(
                        new StringBuilder(temporaryPath).append(attachmentFile.getTemporaryToken()).toString());
                OutputStream streamToWrite = new FileOutputStream(fileToWrite);
                byte[] buffer = new byte[file.getInputStream().available()];
                file.getInputStream().read(buffer);
                streamToWrite.write(buffer);
                streamToWrite.close();
            }
        } catch (IOException e) {
            // TODO: handle exception
        }
        return attachmentFiles;
    }

    @Override
    public boolean removeDraftAttachmentFile(String temporaryToken) {
        if (temporaryToken == null || temporaryToken.isEmpty()) {
            log.debug("Remove draft attachment : temporary token is empty");
            return false;
        }
        final String temporaryPath = applicationProperties.getStorage().getAttachments().getTemporaryPath();
        File fileToRemove = new File(new StringBuilder(temporaryPath).append(temporaryToken).toString());
        log.debug("Remove draft attachment : %s ", fileToRemove.getPath());
        if (!fileToRemove.exists()) {
            log.info("File does not exist : %s ", fileToRemove.getPath());
            return false;
        }
        return fileToRemove.delete();
    }

    @Override
    public List<AttachmentFileDTO> saveAttachmentFiles(Long id, List<AttachmentFileDTO> attachmentFilesDTO)
            throws ServiceException {
        final Demand demand = this.demandService.findOne(id).get();
        final String temporaryPath = applicationProperties.getStorage().getAttachments().getTemporaryPath();
        final String demandPath = new StringBuilder(applicationProperties.getStorage().getAttachments().getPath())
                .append(id).append('/').toString();
        List<AttachmentFile> afs = this.attachmentFileRepository.findByDemandId(id);
        try {
            Iterator<AttachmentFileDTO> attachmentIterator = attachmentFilesDTO.stream()
                    .filter(af -> !StringUtils.isEmpty(af.getTemporaryToken())).iterator();
            AttachmentFileDTO attachmentFileDTO = null;
            AttachmentFile attachmentFile = null;
            while (attachmentIterator.hasNext()) {
                attachmentFileDTO = attachmentIterator.next();
                attachmentFile = attachmentFileMapper.fromDTOToEntity(attachmentFileDTO);
                attachmentFile.setDemand(demand);
                afs.add(attachmentFile);
                this.attachmentFileRepository.save(attachmentFile);
                File temporaryFile = new File(
                        new StringBuilder(temporaryPath).append(attachmentFileDTO.getTemporaryToken()).toString());
                File storedFile = new File(new StringBuilder(demandPath).append(attachmentFile.getId()).toString());
                FileUtils.moveFile(temporaryFile, storedFile);
            }
            List<AttachmentFile> toRemove = afs.parallelStream()
                .filter(af ->
                    attachmentFilesDTO.stream()
                        .noneMatch(
                            afDTO -> afDTO.getId().equals(af.getId())
                        )
                    ).collect(Collectors.toList());
            afs.removeIf(af -> toRemove.contains(af));
            for (AttachmentFile afToRemove : toRemove) {
                File fileToRemove = new File(new StringBuilder(demandPath).append(afToRemove.getId()).toString());
                fileToRemove.delete();
                this.attachmentFileRepository.delete(afToRemove);
            }
        } catch (IOException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return this.attachmentFileMapper.fromEntitiesToDTOs(afs);
    }

    @Override
    public List<AttachmentFileDTO> getAttachmentFiles(Long id) {
        return this.attachmentFileMapper.fromEntitiesToDTOs(this.attachmentFileRepository.findByDemandId(id));
    }

    @Override
    public AttachmentFileDTO getAttachmentFile(Long id) throws ServiceException {
        AttachmentFileDTO attachmentFileDTO = this.attachmentFileMapper.fromEntityToDTO(this.attachmentFileRepository.getOne(id));
        final String attachmentPath = new StringBuilder(applicationProperties.getStorage().getAttachments().getPath())
                .append(attachmentFileDTO.getDemandId()).append('/').append(id).toString();
        InputStream is = null;
        try {
            File file = new File(attachmentPath);
            is = new FileInputStream(file);
            byte[] buffer = new byte[attachmentFileDTO.getSize().intValue()];
            is.read(buffer);
            is.close();
            attachmentFileDTO.setContent(buffer);
            return attachmentFileDTO;
        } catch (FileNotFoundException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (IOException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public byte[] getDraftAttachmentFile(String token) throws ServiceException {
        final String temporaryPath = applicationProperties.getStorage().getAttachments().getTemporaryPath();
        try {
            FileInputStream fis = new FileInputStream(new StringBuilder(temporaryPath).append(token).toString());
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            return buffer;
        } catch (IOException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
