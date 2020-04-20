package com.dhallatmaine.artistcatalog.bands.load;

import com.dhallatmaine.artistcatalog.bands.Band;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BandDataService {

    public List<Band> loadBands() {
        List<String> bands = new ArrayList<>();
        try (InputStream inputStream = new ClassPathResource("data/bands.txt").getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            try (BufferedReader br = new BufferedReader(inputStreamReader)) {
                bands = br.lines().collect(Collectors.toList());
            } catch (IOException e) {
                log.error("Exception caught while parsing data/bands.txt", e);
            }
        } catch (IOException e) {
            log.error("Exception caught while loading data/bands.txt", e);
        }
        return bands.stream()
                .map(band -> new Band()
                        .setId(UUID.randomUUID().toString())
                        .setName(band))
                .collect(Collectors.toList());
    }

}
