// package com.bank.publicinfo.controller;
// 
// import com.bank.publicinfo.dto.AtmDto;
// import com.bank.publicinfo.exception.GlobalExceptionHandler;
// import com.bank.publicinfo.service.AtmService;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.context.annotation.Import;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// 
// import java.util.Arrays;
// import java.util.List;
// 
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.eq;
// import static org.mockito.Mockito.doNothing;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
// 
// @WebMvcTest(AtmController.class)
// @Import(GlobalExceptionHandler.class)
// class AtmControllerTest {
// 
//     @Autowired
//     private MockMvc mockMvc;
// 
//     @MockBean
//     private AtmService atmService;
// 
//     @Autowired
//     private ObjectMapper objectMapper;
// 
//     private AtmDto atmDto;
// 
//     @BeforeEach
//     void setUp() {
//         atmDto = AtmDto.builder()
//                 .id(1L)
//                 .address("Test Address")
//                 .atmName("ATM-001")
//                 .allDay(true)
//                 .bankDetailsId(1L)
//                 .build();
//     }
// 
//     @Test
//     void getAll_ShouldReturnAllAtms() throws Exception {
//         List<AtmDto> atms = Arrays.asList(atmDto);
//         when(atmService.findAll()).thenReturn(atms);
// 
//         mockMvc.perform(get("/atms"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[0].atmName").value("ATM-001"));
//     }
// 
//     @Test
//     void getById_ShouldReturnAtm() throws Exception {
//         when(atmService.findById(1L)).thenReturn(atmDto);
// 
//         mockMvc.perform(get("/atms/1"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.atmName").value("ATM-001"));
//     }
// 
//     @Test
//     void create_ShouldCreateAtm() throws Exception {
//         when(atmService.create(any())).thenReturn(atmDto);
// 
//         mockMvc.perform(post("/atms")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(atmDto)))
//                 .andExpect(status().isCreated());
//     }
// 
//     @Test
//     void update_ShouldUpdateAtm() throws Exception {
//         when(atmService.update(eq(1L), any())).thenReturn(atmDto);
// 
//         mockMvc.perform(put("/atms/1")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(atmDto)))
//                 .andExpect(status().isOk());
//     }
// 
//     @Test
//     void delete_ShouldDeleteAtm() throws Exception {
//         doNothing().when(atmService).delete(1L);
// 
//         mockMvc.perform(delete("/atms/1"))
//                 .andExpect(status().isNoContent());
//     }
// }
