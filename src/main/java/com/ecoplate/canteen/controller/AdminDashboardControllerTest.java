package com.ecoplate.controller;

import com.ecoplate.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminDashboardController.class)
public class AdminDashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @MockBean
    private HttpSession session;

    @Test
    public void testDashboardAccessWithAdminSession() throws Exception {
        Mockito.when(session.getAttribute("role")).thenReturn("ADMIN");

        Map<String, Object> stats = Map.of(
                "totalOrders", 50,
                "cancelledOrders", 10,
                "completedOrders", 35
        );

        Mockito.when(adminService.getDashboardStats()).thenReturn(stats);

        mockMvc.perform(get("/admin/dashboard")
                        .sessionAttr("role", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalOrders").value(50))
                .andExpect(jsonPath("$.cancelledOrders").value(10))
                .andExpect(jsonPath("$.completedOrders").value(35));
    }

    @Test
    public void testDashboardAccessWithoutAdminSession() throws Exception {
        mockMvc.perform(get("/admin/dashboard")
                        .sessionAttr("role", "USER")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Access denied"));
    }
}
