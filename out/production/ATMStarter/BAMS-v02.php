<?php
$servername = "cslinux0.comp.hkbu.edu.hk";
$username = "comp4107_grp10";
$password = "689748";
$dbname = "comp4107_grp10";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
  die("Connection failed: " . $conn->connect_error);
}

$req = json_decode($_POST["BAMSReq"], false);

if (strcmp($req->msgType, "LoginReq") === 0) {
  $cardAndDate = cardNo. date(DATE_RFC2822);
  $cred = sha1($cardAndDate);

  $sql = "SELECT cardNo FROM Cards WHERE cardNo = " . "'" . $req->cardNo . "'" . " and pin = " . "'" . $req->pin . "'";
  $result = $conn->query($sql);

  if ($result->num_rows > 0) {
    $sql2 = "UPDATE Cards SET cred = " . "'"  . $cred . "'" . " WHERE cardNo = " . "'" . $req->cardNo . "'";
    $result2 = $conn->query($sql2);

    $reply->cred = $cred;
  } else {
    $reply->cred = "LoginFail" . ' ' . $req->cardNo . ' ' . $req->pin;
  }

  $reply->msgType = "LoginReply";
  $reply->cardNo = $req->cardNo;
  $reply->pin = $req->pin;
//   $reply->cred = "Credible_Credential!!!";
} else if (strcmp($req->msgType, "LogoutReq") === 0) {
    $sql = "UPDATE Cards SET cred=null WHERE cardNo = " . "'" . $req->cardNo . "'";
    $result = $conn->query($sql);

  $reply->msgType = "LogoutReply";
  $reply->cardNo = $req->cardNo;
  $reply->cred = $req->cred;
  $reply->result = "succ";

} else if (strcmp($req->msgType, "GetAccReq") === 0) {

  $sql = "SELECT cardNo FROM Cards WHERE cardNo = " . "'" . $req->cardNo . "'" . " and cred = " . "'" . $req->cred . "'";
  $result = $conn->query($sql);

  if ($result->num_rows > 0) {
      // $sql = "SELECT aid FROM Accounts WHERE cardNo = " . "'" . $req->cardNo . "'" . " and cred = " . "'" . $req->cred . "'";
    $sql = "SELECT aid FROM Accounts WHERE cardNo = " . "'" . $req->cardNo . "'";
    $result = $conn->query($sql);
    if ($result->num_rows > 0) {
  // loop through data of each row
      while($row = $result->fetch_assoc()) {
        $reply->accounts = $reply->accounts . $row["aid"] . '/';
      }
    } else {
      $reply->accounts = "Error";
    }
  } else {
    $reply->accounts = "Error";
  }

  $reply->msgType = "GetAccReply";
  $reply->cardNo = $req->cardNo;
  $reply->cred = $req->cred;

} else if (strcmp($req->msgType, "WithdrawReq") === 0) {

  $sql = "SELECT cardNo FROM Cards WHERE cardNo = " . "'" . $req->cardNo . "'" . " and cred = " . "'" . $req->cred . "'";
  $result = $conn->query($sql);

  if ($result->num_rows > 0) {
    $sql = "SELECT balance FROM Accounts WHERE cardNo = " . "'" . $req->cardNo . "'" . " and aid = " . "'" . $req->accNo . "'";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
      while($row = $result->fetch_assoc()) {
        $req->outAmount = $row["balance"];
      }

      if ($req->outAmount > $req->amount) {
        $reply->outAmount = strval($req->outAmount - floatval($req->amount));
        $sql = "UPDATE Accounts SET balance = " . "'" . $reply->outAmount . "'" . " WHERE cardNo = " . "'" . $req->cardNo . "'" . " and aid = " . "'" . $req->accNo . "'";

        if ($conn->query($sql) === TRUE) {

        } else {
          $reply->amount = "Error";
          $reply->outAmount = "Error";
        }
      } else {
        $reply->amount = "Error";
        $reply->outAmount = "Error";
      }

    } else {
      $reply->amount = "Error";
      $reply->outAmount = "Error";
    }

  } else {
    $reply->amount = "Error";
    $reply->outAmount = "Error";
  }

  $reply->msgType = "WithdrawReply";
  $reply->cardNo = $req->cardNo;
  $reply->accNo = $req->accNo;
  $reply->cred = $req->cred;
  $reply->amount = $req->amount;
  // $reply->outAmount = $req->amount;
} else if (strcmp($req->msgType, "DepositReq") === 0) {

  $sql = "SELECT cardNo FROM Cards WHERE cardNo = " . "'" . $req->cardNo . "'" . " and cred = " . "'" . $req->cred . "'";
  $result = $conn->query($sql);

  if ($result->num_rows > 0) {

    $sql = "SELECT balance FROM Accounts WHERE cardNo = " . "'" . $req->cardNo . "'" . " and aid = " . "'" . $req->accNo . "'";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {

      while($row = $result->fetch_assoc()) {
        $req->depAmount = $row["balance"];
      }

      $reply->depAmount = strval($req->depAmount + floatval($req->amount));

      $sql = "UPDATE Accounts SET balance = " . "'" . $reply->depAmount . "'" . " WHERE cardNo = " . "'" . $req->cardNo . "'" . " and aid = " . "'" . $req->accNo . "'";

      if ($conn->query($sql) === TRUE) {
        // $reply->amount = $req->amount;
        // $reply->depAmount = $req->depAmount;
      } else {
        $reply->amount = "Error";
        $reply->depAmount = "Error";
      }
    } else {
      $reply->amount = "Error";
      $reply->depAmount = "Error";
    }
  } else {
    $reply->amount = "Error";
    $reply->depAmount = "Error";
  }

  $reply->msgType = "DepositReply";
  $reply->cardNo = $req->cardNo;
  $reply->accNo = $req->accNo;
  $reply->cred = $req->cred;
  $reply->amount = $req->amount;
  // $reply->depAmount = $req->amount;
} else if (strcmp($req->msgType, "EnquiryReq") === 0) {

  $sql = "SELECT cardNo FROM Cards WHERE cardNo = " . "'" . $req->cardNo . "'" . " and cred = " . "'" . $req->cred . "'";
  $result = $conn->query($sql);

  if ($result->num_rows > 0) {

    $sql = "SELECT balance FROM Accounts WHERE cardNo = " . "'" . $req->cardNo . "'" . " and aid = " . "'" . $req->accNo . "'";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
      $row = $result->fetch_assoc();
      $reply->amount = strval($row["balance"]);
    } else {
      $reply->amount = "Error";
    }
  } else {
    $reply->amount = "Error";
  }

  $reply->msgType = "EnquiryReply";
  $reply->cardNo = $req->cardNo;
  $reply->accNo = $req->accNo;
  $reply->cred = $req->cred;

} else if (strcmp($req->msgType, "TransferReq") === 0) {

   $sql = "SELECT cardNo FROM Cards WHERE cardNo = " . "'" . $req->cardNo . "'" . " and cred = " . "'" . $req->cred . "'";
   $result = $conn->query($sql);

   $returnTransAmount = 0;

  if ($result->num_rows > 0) {

    $sql = "SELECT balance FROM Accounts WHERE cardNo = " . "'" . $req->cardNo . "'" . " and aid = " . "'" . $req->fromAcc . "'";
    $result = $conn->query($sql);
    if($result->num_rows > 0){
       while($row = $result->fetch_assoc()) {
        $reply->CurrAmount = $row["balance"];
       }

       $returnTransAmount = floatval($req->amount);

       if($reply->CurrAmount >= $returnTransAmount){
         $reply->upFromAccAmount =  $reply->CurrAmount - $returnTransAmount;
         $sql = "SELECT balance FROM Accounts WHERE cardNo = "."'" . $req->cardNo . "'" . "and aid = " . "'" . $req->toAcc . "'";
         $result = $conn->query($sql);
         if($result->num_rows > 0){
           while($row = $result->fetch_assoc()) {
             $reply->ToAccCurrAmount = $row["balance"];
            }
          }
          $reply->upToAccAmount = $reply->ToAccCurrAmount + $returnTransAmount;

         $sql = "UPDATE Accounts SET balance =" . $reply->upFromAccAmount . " WHERE cardNo = " . "'" . $req->cardNo . "'" . " and aid = " . "'" . $req->fromAcc . "'";


         if($conn->query($sql) ===True){
           $sql = "UPDATE Accounts SET balance =" . $reply->upToAccAmount . " WHERE cardNo = " . "'" . $req->cardNo . "'" . " and aid = " . "'" . $req->toAcc . "'";
            if($conn->query($sql) ===True){
                $returnTransAmount = (string)$returnTransAmount;
                $reply->upFromAccAmount = (string)$upFromAccAmount;
                $reply->upToAccAmount = (string)$upToAccAmount;
            }

         } else{
            $returnTransAmount = "-1";
            $reply->upFromAccAmount = "Error";
            $reply->upToAccAmount = "Error";
         }
       } else{
          $returnTransAmount = "-1";
           $reply->upFromAccAmount = "Error";
           $reply->upToAccAmount = "Error";
        }

     }
   }
  $reply->msgType = "TransferReply";
  $reply->cardNo = $req->cardNo;
  $reply->cred = $req->cred;
  $reply->fromAcc = $req->fromAcc;
  $reply->toAcc = $req->toAcc;
  $reply->amount = $req->amount;
  $reply->transAmount = $returnTransAmount;
} else if (strcmp($req->msgType, "AccStmtReq") === 0) {
  $reply->msgType = "AccStmtReply";
  $reply->cardNo = $req->cardNo;
  $reply->accNo = $req->accNo;
  $reply->cred = $req->cred;
  $reply->result = "succ";
} else if (strcmp($req->msgType, "ChqBookReq") === 0) {
  $reply->msgType = "ChqBookReply";
  $reply->cardNo = $req->cardNo;
  $reply->accNo = $req->accNo;
  $reply->cred = $req->cred;
  $reply->result = "succ";
} else if (strcmp($req->msgType, "ChgPinReq") === 0) {

  $sql = "SELECT cardNo FROM Cards WHERE cardNo = " . "'" . $req->cardNo . "'" . " and cred = " . "'" . $req->cred . "'";
  $result = $conn->query($sql);

  if ($result->num_rows > 0) {
    $sql = "UPDATE Cards SET pin = " . "'" . $req->newPin . "'" . " WHERE cardNo = " . "'" . $req->cardNo . "'" . " and cred = " . "'" . $req->cred . "'" . " and pin = " . "'" . $req->oldPin . "'";

    if ($conn->query($sql) === TRUE) {
      $reply->result = "succ";
    } else {
      $reply->result = "Error";
    }
  } else {
    $reply->result = "Error";
  }

  $reply->msgType = "ChgPinReply";
  $reply->cardNo = $req->cardNo;
  $reply->oldPin = $req->oldPin;
  $reply->newPin = $req->newPin;
  $reply->cred = $req->cred;

} else if (strcmp($req->msgType, "ChgLangReq") === 0) {
  $reply->msgType = "ChgLangReply";
  $reply->cardNo = $req->cardNo;
  $reply->oldLang = $req->oldLang;
  $reply->newLang = $req->newLang;
  $reply->cred = $req->cred;
  $reply->result = "succ";
}

echo json_encode($reply);
?>