package com.nttdata.credit.model.entity;

import com.nttdata.credit.model.enums.TypeCredit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * Represents a credit in the system.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "credit")
public class Credit {
    /**
     * Unique identifier for the credit.
     */
    @Id
    private String id;

    /**
     * Type of the credit.
     */
    private TypeCredit type;

    /**
     * Amount of the credit.
     */
    private double amount;

    /**
     * Interest rate of the credit.
     */
    private double interestRate;

    /**
     * Start date of the credit.
     */
    private Date startDate;

    /**
     * End date of the credit.
     */
    private Date endDate;

    /**
     * Outstanding balance of the credit.
     */
    private double outstandingBalance;

    /**
     * Client ID associated with the credit.
     */
    private String clientId;

    /**
     * List of payments associated with the credit.
     */
    private List<Payment> payments;

    /**
     * List of transactions associated with the credit.
     */
    private List<Transaction> transactions;

    /**
     * Balance details of the credit.
     */
    private Balance balances;
}
