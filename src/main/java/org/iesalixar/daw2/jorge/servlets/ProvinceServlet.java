package org.iesalixar.daw2.jorge.servlets;

import org.iesalixar.daw2.jorge.dao.ProvinceDAO;
import org.iesalixar.daw2.jorge.dao.ProvinceDAOImpl;
import org.iesalixar.daw2.jorge.dao.RegionDAO;
import org.iesalixar.daw2.jorge.dao.RegionDAOImpl;
import org.iesalixar.daw2.jorge.entity.Province;
import org.iesalixar.daw2.jorge.entity.Region;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet que maneja el CRUD de Provincias.
 */
@WebServlet("/provinces")
public class ProvinceServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ProvinceServlet.class);
    private ProvinceDAO provinceDAO;
    private RegionDAO regionDAO;

    @Override
    public void init() throws ServletException {
        try {
            provinceDAO = new ProvinceDAOImpl();
            regionDAO = new RegionDAOImpl();
        } catch (Exception e) {
            logger.error("Error al inicializar los DAOs en ProvinceServlet", e);
            throw new ServletException("Error al inicializar los DAOs", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entrando en doGet de ProvinceServlet");
        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                default:
                    listProvinces(request, response);
                    break;
            }
        } catch (SQLException ex) {
            logger.error("Error SQL en doGet", ex);
            throw new ServletException(ex);
        }
        logger.info("Saliendo de doGet");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entrando en doPost de ProvinceServlet");
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "insert":
                    insertProvince(request, response);
                    break;
                case "update":
                    updateProvince(request, response);
                    break;
                case "delete":
                    deleteProvince(request, response);
                    break;
            }
        } catch (SQLException ex) {
            logger.error("Error SQL en doPost", ex);
            throw new ServletException(ex);
        }
        logger.info("Saliendo de doPost");
    }

    private void listProvinces(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        logger.info("Ejecutando listProvinces");
        List<Province> listProvinces = provinceDAO.listAllProvinces();
        request.setAttribute("listProvinces", listProvinces);
        request.getRequestDispatcher("province.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        logger.info("Mostrando formulario para nueva provincia");
        List<Region> listRegions = regionDAO.listAllRegions();
        request.setAttribute("listRegions", listRegions);
        request.getRequestDispatcher("province-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        logger.info("Mostrando formulario para editar provincia ID {}", id);
        Province existingProvince = provinceDAO.getProvinceById(id);
        List<Region> listRegions = regionDAO.listAllRegions();

        request.setAttribute("province", existingProvince);
        request.setAttribute("listRegions", listRegions);
        request.getRequestDispatcher("province-form.jsp").forward(request, response);
    }

    private void insertProvince(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        logger.info("Intentando insertar nueva provincia");
        String code = request.getParameter("code").trim();
        String name = request.getParameter("name").trim();
        String idRegionStr = request.getParameter("id_region");

        if (code.isEmpty() || name.isEmpty() || idRegionStr == null || idRegionStr.isEmpty()) {
            request.setAttribute("errorMessage", "Ningún campo puede estar vacío.");
            showNewForm(request, response);
            return;
        }

        if (provinceDAO.existsProvinceByCode(code)) {
            request.setAttribute("errorMessage", "El código de la provincia ya existe.");
            showNewForm(request, response);
            return;
        }

        int idRegion = Integer.parseInt(idRegionStr);
        Region region = regionDAO.getRegionById(idRegion);
        Province newProvince = new Province(code, name, region);

        provinceDAO.insertProvince(newProvince);
        response.sendRedirect("provinces");
    }

    private void updateProvince(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        logger.info("Intentando actualizar provincia ID {}", id);

        String code = request.getParameter("code").trim();
        String name = request.getParameter("name").trim();
        String idRegionStr = request.getParameter("id_region");

        if (code.isEmpty() || name.isEmpty() || idRegionStr == null || idRegionStr.isEmpty()) {
            request.setAttribute("errorMessage", "Ningún campo puede estar vacío.");
            showEditForm(request, response);
            return;
        }

        if (provinceDAO.existsProvinceByCodeAndNotId(code, id)) {
            request.setAttribute("errorMessage", "El código de la provincia ya pertenece a otra.");
            showEditForm(request, response);
            return;
        }

        int idRegion = Integer.parseInt(idRegionStr);
        Region region = regionDAO.getRegionById(idRegion);
        Province updatedProvince = new Province(id, code, name, region);

        provinceDAO.updateProvince(updatedProvince);
        response.sendRedirect("provinces");
    }

    private void deleteProvince(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        logger.info("Eliminando provincia ID {}", id);
        provinceDAO.deleteProvince(id);
        response.sendRedirect("provinces");
    }
}