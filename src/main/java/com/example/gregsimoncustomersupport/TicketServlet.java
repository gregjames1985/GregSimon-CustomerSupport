package com.example.gregsimoncustomersupport;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet(name = "ticket", value="/ticket")
public class TicketServlet extends HttpServlet {
    private volatile int Ticket_ID = 1;
    private Map<Integer, Ticket> TicketDB = new LinkedHashMap<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/html");

            String action = request.getParameter("action");

            if(action == null){
                action = "list";
            }
            switch(action){
                case "createTicket" -> showTicketForm(request,response);
                case "viewTicket" -> viewTicket(request,response);
                case "download" -> downloadAttachment(request,response);
                default -> listTickets(request,response);

            }
    }

    private void listTickets(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        PrintWriter out = response.getWriter();

        out.println("<html><body><h2>BlogPost</h2>");
        out.println("<a href=\"ticket?action=createTicket\">Create Ticket</a><br><br>");

        //list out tickets
        if(TicketDB.size() == 0){
            out.println("there are no Tickets");
        }
        else{
            for(int id : TicketDB.keySet()){
                Ticket ticket = TicketDB.get(id);
                out.println("Ticket #" + id);
                out.println(": <a href=\"ticket?action=view&ticketID=" + id + "\">");
                out.println("Ticket"+ id +"</a><br>");
            }
        }
        out.println("</body></html>");


    }

    private void downloadAttachment(HttpServletRequest request, HttpServletResponse response) {
    }

    private void viewTicket(HttpServletRequest request, HttpServletResponse response) {
    }

    private void showTicketForm(HttpServletRequest request, HttpServletResponse response) {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        String action = request.getParameter("action");

        if(action == null){
            action = "list";
        }
        switch(action){
            case "create" -> createTicket(request,response);
            default -> response.sendRedirect("ticket");

        }
    }

    private void createTicket(HttpServletRequest request, HttpServletResponse response) {
    }
}
