package com.example.gregsimoncustomersupport;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet(name = "ticket", value="/ticket")
@MultipartConfig(fileSizeThreshold = 5_242_880, maxFileSize = 20971520L, maxRequestSize = 41_943_040L)
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
                case "view" -> viewTicket(request,response);
                case "download" -> downloadAttachment(request,response);
                default -> listTickets(request,response);

            }
    }

    private void listTickets(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        PrintWriter out = response.getWriter();

        out.println("<html><body><h2>Tickets</h2>");
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

    private void downloadAttachment(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String idString = request.getParameter("ticketId");
        Ticket ticket = getTicket(idString, response);

        String name = request.getParameter("attachment");
        if(name == null){
            response.sendRedirect("ticket?action=view&ticketID="+ idString);
        }

        HashMap<String, Attachment> attachment = ticket.getAttachments();
        if(attachment == null){
            response.sendRedirect("ticket?action=view&ticketId=" + idString);
            return;
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + Attachment.getName());
        response.setContentType("application/octet-stream");

        ServletOutputStream out = response.getOutputStream();
        out.write(Attachment.getContents());
    }

    private Ticket getTicket(String idString, HttpServletResponse response)throws ServletException, IOException{
        if(idString == null || idString.length() ==0){
            response.sendRedirect("ticket");
            return null;
        }
        try{
            int id = Integer.parseInt(idString);
            Ticket ticket = TicketDB.get(id);
            if(ticket == null){
                response.sendRedirect("ticket");
                return null;
            }
        }
        catch(Exception e){
            response.sendRedirect("ticket");
            return null;
        }
        return null;
    }

    private void viewTicket(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
            String idString = request.getParameter("ticketId");


                Ticket ticket = getTicket(idString,response);

                PrintWriter out = response.getWriter();
                out.println("<html><body><h2>Ticket Created</h2>");
                out.println("<h3>" + ticket.getSubject() + "<h3>");
                out.println("<p>Name:"+ticket.getCustomerName() + "</p>");
                out.println("<p>Body:"+ticket.getBodyOfTicket() + "</p>");

                if(ticket.hasAttachment()){
                    out.println("<a href=\"ticket?action=download&ticketId=" + idString + "&image=\">image</a><br/><br/>");
                }
                out.println("<a href=\"ticket\">Return to ticket list</a>");
                out.println("</body></html>");
    }

    private void showTicketForm(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.println("<form method=\"POST\" action=\"ticket\" enctype=\"multipart/form-data\" class=\"form-horizontal\">");
                out.println("<fieldset>");
                out.println("<input type=\"hidden\" name=\"action\" value=\"create\">");


        out.println("<legend>Form Name</legend>");


                out.println("<div class=\"form-group\">");
                out.println("<label class=\"col-md-4 control-label\" for=\"textinput\">Customer Name</label>");
                out.println("<div class=\"col-md-4\">");
                out.println("<input id=\"textinput\" name=\"customer\" type=\"text\" placeholder=\"placeholder\" class=\"form-control input-md\">");
                out.println("<span class=\"help-block\">help</span>");
                out.println("</div>");
                out.println("</div>");

                out.println("<div class=\"form-group\">");
                out.println("<label class=\"col-md-4 control-label\" for=\"textinput\">Subject</label>");
                out.println("<div class=\"col-md-4\">");
                out.println("<input id=\"textinput\" name=\"subject\" type=\"text\" placeholder=\"placeholder\" class=\"form-control input-md\">");
                out.println("<span class=\"help-block\">help</span>");
                out.println("</div>");
                out.println("</div>");


                out.println("<div class=\"form-group\">");
                out.println( "<label class=\"col-md-4 control-label\" for=\"textarea\">Body</label>");
                out.println("<div class=\"col-md-4\">");
                out.println( "<textarea class=\"form-control\" id=\"textarea\" name=\"body\">default text</textarea>");
                out.println("</div>");
                out.println("</div>");


                out.println("<div class=\"form-group\">");
                out.println("<label class=\"col-md-4 control-label\" for=\"filebutton\">Attachment</label>");
                out.println( "<div class=\"col-md-4\">");
                out.println("<input id=\"filebutton\" name=\"attachment\" class=\"input-file\" type=\"file\">");
                out.println("</div>");
                out.println("</div>");

                out.println("<div class=\"form-group\">");
                out.println("<label class=\"col-md-4 control-label\" for=\"singlebutton\"></label>");
                out.println("<div class=\"col-md-4\">");
                out.println("<button id=\"singlebutton\" name=\"singlebutton\" class=\"btn btn-primary\">Submit</button>");
                out.println("</div>");
                out.println("</div>");

                out.println("</fieldset>");
                out.println("</form>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        String action = request.getParameter("action");

        if(action == null){
            action = "list";
        }
        switch(action){
            case "create" -> createTickets(request,response);
            default -> response.sendRedirect("ticket");

        }
    }

    private void createTickets(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        Ticket ticket = new Ticket();
        ticket.setCustomerName(request.getParameter("customer"));
        ticket.setSubject(request.getParameter("subject"));
        ticket.setBodyOfTicket("body");


        Part file = request.getPart("attachment");
        if(file != null && file.getSize() > 0)
        {
            Attachment attachment = this.processAttachment(file);
            if(attachment != null)
                ticket.addAttachment(attachment);
        }
        int id;
        synchronized(this){
            id = this.Ticket_ID++;
            TicketDB.put(id, ticket);
        }
        response.sendRedirect("ticket?action=view&ticketId=" + id);

    }

    private Attachment processAttachment(Part file) throws IOException {
        InputStream in = file.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int read;
        final byte[] bytes = new byte[1024];
        while ((read = in.read(bytes)) != -1){
            out.write(bytes,0,read);
        }

        Attachment attachment = new Attachment();
        attachment.setName(file.getSubmittedFileName());
        attachment.setContents(out.toByteArray());
        return attachment;
    }
}
