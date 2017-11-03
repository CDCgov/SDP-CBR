package gov.cdc.sdp.cbr;

public class TestUtilities {

    private static boolean error = false;
    
    public boolean generateError() throws Exception {
        try {
            if (error) {
                throw new Exception("TestError");
            }
        } finally {
            error = !error;
        }
        return true;
    }
    
}
