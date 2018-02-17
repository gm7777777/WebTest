package hello.controller;

import hello.model.Customer;
import hello.service.CustomerSerivce;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/customer")
public class CustomerServlet extends HttpServlet {

    private CustomerSerivce cusService;

    @Override
    public void init() throws ServletException{
        cusService = new CustomerSerivce();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Customer> customerList = cusService.getCustomers1();
        req.setAttribute("customerList",customerList);
        req.getRequestDispatcher("/pages/customer.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}


