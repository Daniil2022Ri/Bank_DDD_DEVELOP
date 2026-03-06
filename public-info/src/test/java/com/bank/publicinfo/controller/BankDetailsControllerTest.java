// package com.bank.publicinfo.controller;
// 
// import com.bank.publicinfo.dto.BankDetailsDto;
// import com.bank.publicinfo.exception.EntityNotFoundException;
// import com.bank.publicinfo.exception.GlobalExceptionHandler;
// import com.bank.publicinfo.service.BankDetailsService;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.context.annotation.Import;
// import org.springframework.http.MediaType;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.web.servlet.MockMvc;
// 
// import java.util.Arrays;
// import java.util.List;
// 
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.eq;
// import static org.mockito.Mockito.doNothing;
// import static org.mockito.Mockito.when;
// import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
// 
// @WebMvcTest(BankDetailsController.class)
// @Import(GlobalExceptionHandler.class)
// class BankDetailsControllerTest {
// 
//     @Autowired
//     private MockMvc mockMvc;
// 
//     @MockBean
//     private BankDetailsService bankDetailsService;
// 
//     @Autowired
//     private ObjectMapper objectMapper;
// 
//     private BankDetailsDto bankDetailsDto;
// 
//     @BeforeEach
//     void setUp() {
//         bankDetailsDto = BankDetailsDto.builder()
//                 .id(1L)
//                 .bic("123456789")
//                 .inn("1234567890")
//                 .kpp("123456789")
//                 .bankName("Test Bank")
//                 .address("Test Address")
//                 .build();
//     }
// 
//     @Test
//     @WithMockUser
//     void getAll_ShouldReturnAllBanks() throws Exception {
//         List<BankDetailsDto> banks = Arrays.asList(bankDetailsDto);
//         when(bankDetailsService.findAll()).thenReturn(banks);
// 
//         mockMvc.perform(get("/banks"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[0].bankName").value("Test Bank"));
//     }
// 
//     @Test
//     @WithMockUser
//     void getById_WhenExists_ShouldReturnBank() throws Exception {
//         when(bankDetailsService.findById(1L)).thenReturn(bankDetailsDto);
// 
//         mockMvc.perform(get("/banks/1"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.bankName").value("Test Bank"));
//     }
// 
//     @Test
//     @WithMockUser
//     void getById_WhenNotExists_ShouldReturn404() throws Exception {
//         when(bankDetailsService.findById(1L)).thenThrow(new EntityNotFoundException("Not found"));
// 
//         mockMvc.perform(get("/banks/1"))
//                 .andExpect(status().isNotFound());
//     }
// 
//     @Test
//     @WithMockUser
//     void create_ShouldCreateBank() throws Exception {
//         when(bankDetailsService.create(any())).thenReturn(bankDetailsDto);
// 
//         mockMvc.perform(post("/banks")
//                         .with(csrf())
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(bankDetailsDto)))
//                 .andExpect(status().isCreated())
//                 .andExpect(jsonPath("$.bankName").value("Test Bank"));
//     }
// 
//     @Test
//     @WithMockUser
//     void update_ShouldUpdateBank() throws Exception {
//         when(bankDetailsService.update(eq(1L), any())).thenReturn(bankDetailsDto);
// 
//         mockMvc.perform(put("/banks/1")
//                         .with(csrf())
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(bankDetailsDto)))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.bankName").value("Test Bank"));
//     }
// 
//     @Test
//     @WithMockUser
//     void delete_ShouldDeleteBank() throws Exception {
//         doNothing().when(bankDetailsService).delete(1L);
// 
//         mockMvc.perform(delete("/banks/1")
//                         .with(csrf()))
//                 .andExpect(status().isNoContent());
//     }
// }
