package ATMSS.TouchDisplayHandler.Emulator;

import ATMSS.ATMSSStarter;
import ATMSS.TouchDisplayHandler.TouchDisplayHandler;
import AppKickstarter.misc.Msg;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * TouchDisplay Emulator class is using to simulate a real touch display of the atm machine
 */
//======================================================================
// TouchDisplayEmulator
public class TouchDisplayEmulator extends TouchDisplayHandler {
    private final int WIDTH = 680;
    private final int HEIGHT = 520;
    private ATMSSStarter atmssStarter;
    private String id;
    private Stage myStage;
    private TouchDisplayEmulatorController touchDisplayEmulatorController;

	/**
	 * The constructor for the touch display emulator class
	 * @param id the name of the touch display
	 * @param atmssStarter a reference to the AppKickstarter
	 */
    //------------------------------------------------------------
    // TouchDisplayEmulator
    public TouchDisplayEmulator(String id, ATMSSStarter atmssStarter) throws Exception {
	super(id, atmssStarter);
	this.atmssStarter = atmssStarter;
	this.id = id;
    } // TouchDisplayEmulator

	/**
	 * The start function configures the GUI setup of the touch display emulator and runs the GUI
	 * @throws Exception
	 */
    //------------------------------------------------------------
    // start
    public void start() throws Exception {
	Parent root;
	myStage = new Stage();
	FXMLLoader loader = new FXMLLoader();
	String fxmlName = "TouchDisplayInitial.fxml";
//	String fxmlName = "TouchDisplayMoneyTransferError.fxml";
	loader.setLocation(TouchDisplayEmulator.class.getResource(fxmlName));
	root = loader.load();
	touchDisplayEmulatorController = (TouchDisplayEmulatorController) loader.getController();
	touchDisplayEmulatorController.initialize(id, atmssStarter, log, this);
	myStage.initStyle(StageStyle.DECORATED);
	myStage.setScene(new Scene(root, WIDTH, HEIGHT));
	myStage.setTitle("Touch Display");
	myStage.setResizable(false);
	myStage.setOnCloseRequest((WindowEvent event) -> {
	    atmssStarter.stopApp();
	    Platform.exit();
	});
	myStage.show();
    } // TouchDisplayEmulator

	/**
	 * This function is for handling the dusplay update request from "atmss"
	 * @param msg The display page
	 */
    //------------------------------------------------------------
    // handleUpdateDisplay
    protected void handleUpdateDisplay(Msg msg) {
	log.info(id + ": update display -- " + msg.getDetails());
	if (msg.getDetails().contains("SelectNextAcc")) {
		System.out.println("I made it!!! "+msg.getDetails());
		reloadStage("TouchDisplaySelNextAcc.fxml");
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		touchDisplayEmulatorController.setNextAccStackVisible(msg.getDetails());
	} else if(msg.getDetails().contains("AmountInputted")) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					touchDisplayEmulatorController.AmountInputted(msg.getDetails());
				} catch (Exception e) {
					log.severe(id + ": failed to update Amount inputted");
					e.printStackTrace();
				}
			}
		});
	}else {switch (msg.getDetails()) {
		case "BlankScreen":
			reloadStage("TouchDisplayEmulator.fxml");
			break;

		case "MainMenu":
			reloadStage("TouchDisplayMainMenu.fxml");
			break;

		case "Confirmation":
			reloadStage("TouchDisplayConfirmation.fxml");
			break;

		case "PinInputPage":
			reloadStage("TouchDisplayPinInput.fxml");
			break;

		case "OpenDeposit":
			reloadStage("TouchDisplayOpenDeposit.fxml");
			break;

		case "Waiting":
			reloadStage("TouchDisplayWaitPushCash.fxml");
			break;

		case "GetCash":
			reloadStage("TouchDisplayGetCash.fxml");
			break;

		case "InputTransAmount":
			reloadStage("TouchDisplayInputTransAmount.fxml");
			break;

		case "TransMoneySelectAC":
			TransMoneySelectAC(msg.getDetails());
			break;

		case "ChangePinExisting":
			handleChangePin(msg.getDetails());
			break;

		case "ChangePinNew":
			handleChangePin(msg.getDetails());
			break;

		case "ChangePinInputtedWrong":
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						touchDisplayEmulatorController.pinWrong();
					} catch (Exception e) {
						log.severe(id + ": failed to change pin");
						e.printStackTrace();
					}
				}
			});
			break;

		case "ChangePinInputtedWrong3Times":
			reloadStage("TouchDisplayCheckBalanceError.fxml");

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			touchDisplayEmulatorController.setBalanceErrorText();
			break;

		case "PinInputted":
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						touchDisplayEmulatorController.pinInput();
					} catch (Exception e) {
						log.severe(id + ": failed to update PinInputted");
						e.printStackTrace();
					}
				}
			});
			break;

		case "PinErase":
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						touchDisplayEmulatorController.pinErase();
					} catch (Exception e) {
						log.severe(id + ": failed to do PinErase");
						e.printStackTrace();
					}
				}
			});
			break;

		case "PinInputtedWrong":
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						touchDisplayEmulatorController.pinWrong();
					} catch (Exception e) {
						log.severe(id + ": failed to update PinInputtedWrong");
						e.printStackTrace();
					}
				}
			});
			break;

		case "CardLocked":
			reloadStage("TouchDisplayCardLocked.fxml");
			break;

		case "EjectCard":
			reloadStage("TouchDisplayEjectCard.fxml");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			atmss.send(new Msg(id, mbox, Msg.Type.CR_EjectCard, "EjectCard"));
			break;

		case "Initialization":
			reloadStage("TouchDisplayInitial.fxml");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;

		case "RemoveCardTimeOut":
			reloadStage("TouchDisplayCardRetaining.fxml");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			atmss.send(new Msg(id, mbox, Msg.Type.CR_Lock, ""));
			break;

		case "MoneyTransferError":
			reloadStage("TouchDisplayMoneyTransferError.fxml");
			break;

