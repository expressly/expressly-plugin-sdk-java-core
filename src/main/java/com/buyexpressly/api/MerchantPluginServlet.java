package com.buyexpressly.api;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class MerchantPluginServlet extends HttpServlet {
    private MerchantServiceRouter router;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        router = (MerchantServiceRouter) Objects.requireNonNull(config.getServletContext().getAttribute(
                MerchantServiceRouter.class.getName()),
                String.format(
                        "Instance of [%s] not found under ServletContext attribute [%s]",
                        MerchantServiceRouter.class.getCanonicalName(),
                        MerchantServiceRouter.class.getName()));
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        router.route(req, resp);
    }
}
