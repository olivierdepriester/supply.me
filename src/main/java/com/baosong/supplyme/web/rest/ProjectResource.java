package com.baosong.supplyme.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.baosong.supplyme.domain.Project;
import com.baosong.supplyme.domain.errors.ParameterizedServiceException;
import com.baosong.supplyme.domain.errors.ServiceException;
import com.baosong.supplyme.service.ProjectService;
import com.baosong.supplyme.web.rest.errors.BadRequestAlertException;
import com.baosong.supplyme.web.rest.errors.BadRequestServiceException;
import com.baosong.supplyme.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Project.
 */
@RestController
@RequestMapping("/api")
public class ProjectResource {

    private final Logger log = LoggerFactory.getLogger(ProjectResource.class);

    private static final String ENTITY_NAME = "project";

    private final ProjectService projectService;

    public ProjectResource(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * POST /projects : Create a new project.
     *
     * @param project the project to create
     * @return the ResponseEntity with status 201 (Created) and with body the new
     *         project, or with status 400 (Bad Request) if the project has already
     *         an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/projects")
    @Timed
    public ResponseEntity<Project> createProject(@Valid @RequestBody Project project) throws URISyntaxException {
        log.debug("REST request to save Project : {}", project);
        if (project.getId() != null) {
            throw new BadRequestAlertException("A new project cannot already have an ID", ENTITY_NAME, "idexists");
        }
        try {
            Project result = projectService.save(project);
            return ResponseEntity.created(new URI("/api/projects/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
        } catch (ParameterizedServiceException e) {
            throw new BadRequestServiceException(e, ENTITY_NAME);
        } catch (ServiceException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "create");
        }
    }

    /**
     * PUT /projects : Updates an existing project.
     *
     * @param project the project to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     *         project, or with status 400 (Bad Request) if the project is not
     *         valid, or with status 500 (Internal Server Error) if the project
     *         couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/projects")
    @Timed
    public ResponseEntity<Project> updateProject(@Valid @RequestBody Project project) throws URISyntaxException {
        log.debug("REST request to update Project : {}", project);
        if (project.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Project result;
        try {
            result = projectService.save(project);
            return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, project.getId().toString())).body(result);
        } catch (ParameterizedServiceException e) {
            throw new BadRequestServiceException(e, ENTITY_NAME);
        } catch (ServiceException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "update");
        }
    }

    /**
     * GET /projects : get all the projects.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of projects in
     *         body
     */
    @GetMapping("/projects")
    @Timed
    public List<Project> getAllProjects() {
        log.debug("REST request to get all Projects");
        return projectService.findAll();
    }

    /**
     * GET /projects/:id : get the "id" project.
     *
     * @param id the id of the project to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the project, or
     *         with status 404 (Not Found)
     */
    @GetMapping("/projects/{id}")
    @Timed
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        log.debug("REST request to get Project : {}", id);
        Optional<Project> project = projectService.findOne(id);
        return ResponseUtil.wrapOrNotFound(project);
    }

    /**
     * DELETE /projects/:id : delete the "id" project.
     *
     * @param id the id of the project to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/projects/{id}")
    @Timed
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        log.debug("REST request to delete Project : {}", id);
        try {
            projectService.delete(id);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString()))
                    .build();
        } catch (ParameterizedServiceException e) {
            throw new BadRequestServiceException(e, ENTITY_NAME);
        } catch (ServiceException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "delete");
        }
    }

    /**
     * SEARCH /_search/projects?query=:query : search for the project corresponding
     * to the query.
     *
     * @param query the query of the project search
     * @return the result of the search
     */
    @GetMapping("/_search/projects")
    @Timed
    public List<Project> searchProjects(@RequestParam String query) {
        log.debug("REST request to search Projects for query {}", query);
        return projectService.search(query);
    }

}
