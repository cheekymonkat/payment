
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
    "contract_reference",
    "exchange_rate",
    "original_amount",
    "original_currency"
})
public class Fx {

    @JsonProperty("contract_reference")
    private String contractReference;
    @JsonProperty("exchange_rate")
    private String exchangeRate;
    @JsonProperty("original_amount")
    private String originalAmount;
    @JsonProperty("original_currency")
    private String originalCurrency;

}
