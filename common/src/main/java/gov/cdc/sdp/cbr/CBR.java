package gov.cdc.sdp.cbr;

public interface CBR {
    /*
     * Identifies the source system from which CBR received the message. For the
     * CDC PHIN- MS the value will be PHIN_MS.
     */
    String SOURCE = "SOURCE";
    /* The message identifier from the source system. */
    String SOURCE_ID = "SOURCE_ID";
    /*
     * The time the message was received by the source system
     */
    String SOURCE_RECEIVED_TIME = "SOURCE_RECEIVED_TIME";
    /*
     * The message payload, the media type of the part will define whether the
     * message is binary (application/octet-stream) or an HL7 V2 message
     * (application/hl7-v2).
     */
    String PAYLOAD = "PAYLOAD";
    /*
     * Specifies whether the message was part of a batch of messages received at
     * the source system. The part will be present if the message is part of a
     * batch, omitted if not.
     */
    String BATCH = "BATCH";
    /*
     * Specifies the index of the message in the batch, the first message in the
     * batch will have index 0.
     */
    String BATCH_INDEX = "BATCH_INDEX";
    /*
     * The identifier of the individual batch within a message.
     */
    String BATCH_ID = "BATCH_ID";

    /* The CBR assigned message identifier. */
    String ID = "ID";
    /* The CBR assigned message identifier. */
    String CBR_ID = "CBR_ID";

    /* The sender of the message. */
    String SENDER = "SENDER";
    /* The intended recipient of the message. */

    String RECIPIENT = "RECIPIENT";
    /*
     * Additional attributes from the source system, the value will be formatted
     * according to the application/x-www-form-urlencoded media type as defined
     * by HTML 5. CBR_RECEIVED_TIME: The time the message was received by the
     * CBR service, the value is formatted as per SOURCE_RECEIVED_TIME above.
     */
    String SOURCE_ATTRIBUTES = "SOURCE_ATTRIBUTES";

    /*
     * The time the message was received by the CBR service
     *
     */
    String CBR_RECEIVED_TIME = "CBR_RECEIVED_TIME";
    /*
     * The time the message was delivered by the CBR service
     */
    String CBR_DELIVERED_TIME = "CBR_DELIVERED_TIME";
    String ROUTE = "ROUTE";
    String MESSAGE = "MESSAGE";
    String TIMESTAMP = "TIMESTAMP";
}
