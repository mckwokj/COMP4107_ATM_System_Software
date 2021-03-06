package ATMSS.BAMSHandler;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;
//import com.sun.javafx.tools.packager.Log;

import java.io.IOException;

//======================================================================
// BAMSHandler
public class BAMSHandlerInATMSS extends AppThread {
    protected MBox atmss = null;
    protected String urlPrefix = "http://cslinux0.comp.hkbu.edu.hk/comp4107_20-21_grp10/BAMS-v02.php";
    protected BAMSHandler bams = null;

    //------------------------------------------------------------
    // BAMSHandlerInATMSS
    public BAMSHandlerInATMSS(String id, AppKickstarter appKickstarter) {
        super(id, appKickstarter);
        bams = new BAMSHandler(urlPrefix);
    } // BAMSHandlerInATMSS

    //------------------------------------------------------------
    // run
    public void run() {
        atmss = appKickstarter.getThread("ATMSS").getMBox();
        log.info(id + ": starting...");

        for (boolean quit = false; !quit; ) {
            Msg msg = mbox.receive();

            log.fine(id + ": message received: [" + msg + "].");

            switch (msg.getType()) {
                case Poll:
                    atmss.send(new Msg(id, mbox, Msg.Type.PollAck, id + " is up!"));
                    break;

                case Terminate:
                    quit = true;
                    break;

                case BAMS_Request:
                    handleBAMSRequest(msg.getDetails());
                    break;

                default:
                    processMsg(msg);
            }
        }

        // declaring our departure
        appKickstarter.unregThread(this);
        log.info(id + ": terminating...");
    } // run


    //------------------------------------------------------------
    // processMsg
    protected void processMsg(Msg msg) {
        log.warning(id + ": unknown message type: [" + msg + "]");
    } // processMsg

    protected void handleBAMSRequest(String request) {
        try {
            if (request.contains("LoginReq")) {
                Login(bams, request);
            } else if (request.contains("GetAccReq")) {
                getAcc(bams, request);
            } else if (request.contains("EnquiryReq")) {
                enquiry(bams, request);
            } else if (request.contains("ChgPinReq")) {
                chgPinReq(bams, request);
            } else if (request.contains("DepositReq")) {
                testDeposit(bams, request);
            }else if (request.contains("GetAccDeposit")) {
                getAcc(bams, request);
            }else if (request.contains("GetAccWithdrawal")) {
                getAcc(bams, request);
            }else if (request.contains("WithdrawReq")) {
                testWithdraw(bams, request);
            }else if (request.contains("Logout")){
                Logout(bams, request);
            }  else if (request.contains("TransferReq")) {
                Transfer(bams,request);
            } else if (request.contains("SelectAccReq")) {
                getSelectNextAcc(bams, request);
            }else {
                switch (request) {
                    case "LogoutReq":
                        testLogout(bams);
                        break;

//                    case "GetAccReq":
//                        getAcc(bams);
//                    testGetAcc(bams);
//                        break;

                   // case "WithdrawReq":
                        //testWithdraw(bams);
                      //  break;

                    //case "DepositReq":
                        //testDeposit(bams);
                       // break;

//                    case "EnquiryReq1":
//                        enquiry(bams, "1");
////                    testEnquiry(bams);
//                        break;
//
//                    case "EnquiryReq2":
//                        enquiry(bams, "2");
////                    testEnquiry(bams);
//                        break;
//
//                    case "EnquiryReq3":
//                        enquiry(bams, "3");
////                    testEnquiry(bams);
//                        break;
//
//                    case "EnquiryReq4":
//                        enquiry(bams, "4");
////                    testEnquiry(bams);
//                        break;

                    case "TransferReq":
                        testTransfer(bams);
                        break;

                    case "AccStmtReq":
                        testAccStmtReq(bams);
                        break;

                    case "ChqBookReq":
                        testChqBookReq(bams);
                        break;

//                    case "ChgPinReq":
//                        chgPinReq(bams);
//                        break;
                }
        }

        } catch (BAMSInvalidReplyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //------------------------------------------------------------
    // Login
    protected void Login(BAMSHandler bams, String request) throws BAMSInvalidReplyException, IOException {
        String[] details = request.split(",");
        log.info(details[0]);
        log.info(details[1]);
        log.info(details[2]);
        log.info("Login:");
        String cred = bams.login(details[1], details[2]);
        log.info("cred: " + cred);
        atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "cred:" + cred));
    } // Login

