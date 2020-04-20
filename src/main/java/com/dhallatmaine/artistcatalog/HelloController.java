package com.dhallatmaine.artistcatalog;

import com.dhallatmaine.artistcatalog.bands.fetch.BandFetcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {

	@Autowired
	private BandFetcherService bandFetcherService;

	@RequestMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

}
