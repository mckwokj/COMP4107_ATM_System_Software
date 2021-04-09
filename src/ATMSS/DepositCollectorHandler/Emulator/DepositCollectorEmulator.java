package ATMSS.DepositCollectorHandler.Emulator;

import ATMSS.ATMSSStarter;
import ATMSS.DepositCollectorHandler.DepositCollectorHandler;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.util.Date;

public class DepositCollectorEmulator extends DepositCollectorHandler {
    private ATMSSStarter atmssStarter;
    private String id;
    private Stage myStage;
    private DepositCollectorEmulatorController depositCollectorEmulatorController;

    //------------------------------------------------------------
    // DepositCollectorEmulator
    public DepositCollectorEmulator(String id, ATMSSStarter atmssStarter) {
        super(id, atmssStarter);
        this.atmssStarter = atmssStarter;
        this.id = id;
    } // DepositCollectorEmulator


    //------------------------------------------------------------
    // start
    public void start() throws Exception {
        Parent root;
        myStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        String fxmlName = "DepositCollectorEmulator.fxml";
        loader.setLocation(DepositCollectorEmulatorController.class.getResource(fxmlName));
        root = loader.load();
        depositCollectorEmulatorController = (DepositCollectorEmulatorController) loader.getController();
        depositCollectorEmulatorController.initialize(id, atmssStarter, log, this);
        myStage.initStyle(StageStyle.DECORATED);
        myStage.setScene(new Scene(root, 350, 470));
        myStage.setTitle("Deposit Collector");
        myStage.setResizable(false);
        myStage.setOnCloseRequest((WindowEvent event) -> {
            atmssStarter.stopApp();
            Platform.exit();
        });
        myStage.show();
    }
    // DepositCollectorEmulator
    //------------------------------------------------------------
    // handle Deposit Collector
    /*
    protected void handleDepositCollector(String content) {
        // fixme
        super.depositCollectorEmulatorController(content);
        depositCollectorEmulatorController(content);
    }
    */// handle Deposit Collector

}