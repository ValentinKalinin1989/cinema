package servlets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import database.InteractionWithPostgresDb;
import model.Hall;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class HallServlet extends HttpServlet {
    private final InteractionWithPostgresDb database = InteractionWithPostgresDb.getInstance();
    private static final Logger LOG = LogManager.getLogger(HttpServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Hall hall = database.getHall();
        String listToJson = objectMapper.writeValueAsString(hall);
        PrintWriter writer = response.getWriter();
        writer.println(listToJson);
        writer.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader reader = request.getReader();
        StringBuilder stringBuilder = new StringBuilder();
        reader.lines().forEach(stringBuilder::append);
        LOG.info(stringBuilder.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(stringBuilder.toString());
        int row = jsonNode.get("row").asInt();
        int column = jsonNode.get("column").asInt();
        String userName = jsonNode.get("username").asText();
        String phone = jsonNode.get("phone").asText();
        LOG.info("row " + row + "   column " + column + "   username " + userName + "   phone " + phone);
        ObjectNode responseNode = objectMapper.createObjectNode();
        if (database.buyPlaceAndAddBuyerToDB(row, column, userName, phone)) {
            responseNode.put("success", true).put("row", row).put("column", column);
        } else {
            responseNode.put("success", false);
        }
        PrintWriter writer = response.getWriter();
        writer.append(objectMapper.writeValueAsString(responseNode));
        writer.flush();
    }
}
