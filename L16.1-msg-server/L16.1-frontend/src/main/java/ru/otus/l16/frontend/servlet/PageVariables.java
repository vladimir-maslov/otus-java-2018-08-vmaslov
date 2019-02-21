package ru.otus.l16.frontend.servlet;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class PageVariables {
    private static final String DEFAULT_USER_NAME = "UNKNOWN";

    private static final String METHOD_VARIABLE_NAME = "method";
    private static final String URL_VARIABLE_NAME = "URL";
    private static final String LOCALE_VARIABLE_NAME = "locale";
    private static final String SESSIONID_VARIABLE_NAME = "sessionId";
    private static final String PARAMETERS_VARIABLE_NAME = "parameters";
    private static final String LOGIN_VARIABLE_NAME = "login";

    public static Map<String, Object> create(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();

        pageVariables.put(METHOD_VARIABLE_NAME, request.getMethod());
        pageVariables.put(URL_VARIABLE_NAME, request.getRequestURL().toString());
        pageVariables.put(LOCALE_VARIABLE_NAME, request.getLocale());
        pageVariables.put(SESSIONID_VARIABLE_NAME, request.getSession().getId());
        pageVariables.put(PARAMETERS_VARIABLE_NAME, request.getParameterMap().toString());

        // let's get login from session
        String login = (String) request.getSession().getAttribute(LoginServlet.LOGIN_PARAMETER_NAME);
        pageVariables.put(LOGIN_VARIABLE_NAME, login != null ? login : DEFAULT_USER_NAME);

        return pageVariables;
    }
}
