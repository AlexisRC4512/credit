package com.nttdata.credit.model.entity;


import com.nttdata.credit.model.enums.TypeTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
/**
 * Represents a transaction in the system.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    /**
     * Client ID associated with the transaction.
     */
    private String clientId;

    /**
     * Type of transaction.
     */
    private TypeTransaction type;

    /**
     * Amount of the transaction.
     */
    private double amount;

    /**
     * Date of the transaction.
     */
    private Date date;

    /**
     * Description of the transaction.
     */
    private String description;
}