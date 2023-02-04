package com.qsspy.watmerchbackend.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qsspy.watmerchbackend.entity.Category;
import com.qsspy.watmerchbackend.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    @Qualifier("authUserDetailsService")
    private UserDetailsService userDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldGetAllCategories() throws Exception {
        //given
        Category category1 = new Category("cat1");
        category1.setId(1);
        Category category2 = new Category("cat2");
        category2.setId(2);
        List<Category> categories = List.of(category1, category2);

        given(productService.getCategories()).willReturn(categories);
        //when
        ResultActions result = this.mockMvc.perform(get("/api/categories"));
        //then
        result.andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(categories)));
    }

    @Test
    @WithMockUser(value = "employee", roles = "EMPLOYEE")
    void postCategory() throws Exception {
        //given
        Category category = new Category("cat1");
        category.setId(1);

        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(category));

        given(productService.postCategory(category)).willReturn(category);

        //when
        ResultActions result = this.mockMvc.perform(request);
        //then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(category)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getProducts() {
    }

    @Test
    void getProduct() {
    }

    @Test
    void postProduct() {
    }

    @Test
    void getRandomProducts() {
    }
}