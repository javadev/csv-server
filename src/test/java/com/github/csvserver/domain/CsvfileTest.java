package com.github.csvserver.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.github.csvserver.web.rest.TestUtil;

public class CsvfileTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Csvfile.class);
        Csvfile csvfile1 = new Csvfile();
        csvfile1.setId(1L);
        Csvfile csvfile2 = new Csvfile();
        csvfile2.setId(csvfile1.getId());
        assertThat(csvfile1).isEqualTo(csvfile2);
        csvfile2.setId(2L);
        assertThat(csvfile1).isNotEqualTo(csvfile2);
        csvfile1.setId(null);
        assertThat(csvfile1).isNotEqualTo(csvfile2);
    }
}
