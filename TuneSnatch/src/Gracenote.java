//import java.io.File;
//
//import radams.gracenote.webapi.GracenoteException;
////import radams.gracenote.webapi.GracenoteMetadata;
//import radams.gracenote.webapi.GracenoteWebAPI;
//
//public class Gracenote {
//	private static String clientID  = "7878656-4647BE9E320133F431FCA2A5782337E6"; 	// Gracenote ClinetID
//    private static String clientTag = "4647BE9E320133F431FCA2A5782337E6"; 			// Gracenote ClientTag
//    private String userID;
//    
//    private File gracenoteDataFile;
//    
//    public GracenoteWebAPI api;
//    private UserProfile USERPROFILE;
//    
//    public Gracenote(){
//    	USERPROFILE = new UserProfile();
//    	gracenoteDataFile = new File(USERPROFILE.getDataDirectory() + File.separator + "gracenote.dat");
//    	
//    	if(!userIDExists()){
//    		try {
//                api = new GracenoteWebAPI(clientID, clientTag);
//                userID = api.register();
//                saveGracenoteData();
//            } catch (GracenoteException e) {
//                e.printStackTrace();
//            }
//    	} else {
//    		try {
//				api = new GracenoteWebAPI(clientID, clientTag, userID);
//			} catch (GracenoteException e) {
//				e.printStackTrace();
//			}
//    	}
//    	
//    	System.out.println("UserID = " + userID);
//    }
//    
//    private boolean userIDExists(){
//		restoreGracenoteData();
//		
//		if(userID.isEmpty()){
//			System.out.println("UserID does not exist. Registering now.");
//			return false;
//		} else {
//			System.out.println("UserID exists. Restoring now.");
//			return true;
//		}
//    }
//    
//    private void restoreGracenoteData(){
//    	System.out.println("Restoring Gracenote data.");
//    	userID = (String) USERPROFILE.restoreData(gracenoteDataFile);
//    }
//    
//    private void saveGracenoteData(){
//		USERPROFILE.saveData(userID, gracenoteDataFile);
//	}
//}
