// package com.bank.publicinfo.controller;
// 
// import com.bank.publicinfo.dto.AuditDto;
// import com.bank.publicinfo.exception.GlobalExceptionHandler;
// import com.bank.publicinfo.service.AuditService;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.context.annotation.Import;
// import org.springframework.test.web.servlet.MockMvc;
// 
// import java.util.Arrays;
// import java.util.List;
// 
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// 
// @WebMvcTest(AuditController.class)
// @Import(GlobalExceptionHandler.class)
// class AuditControllerTest {
// 
//     @Autowired
//     private MockMvc mockMvc;
// 
//     @MockBean
//     private AuditService auditService;
// 
//     private AuditDto auditDto;
// 
//     @BeforeEach
//     void setUp() {
//         auditDto = AuditDto.builder()
//                 .id(1L)
//                 .entityType("BankDetails")
//                 .operationType("CREATE")
//                 .createdBy("testuser")
//                 .entityJson("{}")
//                 .build();
//     }
// 
//     @Test
//     void getAll_ShouldReturnAllAudits() throws Exception {
//         List<AuditDto> audits = Arrays.asList(auditDto);
//         when(auditService.findAll()).thenReturn(audits);
// 
//         mockMvc.perform(get("/audits"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[0].entityType").value("BankDetails"));
//     }
// 
//     @Test
//     void getById_ShouldReturnAudit() throws Exception {
//         when(auditService.findById(1L)).thenReturn(auditDto);
// 
//         mockMvc.perform(get("/audits/1"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.entityType").value("BankDetails"));
//     }
// 
//     @Test
//     void getByEntityType_ShouldReturnAudits() throws Exception {
//         List<AuditDto> audits = Arrays.asList(auditDto);
//         when(auditService.findByEntityType("BankDetails")).thenReturn(audits);
// 
//         mockMvc.perform(get("/audits/entity-type")
//                         .param("entityType", "BankDetails"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[0].entityType").value("BankDetails"));
//     }
// }
