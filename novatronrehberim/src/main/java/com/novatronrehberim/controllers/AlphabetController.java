package com.novatronrehberim.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

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
            // Get the letter from the form data using getParameter
            String letter = request.getParameter("letter");

            if (letter == null || letter.isEmpty()) {
                throw new Exception("No letter provided");
            }

            // Get AI response based on the letter
            String aiResponse = ai.getChatbotResponse(letter, "user", "alfabe");

            // Set AI response as an attribute for JSP
            request.setAttribute("aiResponse", aiResponse);

            // Generate speech from the AI response
            voice.generate(aiResponse);

            // Check if the audio file was successfully created

            // Forward the request and response to the JSP page
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/alphabet.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            // Optionally, send an error message to the JSP
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/error.jsp");
            dispatcher.forward(request, response);
        }
    }
}

