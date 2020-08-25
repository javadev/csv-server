package com.github.csvserver.web.rest;

import com.github.csvserver.CsvserverApp;
import com.github.csvserver.domain.Csvfile;
import com.github.csvserver.repository.CsvfileRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CsvfileResource} REST controller.
 */
@SpringBootTest(classes = CsvserverApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class CsvfileResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_DATA = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private CsvfileRepository csvfileRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCsvfileMockMvc;

    private Csvfile csvfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Csvfile createEntity(EntityManager em) {
        Csvfile csvfile = new Csvfile()
            .name(DEFAULT_NAME)
            .data(DEFAULT_DATA)
            .date(DEFAULT_DATE);
        return csvfile;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Csvfile createUpdatedEntity(EntityManager em) {
        Csvfile csvfile = new Csvfile()
            .name(UPDATED_NAME)
            .data(UPDATED_DATA)
            .date(UPDATED_DATE);
        return csvfile;
    }

    @BeforeEach
    public void initTest() {
        csvfile = createEntity(em);
    }

    @Test
    @Transactional
    public void createCsvfile() throws Exception {
        int databaseSizeBeforeCreate = csvfileRepository.findAll().size();

        // Create the Csvfile
        restCsvfileMockMvc.perform(post("/api/csvfiles").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(csvfile)))
            .andExpect(status().isCreated());

        // Validate the Csvfile in the database
        List<Csvfile> csvfileList = csvfileRepository.findAll();
        assertThat(csvfileList).hasSize(databaseSizeBeforeCreate + 1);
        Csvfile testCsvfile = csvfileList.get(csvfileList.size() - 1);
        assertThat(testCsvfile.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCsvfile.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testCsvfile.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createCsvfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = csvfileRepository.findAll().size();

        // Create the Csvfile with an existing ID
        csvfile.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCsvfileMockMvc.perform(post("/api/csvfiles").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(csvfile)))
            .andExpect(status().isBadRequest());

        // Validate the Csvfile in the database
        List<Csvfile> csvfileList = csvfileRepository.findAll();
        assertThat(csvfileList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = csvfileRepository.findAll().size();
        // set the field null
        csvfile.setName(null);

        // Create the Csvfile, which fails.

        restCsvfileMockMvc.perform(post("/api/csvfiles").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(csvfile)))
            .andExpect(status().isBadRequest());

        List<Csvfile> csvfileList = csvfileRepository.findAll();
        assertThat(csvfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCsvfiles() throws Exception {
        // Initialize the database
        csvfileRepository.saveAndFlush(csvfile);

        // Get all the csvfileList
        restCsvfileMockMvc.perform(get("/api/csvfiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(csvfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getCsvfile() throws Exception {
        // Initialize the database
        csvfileRepository.saveAndFlush(csvfile);

        // Get the csvfile
        restCsvfileMockMvc.perform(get("/api/csvfiles/{id}", csvfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(csvfile.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCsvfile() throws Exception {
        // Get the csvfile
        restCsvfileMockMvc.perform(get("/api/csvfiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCsvfile() throws Exception {
        // Initialize the database
        csvfileRepository.saveAndFlush(csvfile);

        int databaseSizeBeforeUpdate = csvfileRepository.findAll().size();

        // Update the csvfile
        Csvfile updatedCsvfile = csvfileRepository.findById(csvfile.getId()).get();
        // Disconnect from session so that the updates on updatedCsvfile are not directly saved in db
        em.detach(updatedCsvfile);
        updatedCsvfile
            .name(UPDATED_NAME)
            .data(UPDATED_DATA)
            .date(UPDATED_DATE);

        restCsvfileMockMvc.perform(put("/api/csvfiles").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCsvfile)))
            .andExpect(status().isOk());

        // Validate the Csvfile in the database
        List<Csvfile> csvfileList = csvfileRepository.findAll();
        assertThat(csvfileList).hasSize(databaseSizeBeforeUpdate);
        Csvfile testCsvfile = csvfileList.get(csvfileList.size() - 1);
        assertThat(testCsvfile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCsvfile.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testCsvfile.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingCsvfile() throws Exception {
        int databaseSizeBeforeUpdate = csvfileRepository.findAll().size();

        // Create the Csvfile

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCsvfileMockMvc.perform(put("/api/csvfiles").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(csvfile)))
            .andExpect(status().isBadRequest());

        // Validate the Csvfile in the database
        List<Csvfile> csvfileList = csvfileRepository.findAll();
        assertThat(csvfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCsvfile() throws Exception {
        // Initialize the database
        csvfileRepository.saveAndFlush(csvfile);

        int databaseSizeBeforeDelete = csvfileRepository.findAll().size();

        // Delete the csvfile
        restCsvfileMockMvc.perform(delete("/api/csvfiles/{id}", csvfile.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Csvfile> csvfileList = csvfileRepository.findAll();
        assertThat(csvfileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
