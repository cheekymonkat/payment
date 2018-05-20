
package tech.form3.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "amount",
    "beneficiary_party",
    "charges_information",
    "currency",
    "debtor_party",
    "end_to_end_reference",
    "fx",
    "numeric_reference",
    "payment_id",
    "payment_purpose",
    "payment_scheme",
    "payment_type",
    "processing_date",
    "reference",
    "scheme_payment_sub_type",
    "scheme_payment_type",
    "sponsor_party"
})
public class Attributes {

    @JsonProperty("amount")
    private String amount;
    @JsonProperty("beneficiary_party")
    private BeneficiaryParty beneficiaryParty;
    @JsonProperty("charges_information")
    private ChargesInformation chargesInformation;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("debtor_party")
    private DebtorParty debtorParty;
    @JsonProperty("end_to_end_reference")
    private String endToEndReference;
    @JsonProperty("fx")
    private Fx fx;
    @JsonProperty("numeric_reference")
    private String numericReference;
    @JsonProperty("payment_id")
    private String paymentId;
    @JsonProperty("payment_purpose")
    private String paymentPurpose;
    @JsonProperty("payment_scheme")
    private String paymentScheme;
    @JsonProperty("payment_type")
    private String paymentType;
    @JsonProperty("processing_date")
    private String processingDate;
    @JsonProperty("reference")
    private String reference;
    @JsonProperty("scheme_payment_sub_type")
    private String schemePaymentSubType;
    @JsonProperty("scheme_payment_type")
    private String schemePaymentType;
    @JsonProperty("sponsor_party")
    private SponsorParty sponsorParty;

}
