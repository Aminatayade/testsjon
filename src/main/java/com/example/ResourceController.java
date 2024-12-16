package com.example;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Path("/")
public class ResourceController {

    // Charger un fichier en fonction du nom (GET)
    @GET
    @Path("/{fileName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFileContent(@PathParam("fileName") String fileName) {
        try {
            String content = readJsonFile(fileName + ".json");
            return Response.ok(content, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    // Mettre à jour un fichier (PUT)
    @PUT
    @Path("/{fileName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateFileContent(@PathParam("fileName") String fileName, String newContent) {
        try {
            writeJsonFile(fileName + ".json", newContent);
            return Response.ok("{\"message\": \"Contenu mis à jour avec succès\"}", MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    // Écrire dans un fichier existant ou nouveau (POST)
    @POST
    @Path("/{fileName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createFileContent(@PathParam("fileName") String fileName, String content) {
        try {
            writeJsonFile(fileName + ".json", content);
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Contenu ajouté avec succès\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    // Supprimer un fichier (DELETE)
    @DELETE
    @Path("/{fileName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFile(@PathParam("fileName") String fileName) {
        File file = new File(getFilePath(fileName + ".json"));
        if (file.exists() && file.delete()) {
            return Response.ok("{\"message\": \"Fichier supprimé avec succès\"}", MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Fichier non trouvé ou impossible à supprimer\"}")
                    .build();
        }
    }

    // Méthodes utilitaires pour lire et écrire des fichiers
    private String readJsonFile(String fileName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new FileNotFoundException("Fichier " + fileName + " introuvable !");
            }
            Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8).useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void writeJsonFile(String fileName, String content) {
        try {
            File file = new File(getFilePath(fileName));
            try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
                writer.write(content);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'écriture dans le fichier " + fileName + ": " + e.getMessage());
        }
    }

    private String getFilePath(String fileName) {
        // Obtenir le chemin du fichier dans les ressources du projet
        return getClass().getClassLoader().getResource("").getPath() + fileName;
    }
}