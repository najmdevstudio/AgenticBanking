package com.knightriders.agenticbanking.tools;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;


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

    @Tool
    public String createNewAccount(
            String name,
            String email,
            String phone,
            String accountType,
            Double balance) {

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

    @Tool
    public String getAccountByID(
             Long id) {
        try {
            System.out.println(" Tool called for ID: " + id);
            return accountClient.get()
                    .uri("/accounts/" + id)
                    .retrieve()
                    .body(String.class);
        } catch (RestClientResponseException e) {
            return "Failed to fetch account details: " + e.getResponseBodyAsString();
        }
    }

    @Tool
    public String updateAccount(
            Long id,
            String name,
            String email,
            String phone,
            String accountType,
            Double balance) {

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

    @Tool
    public String deleteAccount(
             Long id) {
        try {
            return accountClient.delete()
                    .uri("/accounts/" + id)
                    .retrieve()
                    .body(String.class);
        } catch (RestClientResponseException e) {
            return "Failed to delete account: " + e.getResponseBodyAsString();
        }
    }

    @Tool
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

    @Tool
    public String createDeposit(
             Long accountId,
             String accountName,
             Float principal,
             Float interestRate,
             String maturityDate,
             String condition) {

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

    @Tool
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

    @Tool
    public String getDepositsByAccountId(
             Long accountId) {
        try {
            return depositClient.get()
                    .uri("/deposits/account/" + accountId)
                    .retrieve()
                    .body(String.class);
        } catch (RestClientResponseException e) {
            return "Failed to fetch deposits for account: " + e.getResponseBodyAsString();
        }
    }

    @Tool
    public String updateDeposit(
             Long depositId,
             Float principal,
             Float interestRate,
             String maturityDate,
             String condition) {

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

    @Tool
    public String deleteDeposit(
             Long depositId) {
        try {
            return depositClient.delete()
                    .uri("/deposits/" + depositId)
                    .retrieve()
                    .body(String.class);
        } catch (RestClientResponseException e) {
            return "Failed to delete deposit: " + e.getResponseBodyAsString();
        }
    }

    @Tool
    public String performTransaction(
             Long fromAccountId,
             Long toAccountId,
             Double amount) {

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

    @Tool
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
