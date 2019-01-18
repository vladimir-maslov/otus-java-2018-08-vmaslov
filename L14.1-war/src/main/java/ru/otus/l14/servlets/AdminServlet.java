package ru.otus.l14.servlets;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class AdminServlet extends HttpServlet {

    private static final String ADMIN_PAGE_TEMPLATE = "admin.html";

    private static final String CONTENT_TYPE = "text/html;charset=utf-8";

    private TemplateProcessor templateProcessor;

    public void init(){
        ApplicationContext context = new ClassPathXmlApplicationContext("SpringBeans.xml");
        templateProcessor = context.getBean("templateProcessor", TemplateProcessor.class);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = PageVariables.create(request);

        response.setContentType(CONTENT_TYPE);
        String page = templateProcessor.getPage(ADMIN_PAGE_TEMPLATE, pageVariables);
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
