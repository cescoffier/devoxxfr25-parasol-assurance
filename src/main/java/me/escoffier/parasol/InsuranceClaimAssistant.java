package me.escoffier.parasol;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService
@SystemMessage("""
            You are helping the customer with their insurance claim.
        """)
public interface InsuranceClaimAssistant {

    @UserMessage("""
            Summarize the given claim. Your summary must not be longer than 200 words.
            If the claim is about a car accident, mention the location and time of the accident.
            Also, if the claim is not in English, write the summary in English.
            
            Make sure to extract a subject line from the claim and the policy number if it is mentioned.
            The subject line should contain the customer name and policy number if mentioned.
            
            In your summary, determine if the claim is valid according to the policy documents.
            
            Your answer must be a JSON object (and just this object) with the following fields:
            - title: The subject line of the claim.
            - summary: The summary of the claim.
            - date: The date of the claim.
            - location: The location of the claim.
            - policyNumber: The policy number mentioned in the claim.
            
            ----
            {claim}
            ----
            
            """)
    ClaimSummary summarize(String claim);

    @UserMessage("""
            Analyze the sentiment of the given claim. The sentiment can be positive, negative, or neutral.
            
            ----
            {claim}
            ----
            
            """)
    Sentiment analyzeSentiment(String claim);

}
