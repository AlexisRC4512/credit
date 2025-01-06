package com.nttdata.credit.model.response;

import com.nttdata.credit.model.entity.Balance;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BalanceResponse {
    private String clientId;
    private List<Balance> balances;
}
