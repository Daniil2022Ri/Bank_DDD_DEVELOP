package com.bank.authorization.dtos.events;

import com.bank.authorization.dtos.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserGetResponse {
    private String requestId;
    private String status;
    private List<UserDto> data;


}
