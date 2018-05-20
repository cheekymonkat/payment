
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
    "account_name",
    "account_number",
    "account_number_code",
    "account_type",
    "address",
    "bank_id",
    "bank_id_code",
    "name"
})
public class BeneficiaryParty {

    @JsonProperty("account_name")
    private String accountName;
    @JsonProperty("account_number")
    private String accountNumber;
    @JsonProperty("account_number_code")
    private String accountNumberCode;
    @JsonProperty("account_type")
    private Integer accountType;
    @JsonProperty("address")
    private String address;
    @JsonProperty("bank_id")
    private String bankId;
    @JsonProperty("bank_id_code")
    private String bankIdCode;
    @JsonProperty("name")
    private String name;


}
