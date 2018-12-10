package ru.otus.l12.servlets;

import ru.otus.l12.dataset.UserDataSet;
import ru.otus.l12.db.DBService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserServlet extends HttpServlet {

    private static final String DEFAULT_USER_NAME = "UNKNOWN";
    private static final String USER_PAGE_TEMPLATE = "user.html";

    private final TemplateProcessor templateProcessor;
    private final DBService dbService;

    private Long cachedUserCount = null;

    @SuppressWarnings("WeakerAccess")
    public UserServlet(TemplateProcessor templateProcessor, DBService dbService) throws IOException {
        this.templateProcessor = templateProcessor;
        this.dbService = dbService;
    }

    private static Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("method", request.getMethod());
        pageVariables.put("URL", request.getRequestURL().toString());
        pageVariables.put("locale", request.getLocale());
        pageVariables.put("sessionId", request.getSession().getId());
        pageVariables.put("parameters", request.getParameterMap().toString());

        // let's get login from session
        String login = (String) request.getSession().getAttribute(LoginServlet.LOGIN_PARAMETER_NAME);
        pageVariables.put("login", login != null ? login : DEFAULT_USER_NAME);

        return pageVariables;
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(request);

        if (request.getParameter("submit_add") != null) {
            String userName = request.getParameter("name");
            String userAge = request.getParameter("age");

            if (!userName.equals("") && !userAge.equals("")) {
                UserDataSet user = new UserDataSet(userName, Integer.parseInt(userAge));
                try {
                    dbService.save(user);
                } catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                pageVariables.put("isAddError", "Please fill the form");
            }
        }

        cachedUserCount = dbService.count();
        pageVariables.put("userCount", cachedUserCount);

        response.setContentType("text/html;charset=utf-8");
        String page = templateProcessor.getPage(USER_PAGE_TEMPLATE, pageVariables);
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = createPageVariablesMap(request);

        cachedUserCount = (cachedUserCount == null) ? dbService.count() : cachedUserCount;
        pageVariables.put("userCount", cachedUserCount);

        if (request.getParameter("submit_search") != null) {
            String userId = request.getParameter("id");
            if (!userId.equals("")) {
                try {
                    String userName = dbService.read(Long.parseLong(userId)).getName();
                    pageVariables.put("userName", userName);
                } catch (Exception e) {
                    e.printStackTrace();
                    pageVariables.put("userName", "User not found");
                }
            } else
                pageVariables.put("isSearchError", "No user ID specified");
        }

        response.setContentType("text/html;charset=utf-8");
        String page = templateProcessor.getPage(USER_PAGE_TEMPLATE, pageVariables);
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
