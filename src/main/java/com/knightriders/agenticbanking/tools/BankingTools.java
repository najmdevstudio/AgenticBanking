package com.knightriders.agenticbanking.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@Component
public class BankingTools {

    private final RestClient accountClient;
    private final RestClient depositClient;
    private final RestClient transactionClient;

    public BankingTools() {
        this.accountClient = RestClient.builder()
                .baseUrl("http://localhost:8080/api")
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();

        this.depositClient = RestClient.builder()
                .baseUrl("http://localhost:8081/api")
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();

        this.transactionClient = RestClient.builder()
                .baseUrl("http://localhost:8082/api")
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Tool(description = "Creates a new customer account in the banking system.")
    public String createAccount(
            @ToolParam(description = "Name of the account holder", required = true) String name,
            @ToolParam(description = "Email of the account holder", required = true) String email,
            @ToolParam(description = "Phone number of the account holder", required = true) String phone,
            @ToolParam(description = "Type of the account, e.g., SAVINGS or CURRENT", required = true) String accountType,
            @ToolParam(description = "Initial balance to be deposited", required = true) Double balance) {

        String jsonBody = String.format("""
                {
                    "name": "%s",
                    "email": "%s",
                    "phone": "%s",
                    "accountType": "%s",
                    "balance": %.2f
                }
                """, name, email, phone, accountType, balance);

        try {
            return accountClient.post()
                    .uri("/accounts/create")
                    .body(jsonBody)
                    .retrieve()
                    .body(String.class);
        } catch (RestClientResponseException e) {
            return "Failed to create account: " + e.getResponseBodyAsString();
        }
    }

    @Tool(description = "Fetches the account details based on account ID.")
    public String getAccount(
            @ToolParam(description = "Unique identifier of the account", required = true) Long id) {
        try {
            return accountClient.get()
                    .uri("/accounts/" + id)
                    .retrieve()
                    .body(String.class);
        } catch (RestClientResponseException e) {
            return "Failed to fetch account details: " + e.getResponseBodyAsString();
        }
    }

    @Tool(description = "Updates an existing customer account.")
    public String updateAccount(
            @ToolParam(description = "ID of the account", required = true) Long id,
            @ToolParam(description = "New name of the account holder", required = false) String name,
            @ToolParam(description = "New email of the account holder", required = false) String email,
            @ToolParam(description = "New phone number", required = false) String phone,
            @ToolParam(description = "Updated account type", required = false) String accountType,
            @ToolParam(description = "Updated balance", required = false) Double balance) {

        StringBuilder jsonBuilder = new StringBuilder("{");
        boolean first = true;
        if (name != null) {
            jsonBuilder.append(String.format("\"name\":\"%s\"", name));
            first = false;
        }
        if (email != null) {
            if (!first) jsonBuilder.append(",");
            jsonBuilder.append(String.format("\"email\":\"%s\"", email));
            first = false;
        }
        if (phone != null) {
            if (!first) jsonBuilder.append(",");
            jsonBuilder.append(String.format("\"phone\":\"%s\"", phone));
            first = false;
        }
        if (accountType != null) {
            if (!first) jsonBuilder.append(",");
            jsonBuilder.append(String.format("\"accountType\":\"%s\"", accountType));
            first = false;
        }
        if (balance != null) {
            if (!first) jsonBuilder.append(",");
            jsonBuilder.append(String.format("\"balance\":%.2f", balance));
        }
        jsonBuilder.append("}");

        try {
            return accountClient.put()
                    .uri("/accounts/" + id)
                    .body(jsonBuilder.toString())
                    .retrieve()
                    .body(String.class);
        } catch (RestClientResponseException e) {
            return "Failed to update account: " + e.getResponseBodyAsString();
        }
    }

    @Tool(description = "Deletes an account from the banking system.")
    public String deleteAccount(
            @ToolParam(description = "Account ID to be deleted", required = true) Long id) {
        try {
            return accountClient.delete()
                    .uri("/accounts/" + id)
                    .retrieve()
                    .body(String.class);
        } catch (RestClientResponseException e) {
            return "Failed to delete account: " + e.getResponseBodyAsString();
        }
    }

    @Tool(description = "Retrieves all customer accounts.")
    public String getAllAccounts() {
        try {
            return accountClient.get()
                    .uri("/accounts")
                    .retrieve()
                    .body(String.class);
        } catch (RestClientResponseException e) {
            return "Failed to fetch accounts: " + e.getResponseBodyAsString();
        }
    }

    @Tool(description = "Creates a new deposit for a specific account.")
    public String createDeposit(
            @ToolParam(description = "Account ID for which the deposit is made", required = true) Long accountId,
            @ToolParam(description = "Name of the account holder", required = true) String accountName,
            @ToolParam(description = "Principal amount being deposited", required = true) Float principal,
            @ToolParam(description = "Rate of interest for the deposit", required = true) Float interestRate,
            @ToolParam(description = "Maturity date of the deposit (yyyy-MM-dd)", required = true) String maturityDate,
            @ToolParam(description = "Special conditions or terms", required = false) String condition) {

        String jsonBody = String.format("""
                {
                    "accountId": %d,
                    "accountName": "%s",
                    "principal": %.2f,
                    "interestRate": %.2f,
                    "maturityDate": "%s",
                    "condition": "%s"
                }
                """, accountId, accountName, principal, interestRate, maturityDate, condition == null ? "" : condition);

        try {
            return depositClient.post()
                    .uri("/deposits/create")
                    .body(jsonBody)
                    .retrieve()
                    .body(String.class);
        } catch (RestClientResponseException e) {
            return "Failed to create deposit: " + e.getResponseBodyAsString();
        }
    }

    @Tool(description = "Fetches all deposits made in the bank.")
    public String getAllDeposits() {
        try {
            return depositClient.get()
                    .uri("/deposits")
                    .retrieve()
                    .body(String.class);
        } catch (RestClientResponseException e) {
            return "Failed to fetch deposits: " + e.getResponseBodyAsString();
        }
    }

    @Tool(description = "Fetches all deposits made for a specific account.")
    public String getDepositsByAccountId(
            @ToolParam(description = "ID of the account", required = true) Long accountId) {
        try {
            return depositClient.get()
                    .uri("/deposits/account/" + accountId)
                    .retrieve()
                    .body(String.class);
        } catch (RestClientResponseException e) {
            return "Failed to fetch deposits for account: " + e.getResponseBodyAsString();
        }
    }

    @Tool(description = "Updates an existing deposit record.")
    public String updateDeposit(
            @ToolParam(description = "ID of the deposit to update", required = true) Long depositId,
            @ToolParam(description = "New principal deposit", required = false) Float principal,
            @ToolParam(description = "New rate of interest", required = false) Float interestRate,
            @ToolParam(description = "Updated maturity date (yyyy-MM-dd)", required = false) String maturityDate,
            @ToolParam(description = "Updated condition", required = false) String condition) {

        StringBuilder jsonBuilder = new StringBuilder("{");
        boolean first = true;
        if (principal != null) {
            jsonBuilder.append(String.format("\"principal\":%.2f", principal));
            first = false;
        }
        if (interestRate != null) {
            if (!first) jsonBuilder.append(",");
            jsonBuilder.append(String.format("\"interestRate\":%.2f", interestRate));
            first = false;
        }
        if (maturityDate != null) {
            if (!first) jsonBuilder.append(",");
            jsonBuilder.append(String.format("\"maturityDate\":\"%s\"", maturityDate));
            first = false;
        }
        if (condition != null) {
            if (!first) jsonBuilder.append(",");
            jsonBuilder.append(String.format("\"condition\":\"%s\"", condition));
        }
        jsonBuilder.append("}");

        try {
            return depositClient.put()
                    .uri("/deposits/" + depositId)
                    .body(jsonBuilder.toString())
                    .retrieve()
                    .body(String.class);
        } catch (RestClientResponseException e) {
            return "Failed to update deposit: " + e.getResponseBodyAsString();
        }
    }

    @Tool(description = "Deletes a deposit record.")
    public String deleteDeposit(
            @ToolParam(description = "ID of the deposit to delete", required = true) Long depositId) {
        try {
            return depositClient.delete()
                    .uri("/deposits/" + depositId)
                    .retrieve()
                    .body(String.class);
        } catch (RestClientResponseException e) {
            return "Failed to delete deposit: " + e.getResponseBodyAsString();
        }
    }

    @Tool(description = "Performs a transaction between two accounts.")
    public String performTransaction(
            @ToolParam(description = "ID of the account sending the money", required = true) Long fromAccountId,
            @ToolParam(description = "ID of the account receiving the money", required = true) Long toAccountId,
            @ToolParam(description = "Amount of money to transfer", required = true) Double amount) {

        String jsonBody = String.format("""
                {
                    "fromAccountId": %d,
                    "toAccountId": %d,
                    "amount": %.2f
                }
                """, fromAccountId, toAccountId, amount);

        try {
            return transactionClient.post()
                    .uri("/transactions/perform")
                    .body(jsonBody)
                    .retrieve()
                    .body(String.class);
        } catch (RestClientResponseException e) {
            return "Failed to perform transaction: " + e.getResponseBodyAsString();
        }
    }

    @Tool(description = "Retrieves all transactions.")
    public String getAllTransactions() {
        try {
            return transactionClient.get()
                    .uri("/transactions")
                    .retrieve()
                    .body(String.class);
        } catch (RestClientResponseException e) {
            return "Failed to fetch transactions: " + e.getResponseBodyAsString();
        }
    }
}
