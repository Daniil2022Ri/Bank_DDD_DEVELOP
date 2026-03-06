// package com.bank.publicinfo.controller;
// 
// import com.bank.publicinfo.dto.CertificateDto;
// import com.bank.publicinfo.exception.GlobalExceptionHandler;
// import com.bank.publicinfo.service.CertificateService;
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
// @WebMvcTest(CertificateController.class)
// @Import(GlobalExceptionHandler.class)
// class CertificateControllerTest {
// 
//     @Autowired
//     private MockMvc mockMvc;
// 
//     @MockBean
//     private CertificateService certificateService;
// 
//     @Autowired
//     private ObjectMapper objectMapper;
// 
//     private CertificateDto certificateDto;
// 
//     @BeforeEach
//     void setUp() {
//         certificateDto = CertificateDto.builder()
//                 .id(1L)
//                 .certificateNumber("CERT-123456")
//                 .certificateType("SSL")
//                 .organizationName("Test Org")
//                 .dateOfIssue(LocalDateTime.now())
//                 .status("Active")
//                 .bankDetailsId(1L)
//                 .build();
//     }
// 
//     @Test
//     void getAll_ShouldReturnAllCertificates() throws Exception {
//         List<CertificateDto> certificates = Arrays.asList(certificateDto);
//         when(certificateService.findAll()).thenReturn(certificates);
// 
//         mockMvc.perform(get("/certificates"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[0].certificateNumber").value("CERT-123456"));
//     }
// 
//     @Test
//     void getById_ShouldReturnCertificate() throws Exception {
//         when(certificateService.findById(1L)).thenReturn(certificateDto);
// 
//         mockMvc.perform(get("/certificates/1"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.certificateNumber").value("CERT-123456"));
//     }
// 
//     @Test
//     void create_ShouldCreateCertificate() throws Exception {
//         when(certificateService.create(any())).thenReturn(certificateDto);
// 
//         mockMvc.perform(post("/certificates")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(certificateDto)))
//                 .andExpect(status().isCreated());
//     }
// 
//     @Test
//     void update_ShouldUpdateCertificate() throws Exception {
//         when(certificateService.update(eq(1L), any())).thenReturn(certificateDto);
// 
//         mockMvc.perform(put("/certificates/1")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(certificateDto)))
//                 .andExpect(status().isOk());
//     }
// 
//     @Test
//     void delete_ShouldDeleteCertificate() throws Exception {
//         doNothing().when(certificateService).delete(1L);
// 
//         mockMvc.perform(delete("/certificates/1"))
//                 .andExpect(status().isNoContent());
//     }
// }
