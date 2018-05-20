package tech.form3.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.Arrays;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.fest.assertions.Assertions.assertThat;

@RunWith(JUnit4.class)
public class ModelVerifyTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void whenPaymentList_validateOk() throws IOException {
        Data data = MAPPER.readValue(fixture("fixtures/payments.json"), Data.class);
        assertThat(data.getData()).hasAllElementsOfType(Payment.class);
        assertThat(data.getLinks().getSelf()).isEqualTo("https://api.test.form3.tech/v1/payments");
    }

    @Test
    public void whenPayment_validateOk() throws IOException {

        BeneficiaryParty beneficiaryParty = BeneficiaryParty.builder()
                .accountName("W Owens")
                .accountNumber("31926819")
                .accountNumberCode("BBAN")
                .accountType(0)
                .address("1 The Beneficiary Localtown SE2")
                .bankId("403000")
                .bankIdCode("GBDSC")
                .name("Wilfred Jeremiah Owens").build();


        ChargesInformation chargesInformation = ChargesInformation.builder()
                .bearerCode("SHAR")
                .senderCharges(Arrays.asList(
                        SenderCharge.builder().amount("5.00").currency("GBP").build(),
                        SenderCharge.builder().amount("10.00").currency("USD").build()
                ))
                .receiverChargesAmount("1.00")
                .receiverChargesCurrency("USD").build();

        DebtorParty debtorParty = DebtorParty.builder()
                .accountName("EJ Brown Black")
                .accountNumber("GB29XABC10161234567801")
                .accountNumberCode("IBAN")
                .address("10 Debtor Crescent Sourcetown NE1")
                .bankId("203301")
                .bankIdCode("GBDSC")
                .name("Emelia Jane Brown").build();

        Fx fx = Fx.builder()
                .contractReference("FX123")
                .exchangeRate("2.00000")
                .originalAmount("200.42")
                .originalCurrency("USD").build();

        SponsorParty sponsorParty = SponsorParty.builder()
                .accountNumber("56781234")
                .bankId("123123")
                .bankIdCode("GBDSC").build();

        Attributes attributes = Attributes.builder()
                .amount("100.21")
                .beneficiaryParty(beneficiaryParty)
                .chargesInformation(chargesInformation)
                .currency("GBP")
                .debtorParty(debtorParty)
                .endToEndReference("Wil piano Jan")
                .fx(fx)
                .numericReference("1002001")
                .paymentId("123456789012345678")
                .paymentPurpose("Paying for goods/services")
                .paymentScheme("FPS")
                .paymentType("Credit")
                .processingDate("2017-01-18")
                .reference("Payment for Em's piano lessons")
                .schemePaymentSubType("InternetBanking")
                .schemePaymentType("ImmediatePayment")
                .sponsorParty(sponsorParty).build();

        Payment payment = Payment.builder()
                .type("Payment")
                .id("4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43")
                .version(0)
                .organisationId("743d5b63-8e6f-432e-a8fa-c5d8d2ee5fcb")
                .attributes(attributes).build();

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/payment.json"), Payment.class));

        assertThat(MAPPER.writeValueAsString(payment)).isEqualTo(expected);
    }

}