//		case "CheckBalance":
//			reloadStage("TouchDisplayCheckBalance.fxml");
//			break;

		default:
			log.severe(id + ": update display with unknown display type -- " + msg.getDetails());
			break;
	}
	}
    } // handleUpdateDisplay

	//------------------------------------------------------------
	// handleChangePin
	protected void handleChangePin(String msgDetails) {
		reloadStage("TouchDisplayPinInput.fxml");

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					touchDisplayEmulatorController.modifyNotifyLabel(msgDetails);
				} catch (Exception e) {
					log.severe(id + ": failed to update print label (existing pin)");
					e.printStackTrace();
				}
			}
		});

	} // handleChangePin

	//------------------------------------------------------------
	// TransMoneySelectAC
	protected void TransMoneySelectAC(String msgDetails) {
		reloadStage("MoneyTransferSelectAccount.fxml");

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					touchDisplayEmulatorController.modifyNotifyLabel(msgDetails);
				} catch (Exception e) {
					log.severe(id + ": failed to update money transfer select account touch screen");
					e.printStackTrace();
				}
			}
		});

	} // TransMoneySelectAC

	//------------------------------------------------------------
	// handleBAMSErrorDisplay
	protected void handleBAMSErrorDisplay(Msg msg) {
    	reloadStage("TouchDisplayCheckBalanceError.fxml");
	} // handleBAMSErrorDisplay

	//------------------------------------------------------------
	// handleBAMSUpdateDisplay
	protected void handleBAMSUpdateDisplay(Msg msg) {
    	if (msg.getDetails().contains("accounts")) {
			reloadStage("TouchDisplayCheckBalance.fxml");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			touchDisplayEmulatorController.setStackPaneVisibiliy(msg.getDetails());
		} else if (msg.getDetails().contains("amount")) {
//    		System.out.println("I made it!!! "+msg.getDetails());
			reloadStage("TouchDisplayCheckAccBalance.fxml");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			touchDisplayEmulatorController.setAccBalanceText(msg.getDetails());
		} else if (msg.getDetails().contains("chgPinReq")) {
			//    		System.out.println("I made it!!! "+msg.getDetails());
			reloadStage("TouchDisplayCheckAccBalance.fxml");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			touchDisplayEmulatorController.setChangePinText();
		}else if (msg.getDetails().contains("accountDeposit")) {
			System.out.print("Loading next page:" + msg.getDetails());
    		reloadStage("TouchDisplayDeposit.fxml");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			touchDisplayEmulatorController.setStackPaneVisibiliy(msg.getDetails());

		}else if (msg.getDetails().contains("DepositReq")) {
			reloadStage("TouchDisplayOpenDeposit.fxml");
			atmss.send(new Msg(id, mbox, Msg.Type.TD_selectedAcc, msg.getDetails()));
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else if (msg.getDetails().contains("accountWithdrawal")) {
			reloadStage("TouchDisplayWithdrawal.fxml");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			touchDisplayEmulatorController.setStackPaneVisibiliy(msg.getDetails());
		}else if (msg.getDetails().contains("WithdrawalReq")) {
			reloadStage("TouchDisplayWaitPushCash.fxml");
			atmss.send(new Msg(id, mbox, Msg.Type.TD_selectedAccWithdrawal, msg.getDetails()));
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else if (msg.getDetails().contains("SelectTransAC")) {
//			    		System.out.println("I made it!!! "+msg.getDetails());
			reloadStage("TouchDisplayMoneyTransSelAcc.fxml");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			touchDisplayEmulatorController.setStackPaneVisibiliy(msg.getDetails());
		} else if (msg.getDetails().contains("transAmount")) {
			reloadStage("TouchDisplayMainMenu.fxml");

		} else if (msg.getDetails().contains("NotEnoughACError")){
			reloadStage("TouchDisplayShowTransDetails.fxml");
		}
	} // handleBAMSUpdateDisplay

	/**
	 * This function is using to reload the GUI display by a fxml file.
	 * @param fxmlFName The name of the fxml file.
	 */
    //------------------------------------------------------------
    // reloadStage
    private void reloadStage(String fxmlFName) {
        TouchDisplayEmulator touchDisplayEmulator = this;
        Platform.runLater(new Runnable() {
	    @Override
	    public void run() {
		try {
		    log.info(id + ": loading fxml: " + fxmlFName);
		    Parent root;
		    FXMLLoader loader = new FXMLLoader();
		    loader.setLocation(TouchDisplayEmulator.class.getResource(fxmlFName));
		    root = loader.load();
		    touchDisplayEmulatorController = (TouchDisplayEmulatorController) loader.getController();
		    touchDisplayEmulatorController.initialize(id, atmssStarter, log, touchDisplayEmulator);
		    myStage.setScene(new Scene(root, WIDTH, HEIGHT));
		} catch (Exception e) {
		    log.severe(id + ": failed to load " + fxmlFName);
		    e.printStackTrace();
		}
	    }
	});
    } // reloadStage
} // TouchDisplayEmulator
