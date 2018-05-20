# Form3 Payments

## Approach

1. I considered Spring Boot - very easy to get an API up and running using:
    ```
    @RepositoryRestResource(collectionResourceRel = "payment", path = "v1")
       public interface PaymentRepository extends MongoRepository<Payment, UUID> {
       }
   ```
But had a small issue with the '_links' not matching your requirement (may be able to override it)
Not only that - Although I've had plenty of exposure to Spring - Spring Boot is quite new to me so thought it was a risk considering the timeframe.

2. Decided on Dropwizard as I'd used it extensively about 2 years ago for rest APIs and is very nice.

4. Decided on mongo DB and use an in memory for this.

## Time taken

Took me a little longer than I expected due to the following:

- It's been a while since I've used dropwizard so had to re-learn it!
- Never used mongo DB with DW and the libraries included so that was interesting.
- Wasted a bit of time trying to be clever in using Junit 5 but it caused several strange errors with DW - moving back to 4 solved this.

## What was done

- Application will run and store data in memory
- All endpoints are there as design
- Tests cover general checks - no validation of fields except for null ID checks.
- Time boxed myself to 4 hours of coding (excluding issues I had to solve)
- Checkstyle failure will fail the build

## What I would do better

- Tests could do with a possible refactor - though I do like them being quite clear.  They are repetitive so could be tightened up.
- Validation on fields most definitely - general annotations with regex,etc would help.  Alternatively a json schema.
- Add integration tests.
- Review and handle responses better in the RestController
- Handle the database layer a bit more intelligently - try to make it more generic so any database could be used - I just didn't have time to spend on it.  Likely something exists but I've missed it.
- No logging whatsoever.. Obviously this would be added and thrown in Splunk or something similar.
- Create a service layer if it was more complex

How to start the Form3 Payments application
---

1. Run `mvn clean install` to build the application
1. Start application with `java -jar target/payments-1.0-0.jar server config.yml`
1. To check that the application is running enter url `http://localhost:8090/v1/payments`

Health Check
---

To see the applications health enter url `http://localhost:8091/healthcheck`
