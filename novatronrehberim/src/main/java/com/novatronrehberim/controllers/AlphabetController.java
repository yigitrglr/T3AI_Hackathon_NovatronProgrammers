package com.novatronrehberim.controllers;

import java.io.File;
import java.io.IOException;

import com.novatronrehberim.services.AIService;
import com.novatronrehberim.services.TextToSpeech;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AlphabetController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/alphabet.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
        AIService ai = new AIService();
        TextToSpeech voice = new TextToSpeech();

        try {
            String letter = request.getParameter("letter");

            if (letter == null || letter.isEmpty()) {
                throw new Exception("No letter provided");
            }

            String aiResponse = ai.getChatbotResponse(letter, "user", "alfabe");

            request.setAttribute("aiResponse", aiResponse);

             String resourcesDir = request.getServletContext().getRealPath("/resources/audio");

            // Ensure the directory exists (create if necessary)
            File audioDir = new File(resourcesDir);
            if (!audioDir.exists()) {
                audioDir.mkdirs();  // Create the directory if it doesn't exist
            }

            // Set the path to the output file
            String outputFilePath = resourcesDir + "/output.mpeg";
            
            // Delete the existing file if it exists
            File outputFile = new File(outputFilePath);
            if (outputFile.exists()) {
                outputFile.delete();  // Delete the old file
            }

            // Call the method to generate the audio and save it
            voice.generateSpeech(aiResponse, outputFilePath);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/alphabet.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/error.jsp");
            dispatcher.forward(request, response);
        }
    }
}

