<%-- 
    Document   : index
    Created on : 29 de out. de 2024, 11:29:10
    Author     : rlarg
--%>

<%@page import="ai.DevOpenAI"%>
<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%
    if(request.getParameter("invoke")!=null){
        try{
            String prompt = request.getParameter("prompt");
            String completion = DevOpenAI.getCompletion(prompt);
            request.setAttribute("completion", completion);
        }catch(Exception ex){
            request.setAttribute("error", ex.getMessage());
        }
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>My ChatGPT</title>
    </head>
    <body>
        <h1>ChatGPT</h1>
        
        <% if(request.getAttribute("error") != null) { %>
        <div>ERRO: <%= request.getAttribute("error") %></div>
        <hr/>
        <% } else if(request.getAttribute("completion") != null) { %>
        <h2>Prompt</h2>
        <div><%= request.getParameter("prompt") %></div>
        <h2>Completion</h2>
        <div><%= request.getAttribute("completion") %></div>
        <hr/>
        <% } %>
        
        Novo Prompt:<br/>
        <form>
            <input type="text" name="prompt" size="100"/>
            <input type="submit" name="invoke" value="Enviar"/>
        </form>
    </body>
</html>
