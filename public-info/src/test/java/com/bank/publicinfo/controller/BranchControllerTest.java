// package com.bank.publicinfo.controller;
// 
// import com.bank.publicinfo.dto.BranchDto;
// import com.bank.publicinfo.exception.GlobalExceptionHandler;
// import com.bank.publicinfo.service.BranchService;
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
// @WebMvcTest(BranchController.class)
// @Import(GlobalExceptionHandler.class)
// class BranchControllerTest {
// 
//     @Autowired
//     private MockMvc mockMvc;
// 
//     @MockBean
//     private BranchService branchService;
// 
//     @Autowired
//     private ObjectMapper objectMapper;
// 
//     private BranchDto branchDto;
// 
//     @BeforeEach
//     void setUp() {
//         branchDto = BranchDto.builder()
//                 .id(1L)
//                 .address("Test Address")
//                 .phone("+7(495)123-45-67")
//                 .bankDetailsId(1L)
//                 .build();
//     }
// 
//     @Test
//     @WithMockUser
//     void getAll_ShouldReturnAllBranches() throws Exception {
//         List<BranchDto> branches = Arrays.asList(branchDto);
//         when(branchService.findAll()).thenReturn(branches);
// 
//         mockMvc.perform(get("/branches"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[0].address").value("Test Address"));
//     }
// 
//     @Test
//     @WithMockUser
//     void getById_ShouldReturnBranch() throws Exception {
//         when(branchService.findById(1L)).thenReturn(branchDto);
// 
//         mockMvc.perform(get("/branches/1"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.address").value("Test Address"));
//     }
// 
//     @Test
//     @WithMockUser
//     void getByBankDetailsId_ShouldReturnBranches() throws Exception {
//         List<BranchDto> branches = Arrays.asList(branchDto);
//         when(branchService.findByBankDetailsId(1L)).thenReturn(branches);
// 
//         mockMvc.perform(get("/branches/bank/1"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[0].address").value("Test Address"));
//     }
// 
//     @Test
//     @WithMockUser
//     void create_ShouldCreateBranch() throws Exception {
//         when(branchService.create(any())).thenReturn(branchDto);
// 
//         mockMvc.perform(post("/branches")
//                         .with(csrf())
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(branchDto)))
//                 .andExpect(status().isCreated());
//     }
// 
//     @Test
//     @WithMockUser
//     void update_ShouldUpdateBranch() throws Exception {
//         when(branchService.update(eq(1L), any())).thenReturn(branchDto);
// 
//         mockMvc.perform(put("/branches/1")
//                         .with(csrf())
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(branchDto)))
//                 .andExpect(status().isOk());
//     }
// 
//     @Test
//     @WithMockUser
//     void delete_ShouldDeleteBranch() throws Exception {
//         doNothing().when(branchService).delete(1L);
// 
//         mockMvc.perform(delete("/branches/1")
//                         .with(csrf()))
//                 .andExpect(status().isNoContent());
//     }
// }
