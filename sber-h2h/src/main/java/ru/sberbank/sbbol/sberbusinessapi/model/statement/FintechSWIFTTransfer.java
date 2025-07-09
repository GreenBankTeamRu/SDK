package ru.sberbank.sbbol.sberbusinessapi.model.statement;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Данные перевода SWIFT MT103 (только для валютных операций)
 */
@Data
public class FintechSWIFTTransfer implements Serializable {
  @JsonProperty("bankOperationCode")
  private String bankOperationCode;

  @JsonProperty("beneficiaryBankAccount")
  private String beneficiaryBankAccount;

  @JsonProperty("beneficiaryBankName")
  private String beneficiaryBankName;

  @JsonProperty("beneficiaryBankOption")
  private String beneficiaryBankOption;

  @JsonProperty("beneficiaryCustomerAccount")
  private String beneficiaryCustomerAccount;

  @JsonProperty("beneficiaryCustomerName")
  private String beneficiaryCustomerName;

  @JsonProperty("detailsOfCharges")
  private String detailsOfCharges;

  @JsonProperty("exchangeRate")
  private String exchangeRate;

  @JsonProperty("instructedAmount")
  private String instructedAmount;

  @JsonProperty("instructionCode")
  private String instructionCode;

  @JsonProperty("intermediaryBankAccount")
  private String intermediaryBankAccount;

  @JsonProperty("intermediaryBankName")
  private String intermediaryBankName;

  @JsonProperty("intermediaryBankOption")
  private String intermediaryBankOption;

  @JsonProperty("messageDestinator")
  private String messageDestinator;

  @JsonProperty("messageIdentifier")
  private String messageIdentifier;

  @JsonProperty("messageOriginator")
  private String messageOriginator;

  @JsonProperty("messageReceiveTime")
  private String messageReceiveTime;

  @JsonProperty("messageSendTime")
  private String messageSendTime;

  @JsonProperty("messageType")
  private String messageType;

  @JsonProperty("orderingCustomerAccount")
  private String orderingCustomerAccount;

  @JsonProperty("orderingCustomerName")
  private String orderingCustomerName;

  @JsonProperty("orderingCustomerOption")
  private String orderingCustomerOption;

  @JsonProperty("orderingInstitutionAccount")
  private String orderingInstitutionAccount;

  @JsonProperty("orderingInstitutionName")
  private String orderingInstitutionName;

  @JsonProperty("orderingInstitutionOption")
  private String orderingInstitutionOption;

  @JsonProperty("receiverCharges")
  private String receiverCharges;

  @JsonProperty("receiverCorrespondentAccount")
  private String receiverCorrespondentAccount;

  @JsonProperty("receiverCorrespondentName")
  private String receiverCorrespondentName;

  @JsonProperty("receiverCorrespondentOption")
  private String receiverCorrespondentOption;

  @JsonProperty("regulatoryReporting")
  private String regulatoryReporting;

  @JsonProperty("remittanceInformation")
  private String remittanceInformation;

  @JsonProperty("senderCharges")
  private String senderCharges;

  @JsonProperty("senderCorrespondentAccount")
  private String senderCorrespondentAccount;

  @JsonProperty("senderCorrespondentName")
  private String senderCorrespondentName;

  @JsonProperty("senderCorrespondentOption")
  private String senderCorrespondentOption;

  @JsonProperty("senderToReceiverInformation")
  private String senderToReceiverInformation;

  @JsonProperty("transactionReferenceNumber")
  private String transactionReferenceNumber;

  @JsonProperty("transactionRelatedReference")
  private String transactionRelatedReference;

  @JsonProperty("transactionTypeCode")
  private String transactionTypeCode;

  @JsonProperty("urgent")
  private String urgent;

  @JsonProperty("valueDateCurrencyInterbankSettledAmount")
  private String valueDateCurrencyInterbankSettledAmount;
}

