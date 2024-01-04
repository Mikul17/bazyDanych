import paletteProvider from "@/constants/color-palette";
import {
  Alert,
  Box,
  Button,
  IconButton,
  Modal,
  TextField,
  Typography,
  alpha,
} from "@mui/material";
import { HighlightOffOutlined } from "@mui/icons-material";
import { buttonStyle, headerStyle, inputStyle } from "@/constants/Styles";
import { jwtDecode } from "jwt-decode";
import { useState } from "react";

type NewTransactionModalProps = {
  openModal: boolean;
  handleCloseModal: () => void;
  updateBalance: () => void;
};

interface alertState {
  message : string;
  type: "success"|"error"|''|"info";
}

const getUserId = () => {
  const token = sessionStorage.getItem("token");
  if (!token) {
    throw new Error("Token not found");
  }
  const decoded = jwtDecode<{ userId: number }>(token!);
  const userId = decoded.userId;
  if (!userId) {
    throw new Error("User id not found");
  }
  return userId;
}





const NewTransactionModal = (props: NewTransactionModalProps) => {
  const palette = paletteProvider();
  const [amount, setAmount] = useState<string>("");
  const [alertState, setAlertState] = useState<alertState>({message:"", type:''});


  const modalStyle = {
    position: "absolute" as "absolute",
    top: "50%",
    left: "50%",
    padding: "0 1rem 1rem 1rem",
    transform: "translate(-50%, -50%)",
    width: "60%",
    "@media (min-width:450px)": {
      width: "50%",
    },
    "@media (min-width:600px)": {
      width: "40%",
    },
    "@media (min-width:1050px)": {
      width: "20%",
    },
    backgroundColor: palette.primary.light,
    borderRadius: "1.25rem",
  };

  const clearAlert = () => {
    setAlertState({message:"", type:''});
  }


  const handleDeposit = async () => {
    const userId = getUserId();
    const type = "DEPOSIT"
    const parsedAmount = parseFloat(amount);
    if (isNaN(parsedAmount) || parsedAmount <= 0) {
      setAlertState({message:"Invalid amount", type:"error"});
      return;
    }
    console.log(userId, type, parsedAmount)
    try{
      const response = await fetch('http://localhost:8080/api/transactions/add',{
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          type: type,
          userId: userId,
          amount: parsedAmount
        })
      });
      
      const jsonResponse = await response.json();
  
      if(response.ok && jsonResponse.transactionStatus=== "SUCCESS"){
        setAlertState({message:"Deposit successful", type:"success"});
      }else if(response.ok && jsonResponse.transactionStatus=== "PENDING"){
        setAlertState({message:"Deposit transaction pending. Check in few minutes. ", type:"info"});
      }else{
        setAlertState({message:"Deposit failed: ", type:"error"});
      }
    }catch(errorResponse){
      if (errorResponse instanceof Response) {
          const errorMessage = await errorResponse.text();
          setAlertState({message:"Deposit failed: "+errorMessage, type:"error"});
      }
          else{
            setAlertState({message:"Error while connecting to server", type:"error"});
          }
      }
  };

  const handleWithdraw = async () => {
    const userId = getUserId();
    const type = "WITHDRAW"
    const parsedAmount = parseFloat(amount);
    if (isNaN(parsedAmount) || parsedAmount <= 0) {
      setAlertState({message:"Invalid amount", type:"error"});
      return;
    }
    try{
      const response = await fetch('http://localhost:8080/api/transactions/add',{
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          userId: userId,
          type: type,
          amount: parsedAmount
        })
      });

      const jsonResponse = await response.json();
  
      if(response.ok && jsonResponse.transactionStatus=== "SUCCESS"){
        setAlertState({message:"Withdraw successful", type:"success"});
      }else if(response.ok && jsonResponse.transactionStatus=== "PENDING"){
        setAlertState({message:"Withdraw pending. Check in few minutes. ", type:"info"});
      }else{
        setAlertState({message:"Withdraw failed", type:"error"});
      }
    }catch(errorResponse){
      if (errorResponse instanceof Response) {
          const errorMessage = await errorResponse.text();
          setAlertState({message:"Withdraw failed: "+errorMessage, type:"error"});
      }
          else{
            setAlertState({message:"Error while connecting to server", type:"error"});
          }
      }
  };

  const handleAmountChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const value = event.target.value;
    if (/^\d*[\.,]?\d{0,2}$/.test(value)) {
      setAmount(value);
    }
  };

  const handleExit = () => {
    setAmount("");
    props.updateBalance();
    clearAlert();
    props.handleCloseModal();
  }


  return (
    <Modal
      open={props.openModal}
      onClose={handleExit}
      aria-labelledby="deposit-withdraw-modal"
      aria-describedby="modal-modal-description"
    >
      <Box sx={modalStyle}>
        <Box sx={headerStyle("space-around")}>
          <Typography variant="h6" component="h2" fontWeight={"bold"} marginLeft={"1rem"}>
            Deposit/Withdraw
          </Typography>
          <IconButton onClick={handleExit}>
            <HighlightOffOutlined sx={{ color: palette.primary.main }} />
          </IconButton>
        </Box>
        <Box
          display={"flex"}
          justifyContent={"center"}
          alignItems={"center"}
          flexDirection={"column"}
        >
          <TextField
            margin="normal"
            fullWidth
            id="amount"
            label="Amount"
            name="amount"
            autoFocus
            variant="outlined"
            value={amount}
            onChange={handleAmountChange}
            defaultValue={0}
            InputProps={{ inputProps: { min: 0} }}
            InputLabelProps={{sx:{"&.Mui-focused": {
              color: palette.text.secondary,
            },}}}
            sx={inputStyle}
          />
        <Button variant="contained" style={buttonStyle(palette.secondary.main, "50%")}  onClick={handleDeposit}>Deposit</Button>
        <Button variant="contained" style={buttonStyle(palette.error.main, "50%")} onClick={handleWithdraw}>Withdraw</Button>
        {alertState.message && <Alert sx={{marginTop:"1rem"}} severity={alertState.type || undefined} onClose={clearAlert}>{alertState.message}</Alert>}
        </Box>

      </Box>
    </Modal>
  );
};

export default NewTransactionModal;
