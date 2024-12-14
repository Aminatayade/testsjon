package com.example;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Path("/json")
public class ExampleResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON) // Retourne JSON au client
    public String getJsonFromFile() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("example.json")) {
            if (inputStream == null) {
                throw new RuntimeException("Fichier JSON non trouvé !");
            }
            // Lisons le contenu du fichier JSON et retournons-le sous forme de String
            Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8).useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        } catch (Exception e) {
            // En cas d'erreur, retournez un JSON générique
            return "{\"error\": \"Impossible de lire le fichier JSON\"}";
        }
    }
}