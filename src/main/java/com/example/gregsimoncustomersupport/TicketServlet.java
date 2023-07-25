package com.example.gregsimoncustomersupport;

import jakarta.servlet.ServletException;
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

    private void showTicketForm(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.println("<form method=\"POST\" action=\"ticket\" enctype=\"multipart/form-data\" class=\"form-horizontal\">");
                out.println("<fieldset>");


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
            case "create" -> createTicket(request,response);
            default -> response.sendRedirect("ticket");

        }
    }

    private void createTicket(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        Ticket ticket = new Ticket();
        ticket.setCustomerName(request.getParameter("customer"));
        ticket.setSubject(request.getParameter("subject"));
        ticket.setBodyOfTicket("body");


        Part file = request.getPart("attachment");
        if (file != null){
            HashMap<String, String> attachment = this.processAttachment(file);
                if(attachment != null){
                    ticket.setAttachments(Ticket_ID,attachment);
                }
        }
        int id;
        synchronized(this){
            id = this.Ticket_ID++;
            TicketDB.put(id, ticket);
        }
        response.sendRedirect("blog?action=view&ticketId=" + id);

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
