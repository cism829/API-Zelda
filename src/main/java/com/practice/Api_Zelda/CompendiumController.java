package com.practice.Api_Zelda;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.ui.Model;


@Controller
public class CompendiumController {

    @GetMapping("/home")
    public String homePage() {
        return "test";
    }

    @GetMapping("/compendium/entry")
    public String getEntry(@RequestParam String name, Model model) {
        try {
            String url = "https://botw-compendium.herokuapp.com/api/v2/entry/" + name;
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();

            String jsonResponse = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(jsonResponse);

            // Check if the response contains an entry
            if (root.has("data") && !root.get("data").isNull()) {
                JsonNode entryData = root.get("data");
// Extract specific fields
                String entryName = entryData.get("name").asText();
                String entryDescription = entryData.get("description").asText();
                String image = entryData.get("image").asText();

                // Create an entry object or use a map for simplicity
                model.addAttribute("entry", entryData);
                model.addAttribute("entryName", entryName);
                model.addAttribute("entryDescription", entryDescription);
                model.addAttribute("image", image);

                return "test";  // Return the name of the Thymeleaf template (entry.html)
            } else {
                model.addAttribute("message", "No entry found");
                return "error";  // Return an error view
            }
        } catch (Exception ex) {
            Logger.getLogger(CompendiumController.class.getName()).log(Level.SEVERE, null, ex);
            model.addAttribute("message", "Error retrieving entry from Hyrule Compendium API");
            return "error";  // Return an error view
        }
    }
}