    // Logout
    protected void Logout(BAMSHandler bams, String request) throws BAMSInvalidReplyException, IOException {
        log.info("Logout:");
        String[] details = request.split(",");
        log.info(details[0]);
        log.info(details[1]);
        log.info(details[2]);
        String result = bams.logout(details[1], details[2]);
        log.info("result: " + result);
        atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "logout result: " + result));
    } // Logout


    //------------------------------------------------------------
    // testLogin
    protected void testLogin(BAMSHandler bams) throws BAMSInvalidReplyException, IOException {
        log.info("Login:");
        String cred = bams.login("4107-7014", "123456");
        log.info("cred: " + cred);
        atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "cred: " + cred));
    } // testLogin


    //------------------------------------------------------------
    // testLogout
    protected void testLogout(BAMSHandler bams) throws BAMSInvalidReplyException, IOException {
        log.info("Logout:");
        String result = bams.logout("12345678-0", "cred-0");
        log.info("result: " + result);
        atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "logout result: " + result));
    } // testLogout

    //------------------------------------------------------------
    // getAcc
    protected void getAcc(BAMSHandler bams, String request) throws BAMSInvalidReplyException, IOException {
        if(request.contains("GetAccDeposit")){
            log.info("GetAcc: " );
            String[] details = request.split(",");

            String cardNo = details[1];
            String cred = details[2];

            log.info("cardNo: " + cardNo);
            log.info("cred: " + cred);

            String accounts = bams.getAccounts(cardNo, cred);

            log.info("accounts: " + accounts );

            if (!accounts.contains("Error")) {
                atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "accountDeposit: " + accounts));
            } else {
                // Handle error
                atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Error, "Error"));
            }
        } else if (request.contains("GetAccReq")){
            log.info("GetAcc: " );
            String[] details = request.split(",");

            String cardNo = details[1];
            String cred = details[2];

            log.info("cardNo: " + cardNo);
            log.info("cred: " + cred);

            String accounts = bams.getAccounts(cardNo, cred);

            log.info("accounts: " + accounts );

            if (!accounts.contains("Error")) {
                atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "accounts: " + accounts));
            } else {
                // Handle error
                atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Error, "Error"));
            }
        }else if (request.contains("GetAccWithdrawal")){
            log.info("GetAcc: " );
            String[] details = request.split(",");

            String cardNo = details[1];
            String cred = details[2];

            log.info("cardNo: " + cardNo);
            log.info("cred: " + cred);

            String accounts = bams.getAccounts(cardNo, cred);

            log.info("accounts: " + accounts );

            if (!accounts.contains("Error")) {
//            System.out.println("accounts: " + accounts);
                System.out.println();
                atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "accountWithdrawal: " + accounts));
            } else {
                // Handle error
                atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Error, "Error"));
            }
        }
    } // getAcc


//    //------------------------------------------------------------
//    // testGetAcc
//    protected void testGetAcc(BAMSHandler bams) throws BAMSInvalidReplyException, IOException {
//        System.out.println("GetAcc:");
//        String accounts = bams.getAccounts("12345678-1", "cred-1");
//        System.out.println("accounts: " + accounts);
//        System.out.println();
//        atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "accounts: " + accounts));
//    } // testGetAcc


    //------------------------------------------------------------
    // testWithdraw
    protected void testWithdraw(BAMSHandler bams, String request) throws BAMSInvalidReplyException, IOException {
        try {
            log.info("Withdraw: " );
            String[] details = request.split(",");
            String cardNo = details[1];
            String aid = details[2];
            String cred = details[3];
            String amount = details[4];

            int outAmount = bams.withdraw(cardNo, aid, cred, amount);
            log.info("outAmount: " + outAmount );

            atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response,
                    "outAmount," + outAmount+ "," + amount + "," + aid + "," + cardNo));
        } catch (NumberFormatException exception) {
            atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Error, "Error"));
        }
        // testWithdraw
    }


    //------------------------------------------------------------
    // testDeposit
    protected void testDeposit(BAMSHandler bams, String request) throws BAMSInvalidReplyException, IOException {
        try {
            log.info("Deposit:");
            String[] details = request.split(",");
            String cardNo = details[1];
            String aid = details[2];
            String cred = details[3];
            String amount = details[4];

            double depAmount = bams.deposit(cardNo, aid, cred, amount);
            log.info("depAmount: " + depAmount);

            atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response,
                    "reDeposit," +  depAmount + "," + amount + "," + aid + "," + cardNo));
        } catch (NumberFormatException exception) {
            atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Error, "Error"));
        }

    } // testDeposit

    //------------------------------------------------------------
    // enquiry
    protected void enquiry(BAMSHandler bams, String request) throws BAMSInvalidReplyException, IOException {

        try {
            log.info("Enquiry:");

            String[] details = request.split(",");
            String aid = details[1];
            String cardNo = details[2];
            String cred = details[3];

            log.info("Card no: " + cardNo);
            log.info("aid: " + aid);

            double amount = bams.enquiry(cardNo, aid, cred);
            log.info("amount: " + amount);
            atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response,
                    "card no: " + cardNo + "@account id: " + aid + "@amount: " + amount));
        } catch (NumberFormatException exception) {
            atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Error, "Error"));
        }
    } // enquiry


