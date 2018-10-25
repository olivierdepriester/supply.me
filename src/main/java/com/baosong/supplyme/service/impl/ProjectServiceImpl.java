package com.baosong.supplyme.service.impl;

import com.baosong.supplyme.service.ProjectService;
import com.baosong.supplyme.service.UserService;
import com.baosong.supplyme.domain.Project;
import com.baosong.supplyme.domain.errors.ForbiddenAccessException;
import com.baosong.supplyme.domain.errors.ServiceException;
import com.baosong.supplyme.repository.ProjectRepository;
import com.baosong.supplyme.repository.search.ProjectSearchRepository;
import com.baosong.supplyme.security.AuthoritiesConstants;
import com.baosong.supplyme.security.SecurityUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Project.
 */
@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final static String ENTITY_NAME = "project";

    private final ProjectRepository projectRepository;

    private final ProjectSearchRepository projectSearchRepository;

    @Autowired
    private UserService userService;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectSearchRepository projectSearchRepository) {
        this.projectRepository = projectRepository;
        this.projectSearchRepository = projectSearchRepository;
    }

    /**
     * Save a project.
     *
     * @param project the entity to save
     * @return the persisted entity
     * @throws ServiceException
     */
    @Override
    public Project save(Project project) throws ServiceException {
        log.debug("Request to save Project : {}", project);
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.PROJECT_MANAGER)) {
            throw new ForbiddenAccessException(ENTITY_NAME, project.getId());
        }
        if (project.getId() == null) {
            project.setCreationDate(Instant.now());
            project.setCreationUser(userService.getCurrentUser().get());
        }
        Project result = projectRepository.save(project);
        projectSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the projects.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Project> findAll() {
        log.debug("Request to get all Projects");
        return projectRepository.findAll();
    }


    /**
     * Get one project by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Project> findOne(Long id) {
        log.debug("Request to get Project : {}", id);
        return projectRepository.findById(id);
    }

    /**
     * Delete the project by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) throws ServiceException {
        log.debug("Request to delete Project : {}", id);
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.PROJECT_MANAGER)) {
            throw new ForbiddenAccessException(ENTITY_NAME, id);
        }
        projectRepository.deleteById(id);
        projectSearchRepository.deleteById(id);
    }

    /**
     * Search for the project corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Project> search(String query) {
        log.debug("Request to search Projects for query {}", query);
        return StreamSupport
            .stream(projectSearchRepository.search(
                queryStringQuery(
                    query.endsWith("*") ? query.toLowerCase() : new StringBuilder(query.toLowerCase()).append("*").toString()
                    )).spliterator(),
                false)
            .collect(Collectors.toList());
    }
}
