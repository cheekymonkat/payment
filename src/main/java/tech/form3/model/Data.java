package tech.form3.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Data {

    private Payment[] data;
    private Links links;

    public static class DataBuilder {
        public DataBuilder data(final List<Payment> payments) {
            this.data = payments.toArray(new Payment[0]);
            return this;
        }

        public DataBuilder data(final Payment... payments) {
            this.data = payments;
            return this;
        }
    }

}