//    //------------------------------------------------------------
//    // testEnquiry
//    protected void testEnquiry(BAMSHandler bams) throws BAMSInvalidReplyException, IOException {
//        System.out.println("Enquiry:");
//        double amount = bams.enquiry("12345678-4", "111-222-334", "cred-4");
//        System.out.println("amount: " + amount);
//        System.out.println();
//        atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "amount: " + amount));
//    } // testEnquiry

    //------------------------------------------------------------
    // testTransfer
    protected void Transfer(BAMSHandler bams,String request) throws BAMSInvalidReplyException, IOException {
        System.out.println("Transfer:" + request);
        String[] details =  request.split(",");
        String cardNo = details[1];
        String cred = details[2];
        String fromAcc = details[3];
        String toAcc =details[4];
        String amount = details[5];

        System.out.println("Trans Acc:" + fromAcc);
        try {
            double transAmount = bams.transfer(cardNo, cred, fromAcc, toAcc, amount);
            System.out.println("transAmount: " +  transAmount);
            System.out.println(bams.toString());
            atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response,
                    "TransAmount," + transAmount+ "," + amount + "," + fromAcc + "," + cardNo + "," + toAcc));
        } catch (java.io.IOException exception){
            System.out.println("Trans Error");
        }


    } // testTransfer
    protected void getSelectNextAcc(BAMSHandler bams, String request) throws BAMSInvalidReplyException, IOException {
//        System.out.println("GetNextAcc:");
        String[] details = request.split(",");

        String cardNo = details[1];
        String cred = details[2];

        log.info("cardNo: "+cardNo);
        log.info("cred: "+cred);

        String accounts = bams.getAccounts(cardNo, cred);

//        System.out.println("accounts: " + accounts);
        if (!accounts.contains("Error")) {
//            System.out.println("accounts: " + accounts);
            System.out.println();
            atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "SelectTransAC: " + accounts));
        } else {
            // Handle error
            atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Error, "Error"));
        }
    }
    //------------------------------------------------------------
    // testTransfer
    protected void testTransfer(BAMSHandler bams) throws BAMSInvalidReplyException, IOException {
        log.info("Transfer:");
        double transAmount = bams.transfer("12345678-5", "cred-5", "111-222-335", "11-222-336", "109705");
        log.info("transAmount: " + transAmount);
        atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "transAmount: " + transAmount));
    } // testTransfer


    //------------------------------------------------------------
    // testAccStmtReq
    protected void testAccStmtReq(BAMSHandler bams) throws BAMSInvalidReplyException, IOException {
        log.info("AccStmtReq:");
        String result = bams.accStmtReq("12345678-4", "111-222-334", "cred-6");
        log.info("result: " + result);
        atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "accStmtReq result: " + result));
    } // testAccStmtReq


    //------------------------------------------------------------
    // testChqBookReq
    protected void testChqBookReq(BAMSHandler bams) throws BAMSInvalidReplyException, IOException {
        log.info("ChqBookReq:");
        String result = bams.chqBookReq("12345678-4", "111-222-334", "cred-7");
        log.info("result: " + result);
        atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "chqBookReq result: " + result));
    } // testChqBookReq


    //------------------------------------------------------------
    // chgPinReq
    protected void chgPinReq(BAMSHandler bams, String request) throws BAMSInvalidReplyException, IOException {

        log.info("chgPinReq:");
        String[] details = request.split(",");
        log.info(details[0]);
        log.info(details[1]);
        log.info(details[2]);
        log.info(details[3]);
        log.info(details[4]);

        String cardNo = details[1];
        String oldPin = details[2];
        String newPin = details[3];
        String cred = details[4];

        log.info("cardNo: "+cardNo);
        log.info("oldPin: "+oldPin);
        log.info("newPin: "+newPin);
        log.info("cred: "+cred);

        log.info("ChgPinReq:");

        String result = bams.chgPinReq(cardNo, oldPin, newPin, cred);

        log.info("result: " + result);

        if (result.equals("succ")) {
            atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "chgPinReq result: " + result + ","+ newPin));
        } else {

        }
    } // chgPinReq


//    //------------------------------------------------------------
//    // testChgPinReq
//    protected void testChgPinReq(BAMSHandler bams) throws BAMSInvalidReplyException, IOException {
//        System.out.println("ChgPinReq:");
//        String result = bams.chgPinReq("12345678-4", "456123789", "987321654", "cred-8");
//        System.out.println("result: " + result);
//        System.out.println();
//        atmss.send(new Msg(id, mbox, Msg.Type.BAMS_Response, "chgPinReq result: " + result));
//    } // testChgPinReq

}
