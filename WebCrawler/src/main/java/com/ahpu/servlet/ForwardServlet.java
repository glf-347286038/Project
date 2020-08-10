package com.ahpu.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ForwardServlet", urlPatterns = "/login")
public class ForwardServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置响应编码格式
        resp.setContentType("text/html;charset=utf-8");
        req.setCharacterEncoding("utf-8");
        String action = req.getParameter("action");
        System.out.println("action为：" + action);
        //处理请求
        if (action == null || ("init").equals(action)) {
            req.getRequestDispatcher("/page/index.jsp").forward(req, resp);
            return;
        }

        switch (action) {
            case "find":
                req.getRequestDispatcher("page/SearchNew.jsp").forward(req, resp);
                System.out.println("跳转到搜寻页面");
                break;
        }

    }
}
