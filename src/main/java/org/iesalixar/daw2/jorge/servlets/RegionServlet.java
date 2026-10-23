package org.iesalixar.daw2.jorge.servlets;

import org.iesalixar.daw2.jorge.dao.RegionDAO;
import org.iesalixar.daw2.jorge.dao.RegionDAOImpl;
import org.iesalixar.daw2.jorge.entity.Region;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/regions")
public class RegionServlet extends HttpServlet {
    private RegionDAO regionDAO;

    @Override
    public void init() throws ServletException {
        try {
            regionDAO = new RegionDAOImpl();
        } catch (Exception e) {
            throw new ServletException("Error al inicializar el RegionDAO", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            if (action == null) {
                action = "list";
            }
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                default:
                    listRegions(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action) {
                case "insert":
                    insertRegion(request, response);
                    break;
                case "update":
                    updateRegion(request, response);
                    break;
                case "delete":
                    deleteRegion(request, response);
                    break;
                default:
                    listRegions(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listRegions(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        List<Region> listRegions = regionDAO.listAllRegions();
        request.setAttribute("listRegions", listRegions);
        request.getRequestDispatcher("region.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("region-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Region existingRegion = regionDAO.getRegionById(id);
        request.setAttribute("region", existingRegion);
        request.getRequestDispatcher("region-form.jsp").forward(request, response);
    }

    private void insertRegion(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        String code = request.getParameter("code").trim().toUpperCase();
        String name = request.getParameter("name").trim();
        if (code.isEmpty() || name.isEmpty()) {
            request.setAttribute("errorMessage", "El código y el nombre no pueden estar vacíos.");
            request.getRequestDispatcher("region-form.jsp").forward(request, response);
            return;
        }
        if (regionDAO.existsRegionByCode(code)) {
            request.setAttribute("errorMessage", "El código de la región ya existe.");
            request.getRequestDispatcher("region-form.jsp").forward(request, response);
            return;
        }
        Region newRegion = new Region(code, name);
        try {
            regionDAO.insertRegion(newRegion);
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                request.setAttribute("errorMessage", "El código de la región debe ser único.");
                request.getRequestDispatcher("region-form.jsp").forward(request, response);
            } else {
                throw e;
            }
        }
        response.sendRedirect("regions");
    }

    private void updateRegion(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        String code = request.getParameter("code").trim().toUpperCase();
        String name = request.getParameter("name").trim();
        if (code.isEmpty() || name.isEmpty()) {
            request.setAttribute("errorMessage", "El código y el nombre no pueden estar vacíos.");
            request.getRequestDispatcher("region-form.jsp").forward(request, response);
            return;
        }
        if (regionDAO.existsRegionByCodeAndNotId(code, id)) {
            request.setAttribute("errorMessage", "El código de la región ya existe para otra región.");
            request.getRequestDispatcher("region-form.jsp").forward(request, response);
            return;
        }
        Region updatedRegion = new Region(id, code, name);
        try {
            regionDAO.updateRegion(updatedRegion);
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                request.setAttribute("errorMessage", "El código de la región debe ser único.");
                request.getRequestDispatcher("region-form.jsp").forward(request, response);
            } else {
                throw e;
            }
        }
        response.sendRedirect("regions");
    }

    private void deleteRegion(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        regionDAO.deleteRegion(id);
        response.sendRedirect("regions");
    }
}