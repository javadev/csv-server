package com.github.csvserver.web.rest;

import com.github.csvserver.domain.Csvfile;
import com.github.csvserver.repository.CsvfileRepository;
import com.github.csvserver.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.github.csvserver.domain.Csvfile}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CsvfileResource {

    private final Logger log = LoggerFactory.getLogger(CsvfileResource.class);

    private static final String ENTITY_NAME = "csvfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CsvfileRepository csvfileRepository;

    public CsvfileResource(CsvfileRepository csvfileRepository) {
        this.csvfileRepository = csvfileRepository;
    }

    /**
     * {@code POST  /csvfiles} : Create a new csvfile.
     *
     * @param csvfile the csvfile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new csvfile, or with status {@code 400 (Bad Request)} if the csvfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/csvfiles")
    public ResponseEntity<Csvfile> createCsvfile(@Valid @RequestBody Csvfile csvfile) throws URISyntaxException {
        log.debug("REST request to save Csvfile : {}", csvfile);
        if (csvfile.getId() != null) {
            throw new BadRequestAlertException("A new csvfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Csvfile result = csvfileRepository.save(csvfile);
        return ResponseEntity.created(new URI("/api/csvfiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /csvfiles} : Updates an existing csvfile.
     *
     * @param csvfile the csvfile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated csvfile,
     * or with status {@code 400 (Bad Request)} if the csvfile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the csvfile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/csvfiles")
    public ResponseEntity<Csvfile> updateCsvfile(@Valid @RequestBody Csvfile csvfile) throws URISyntaxException {
        log.debug("REST request to update Csvfile : {}", csvfile);
        if (csvfile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Csvfile result = csvfileRepository.save(csvfile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, csvfile.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /csvfiles} : get all the csvfiles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of csvfiles in body.
     */
    @GetMapping("/csvfiles")
    public ResponseEntity<List<Csvfile>> getAllCsvfiles(Pageable pageable) {
        log.debug("REST request to get a page of Csvfiles");
        Page<Csvfile> page = csvfileRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /csvfiles/:id} : get the "id" csvfile.
     *
     * @param id the id of the csvfile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the csvfile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/csvfiles/{id}")
    public ResponseEntity<Csvfile> getCsvfile(@PathVariable Long id) {
        log.debug("REST request to get Csvfile : {}", id);
        Optional<Csvfile> csvfile = csvfileRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(csvfile);
    }

    /**
     * {@code DELETE  /csvfiles/:id} : delete the "id" csvfile.
     *
     * @param id the id of the csvfile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/csvfiles/{id}")
    public ResponseEntity<Void> deleteCsvfile(@PathVariable Long id) {
        log.debug("REST request to delete Csvfile : {}", id);
        csvfileRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
