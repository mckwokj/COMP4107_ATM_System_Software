package ATMSS.TouchDisplayHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Touch Display Emulator Controller class is using to interact with the touch display emulator
 */

//======================================================================
// TouchDisplayEmulatorController
public class TouchDisplayEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private TouchDisplayEmulator touchDisplayEmulator;
    private MBox touchDisplayMBox;

    public StackPane account1StackPane;
    public StackPane account2StackPane;
    public StackPane account3StackPane;
    public StackPane account4StackPane;

    public Text account1Text;
    public Text account2Text;
    public Text account3Text;
    public Text account4Text;
    public Text balanceErrorText;

    public Text accBalanceAccNo;
    public Text accBalanceCardNo;
    public Text accBalanceAmount;
    public ArrayList<Text> accBalanceTexts = new ArrayList<>();

    public ArrayList<StackPane> stackPanes = new ArrayList<>();
    public ArrayList<Text> accountTexts = new ArrayList<>();

    public Label pinLabel;
    public Label pinWrongLabel;
    public Label notifyLabel;

    /**
     * Initialize the reference
     * @param id the name of buzzer
     * @param appKickstarter a reference to the AppKickstarter
     * @param log a reference to the logger
     * @param touchDisplayEmulator a reference to the emulator
     */
    //------------------------------------------------------------
    // initialize
    public void initialize(String id, AppKickstarter appKickstarter, Logger log, TouchDisplayEmulator touchDisplayEmulator) {
    this.id = id;
	this.appKickstarter = appKickstarter;
	this.log = log;
	this.touchDisplayEmulator = touchDisplayEmulator;
	this.touchDisplayMBox = appKickstarter.getThread("TouchDisplayHandler").getMBox();
    } // initialize
    //------------------------------------------------------------

    /**
     * Handle the button press event
     * @param event
     */
    // td_withdrawal
    public void td_withdrawal(Event event) {
        log.info("event: " + event);
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.BAMS_Request, "GetAccWithdrawal"));
    }
    // td_withdrawal
    //------------------------------------------------------------

    /**
     * Handle the button press event
     * @param event
     */
    // td_deposit
    public void td_deposit(Event event) {
        log.info("event: " + event);
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.BAMS_Request, "GetAccDeposit"));
    }
    // td_deposit
    //------------------------------------------------------------

    /**
     * Handle the button press event
     * @param event
     */
    // td_refresh
    public void td_refresh(Event event) {
        log.info("event: " + event);
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "RefreshDeposit"));
    }
    // td_refresh
    //------------------------------------------------------------

    /**
     * Handle the button press event
     * @param event
     */
    //------------------------------------------------------------
    // td_AccWithdrawal
    public void td_AccWithdrawal(Event event) {
        String source = event.getSource().toString(); //yields complete string
        String msgDetail;

        if (source.contains("StackPane[id=account1StackPane]")) {
//            System.out.println("Src1: "+ source);
            msgDetail = "WithdrawalReq,1";
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_selectedAccWithdrawal, msgDetail));
        } else if (source.contains("StackPane[id=account2StackPane]")) {
//            System.out.println("Src2: "+ source);
            msgDetail = "WithdrawalReq,2";
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_selectedAccWithdrawal, msgDetail));
        } else if (source.contains("StackPane[id=account3StackPane]")) {
//            System.out.println("Src3: "+ source);
            msgDetail = "WithdrawalReq,3";
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_selectedAccWithdrawal, msgDetail));
        } else if (source.contains("StackPane[id=account4StackPane]")) {
//            System.out.println("Src4: "+ source);
            msgDetail = "WithdrawalReq,4";
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_selectedAccWithdrawal, msgDetail));
        }
    }
    // td_AccWithdrawal
    //------------------------------------------------------------

    /**
     * Handle the button press event
     * @param event
     */
    //------------------------------------------------------------
    // td_AccDeposit
    public void td_AccDeposit(Event event) {
        String source = event.getSource().toString(); //yields complete string
        String msgDetail;

        if (source.contains("StackPane[id=account1StackPane]")) {
//            System.out.println("Src1: "+ source);
            msgDetail = "DepositReq,1";
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_selectedAcc, msgDetail));
        } else if (source.contains("StackPane[id=account2StackPane]")) {
//            System.out.println("Src2: "+ source);
            msgDetail = "DepositReq,2";
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_selectedAcc, msgDetail));
        } else if (source.contains("StackPane[id=account3StackPane]")) {
//            System.out.println("Src3: "+ source);
            msgDetail = "DepositReq,3";
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_selectedAcc, msgDetail));
        } else if (source.contains("StackPane[id=account4StackPane]")) {
//            System.out.println("Src4: "+ source);
            msgDetail = "DepositReq,4";
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_selectedAcc, msgDetail));
        }
    }



    // td_AccDeposit
    //------------------------------------------------------------

    /**
     * Handle the button press event
     * @param event
     */
    // td_changePin
    public void td_changePin(Event event) {
        log.info("event: " + event);
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "ChangePinExisting"));
    }
    // td_changePin
    //------------------------------------------------------------

    /**
     * Handle the button press event
     * @param event
     */
    // td_checkAccBalance
    public void td_checkBalance(Event event) {
        log.info("event: " + event);
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.BAMS_Request, "GetAccReq"));
    } // td_checkAccBalance

    /**
     * Handle the button press event
     * @param event
     */
    //------------------------------------------------------------
    // td_returnMainMenu
    public void td_returnMainMenu(Event event) {
        log.info("event: " + event);
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "MainMenu"));
    } // td_returnMainMenu

    /**
     * Handle the button press event
     * @param event
     */
    public void td_ejectCard(Event event) {
        log.info("event: " + event);
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "EjectCard"));
    }

    /**
     * Handle the button press event
     * @param event
     */
    //------------------------------------------------------------
    // td_checkAccBalance
    public void td_checkAccBalance(MouseEvent event) {
        String source = event.getSource().toString(); //yields complete string
        String msgDetail;

        if (source.contains("StackPane[id=account1StackPane]")) {
//            System.out.println("Src1: "+ source);
            msgDetail = "EnquiryReq,1";
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.BAMS_Request, msgDetail));
        } else if (source.contains("StackPane[id=account2StackPane]")) {
//            System.out.println("Src2: "+ source);
            msgDetail = "EnquiryReq,2";
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.BAMS_Request, msgDetail));
        } else if (source.contains("StackPane[id=account3StackPane]")) {
//            System.out.println("Src3: "+ source);
            msgDetail = "EnquiryReq,3";
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.BAMS_Request, msgDetail));
        } else if (source.contains("StackPane[id=account4StackPane]")) {
//            System.out.println("Src4: "+ source);
            msgDetail = "EnquiryReq,4";
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.BAMS_Request, msgDetail));
        }

    } // td_checkAccBalance

    /**
     * Handle the mouse click event
     * @param mouseEvent
     */
    //------------------------------------------------------------
    // td_TransferMoney
    public void td_TransferMoney(Event event) {
//        System.out.println("event: " + "Going to Transfer Money");
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.BAMS_Request, "SelectAccReq"));
    } // td_TransferMoney

    //------------------------------------------------------------
    // td_SelectNextAccount
    public void td_SelectNextAccount(Event event) {
        String source = event.getSource().toString(); //yields complete string
        String msgDetail;
        System.out.println("event: " +"Selecting Next Acc");

        if (source.contains("StackPane[id=account1StackPane]")) {

            msgDetail = "SelectNextAccReq,1";
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_NextAcc, msgDetail));
        } else if (source.contains("StackPane[id=account2StackPane]")) {

            msgDetail = "SelectNextAccReq,2";
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_NextAcc, msgDetail));
        } else if (source.contains("StackPane[id=account3StackPane]")) {

            msgDetail = "SelectNextAccReq,3";
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_NextAcc, msgDetail));
        } else if (source.contains("StackPane[id=account4StackPane]")) {

            msgDetail = "SelectNextAccReq,4";
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_NextAcc, msgDetail));
        }
    } // td_SelectNextAccount

    //------------------------------------------------------------
    // td_InputAmount
    public void td_InputTransAmount(Event event) {
        String source = event.getSource().toString(); //yields complete string
        String msgDetail;
        System.out.println("event: " +event);

        if (source.contains("StackPane[id=account1StackPane]")) {

            msgDetail = "InputTransAmount,1";
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_InputTransAmount, msgDetail));
        } else if (source.contains("StackPane[id=account2StackPane]")) {

            msgDetail = "InputTransAmount,2";
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_InputTransAmount, msgDetail));
        } else if (source.contains("StackPane[id=account3StackPane]")) {

            msgDetail = "InputTransAmount,3";
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_InputTransAmount, msgDetail));
        } else if (source.contains("StackPane[id=account4StackPane]")) {

            msgDetail = "InputTransAmount,4";
            touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_InputTransAmount, msgDetail));
        }
    }

    //------------------------------------------------------------
    // setNextAccStackVisible
    public void setNextAccStackVisible(String msgDetails) {
        stackPanes.add(account1StackPane);
        stackPanes.add(account2StackPane);
        stackPanes.add(account3StackPane);
        stackPanes.add(account4StackPane);

        accountTexts.add(account1Text);
        accountTexts.add(account2Text);
        accountTexts.add(account3Text);
        accountTexts.add(account4Text);

        for(int i=0; i<=3; i++){
            if(msgDetails.contains(""+i)){
                stackPanes.get(i-1).setVisible(true);
                accountTexts.get(i-1).setText(i+"");
            }
        }

    } // setNextAccStackVisible

    public void TransAmountInput() {
        String currentPinLabel = pinLabel.getText();
        currentPinLabel = currentPinLabel + "*";
        pinLabel.setText(currentPinLabel);
        System.out.println("currentPinLabel: " + currentPinLabel);
    }

    public void AmountInputted(String msg) {
        pinLabel.setText(msg.split(",")[1]);

    }

    //------------------------------------------------------------
    // td_mouseClick
    public void td_mouseClick(MouseEvent mouseEvent) {
        int x = (int) mouseEvent.getX();
	    int y = (int) mouseEvent.getY();

	log.fine(id + ": mouse clicked: -- (" + x + ", " + y + ")");
	touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_MouseClicked, x + " " + y));
    } // td_mouseClick

    /**
     * Change the GUI label text
     * @param msgDetails the text type
     */
    //------------------------------------------------------------
    // modifyNotifyLabel
    public void modifyNotifyLabel(String msgDetails) {
        switch (msgDetails) {
            case "ChangePinExisting":
                notifyLabel.setText("Please input your existing pin (should be 6-digits long)");
                break;
            case "ChangePinNew":
                notifyLabel.setText("Please input your new pin (should be 6-digits long)");
                break;
        }
    } // modifyNotifyLabel

    /**
     * Change the GUI text
     */
    //------------------------------------------------------------
    // setChangePinText
    public void setChangePinText() {
        accBalanceCardNo.setText("Change pin successful!");
    } // setChangePinText

    /**
     * Change the GUI text
     */
    //------------------------------------------------------------
    // setBalanceErrorText
    public void setBalanceErrorText () {
        balanceErrorText.setText("You have wrongly typed in your existing password / new password 3 times, please try again.");
    } // setBalanceErrorText

    /**
     * Change the GUI text
     */
    //------------------------------------------------------------
    // setAccBalanceText
    public void setAccBalanceText(String msgDetails) {
        accBalanceTexts.add(accBalanceCardNo);
        accBalanceTexts.add(accBalanceAccNo);
        accBalanceTexts.add(accBalanceAmount);
        int i = 0;
        for (String msgDetail: msgDetails.split("@")) {
            accBalanceTexts.get(i).setText(msgDetail);
            i++;
        }
    } // setAccBalanceText

    //------------------------------------------------------------
    // setStackPaneVisibiliy
    public void setStackPaneVisibiliy(String msgDetails) {
        stackPanes.add(account1StackPane);
        stackPanes.add(account2StackPane);
        stackPanes.add(account3StackPane);
        stackPanes.add(account4StackPane);

        accountTexts.add(account1Text);
        accountTexts.add(account2Text);
        accountTexts.add(account3Text);
        accountTexts.add(account4Text);

        int i = 0;
        for (String account: msgDetails.split("/")) {

            if (account.contains(":")) {
                account = account.split(":")[1];
            }
//            System.out.println(stackPanes.get(i));
            stackPanes.get(i).setVisible(true);
            accountTexts.get(i).setText(account);
//            System.out.println(account);
            i++;
        }
//        cardReaderTextArea.appendText(status+"\n");
    } // setStackPaneVisibiliy

    /**
     * Change the GUI text
     */
    public void pinInput() {
        String currentPinLabel = pinLabel.getText();
        currentPinLabel = currentPinLabel + "*";
        pinLabel.setText(currentPinLabel);
        log.info("currentPinLabel: " + currentPinLabel);
    }

    /**
     * Change the GUI text
     */
    public void pinErase() {
        String currentPinLabel = pinLabel.getText();
        currentPinLabel = currentPinLabel.substring(0, currentPinLabel.length() - 1);
        pinLabel.setText(currentPinLabel);
        log.info("currentPinLabel: " + currentPinLabel);
    }

    /**
     * Change the GUI text
     */
    public void pinWrong() {
        pinLabel.setText("");
        pinWrongLabel.setText("Please input a correct pin");
        pinWrongLabel.setTextFill(Color.color(0.9,0,0));
    }

} // TouchDisplayEmulatorController
