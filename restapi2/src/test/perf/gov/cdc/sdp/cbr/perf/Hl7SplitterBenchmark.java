package gov.cdc.sdp.cbr.perf;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.concurrent.TimeUnit;

import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import com.google.gson.Gson;

import gov.cdc.sdp.cbr.HL7V2BatchSplitter;

import gov.cdc.sdp.cbr.model.SDPMessage;

/**
 */
@BenchmarkMode({ Mode.AverageTime })
@Fork(2)
public class Hl7SplitterBenchmark {
    private static final int SIZE = 10;

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        HL7V2BatchSplitter splitter = new HL7V2BatchSplitter();
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.PRINT)
    @OperationsPerInvocation(SIZE)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testBatch100(final BenchmarkState state) {
        // noinspection Convert2streamapi
        Message msg = generateMessage(100);
        for (int i = 0; i < SIZE; i++) {
            state.splitter.splitMessage(msg);
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.PRINT)
    @OperationsPerInvocation(SIZE)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testBatch10(final BenchmarkState state) {
        // noinspection Convert2streamapi
        Message msg = generateMessage(10);
        for (int i = 0; i < SIZE; i++) {
            state.splitter.splitMessage(msg);
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.PRINT)
    @OperationsPerInvocation(SIZE)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testBatch50(final BenchmarkState state) {
        // noinspection Convert2streamapi
        Message msg = generateMessage(50);
        for (int i = 0; i < SIZE; i++) {
            state.splitter.splitMessage(msg);
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.PRINT)
    @OperationsPerInvocation(SIZE)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testSplitRegexAll(final BenchmarkState state) {
        // noinspection Convert2streamapi
        String msg = generateBatch(100);
        for (int i = 0; i < SIZE; i++) {
            msg.split("\\r\\n|\\n|\\r");
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.PRINT)
    @OperationsPerInvocation(SIZE)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testSplitLineRegexOpt(final BenchmarkState state) {
        // noinspection Convert2streamapi
        String msg = generateBatch(100);
        for (int i = 0; i < SIZE; i++) {
            msg.split("\r\n?");
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.PRINT)
    @OperationsPerInvocation(SIZE)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testSplitLineReader(final BenchmarkState state) {
        // noinspection Convert2streamapi
        String msg = generateBatch(100);
        for (int i = 0; i < SIZE; i++) {
            new BufferedReader(new StringReader(msg)).lines();
        }
    }

    private static Message generateMessage(int batchSize) {
        String payload = generateBatch(batchSize);
        Message msg = new DefaultMessage();
        SDPMessage sdpMsg = new SDPMessage();
        sdpMsg.setId("id");
        msg.setHeader(SDPMessage.SDP_MESSAGE_HEADER, new Gson().toJson(sdpMsg));
        msg.setBody(payload);
        return msg;
    }

    private static String generateBatch(int size) {
        StringBuffer buff = new StringBuffer();
        buff.append(
                "FHS|^~\\&|^^|The Iowa Division ^2.16.840.1.114222.4.1.3635^ISO|^^|The Iowa Division ^2.16.840.1.114222.4.1.3635^ISO|39130010134019+0-32400000|||||\n");
        buff.append(
                "BHS|^~\\&|^^|The Iowa Division ^2.16.840.1.114222.4.1.3635^ISO|^^|The Iowa Division ^2.16.840.1.114222.4.1.3635^ISO|39130010134019+0-32400000|||||\n");
        for (int i = 0; i < size; i++) {
            buff.append(
                    "MSH|^~\\&|ACDP^2.16.840.1.114222.4.1.3676^ISO|OR-DHS^2.16.840.1.114222.4.1.3676^ISO|PHINCDS^2.16.840.1.114222.4.3.2.10^ISO|PHIN^2.16.840.1.114222^ISO|20170823102635||ORU^R01^ORU_R01|20170823-OR-000515170|P|2.5.1|||||||||NOTF_ORU_v3.0^PHINProfileID^2.16.840.1.114222.4.10.3^ISO~Generic_MMG_V2.0^PHINMsgMapID^2.16.840.1.114222.4.10.4^ISO~FDD_MMG_V1.0^PHINMsgMapID^2.16.840.1.114222.4.10.4^ISO\n"
                            + "PID|1||515170^^^ACDP&2.16.840.1.114222.4.1.3676&ISO||~^^^^^^S||20101117|F||UNK^Unknown^NULLFL|^^^41^^^^^41003|||||||||||UNK^Unknown^NULLFL\n"
                            + "OBR|1||515170^ACDP^2.16.840.1.114222.4.1.3676^ISO|68991-9^Epidemiologic Information^LN|||20170823102634|||||||||||||||20170823102634|||F||||||11020^Campylobacter^NND\n"
                            + "OBX|1|CWE|77965-2^Immediate National Notifiable Condition^LN||N^No^HL70136||||||F\n"
                            + "OBX|2|CWE|78746-5^Country of Birth^LN||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|3|CWE|77983-5^Country of Usual Residence^LN||USA^United States^ISO3166_1||||||F\n"
                            + "OBX|4|CWE|77966-0^Reporting State^LN||41^Oregon^FIPS5_2||||||F\n"
                            + "OBX|5|CWE|77967-8^Reporting County^LN||41003^Benton^FIPS6_4||||||F\n"
                            + "OBX|6|CWE|77968-6^National Reporting Jurisdiction^LN||41^Oregon^FIPS5_2||||||F\n"
                            + "OBX|7|ST|77969-4^Jurisdiction Code^LN||41||||||F\n"
                            + "OBX|8|DT|77995-9^Date of Report/Referral^LN||20170803||||||F\n"
                            + "OBX|9|CWE|48766-0^Information Source^LN||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|10|TS|77972-8^Earliest Date Reported to County^LN||20170803||||||F\n"
                            + "OBX|11|TS|77973-6^Earliest Date Reported to State^LN||20170803||||||F\n"
                            + "OBX|12|CWE|77974-4^Hospitalized^LN||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|13|TS|11368-8^Date of Illness Onset^LN||20170727||||||F\n"
                            + "OBX|14|CWE|77978-5^Subject Died^LN||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|15|DT|77979-3^Case Investigation Start Date^LN||20170803||||||F\n"
                            + "OBX|16|CWE|77980-1^Case Outbreak indicator^LN||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|17|CWE|77990-0^Case Class Status Code^LN||410605003^Confirmed Present^SCT||||||F\n"
                            + "OBX|18|SN|77991-8^MMWR Week^LN||^31||||||F\n"
                            + "OBX|19|DT|77992-6^MMWR Year^LN||2017||||||F\n"
                            + "OBX|20|ST|77993-4^State Case ID^LN||515170||||||F\n"
                            + "OBX|21|DT|77970-2^Date of First Reported to PHD^LN||20170803||||||F\n"
                            + "OBX|22|ST|74549-7^Person Reporting to CDC - Name^LN||June Bancroft||||||F\n"
                            + "OBX|23|ST|74548-9^Person Reporting to CDC - Phone^LN||9716731111||||||F\n"
                            + "OBX|24|ST|74547-1^Person Reporting to CDC - Email^LN||june.e.bancroft@state.or.us||||||F\n"
                            + "OBX|25|SN|77998-3^Age at case investigation^LN||^6|a^years^UCUM|||||F\n"
                            + "OBX|26|CWE|TRAVEL06^Date of Arrival to Travel Destination^PHINQUESTION||||||||F\n"
                            + "OBX|27|CWE|TRAVEL07^Date of Departure from Travel Destination^PHINQUESTION||||||||F\n"
                            + "OBX|28|CWE|CEA_Ountreat_water^Drink Outside Untreated Water, within 7 days before illness^PHINQUESTION||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|29|CWE|CEA_Sewer_water^Home with Septic System, within 7 days before Illness^PHINQUESTION||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|30|CWE|CEA_Swim_treat^Swim in Treated Water, within 7 days before illness^PHINQUESTION||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|31|CWE|CEA_Swim_untreat^Swim in Untreated Water, within 7 days before illness^PHINQUESTION||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|32|CWE|CEA_Well_water^Drink Well Water, within 7 days before illness^PHINQUESTION||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|33|CWE|CEA_Chx_grnd^Eat Ground Chicken, within 7 days before illness^PHINQUESTION||N^No^HL70136||||||F\n"
                            + "OBX|34|CWE|CEA_Turkey_grnd^Eat Ground Turkey, within 7 days before illness^PHINQUESTION||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|35|CWE|CEA_Softcheese^Eat Soft Cheese, within 7 days before illness^PHINQUESTION||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|36|CWE|CEA_Odairy_raw^Other Raw Dairy Products, within 7 days before illness^PHINQUESTION||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|37|CWE|CEA_Softcheese_raw^Eat Raw Cheese, within 7 days before illness^PHINQUESTION||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|38|CWE|CEA_Beef^Eat Beef, within 7 days before illness^PHINQUESTION||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|39|CWE|CEA_Beef_grnd^Eat Ground Beef, within 7 days before illness^PHINQUESTION||N^No^HL70136||||||F\n"
                            + "OBX|40|CWE|CEA_Beef_out^Eat Beef made outside of home, within 7 days before illness^PHINQUESTION||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|41|CWE|CEA_Beef_unckgrnd^Eat uncooked ground beef, within 7 days before illness^PHINQUESTION||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|42|CWE|CEA_Berries^Eat Fresh Berries, within 7 days before illness^PHINQUESTION||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|43|CWE|CEA_Bird^Bird contact, within 7 days before illness^PHINQUESTION||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|44|CWE|CEA_Cantaloupe^Eat Cantaloupe, within 7 days before illness^PHINQUESTION||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|45|CWE|CEA_Cat^Cat contact, within 7 days before illness^PHINQUESTION||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|46|CWE|CEA_Chx_fresh^Eat Fresh Chicken, within 7 days before illness^PHINQUESTION||N^No^HL70136||||||F\n"
                            + "OBX|47|CWE|CEA_Chx_frozen^Eat Frozen Chicken, within 7 days before illness^PHINQUESTION||N^No^HL70136||||||F\n"
                            + "OBX|48|CWE|CEA_Chx_out^Eat Chicken made outside of home, within 7 days before illness^PHINQUESTION||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|49|CWE|CEA_Dairy^Eat or Drink Dairy, within 7 days before illness^PHINQUESTION||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|50|CWE|CEA_Dog^Dog contact, within 7 days before illness^PHINQUESTION||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|51|CWE|CEA_Eggs^Eat eggs, within 7 days before illness^PHINQUESTION||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|52|CWE|CEA_Eggs_out^Eat eggs made outside of home, within 7 days before illness^PHINQUESTION||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|53|CWE|CEA_Eggs_unck^Eat uncooked eggs, within 7 days before illness^FOODNET||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|54|CWE|CEA_Farm_ranch^Animal exposure (Farm / Ranch), within 7 days before illness^FOODNET||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|55|CWE|CEA_Fish^Eat Fish, within 7 days before illness^FOODNET||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|56|CWE|CEA_Fish_unck^Eat uncooked fish, within 7 days before illness^FOODNET||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|57|CWE|CEA_Handle_raw_poultry^Handle raw poultry, within 7 days before illness^FOODNET||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|58|CWE|CEA_Handle_raw_seafood^Handle raw seafood, within 7 days before illness^FOODNET||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|59|CWE|CEA_Herbs^Eat Fresh herbs, within 7 days before illness^FOODNET||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|60|CWE|CEA_Lamb^Eat Lamb, within 7 days before illness^FOODNET||Y^Yes^HL70136||||||F\n"
                            + "OBX|61|CWE|CEA_Lettuce^Eat Fresh Lettuce, within 7 days before illness^FOODNET||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|62|CWE|CEA_Live_poultry^Live Poultry Contact, within 7 days before illness^FOODNET||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|63|CWE|CEA_Liver_pate^Eat Liver Pate, within 7 days before illness^FOODNET||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|64|CWE|CEA_Liver_raw^Eat Raw Liver, within 7 days before illness^FOODNET||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|65|CWE|CEA_Milk_pasteurized^Drink Pasteurized Milk, within 7 days before illness^FOODNET||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|66|CWE|CEA_Milk_raw^Drink Raw Milk, within 7 days before illness^FOODNET||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|67|CWE|CEA_Pig^Pig Contact, within 7 days before illness^FOODNET||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|68|CWE|CEA_Pocketpet^Other Mamalian Household Pet Contact, within 7 days before Illness^FOODNET||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|69|CWE|CEA_Pork^Eat Pork, within 7 days before illness^FOODNET||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|70|CWE|CEA_Raw_cider^Drink Raw Cider, within 7 days before illness^FOODNET||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|71|CWE|CEA_Reptile_amphib^Reptile or Amphbian Contact, within 7 days before illness^FOODNET||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|72|CWE|CEA_Ruminants^Ruminant Contact, within 7 days before illness^FOODNET||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|73|CWE|CEA_Seafd^Eat Seafood, within 7 days before illness^FOODNET||Y^Yes^HL70136||||||F\n"
                            + "OBX|74|CWE|CEA_Seafd_unck^Eat Uncooked Seafood, within 7 days before illness^FOODNET||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|75|CWE|CEA_Sick_contact^Close contact with Diarrhea, within 7 days before illness^FOODNET||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|76|CWE|CEA_Sick_pet^Sick Pet Contact, within 7 days before illness^FOODNET||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|77|CWE|CEA_Spinach^Eat fresh spinach, within 7 days before illness^FOODNET||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|78|CWE|CEA_Sprouts^Eat Sprouts, within 7 days before illness^FOODNET||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|79|CWE|CEA_Tomatoes^Eat Fresh Tomatoes, within 7 days before illness^FOODNET||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|80|CWE|CEA_Turkey_out^Eat Turkey made outside of home, within 7 days before illness^FOODNET||UNK^Unknown^NULLFL||||||F\n"
                            + "OBX|81|CWE|CEA_Watermelon^Eat Fresh Watermelon, within 7 days before illness^FOODNET||UNK^Unknown^NULLFL||||||F");
        }
        buff.append("BTS|" + size + "|");
        buff.append("\n");
        buff.append("FTS|1|");
        return buff.toString();
    }
}
