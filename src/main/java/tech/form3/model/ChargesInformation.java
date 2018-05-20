
package tech.form3.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "bearer_code",
    "sender_charges",
    "receiver_charges_amount",
    "receiver_charges_currency"
})
public class ChargesInformation {

    @JsonProperty("bearer_code")
    private String bearerCode;
    @JsonProperty("sender_charges")
    private List<SenderCharge> senderCharges = null;
    @JsonProperty("receiver_charges_amount")
    private String receiverChargesAmount;
    @JsonProperty("receiver_charges_currency")
    private String receiverChargesCurrency;

}
