// package com.bank.publicinfo.controller;
// 
// import com.bank.publicinfo.dto.LicenseDto;
// import com.bank.publicinfo.exception.GlobalExceptionHandler;
// import com.bank.publicinfo.service.LicenseService;
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
// import java.time.LocalDateTime;
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
// @WebMvcTest(LicenseController.class)
// @Import(GlobalExceptionHandler.class)
// class LicenseControllerTest {
// 
//     @Autowired
//     private MockMvc mockMvc;
// 
//     @MockBean
//     private LicenseService licenseService;
// 
//     @Autowired
//     private ObjectMapper objectMapper;
// 
//     private LicenseDto licenseDto;
// 
//     @BeforeEach
//     void setUp() {
//         licenseDto = LicenseDto.builder()
//                 .id(1L)
//                 .licenseNumber("L-123456")
//                 .licenseType("General")
//                 .dateOfIssue(LocalDateTime.now())
//                 .status("Active")
//                 .bankDetailsId(1L)
//                 .build();
//     }
// 
//     @Test
//     void getAll_ShouldReturnAllLicenses() throws Exception {
//         List<LicenseDto> licenses = Arrays.asList(licenseDto);
//         when(licenseService.findAll()).thenReturn(licenses);
// 
//         mockMvc.perform(get("/licenses"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[0].licenseNumber").value("L-123456"));
//     }
// 
//     @Test
//     void getById_ShouldReturnLicense() throws Exception {
//         when(licenseService.findById(1L)).thenReturn(licenseDto);
// 
//         mockMvc.perform(get("/licenses/1"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.licenseNumber").value("L-123456"));
//     }
// 
//     @Test
//     void create_ShouldCreateLicense() throws Exception {
//         when(licenseService.create(any())).thenReturn(licenseDto);
// 
//         mockMvc.perform(post("/licenses")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(licenseDto)))
//                 .andExpect(status().isCreated());
//     }
// 
//     @Test
//     void update_ShouldUpdateLicense() throws Exception {
//         when(licenseService.update(eq(1L), any())).thenReturn(licenseDto);
// 
//         mockMvc.perform(put("/licenses/1")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(licenseDto)))
//                 .andExpect(status().isOk());
//     }
// 
//     @Test
//     void delete_ShouldDeleteLicense() throws Exception {
//         doNothing().when(licenseService).delete(1L);
// 
//         mockMvc.perform(delete("/licenses/1"))
//                 .andExpect(status().isNoContent());
//     }
// }
